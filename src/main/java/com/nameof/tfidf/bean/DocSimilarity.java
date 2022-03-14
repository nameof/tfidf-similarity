package com.nameof.tfidf.bean;

import lombok.Data;

@Data
public class DocSimilarity {
    private Document first;
    private Document second;
    private double score;
}
