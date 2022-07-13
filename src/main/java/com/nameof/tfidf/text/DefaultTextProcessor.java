package com.nameof.tfidf.text;

import com.hankcs.hanlp.HanLP;
import com.nameof.tfidf.text.handler.TermHandler;
import lombok.Builder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
public class DefaultTextProcessor implements TextProcessor {

    private final List<TermHandler> termHandlers;

    @Override
    public List<String> segment(String text) {
        Stream<String> termStream = HanLP.segment(text).stream().map(t -> t.word);
        if (termHandlers != null) {
            for (TermHandler termHandler : termHandlers) {
                termStream = termStream.filter(termHandler::filter).map(termHandler::map);
            }
        }
        return Collections.unmodifiableList(termStream.collect(Collectors.toList()));
    }
}
