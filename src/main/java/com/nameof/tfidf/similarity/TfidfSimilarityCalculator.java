package com.nameof.tfidf.similarity;

import com.nameof.tfidf.bean.Keyword;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于关键词的tfidf权重值构造向量，计算余弦相似度
 */
public class TfidfSimilarityCalculator implements SimilarityCalculator {
    private final Function<Set<Keyword>, Set<String>> keywordsExtractor = keywords -> keywords.stream().map(Keyword::getTerm).collect(Collectors.toSet());

    @Override
    public double calculate(Set<Keyword> keywords1, Set<Keyword> keywords2) {
        keywords1 = sortByWeight(keywords1);
        keywords2 = sortByWeight(keywords2);

        Set<String> bagOfWord = keywordsExtractor.apply(keywords1);
        bagOfWord.addAll(keywordsExtractor.apply(keywords2));

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

    private LinkedHashSet<Keyword> sortByWeight(Set<Keyword> keywords) {
        return keywords.stream().sorted(Comparator.comparing(Keyword::getWeight).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
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
