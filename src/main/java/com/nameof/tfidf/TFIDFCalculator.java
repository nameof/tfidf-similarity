package com.nameof.tfidf;

import com.nameof.tfidf.bean.Document;

import java.util.List;

public class TFIDFCalculator {
    public double tf(Document doc, String term) {
        double result = 0;
        for (String current : doc.getTermList()) {
            if (term.equalsIgnoreCase(current))
                result++;
        }
        return result / doc.getTermList().size();
    }

    public double idf(List<Document> corpus, String term) {
        double n = 0;
        for (Document doc : corpus) {
            for (String current : doc.getTermList()) {
                if (term.equalsIgnoreCase(current)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(corpus.size() / n);
    }

    public double tfIdf(Document doc, List<Document> corpus, String term) {
        return tf(doc, term) * idf(corpus, term);
    }
}
