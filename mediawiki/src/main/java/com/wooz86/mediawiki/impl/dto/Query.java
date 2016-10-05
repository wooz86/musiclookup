package com.wooz86.mediawiki.impl.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Query implements Serializable {

    @Key
    private Map<String, Page> pages;

    public Map<String, Page> getPages() {
        return pages;
    }
}
