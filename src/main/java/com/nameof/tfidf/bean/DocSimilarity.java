package com.nameof.tfidf.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocSimilarity {
    private Document first;
    private Document second;
    private double score;
}
