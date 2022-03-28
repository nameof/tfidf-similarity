package com.nameof.tfidf.data;

import com.nameof.tfidf.bean.Tuple2;
import java.util.List;

public interface DataLoader {
    /**
     * 加载指定名称的文档数据
     * @param docName
     * @return
     */
    String loadDocumentText(String docName);

    /**
     * 加载整个语料库文档数据
     * @return 语料库的每个文档名称和文档内容的集合
     */
    List<Tuple2<String, String>> loadCorpusText();
}
