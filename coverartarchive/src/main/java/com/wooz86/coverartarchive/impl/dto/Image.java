package com.wooz86.coverartarchive.impl.dto;

import com.google.api.client.util.Key;

public class Image {

    @Key("image")
    private String url;

    public String getUrl() {
        return url;
    }
}
