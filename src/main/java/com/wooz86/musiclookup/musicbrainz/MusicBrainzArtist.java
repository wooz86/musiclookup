package com.wooz86.musiclookup.musicbrainz;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MusicBrainzArtist {
    private final UUID id;
    private String wikipediaPageUrl;
    private List<MusicBrainzAlbum> albums = new ArrayList<>();

    public MusicBrainzArtist(UUID id, String wikipediaPageUrl, List<MusicBrainzAlbum> albums) {
        this.id = id;
        this.wikipediaPageUrl = wikipediaPageUrl;
        this.albums = albums;
    }

    public UUID getId() {
        return id;
    }

    public String getWikipediaPageUrl() {
        return wikipediaPageUrl;
    }

    public List<MusicBrainzAlbum> getAlbums() {
        return albums;
    }
}
