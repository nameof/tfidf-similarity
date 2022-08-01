package com.nameof.tfidf;

import com.nameof.tfidf.bean.Document;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class TFIDFCalculator {
    public double tf(Document doc, String term, boolean parallel) {
        AtomicInteger result = new AtomicInteger(0);
        Stream<String> stream = parallel ? doc.getTermList().parallelStream() : doc.getTermList().stream();
        stream.forEach(current -> {
            if (term.equals(current))
                result.addAndGet(1);
        });
        return result.doubleValue() / doc.getTermList().size();
    }

    public double idf(List<Document> corpus, String term, boolean parallel) {
        AtomicInteger n = new AtomicInteger(0);
        Stream<Document> stream = parallel ? corpus.parallelStream() : corpus.stream();
        stream.forEach(doc -> {
            if (doc.getTermList().contains(term)) {
                n.addAndGet(1);
            }
        });
        return Math.log10(corpus.size() / n.doubleValue());
    }

    public double tfIdf(Document doc, List<Document> corpus, String term) {
        return tfIdf(doc, corpus, term, true);
    }

    public double tfIdf(Document doc, List<Document> corpus, String term, boolean parallel) {
        return tf(doc, term, parallel) * idf(corpus, term, parallel);
    }
}
