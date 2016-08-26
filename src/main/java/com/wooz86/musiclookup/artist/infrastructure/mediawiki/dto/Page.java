package com.wooz86.musiclookup.artist.infrastructure.mediawiki.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page implements Serializable {
    private String extract;

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    @Override
    public String toString() {
        return "Page{" +
                "extract=" + extract +
                '}';
    }
}
