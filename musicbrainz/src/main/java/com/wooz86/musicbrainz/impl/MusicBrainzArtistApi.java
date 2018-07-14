package com.wooz86.musicbrainz.impl;

import com.google.api.client.http.HttpTransport;
import com.wooz86.musicbrainz.MusicBrainzAlbum;
import com.wooz86.musicbrainz.MusicBrainzApi;
import com.wooz86.musicbrainz.MusicBrainzArtist;
import com.wooz86.musicbrainz.MusicBrainzException;
import com.wooz86.musicbrainz.impl.dto.Artist;
import com.wooz86.musicbrainz.impl.dto.Relation;
import com.wooz86.musicbrainz.impl.dto.ReleaseGroup;
import com.wooz86.musicbrainz.impl.dto.Url;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MusicBrainzArtistApi extends AbstractMusicBrainzApi implements MusicBrainzApi<MusicBrainzArtist> {

    public MusicBrainzArtistApi(HttpTransport transport, MusicBrainzApiConfiguration configuration) {
        super(transport, configuration);
    }

    public MusicBrainzArtist getByMBID(UUID mbid) throws MusicBrainzException {
        URI requestUri = buildRequestUri(mbid);

        Artist artist = dispatchRequest(requestUri, Artist.class);
        String wikipediaPageUrl = getWikipediaPageUrl(artist);
        List<MusicBrainzAlbum> musicBrainzAlbums = getAlbums(artist);

        return new MusicBrainzArtist(mbid, wikipediaPageUrl, musicBrainzAlbums);
    }

    private List<MusicBrainzAlbum> getAlbums(Artist artist) {
        List<ReleaseGroup> albumsReleaseGroups = artist.getReleaseGroupByType("Album");

        return albumsReleaseGroups.stream()
                .map(this::getMusicBrainzAlbum)
                .collect(Collectors.toList());
    }

    private MusicBrainzAlbum getMusicBrainzAlbum(ReleaseGroup releaseGroup) {
        String releaseGroupId = releaseGroup.getId();
        UUID id = UUID.fromString(releaseGroupId);
        String title = releaseGroup.getTitle();

        return new MusicBrainzAlbum(id, title);
    }

    private String getWikipediaPageUrl(Artist artist) {
        Relation wikipediaRelation = getWikipediaRelation(artist);
        Url url = wikipediaRelation.getUrl();
        String urlResource = url.getResource();

        return urlResource;
    }

    private Relation getWikipediaRelation(Artist artist) {
        List<Relation> wikipedia = artist.getRelationsByType("wikipedia");
        Relation wikipediaRelation = wikipedia.get(0);

        return wikipediaRelation;
    }
}
