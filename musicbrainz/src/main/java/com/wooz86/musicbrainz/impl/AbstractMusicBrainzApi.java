package com.wooz86.musicbrainz.impl;

import com.google.api.client.http.*;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.wooz86.musicbrainz.MusicBrainzException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

abstract class AbstractMusicBrainzApi {

    private MusicBrainzApiConfiguration configuration;
    private HttpTransport transport;

    AbstractMusicBrainzApi(HttpTransport transport, MusicBrainzApiConfiguration configuration) {
        this.transport = transport;
        this.configuration = configuration;
    }

    URI buildRequestUri(UUID mbid)  {
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

    <T> T dispatchRequest(URI uri, Class<T> responseType)  {
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

        HttpRequestFactory requestFactory = transport.createRequestFactory(request -> {
            JsonObjectParser parser = new JsonObjectParser(jacksonFactory);
            request.setParser(parser);
        });

        return requestFactory.buildGetRequest(new GenericUrl(uri));
    }
}
