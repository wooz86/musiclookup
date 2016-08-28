package com.wooz86.musiclookup.mediawiki.impl;

import com.wooz86.musiclookup.mediawiki.*;
import com.wooz86.musiclookup.mediawiki.impl.dto.Page;
import com.wooz86.musiclookup.mediawiki.impl.dto.Query;
import com.wooz86.musiclookup.mediawiki.impl.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.Map;
import java.util.Map.Entry;

@Service
public class MediaWikiApiImpl implements MediaWikiApi {
    private static final Logger log = LoggerFactory.getLogger(MediaWikiApiImpl.class);

    private final RestOperations restClient;
    private final MediaWikiApiConfiguration configuration;

    @Autowired
    public MediaWikiApiImpl(RestOperations restClient, MediaWikiApiConfiguration configuration) {
        this.restClient = restClient;
        this.configuration = configuration;
    }

    public MediaWikiPage getPageByTitle(String pageTitle) throws MediaWikiApiException {
        String requestUrl = buildRequestUrl(pageTitle);
        Response response = dispatchRequest(requestUrl);

        Entry<String, Page> pageEntry = getFirstPageEntry(pageTitle, response);

        String pageId = pageEntry.getKey();
        Page page = pageEntry.getValue();

        return new MediaWikiPage(pageId, pageTitle, page.getExtract());
    }

    private Entry<String, Page> getFirstPageEntry(String pageTitle, Response response) throws MediaWikiApiException {
        Query query = response.getQuery();
        Map<String, Page> pages = query.getPages();
        Entry<String, Page> pageEntry = pages.entrySet().iterator().next();

        if (pageEntry.getKey().equals("-1")) {
            throw new MediaWikiApiException("Wikipedia page with title \"" + pageTitle + "\" not found.");
        }
        return pageEntry;
    }

    private Response dispatchRequest(String requestUrl) throws MediaWikiApiException {
        try {
            return restClient.getForObject(requestUrl, Response.class);
        } catch (Exception e) {
            throw new MediaWikiApiException("Wikipedia API-request failed.", e);
        }
    }

    private String buildRequestUrl(String pageTitle) {
        return configuration.getBaseUrl() + configuration.getQueryString() + pageTitle;
    }
}
