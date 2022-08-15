package com.nameof.tfidf.similarity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.nameof.tfidf.bean.Keyword;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

public class SimilarityCalculatorTest {
    Set<Keyword> keywords1 = CollUtil.newHashSet(new Keyword("love", 0.1), new Keyword("coding", 0.1), new Keyword("forever", 0.3));
    Set<Keyword> keywords2 = CollUtil.newHashSet(new Keyword("love", 0.01), new Keyword("coding", 0.2), new Keyword("forever", 0.4));

    @Test
    public void testCompare() {
        SimilarityCalculator simple = new SimpleSimilarityCalculator();
        SimilarityCalculator tfidf = new TfidfSimilarityCalculator();

        double simpleScore = simple.calculate(keywords1, keywords2);
        Assert.assertEquals(simpleScore, 1.0, 0.00000001);

        double tfidfScore = tfidf.calculate(keywords1, keywords2);
        Assert.assertTrue(tfidfScore < simpleScore);
    }

    @Test
    public void testLimit() {
        SimilarityCalculator simple = new SimpleSimilarityCalculator(2);
        SimilarityCalculator tfidf = new TfidfSimilarityCalculator(2);

        double simpleScore = simple.calculate(keywords1, keywords2);
        Assert.assertEquals(simpleScore, 1.0, 0.00000001);

        double tfidfScore = tfidf.calculate(keywords1, keywords2);
        Assert.assertTrue(tfidfScore < simpleScore);
    }

    @Test
    public void testMinScore() {
        SimilarityCalculator simple = new SimpleSimilarityCalculator(0.6);
        SimilarityCalculator tfidf = new TfidfSimilarityCalculator(0.6);

        double simpleScore = simple.calculate(keywords1, keywords2);
        Assert.assertEquals(simpleScore, 0.0, 0.00000001);

        double tfidfScore = tfidf.calculate(keywords1, keywords2);
        Assert.assertEquals(tfidfScore, 0.0, 0.00000001);
    }
}
