package com.nameof.tfidf.data;

import com.nameof.tfidf.bean.Tuple2;

import java.util.List;
import java.util.Set;

public interface DataLoader {
    /**
     * 获取语料库所有文档名称
     */
    Set<String> getDocNames();

    /**
     * 加载整个语料库文档数据
     * @return 语料库的每个文档名称和文档内容的集合
     */
    List<Tuple2<String, String>> loadCorpusText();
}
