package com.wooz86.musiclookup.musicbrainz.impl.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Relation implements Serializable {
    private String type;
    private Url url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "type=" + type +
                ", url=" + url +
                '}';
    }
}
