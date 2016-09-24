package com.wooz86.musicbrainz.impl.dto;

import com.google.api.client.util.Key;

public class Url {

    @Key
    private String resource;

    public String getResource() {
        return resource;
    }
}
