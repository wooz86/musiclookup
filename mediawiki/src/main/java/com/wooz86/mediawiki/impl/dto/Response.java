package com.wooz86.mediawiki.impl.dto;

import com.google.api.client.util.Key;

public class Response {

    @Key
    private Query query;

    public Query getQuery() {
        return query;
    }
}
