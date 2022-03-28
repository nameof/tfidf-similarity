package com.nameof.tfidf.data;

import cn.hutool.core.io.FileUtil;
import com.nameof.tfidf.bean.Tuple2;
import com.nameof.tfidf.exception.DataLoadException;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
    public String loadDocumentText(String docName) {
        File file = new File(corpusDir, docName);
        if (!file.exists()) {
            throw new DataLoadException("file not found");
        }
        if (file.isDirectory()) {
            throw new DataLoadException("file is directory");
        }
        return FileUtil.readString(file, StandardCharsets.UTF_8);
    }

    @Override
    public List<Tuple2<String, String>> loadCorpusText() {
        List<Tuple2<String, String>> result = new ArrayList<>();
        for (File file : FileUtil.ls(corpusDir)) {
            if (file.isDirectory()) {
                continue;
            }
            result.add(new Tuple2<>(file.getName(), loadDocumentText(file.getName())));
        }
        return result;
    }
}
