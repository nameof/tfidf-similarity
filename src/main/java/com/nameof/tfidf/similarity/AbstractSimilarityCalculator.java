package com.nameof.tfidf.similarity;

import com.google.common.base.Preconditions;
import com.nameof.tfidf.bean.Keyword;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
public abstract class AbstractSimilarityCalculator implements SimilarityCalculator {

    protected final Function<Set<Keyword>, Set<String>> keywordsExtractor = keywords -> keywords.stream().map(Keyword::getTerm).collect(Collectors.toSet());

    private static final int DEFAULT_LIMIT = 0;
    private static final double DEFAULT_MIN_SCORE = 0.0;

    /**
     * 计算相似度时，取关键词权重值排名前limit的个数来进行计算
     * 0 表示不限制
     */
    protected int limit = DEFAULT_LIMIT;

    protected double minScore = DEFAULT_MIN_SCORE;

    public AbstractSimilarityCalculator(int limit) {
        this(limit, DEFAULT_MIN_SCORE);
    }

    public AbstractSimilarityCalculator(double minScore) {
        this(DEFAULT_LIMIT, minScore);
    }

    public AbstractSimilarityCalculator(int limit, double minScore) {
        Preconditions.checkArgument(limit >= 0, "limit must >= 0");
        Preconditions.checkArgument(minScore >= DEFAULT_MIN_SCORE, "minScore must >= 0.0");
        this.limit = limit;
        this.minScore = minScore;
    }

    protected LinkedHashSet<Keyword> sortByWeightWithLimitAndMinScore(Set<Keyword> keywords) {
        Stream<Keyword> sorted = keywords.stream()
                .filter(k -> k.getWeight() >= minScore)
                .sorted(Comparator.comparing(Keyword::getWeight).reversed());
        if (limit > 0) {
            sorted = sorted.limit(limit);
        }
        return sorted.collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
