package com.wooz86.coverartarchive.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.wooz86.coverartarchive.CoverArtArchiveApi;
import com.wooz86.coverartarchive.CoverArtArchiveException;
import com.wooz86.coverartarchive.impl.dto.Image;
import com.wooz86.coverartarchive.impl.dto.Response;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

public class CoverArtArchiveApiImpl implements CoverArtArchiveApi {

    private Configuration configuration;
    private NetHttpTransport transport;

    public CoverArtArchiveApiImpl(Configuration configuration) throws CoverArtArchiveException {
        this.configuration = configuration;
        setTransport();
    }

    private void setTransport() throws CoverArtArchiveException {
        try {
            // @todo Extract dependency and use DI instead
            this.transport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable e) {
            throw new CoverArtArchiveException("Invalid transport.", e);
        }
    }

    public String getImageUrlByMBID(UUID mbid) throws CoverArtArchiveException {
        URI requestUri = buildRequestUri(mbid);

        Response response = dispatchRequest(requestUri, Response.class);

        List<Image> images = response.getImages();
        Image firstImage = images.get(0);

        return firstImage.getUrl();
    }

    private Response dispatchRequest(URI uri, Class<Response> responseClass) throws CoverArtArchiveException {
        try {
            HttpRequest httpRequest = buildRequest(uri);
            HttpResponse response = httpRequest.execute();

            return response.parseAs(responseClass);
        } catch (Throwable e) {
            throw new CoverArtArchiveException("Could not find cover art for album.", e);
        }
    }

    private HttpRequest buildRequest(URI uri) throws IOException {
        JacksonFactory jacksonFactory = new JacksonFactory();

        HttpRequestFactory requestFactory = transport.createRequestFactory(request -> {
            JsonObjectParser parser = new JsonObjectParser(jacksonFactory);
            request.setParser(parser);
        });

        return requestFactory.buildGetRequest(new GenericUrl(uri));
    }

    private URI buildRequestUri(UUID mbid) throws CoverArtArchiveException {
        String uriString = configuration.getBaseUrl() + configuration.getEndpoint() + mbid.toString();

        try {
            return new URI(uriString);
        } catch (Throwable e) {
            throw new CoverArtArchiveException("Invalid syntax for URI.", e);
        }
    }
}
