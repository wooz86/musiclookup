package com.wooz86.mediawiki.impl.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.api.client.util.Key;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page implements Serializable {

    @Key
    private String extract;

    public String getExtract() {
        return extract;
    }
}
