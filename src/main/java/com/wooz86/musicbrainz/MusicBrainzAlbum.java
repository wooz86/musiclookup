package com.wooz86.musicbrainz;

import java.util.UUID;

public class MusicBrainzAlbum {
    private UUID id;
    private String title;

    public MusicBrainzAlbum(UUID id) {
        this.id = id;
    }

    public MusicBrainzAlbum(UUID id, String title) {
        this.id = id;
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
