package com.nameof.tfidf.similarity;

import com.nameof.tfidf.bean.Keyword;
import lombok.NoArgsConstructor;
import org.apache.commons.text.similarity.CosineSimilarity;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于关键词的词频统计构造向量，计算余弦相似度
 */
@NoArgsConstructor
public class SimpleSimilarityCalculator extends AbstractSimilarityCalculator {

    private static final CosineSimilarity COSINE_SIMILARITY = new CosineSimilarity();

    public SimpleSimilarityCalculator(int limit) {
        super(limit);
    }

    @Override
    public double calculate(Set<Keyword> keywords1, Set<Keyword> keywords2) {
        keywords1 = sortByWeightAndLimit(keywords1);
        keywords2 = sortByWeightAndLimit(keywords2);
        Map<CharSequence, Integer> vector1 = keywordsExtractor.apply(keywords1).stream().collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
        Map<CharSequence, Integer> vector2 = keywordsExtractor.apply(keywords2).stream().collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
        return COSINE_SIMILARITY.cosineSimilarity(vector1, vector2);
    }
}
