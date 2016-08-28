package com.wooz86.musiclookup.artist.infrastructure.musicbrainz;

import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto.MusicBrainzArtist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.UUID;

@Service
public class MusicBrainzApi {

    private static final Logger log = LoggerFactory.getLogger(MusicBrainzApi.class);

    private RestOperations restClient;
    private final String baseUrl = "http://musicbrainz.org/ws/2";
    private final String endpoint = "/artist/";
    private final String queryString = "?inc=url-rels+release-groups&fmt=json";

    @Autowired
    public MusicBrainzApi(RestOperations restClient) {
        this.restClient = restClient;
    }

    public MusicBrainzArtist getByMBId(UUID mbid) throws MusicBrainzApiException {
        String requestUrl = buildRequestUrl(mbid);
        MusicBrainzArtist artist = dispatchRequest(requestUrl);

        return artist;
    }

    private MusicBrainzArtist dispatchRequest(String requestUrl) throws MusicBrainzApiException {
        try {
            return restClient.getForObject(requestUrl, MusicBrainzArtist.class);
        } catch (Exception e) {
            throw new MusicBrainzApiException("MusicBrainz API-request failed.", e);
        }
    }

    private String buildRequestUrl(UUID mbid) {
        return baseUrl + endpoint + mbid.toString() + queryString;
    }
}
