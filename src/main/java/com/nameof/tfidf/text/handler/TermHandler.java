package com.nameof.tfidf.text.handler;

public interface TermHandler {
    boolean filter(String term);
    String map(String term);
}
