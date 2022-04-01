# tfidf-similarity
基于tfidf的文档关键词提取、文档相似度计算

## 理论知识
* tf-idf是一种加权技术，用以评估一个字词对一个文件集或语料库中的其中一份文件的重要程度。
* 字词的重要性随着它在文件中出现的次数成正比增加，但同时会随着它在语料库中出现的频率成反比下降。
* 所以对于一个语料库，可以计算得到其所有文档的关键词；基于每个文档的关键词，可以计算它们之间的相似度

## 使用
```
    mvn clean install -DskipTests
```
```
    <dependency>
        <groupId>com.nameof</groupId>
        <artifactId>tfidf-similarity</artifactId>
        <version>1.0.0</version>
    </dependency>
```

1.提取关键字
```
    TFIDFProcessor processor = new DefaultTFIDFProcessor();
    DataLoader dataLoader = new FileDataLoader(corpusDir);
    processor.keyword(docName, dataLoader).forEach(System.out::println);
```

2.计算文档相似度
```
    TFIDFProcessor processor = new DefaultTFIDFProcessor();
    DataLoader dataLoader = new FileDataLoader(corpusDir);
    System.out.println(processor.similarity(doc1Name, doc2Name, dataLoader));
```

3.寻找语料库中相似度排行前5的文档
```
    TFIDFProcessor processor = new DefaultTFIDFProcessor();
    DataLoader dataLoader = new FileDataLoader(corpusDir);
    processor.topSimilarity(5, dataLoader).forEach(System.out::println);
```