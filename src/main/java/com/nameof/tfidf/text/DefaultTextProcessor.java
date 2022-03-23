package com.nameof.tfidf.text;

import com.hankcs.hanlp.HanLP;
import com.nameof.tfidf.text.handler.TermHandler;
import lombok.Builder;
import org.apache.commons.text.similarity.CosineSimilarity;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
public class DefaultTextProcessor implements TextProcessor {

    private final List<TermHandler> termHandlers;

    private static final CosineSimilarity COSINE_SIMILARITY = new CosineSimilarity();

    @Override
    public List<String> segment(String text) {
        Stream<String> termStream = HanLP.segment(text).stream().map(t -> t.word);
        if (termHandlers != null) {
            for (TermHandler termHandler : termHandlers) {
                termStream = termStream.filter(termHandler::filter).map(termHandler::map);
            }
        }
        return termStream.collect(Collectors.toList());
    }

    @Override
    public double similarity(Set<String> firstTextWords, Set<String> secondTextWords) {
        Map<CharSequence, Integer> vector1 = firstTextWords.stream().collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
        Map<CharSequence, Integer> vector2 = secondTextWords.stream().collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
        return COSINE_SIMILARITY.cosineSimilarity(vector1, vector2);
    }
}
