package com.nameof.tfidf;

import cn.hutool.core.io.FileUtil;
import com.google.common.base.Preconditions;
import com.nameof.tfidf.bean.DocSimilarity;
import com.nameof.tfidf.bean.Document;
import com.nameof.tfidf.bean.Keyword;
import com.nameof.tfidf.exception.TFIDFException;
import com.nameof.tfidf.text.DefaultTextProcessor;
import com.nameof.tfidf.text.TextProcessor;
import com.nameof.tfidf.text.handler.SimpleTermHandler;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultTFIDFProcessor implements TFIDFProcessor {

    private TextProcessor textProcessor = DefaultTextProcessor.builder()
            .termHandlers(Collections.singletonList(new SimpleTermHandler()))
            .build();

    private TFIDFCalculator tfidfCalculator = new TFIDFCalculator();

    private Function<Set<Keyword>, Set<String>> keywordsExtractor = keywords -> keywords.stream().map(Keyword::getTerm).collect(Collectors.toSet());

    @Override
    public List<Document> analyzeAll(File... corpusFile) {
        List<Document> documentList = loadCorpusData(corpusFile);
        for (Document document : documentList) {
            analyzeKeywords(document, documentList);
        }
        return documentList;
    }

    @Override
    public Document analyze(String docName, File... corpusFile) {
        List<Document> documentList = loadCorpusData(corpusFile);
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

    private List<Document> loadCorpusData(File[] corpusFile) {
        Map<String, String> corpus = new HashMap<>(corpusFile.length);
        for (File file : corpusFile) {
            corpus.put(file.getName(), FileUtil.readString(file, StandardCharsets.UTF_8));
        }

        List<Document> documentList = new ArrayList<>(corpus.size());
        for (Map.Entry<String, String> entry : corpus.entrySet()) {
            Document document = new Document(entry.getKey(), entry.getValue(), textProcessor.segment(entry.getValue()), new HashSet<>());
            documentList.add(document);
        }
        return documentList;
    }

    @Override
    public Set<Keyword> keyword(String docName, File... corpusFile) {
        return analyze(docName, corpusFile).getKeywords();
    }

    @Override
    public DocSimilarity similarity(String firstDocName, String secondDocName, File... corpusFile) {
        List<Document> documentList = loadCorpusData(corpusFile);
        Document first = analyzeKeywords(firstDocName, documentList);
        Document second = analyzeKeywords(secondDocName, documentList);
        return similarity(first, second);
    }

    private DocSimilarity similarity(Document first, Document second) {
        double score = textProcessor.similarity(keywordsExtractor.apply(first.getKeywords()), keywordsExtractor.apply(second.getKeywords()));
        return new DocSimilarity(first, second, score);
    }

    @Override
    public List<DocSimilarity> topSimilarity(int top, File... corpusFile) {
        Preconditions.checkArgument(top > 0, "top must greater than 0");
        List<Document> documentList = analyzeAll(corpusFile);
        Queue<DocSimilarity> queue = new PriorityQueue<>(top);
        for (int i = 0; i < documentList.size() - 1; i++) {
            for (int j = i + 1; j < documentList.size(); j++) {
                DocSimilarity docSimilarity = similarity(documentList.get(i), documentList.get(j));
                if (queue.size() < top || queue.peek().getScore() < docSimilarity.getScore()) {
                    if (queue.size() == top)
                        queue.remove();
                    queue.add(docSimilarity);
                }
            }
        }
        List<DocSimilarity> result = new ArrayList<>(queue);
        Collections.reverse(result);
        return result;
    }
}
