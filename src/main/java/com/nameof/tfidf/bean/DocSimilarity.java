package com.nameof.tfidf.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocSimilarity implements Comparable<DocSimilarity> {
    private Document first;
    private Document second;
    private double score;

    @Override
    public int compareTo(DocSimilarity o) {
        return Double.compare(score, o.getScore());
    }
}
