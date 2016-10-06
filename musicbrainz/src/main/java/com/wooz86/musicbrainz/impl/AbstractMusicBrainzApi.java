package com.wooz86.musicbrainz.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.wooz86.musicbrainz.MusicBrainzException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

abstract class AbstractMusicBrainzApi {

    private MusicBrainzApiConfiguration configuration;
    private NetHttpTransport netHttpTransport;

    AbstractMusicBrainzApi(MusicBrainzApiConfiguration configuration) throws MusicBrainzException {
        this.configuration = configuration;
        setTransport();
    }

    private void setTransport() throws MusicBrainzException {
        try {
            // @todo Extract dependency and use DI instead
            this.netHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable e) {
            throw new MusicBrainzException("Invalid transport.", e);
        }
    }

    URI buildRequestUri(UUID mbid) throws MusicBrainzException {
        String baseUrl = configuration.getBaseUrl();
        String endpoint = configuration.getEndpoint();
        String mbidString = mbid.toString();
        String queryString = configuration.getQueryString();

        String uriString = baseUrl + endpoint + mbidString + queryString;

        try {
            return new URI(uriString);
        } catch (URISyntaxException e) {
            throw new MusicBrainzException("Invalid URL in configuration.", e);
        }
    }

    <T> T dispatchRequest(URI uri, Class<T> responseType) throws MusicBrainzException {
        try {
            HttpRequest httpRequest = buildRequest(uri);
            HttpResponse response = httpRequest.execute();

            return response.parseAs(responseType);
        } catch (Throwable e) {
            throw new MusicBrainzException("MusicBrainz API-request failed.", e);
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
}
