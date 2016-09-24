package com.wooz86.mediawiki.impl.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
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
