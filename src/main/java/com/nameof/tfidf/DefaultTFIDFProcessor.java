package com.nameof.tfidf;

import com.google.common.base.Preconditions;
import com.nameof.tfidf.bean.DocSimilarity;
import com.nameof.tfidf.bean.Document;
import com.nameof.tfidf.bean.Keyword;
import com.nameof.tfidf.data.DataLoader;
import com.nameof.tfidf.exception.TFIDFException;
import com.nameof.tfidf.text.DefaultTextProcessor;
import com.nameof.tfidf.text.TextProcessor;
import com.nameof.tfidf.text.handler.SimpleTermHandler;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
public class DefaultTFIDFProcessor implements TFIDFProcessor {

    private final TFIDFCalculator tfidfCalculator = new TFIDFCalculator();

    private final Function<Set<Keyword>, Set<String>> keywordsExtractor = keywords -> keywords.stream().map(Keyword::getTerm).collect(Collectors.toSet());

    private TextProcessor textProcessor = DefaultTextProcessor.builder()
            .termHandlers(Collections.singletonList(new SimpleTermHandler()))
            .build();

    public DefaultTFIDFProcessor(TextProcessor textProcessor) {
        this.textProcessor = textProcessor;
    }

    @Override
    public List<Document> analyzeAll(DataLoader dataLoader) {
        List<Document> documentList = loadCorpusData(dataLoader);
        documentList.parallelStream().forEach(document -> analyzeKeywords(document, documentList));
        return documentList;
    }

    @Override
    public Document analyze(String docName, DataLoader dataLoader) {
        List<Document> documentList = loadCorpusData(dataLoader);
        return this.analyzeKeywords(docName, documentList);
    }

    private Document analyzeKeywords(String docName, List<Document> documentList) {
        for (Document document : documentList) {
            if (!document.getName().equals(docName)) {
                continue;
            }
            analyzeKeywords(document, documentList);
            return document;
        }
        throw new TFIDFException(docName + " not found");
    }

    private void analyzeKeywords(Document document, List<Document> documentList) {
        for (String term : document.getTermList()) {
            double weight = tfidfCalculator.tfIdf(document, documentList, term);
            if (weight > 0.0) {
                document.getKeywords().add(new Keyword(term, weight));
            }
        }
    }

    private List<Document> loadCorpusData(DataLoader dataLoader) {
        return dataLoader.loadCorpusText().parallelStream()
                .map(tuple -> new Document(tuple.getLeft(), tuple.getRight(), textProcessor.segment(tuple.getRight()), new HashSet<>()))
                .collect(Collectors.toList());
    }

    @Override
    public Set<Keyword> keyword(String docName, DataLoader dataLoader) {
        return analyze(docName, dataLoader).getKeywords();
    }

    @Override
    public DocSimilarity similarity(String firstDocName, String secondDocName, DataLoader dataLoader) {
        List<Document> documentList = loadCorpusData(dataLoader);
        Document first = analyzeKeywords(firstDocName, documentList);
        Document second = analyzeKeywords(secondDocName, documentList);
        return similarity(first, second);
    }

    private DocSimilarity similarity(Document first, Document second) {
        double score = textProcessor.similarity(keywordsExtractor.apply(first.getKeywords()), keywordsExtractor.apply(second.getKeywords()));
        return new DocSimilarity(first, second, score);
    }

    @Override
    public List<DocSimilarity> topSimilarity(int top, DataLoader dataLoader) {
        Preconditions.checkArgument(top > 0, "top must greater than 0");
        List<Document> documentList = analyzeAll(dataLoader);
        List<ForkJoinTask<DocSimilarity>> taskList = new ArrayList<>();
        for (int i = 0; i < documentList.size() - 1; i++) {
            for (int j = i + 1; j < documentList.size(); j++) {
                Document first = documentList.get(i);
                Document second = documentList.get(j);
                ForkJoinTask<DocSimilarity> task = ForkJoinPool.commonPool().submit(() -> similarity(first, second));
                taskList.add(task);
            }
        }
        Queue<DocSimilarity> queue = new PriorityQueue<>(top);
        for (ForkJoinTask<DocSimilarity> task : taskList) {
            DocSimilarity docSimilarity = null;
            try {
                docSimilarity = task.get();
            } catch (Exception e) {
                throw new TFIDFException("计算失败", e);
            }
            if (queue.size() < top || queue.peek().getScore() < docSimilarity.getScore()) {
                if (queue.size() == top)
                    queue.remove();
                queue.add(docSimilarity);
            }
        }
        return queue.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }
}
