package com.nameof.tfidf;

import com.nameof.tfidf.bean.DocSimilarity;
import com.nameof.tfidf.bean.Document;
import com.nameof.tfidf.data.DataLoader;
import com.nameof.tfidf.data.FileDataLoader;
import com.nameof.tfidf.similarity.TfidfSimilarityCalculator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

public class DefaultTFIDFProcessorTest {
    DefaultTFIDFProcessor processor = new DefaultTFIDFProcessor();
    DataLoader dataLoader = new FileDataLoader("C:\\Users\\at\\Desktop\\A");

    @Test
    public void testAnalyzeAll() {
        List<Document> documentList = processor.analyzeAll(dataLoader);
        Assert.assertEquals(dataLoader.getDocNames().size(), documentList.size());
        for (Document document : documentList) {
            Assert.assertFalse(document.getTermList().isEmpty());
            Assert.assertFalse(document.getKeywords().isEmpty());
        }
    }

    @Test
    public void testSimilarity() {
        Iterator<String> iterator = dataLoader.getDocNames().iterator();
        String name1 = iterator.next();
        String name2 = iterator.next();
        DocSimilarity simple = processor.similarity(name1, name2, dataLoader);

        processor.setSimilarityCalculator(new TfidfSimilarityCalculator());
        DocSimilarity tfidf = processor.similarity(name1, name2, dataLoader);
        Assert.assertTrue(simple.getScore() > tfidf.getScore());
    }

    @Test
    public void testTopSimilarity() {
        List<DocSimilarity> top = processor.topSimilarity(10, dataLoader);
        Assert.assertTrue(top.get(0).getScore() >= top.get(1).getScore());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTopSimilarityException() {
        processor.topSimilarity(-1, dataLoader);
    }

    @Test
    public void testParallelCorrect() {
        processor.setParallel(false);
        List<DocSimilarity> serial = processor.topSimilarity(10, dataLoader);

        processor.setParallel(true);
        List<DocSimilarity> parallel = processor.topSimilarity(10, dataLoader);

        Assert.assertEquals(serial.size(), parallel.size());
        for (int i = 0; i < serial.size(); i++) {
            DocSimilarity d1 = serial.get(i);
            DocSimilarity d2 = parallel.get(i);
            Assert.assertEquals(d1.getScore(), d2.getScore(), 0.000001);
            Assert.assertEquals(d1.getFirst().getName(), d2.getFirst().getName());
            Assert.assertEquals(d1.getSecond().getName(), d2.getSecond().getName());
        }

        processor.setParallel(false);
    }
}
