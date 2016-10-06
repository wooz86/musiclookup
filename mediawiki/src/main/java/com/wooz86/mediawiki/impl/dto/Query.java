package com.wooz86.mediawiki.impl.dto;

import com.google.api.client.util.Key;

import java.util.Map;

public class Query {

    @Key
    private Map<String, Page> pages;

    public Map<String, Page> getPages() {
        return pages;
    }
}
