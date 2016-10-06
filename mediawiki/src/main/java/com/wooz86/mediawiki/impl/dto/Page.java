package com.wooz86.mediawiki.impl.dto;

import com.google.api.client.util.Key;

public class Page {

    @Key("extract")
    private String description;

    public String getDescription() {
        return description;
    }
}
