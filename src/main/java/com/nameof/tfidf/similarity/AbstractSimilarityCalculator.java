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

    /**
     * 计算相似度时，取关键词权重值排名前limit的个数来进行计算
     * -1 表示不限制
     */
    protected int limit = -1;

    public AbstractSimilarityCalculator(int limit) {
        Preconditions.checkArgument(limit > 0, "limit must > 0");
        this.limit = limit;
    }

    protected LinkedHashSet<Keyword> sortByWeightAndLimit(Set<Keyword> keywords) {
        Stream<Keyword> sorted = keywords.stream().sorted(Comparator.comparing(Keyword::getWeight).reversed());
        if (limit > 0) {
            sorted = sorted.limit(limit);
        }
        return sorted.collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
