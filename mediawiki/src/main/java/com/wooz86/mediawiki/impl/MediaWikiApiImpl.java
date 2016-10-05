package com.wooz86.mediawiki.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.wooz86.mediawiki.MediaWikiApi;
import com.wooz86.mediawiki.MediaWikiApiException;
import com.wooz86.mediawiki.MediaWikiPage;
import com.wooz86.mediawiki.impl.dto.Page;
import com.wooz86.mediawiki.impl.dto.Query;
import com.wooz86.mediawiki.impl.dto.Response;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Map.Entry;

public class MediaWikiApiImpl implements MediaWikiApi {

    private final MediaWikiApiConfiguration configuration;
    private final NetHttpTransport netHttpTransport;

    public MediaWikiApiImpl(MediaWikiApiConfiguration configuration) throws GeneralSecurityException, IOException {
        this.configuration = configuration;
        this.netHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
    }

    public MediaWikiPage getPageByTitle(String pageTitle) throws MediaWikiApiException, URISyntaxException {
        URI requestUri = buildRequestUri(pageTitle);
        Response response = dispatchRequest(requestUri, Response.class);

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

    private Response dispatchRequest(URI uri, Class<Response> responseClass) throws MediaWikiApiException {
        try {
            HttpRequest httpRequest = buildRequest(uri);
            HttpResponse response = httpRequest.execute();

            return response.parseAs(responseClass);
        } catch (Exception e) {
            throw new MediaWikiApiException("Wikipedia API-request failed.", e);
        }
    }

    private HttpRequest buildRequest(URI uri) throws IOException {
        JacksonFactory jacksonFactory = new JacksonFactory();

        HttpRequestFactory requestFactory = netHttpTransport.createRequestFactory(request -> {
            JsonObjectParser parser = new JsonObjectParser(jacksonFactory);
            request.setParser(parser);
        });

        return requestFactory.buildGetRequest(new GenericUrl(uri));
    }

    private URI buildRequestUri(String pageTitle) throws URISyntaxException {
        String uriString = configuration.getBaseUrl() + configuration.getQueryString() + pageTitle;

        return new URI(uriString);
    }
}
