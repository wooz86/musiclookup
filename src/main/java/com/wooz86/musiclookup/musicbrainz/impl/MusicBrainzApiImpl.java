package com.wooz86.musiclookup.musicbrainz.impl;

import com.wooz86.musiclookup.musicbrainz.MusicBrainzAlbum;
import com.wooz86.musiclookup.musicbrainz.MusicBrainzApi;
import com.wooz86.musiclookup.musicbrainz.MusicBrainzApiException;
import com.wooz86.musiclookup.musicbrainz.MusicBrainzArtist;
import com.wooz86.musiclookup.musicbrainz.impl.dto.Artist;
import com.wooz86.musiclookup.musicbrainz.impl.dto.Relation;
import com.wooz86.musiclookup.musicbrainz.impl.dto.ReleaseGroup;
import com.wooz86.musiclookup.musicbrainz.impl.dto.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MusicBrainzApiImpl implements MusicBrainzApi {

    private static final Logger log = LoggerFactory.getLogger(MusicBrainzApiImpl.class);

    private RestOperations restClient;
    private MusicBrainzApiConfiguration configuration;

    @Autowired
    public MusicBrainzApiImpl(RestOperations restClient, MusicBrainzApiConfiguration configuration) {
        this.restClient = restClient;
        this.configuration = configuration;
    }

    public MusicBrainzArtist getArtistByMBID(UUID mbid) throws MusicBrainzApiException {
        String requestUrl = buildRequestUrl(mbid);
        Artist artist = dispatchRequest(requestUrl);

        String wikipediaPageUrl = getWikipediaPageUrlForArtist(artist);
        List<MusicBrainzAlbum> musicBrainzAlbums = getMusicBrainzAlbumsForArtist(artist);

        return new MusicBrainzArtist(mbid, wikipediaPageUrl, musicBrainzAlbums);
    }

    private Artist dispatchRequest(String requestUrl) throws MusicBrainzApiException {
        try {
            return restClient.getForObject(requestUrl, Artist.class);
        } catch (Exception e) {
            throw new MusicBrainzApiException("MusicBrainz API-request failed.", e);
        }
    }

    public List<MusicBrainzAlbum> getMusicBrainzAlbumsForArtist(Artist artist) {
        List<ReleaseGroup> musicBrainzAlbums = getReleaseGroupByType(artist, "Album");

        return musicBrainzAlbums.stream()
                .map(releaseGroup -> new MusicBrainzAlbum(releaseGroup.getId(), releaseGroup.getTitle()))
                .collect(Collectors.toList());
    }

    private List<ReleaseGroup> getReleaseGroupByType(Artist musicBrainzArtist, String releaseGroupType) {
        return musicBrainzArtist.getReleaseGroups()
                .stream()
                .filter(p -> p.getPrimaryType().equals(releaseGroupType))
                .collect(Collectors.toList());
    }

    private String getWikipediaPageUrlForArtist(Artist artist) {
        Url url = getWikipediaRelation(artist).getUrl();
        return url.getResource();
    }

    private Relation getWikipediaRelation(Artist artist) {
        return getRelationsByType(artist, "wikipedia").get(0);
    }

    private List<Relation> getRelationsByType(Artist artist, String relationType) {
        List<Relation> relationsFound = artist.getRelations()
                .stream()
                .filter(p -> p.getType().equals(relationType))
                .collect(Collectors.toList());

        if (relationsFound.isEmpty()) {
            throw new IllegalArgumentException("No relations with type \"" + relationType + "\" found.");
        }

        return relationsFound;
    }

    private String buildRequestUrl(UUID mbid) {
        return configuration.getBaseUrl() + configuration.getEndpoint() + mbid.toString() +
                configuration.getQueryString();
    }
}
