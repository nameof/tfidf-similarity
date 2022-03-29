package com.nameof.tfidf;

import com.nameof.tfidf.bean.DocSimilarity;
import com.nameof.tfidf.bean.Document;
import com.nameof.tfidf.data.DataLoader;
import com.nameof.tfidf.data.FileDataLoader;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DefaultTFIDFProcessorTest {
    TFIDFProcessor processor = new DefaultTFIDFProcessor();
    DataLoader dataLoader = new FileDataLoader("D:");

    @Test
    public void testAnalyzeAll() {
        List<Document> documentList = processor.analyzeAll(dataLoader);
        for (Document document : documentList) {
            Assert.assertFalse(document.getTermList().isEmpty());
            Assert.assertFalse(document.getKeywords().isEmpty());
        }
    }

    @Test
    public void testSimilarity() {
        String name = "1.txt";
        DocSimilarity similarity = processor.similarity(name, name, dataLoader);
        Assert.assertEquals(1.0, similarity.getScore(), 0.000001);
    }

    @Test
    public void testTopSimilarity() {
        List<DocSimilarity> top = processor.topSimilarity(10, dataLoader);
        Assert.assertTrue(top.get(0).getScore() > top.get(1).getScore());
    }
}
