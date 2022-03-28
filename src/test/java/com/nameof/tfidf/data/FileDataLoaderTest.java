package com.nameof.tfidf.data;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.nameof.tfidf.bean.Tuple2;
import com.nameof.tfidf.exception.DataLoadException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileDataLoaderTest {

    @Test(expected = DataLoadException.class)
    public void testDirMissing() {
        new FileDataLoader(IdUtil.simpleUUID());
    }

    @Test
    public void testLoadSingle() {
        String dir = "/TEMPDIR";
        String fileName = "temp.txt";
        String text = "TF-IDF是一种统计方法，用以评估一字词对于一个文件集或一个语料库中的其中一份文件的重要程度。";
        FileUtil.writeString(text, new File(dir, fileName), StandardCharsets.UTF_8);

        FileDataLoader loader = new FileDataLoader(dir);
        Assert.assertEquals(loader.loadDocumentText(fileName), text);

        FileUtil.del(dir);
    }

    @Test
    public void testLoadAll() {
        String dir = "/TEMPDIR";
        String text = "TF-IDF是一种统计方法，用以评估一字词对于一个文件集或一个语料库中的其中一份文件的重要程度。";
        FileUtil.writeString(text, new File(dir, IdUtil.simpleUUID()), StandardCharsets.UTF_8);
        FileUtil.writeString(text, new File(dir, IdUtil.simpleUUID()), StandardCharsets.UTF_8);

        FileDataLoader loader = new FileDataLoader(dir);
        List<Tuple2<String, String>> corpusText = loader.loadCorpusText();
        Assert.assertEquals(2, corpusText.size());

        List<String> fileNames = FileUtil.listFileNames(dir);
        for (Tuple2<String, String> docNameText : corpusText) {
            Assert.assertTrue(fileNames.contains(docNameText.getLeft()));
            Assert.assertEquals(docNameText.getRight(), text);
        }

        FileUtil.del(dir);
    }
}
