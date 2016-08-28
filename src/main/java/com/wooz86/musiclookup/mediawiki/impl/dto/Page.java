package com.wooz86.musiclookup.mediawiki.impl.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page implements Serializable {
    private String extract;

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public String getExtract() {
        return extract;
    }

    @Override
    public String toString() {
        return "Page{" +
                "extract=" + extract +
                '}';
    }
}
