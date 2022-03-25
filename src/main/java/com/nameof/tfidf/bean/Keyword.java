package com.nameof.tfidf.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Keyword implements Comparable<Keyword> {
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

    @Override
    public int compareTo(Keyword o) {
        return Double.compare(weight, o.getWeight());
    }
}
