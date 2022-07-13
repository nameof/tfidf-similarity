package com.nameof.tfidf.similarity;

import com.nameof.tfidf.bean.Keyword;

import java.util.Set;

public interface SimilarityCalculator {
    double calculate(Set<Keyword> keywords1, Set<Keyword> keywords2);
}
