package com.wooz86.musiclookup.artist.infrastructure.musicbrainz;

import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto.MusicBrainzArtist;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto.Relation;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MusicBrainzService {

    private static final Logger log = LoggerFactory.getLogger(MusicBrainzService.class);

    private RestOperations restClient;
    private final String baseUrl = "http://musicbrainz.org/ws/2";
    private final String endpoint = "/artist/";
    private final String queryString = "?inc=url-rels+release-groups&fmt=json";

    @Autowired
    public MusicBrainzService(RestOperations restClient) {
        this.restClient = restClient;
    }

    public MusicBrainzArtist getByMBId(UUID mbid) throws MusicBrainzException {
        String requestUrl = buildRequestUrl(mbid);
        MusicBrainzArtist artist = dispatchRequest(requestUrl);

        return artist;
    }

    private MusicBrainzArtist dispatchRequest(String requestUrl) throws MusicBrainzException {
        try {
            return restClient.getForObject(requestUrl, MusicBrainzArtist.class);
        } catch (Exception e) {
            throw new MusicBrainzException("MusicBrainz API-request failed.", e);
        }
    }

    public String getWikipediaPageTitleForArtist(MusicBrainzArtist artist) throws MusicBrainzException {
        String url = getRelationByTypeForArtist(artist, "wikipedia")
                .map(Relation::getUrl)
                .map(Url::getResource)
                .orElse(null);

        return getPageTitleFromUrl(url);
    }

    private String getPageTitleFromUrl(String url) throws MusicBrainzException {

        String[] parts = url.split("/");

        if (parts.length == 0) {
            throw new MusicBrainzException("Wikipedia relation not found."); // @todo Move this code, not correctly placed
        }

        return parts[parts.length-1];
    }

    private Optional<Relation> getRelationByTypeForArtist(MusicBrainzArtist artist, String relationType) {
        List<Relation> relations = artist.getRelations();

        return relations
                .stream()
                .filter(p -> p.getType().equals(relationType))
                .findFirst();
    }

    private String buildRequestUrl(UUID mbid) {
        return baseUrl + endpoint + mbid.toString() + queryString;
    }
}
