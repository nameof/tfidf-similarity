package com.nameof.tfidf.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    private String name;
    private String text;
    private List<String> termList;
    private Set<Keyword> keywords;

    private Set<String> getTopKeywords(int top) {
        return keywords.stream()
                .sorted(Comparator.reverseOrder())
                .limit(top)
                .map(Keyword::getTerm)
                .collect(Collectors.toSet());
    }
}
