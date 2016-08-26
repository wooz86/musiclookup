package com.wooz86.musiclookup.artist.infrastructure.mediawiki.dto;

import java.io.Serializable;
import java.util.Map;

public class Query implements Serializable {
    private Map<String, Page> pages;

    public Map<String, Page> getPages() {
        return pages;
    }

    public void setPages(Map<String, Page> pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "Query{" +
                "pages='" + pages + '\'' +
                '}';
    }
}
