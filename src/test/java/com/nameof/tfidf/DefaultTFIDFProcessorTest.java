package com.nameof.tfidf;

import cn.hutool.core.io.FileUtil;
import com.nameof.tfidf.bean.DocSimilarity;
import com.nameof.tfidf.bean.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class DefaultTFIDFProcessorTest {
    TFIDFProcessor processor = new DefaultTFIDFProcessor();
    String corpusPath = "D:";

    @Test
    public void testAnalyzeAll() {
        File[] files = FileUtil.ls(corpusPath);
        List<Document> documentList = processor.analyzeAll(files);
        Assert.assertEquals(files.length, documentList.size());
        for (Document document : documentList) {
            Assert.assertFalse(document.getTermList().isEmpty());
            Assert.assertFalse(document.getKeywords().isEmpty());
        }
    }

    @Test
    public void testSimilarity() {
        File[] files = FileUtil.ls(corpusPath);
        String name = files[0].getName();
        DocSimilarity similarity = processor.similarity(name, name, files);
        Assert.assertEquals(1.0, similarity.getScore(), 0.0);
    }

    @Test
    public void testTopSimilarity() {
        File[] files = FileUtil.ls(corpusPath);
        List<DocSimilarity> top = processor.topSimilarity(10, files);
        Assert.assertTrue(top.get(0).getScore() > top.get(1).getScore());
    }
}
