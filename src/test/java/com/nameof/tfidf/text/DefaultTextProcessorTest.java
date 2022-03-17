package com.nameof.tfidf.text;

import com.nameof.tfidf.text.handler.SimpleTermHandler;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class DefaultTextProcessorTest {
    private String text = "TF-IDF是一种统计方法，用以评估一字词对于一个文件集或一个语料库中的其中一份文件的重要程度。";

    @Test
    public void testTerm() {
        DefaultTextProcessor processor = DefaultTextProcessor.builder()
                .termHandlers(Collections.singletonList(new SimpleTermHandler()))
                .build();
        List<String> terms = processor.segment(text);
        for (String term : terms) {
            Assert.assertTrue(term.length() > 1);
            Assert.assertEquals(term, term.toLowerCase());
        }
    }
}
