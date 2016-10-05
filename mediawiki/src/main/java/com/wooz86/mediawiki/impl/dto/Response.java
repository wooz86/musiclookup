package com.wooz86.mediawiki.impl.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.api.client.util.Key;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response implements Serializable {

    @Key
    private Query query;

    public Query getQuery() {
        return query;
    }
}
