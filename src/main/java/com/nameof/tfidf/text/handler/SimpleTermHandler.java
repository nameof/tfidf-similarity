package com.nameof.tfidf.text.handler;

import cn.hutool.core.util.StrUtil;

public class SimpleTermHandler implements TermHandler {
    @Override
    public boolean filter(String term) {
        return StrUtil.isNotBlank(term) && term.length() > 1;
    }

    @Override
    public String map(String term) {
        return term.toLowerCase();
    }
}
