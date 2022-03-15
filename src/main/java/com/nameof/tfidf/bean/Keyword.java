package com.nameof.tfidf.bean;

import lombok.Data;

import java.util.Objects;

@Data
public class Keyword {
    private String term;
    private double weight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Keyword keyword = (Keyword) o;
        return term.equals(keyword.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term);
    }
}
