package com.nameof.tfidf;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.google.common.base.Preconditions;
import com.nameof.tfidf.bean.DocSimilarity;
import com.nameof.tfidf.bean.Document;
import com.nameof.tfidf.bean.Keyword;
import com.nameof.tfidf.data.DataLoader;
import com.nameof.tfidf.exception.TFIDFException;
import com.nameof.tfidf.similarity.SimilarityCalculator;
import com.nameof.tfidf.similarity.SimpleSimilarityCalculator;
import com.nameof.tfidf.text.DefaultTextProcessor;
import com.nameof.tfidf.text.TextProcessor;
import com.nameof.tfidf.text.handler.SimpleTermHandler;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;

@NoArgsConstructor
public class DefaultTFIDFProcessor implements TFIDFProcessor {

    private final TFIDFCalculator tfidfCalculator = new TFIDFCalculator();

    private TextProcessor textProcessor = DefaultTextProcessor.builder()
            .termHandlers(Collections.singletonList(new SimpleTermHandler()))
            .build();

    @Setter
    private SimilarityCalculator similarityCalculator = new SimpleSimilarityCalculator();

    public DefaultTFIDFProcessor(TextProcessor textProcessor) {
        Preconditions.checkNotNull(textProcessor);
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
        Set<Keyword> keywords = new ConcurrentHashSet<>();
        document.getTermList().parallelStream().forEach(term -> {
            double weight = tfidfCalculator.tfIdf(document, documentList, term);
            if (weight > 0.0) {
                keywords.add(new Keyword(term, weight));
            }
        });
        document.setKeywords(keywords);
    }

    private List<Document> loadCorpusData(DataLoader dataLoader) {
        List<Document> result = dataLoader.loadCorpusText().parallelStream()
                .map(tuple -> new Document(tuple.getLeft(), tuple.getRight(), textProcessor.segment(tuple.getRight()), null))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
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
        double score = similarityCalculator.calculate(first.getKeywords(), second.getKeywords());
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
            if (docSimilarity.getScore() == 0.0) {
                continue;
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
