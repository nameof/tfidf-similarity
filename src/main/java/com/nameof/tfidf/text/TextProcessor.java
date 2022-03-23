package com.nameof.tfidf.text;

import java.util.List;
import java.util.Set;

public interface TextProcessor {
    List<String> segment(String text);
    double similarity(Set<String> firstTextWords, Set<String> secondTextWords);
}
