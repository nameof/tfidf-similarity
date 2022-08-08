package com.nameof.tfidf;

import com.nameof.tfidf.bean.Document;

import java.util.List;

public class TFIDFCalculator {
    public double tf(Document doc, String term) {
        long count = doc.getTermList().parallelStream().filter(term::equals).count();
        return count * 1.0 / doc.getTermList().size();
    }

    public double idf(List<Document> corpus, String term) {
        long count = corpus.parallelStream().filter(doc -> doc.getTermList().contains(term)).count();
        return Math.log10(corpus.size() / (count * 1.0));
    }

    public double tfIdf(Document doc, List<Document> corpus, String term) {
        return tf(doc, term) * idf(corpus, term);
    }
}
