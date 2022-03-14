package com.nameof.tfidf.bean;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class Document {
    private String name;
    private List<String> wordList;
    private Set<Keyword> keywords;
}
