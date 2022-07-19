package com.nameof.tfidf.similarity;

import cn.hutool.core.collection.CollectionUtil;
import com.nameof.tfidf.bean.Keyword;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于关键词的tfidf权重值构造向量，计算余弦相似度
 */
@NoArgsConstructor
public class TfidfSimilarityCalculator extends AbstractSimilarityCalculator {

    public TfidfSimilarityCalculator(int limit) {
        super(limit);
    }

    @Override
    public double calculate(Set<Keyword> keywords1, Set<Keyword> keywords2) {
        keywords1 = sortByWeightAndLimit(keywords1);
        keywords2 = sortByWeightAndLimit(keywords2);

        Collection<String> bagOfWord = CollectionUtil.union(keywordsExtractor.apply(keywords1), keywordsExtractor.apply(keywords2));

        Map<String, Double> map1 = mapKeyword(keywords1);
        Map<String, Double> map2 = mapKeyword(keywords2);

        List<Double> vector1 = new ArrayList<>(bagOfWord.size());
        List<Double> vector2 = new ArrayList<>(bagOfWord.size());
        for (String s : bagOfWord) {
            vector1.add(map1.getOrDefault(s, 0.0));
            vector2.add(map2.getOrDefault(s, 0.0));
        }
        return cosineSimilarity(vector1, vector2);
    }

    private Map<String, Double> mapKeyword(Set<Keyword> keywords) {
        return keywords.stream().collect(Collectors.toMap(Keyword::getTerm, Keyword::getWeight));
    }

    public static double cosineSimilarity(List<Double> vectorA, List<Double> vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            normA += Math.pow(vectorA.get(i), 2);
            normB += Math.pow(vectorB.get(i), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
