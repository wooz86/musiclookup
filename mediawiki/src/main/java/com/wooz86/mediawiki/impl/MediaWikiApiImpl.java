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
import com.wooz86.mediawiki.MediaWikiException;
import com.wooz86.mediawiki.MediaWikiPage;
import com.wooz86.mediawiki.impl.dto.Page;
import com.wooz86.mediawiki.impl.dto.Query;
import com.wooz86.mediawiki.impl.dto.Response;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MediaWikiApiImpl implements MediaWikiApi {

    private MediaWikiApiConfiguration configuration;
    private NetHttpTransport netHttpTransport;

    public MediaWikiApiImpl(MediaWikiApiConfiguration configuration)  {
        this.configuration = configuration;
        setTransport();
    }

    private void setTransport()  {
        try {
            // @todo Extract dependency and use DI instead
            this.netHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable e) {
            throw new MediaWikiException("Invalid transport.", e);
        }
    }

    public MediaWikiPage getPageByTitle(String pageTitle)  {
        URI requestUri = buildRequestUri(pageTitle);
        Response response = dispatchRequest(requestUri, Response.class);

        Entry<String, Page> pageEntry = getFirstPageEntry(pageTitle, response);

        String pageId = pageEntry.getKey();
        Page page = pageEntry.getValue();

        return new MediaWikiPage(pageId, pageTitle, page.getDescription());
    }

    private Entry<String, Page> getFirstPageEntry(String pageTitle, Response response)  {
        Query query = response.getQuery();
        Map<String, Page> pages = query.getPages();
        Set<Entry<String, Page>> entries = pages.entrySet();
        Iterator<Entry<String, Page>> entriesIterator = entries.iterator();
        Entry<String, Page> pageEntry = entriesIterator.next();
        String key = pageEntry.getKey();

        if (key.equals("-1")) {
            throw new MediaWikiException("Wikipedia page with title \"" + pageTitle + "\" not found.");
        }

        return pageEntry;
    }

    private Response dispatchRequest(URI uri, Class<Response> responseClass)  {
        try {
            HttpRequest httpRequest = buildRequest(uri);
            HttpResponse response = httpRequest.execute();

            return response.parseAs(responseClass);
        } catch (Throwable e) {
            throw new MediaWikiException("Wikipedia API-request failed.", e);
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

    private URI buildRequestUri(String pageTitle)  {
        String uriString = configuration.getBaseUrl() + configuration.getQueryString() + pageTitle;

        try {
            return new URI(uriString);
        } catch (Throwable e) {
            throw new MediaWikiException("Invalid URI syntax.", e);
        }
    }
}
