package com.nameof.tfidf.text;

import cn.hutool.core.util.IdUtil;
import com.nameof.tfidf.text.handler.SimpleTermHandler;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultTextProcessorTest {
    private String text = "TF-IDF是一种统计方法，用以评估一字词对于一个文件集或一个语料库中的其中一份文件的重要程度。";
    DefaultTextProcessor processor = DefaultTextProcessor.builder()
            .termHandlers(Collections.singletonList(new SimpleTermHandler()))
            .build();

    @Test
    public void testTerm() {
        List<String> terms = processor.segment(text);
        for (String term : terms) {
            Assert.assertTrue(term.length() > 1);
            Assert.assertEquals(term, term.toLowerCase());
        }
    }

    @Test
    public void testSimilarity() {
        Set<String> terms1 = new HashSet<>(processor.segment(text));
        Set<String> terms2 = new HashSet<>(processor.segment(text));
        double similarity = processor.similarity(terms1, terms2);
        Assert.assertEquals(1.0, similarity, 0.00000001);

        terms2.add(IdUtil.simpleUUID());
        Assert.assertTrue(processor.similarity(terms1, terms2) < 1.0);
    }
}
