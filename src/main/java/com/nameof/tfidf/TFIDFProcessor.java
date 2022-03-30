package com.nameof.tfidf;

import com.nameof.tfidf.bean.DocSimilarity;
import com.nameof.tfidf.bean.Document;
import com.nameof.tfidf.bean.Keyword;
import com.nameof.tfidf.data.DataLoader;

import java.util.List;
import java.util.Set;

public interface TFIDFProcessor {
    /**
     * 解析整个语料库文档
     */
    List<Document> analyzeAll(DataLoader dataLoader);

    /**
     * 解析语料库中单个文档
     */
    Document analyze(String docName, DataLoader dataLoader);

    /**
     * 解析语料库中单个文档的关键字信息
     */
    Set<Keyword> keyword(String docName, DataLoader dataLoader);

    /**
     * 计算语料库中2个文档的相似度
     */
    DocSimilarity similarity(String firstDocName, String secondDocName, DataLoader dataLoader);

    /**
     * 计算语料库中top文档相似度
     */
    List<DocSimilarity> topSimilarity(int top, DataLoader dataLoader);
}
