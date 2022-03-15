package com.nameof.tfidf;

import com.nameof.tfidf.bean.DocSimilarity;
import com.nameof.tfidf.bean.Document;
import com.nameof.tfidf.bean.Keyword;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface TFIDFProcessor {
    Document analyze(String docName, File...corpusFile);

    Set<Keyword> keyword(String docName, File...corpusFile);

    DocSimilarity similarity(String firstDocName, String secondDocName, File...corpusFile);

    List<DocSimilarity> topSimilarity(int top, File...corpusFile);
}
