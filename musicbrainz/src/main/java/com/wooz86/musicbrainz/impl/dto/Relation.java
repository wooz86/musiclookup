package com.wooz86.musicbrainz.impl.dto;

import com.google.api.client.util.Key;

public class Relation {

    @Key
    private String type;

    @Key
    private Url url;

    public String getType() {
        return type;
    }

    public Url getUrl() {
        return url;
    }
}
