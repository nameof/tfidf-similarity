package com.nameof.tfidf;

import com.nameof.tfidf.bean.DocSimilarity;
import com.nameof.tfidf.bean.Document;
import com.nameof.tfidf.bean.Keyword;
import com.nameof.tfidf.data.DataLoader;

import java.util.List;
import java.util.Set;

public interface TFIDFProcessor {
  List<Document> analyzeAll(DataLoader dataLoader);

    Document analyze(String docName, DataLoader dataLoader);

    Set<Keyword> keyword(String docName, DataLoader dataLoader);

    DocSimilarity similarity(String firstDocName, String secondDocName, DataLoader dataLoader);

    List<DocSimilarity> topSimilarity(int top, DataLoader dataLoader);
}
