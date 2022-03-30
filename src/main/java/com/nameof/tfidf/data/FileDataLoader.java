package com.nameof.tfidf.data;

import cn.hutool.core.io.FileUtil;
import com.nameof.tfidf.bean.Tuple2;
import com.nameof.tfidf.exception.DataLoadException;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileDataLoader implements DataLoader {
    private final String corpusDir;

    public FileDataLoader(String corpusDir) {
        if (!FileUtil.exist(corpusDir)) {
            throw new DataLoadException("directory not exists");
        }
        if (!FileUtil.isDirectory(corpusDir)) {
            throw new DataLoadException("corpusDir must be a directory");
        }
        this.corpusDir = corpusDir;
    }

    @Override
    public Set<String> getDocNames() {
        return new HashSet<>(FileUtil.listFileNames(corpusDir));
    }

    @Override
    public List<Tuple2<String, String>> loadCorpusText() {
        List<Tuple2<String, String>> result = new ArrayList<>();
        for (String fileName : FileUtil.listFileNames(corpusDir)) {
            result.add(new Tuple2<>(fileName, loadDocumentText(fileName)));
        }
        return result;
    }

    private String loadDocumentText(String docName) {
        File file = new File(corpusDir, docName);
        if (!file.exists()) {
            throw new DataLoadException("file not found");
        }
        if (file.isDirectory()) {
            throw new DataLoadException("file is directory");
        }
        return FileUtil.readString(file, StandardCharsets.UTF_8);
    }
}
