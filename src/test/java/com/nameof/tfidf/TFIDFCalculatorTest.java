package com.nameof.tfidf;

import cn.hutool.core.collection.CollUtil;
import com.nameof.tfidf.bean.Document;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TFIDFCalculatorTest {
    TFIDFCalculator calculator = new TFIDFCalculator();

    @Test
    public void testTfIdf() {
        Document doc1 = new Document("doc1", "", CollUtil.newArrayList("我", "爱", "北京"), null);
        Document doc2 = new Document("doc2", "", CollUtil.newArrayList("我", "爱", "coding"), null);
        List<Document> corpus = Arrays.asList(doc1, doc2);
        double weight1 = calculator.tfIdf(doc1, corpus, "北京");
        double weight2 = calculator.tfIdf(doc2, corpus, "coding");
        Assert.assertEquals(weight1, weight2, 0.0);

        Assert.assertEquals(calculator.tfIdf(doc1, corpus, "我"), 0.0, 0.0);

        doc1.getTermList().add("北京");
        weight1 = calculator.tfIdf(doc1, corpus, "北京");
        weight2 = calculator.tfIdf(doc2, corpus, "coding");
        Assert.assertTrue(weight1 > weight2);
    }
}
