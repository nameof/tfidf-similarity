package com.nameof.tfidf.text;

import java.util.List;

public interface TextProcessor {
    List<String> segment(String text);
}
