package com.wooz86.musicbrainz.impl.dto;

import com.google.api.client.util.Key;

public class ReleaseGroup {

    @Key
    private String id;

    @Key("primary-type")
    private String primaryType;

    @Key
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public String getTitle() {
        return title;
    }
}
