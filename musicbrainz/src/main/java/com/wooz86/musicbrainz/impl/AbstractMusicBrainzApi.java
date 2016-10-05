package com.wooz86.musicbrainz.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.wooz86.musicbrainz.MusicBrainzApiException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.UUID;

abstract class AbstractMusicBrainzApi {
    private final MusicBrainzApiConfiguration configuration;
    private final NetHttpTransport netHttpTransport;

    AbstractMusicBrainzApi(MusicBrainzApiConfiguration configuration) throws GeneralSecurityException, IOException {
        this.configuration = configuration;
        this.netHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
    }

    URI buildRequestUri(UUID mbid) throws MusicBrainzApiException {
        String uriString = configuration.getBaseUrl() + configuration.getEndpoint() + mbid.toString() +
                configuration.getQueryString();
        try {
            return new URI(uriString);
        } catch (URISyntaxException e) {
            throw new MusicBrainzApiException("Invalid URL in configuration.", e);
        }
    }

    <T> T dispatchRequest(URI uri, Class<T> responseType) throws MusicBrainzApiException {
        try {
            HttpRequest httpRequest = buildRequest(uri);
            HttpResponse response = httpRequest.execute();

            return response.parseAs(responseType);
        } catch (Throwable e) {
            throw new MusicBrainzApiException("MusicBrainz API-request failed.", e);
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
