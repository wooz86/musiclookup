package com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Url implements Serializable {
    private String resource;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "Url{" +
                "resource=" + resource +
                '}';
    }
}
