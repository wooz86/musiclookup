package com.wooz86.coverartarchive.impl.dto;

import com.google.api.client.util.Key;

import java.util.List;

public class Response {

    @Key
    private List<Image> images;

    public List<Image> getImages() {
        return images;
    }
}
