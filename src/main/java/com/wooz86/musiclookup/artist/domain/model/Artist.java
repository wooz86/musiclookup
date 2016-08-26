package com.wooz86.musiclookup.artist.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Artist {

    private UUID id;
    private String description;
    private List<Album> albums = new ArrayList<>();

    public Artist(UUID id) {
        this.id = id;
    }

    public Artist(UUID id, String description) {
        this.id = id;
        this.description = description;
    }

    public Artist(UUID id, String description, List<Album> albums) {
        this.id = id;
        this.description = description;
        this.albums = albums;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void addAlbum(Album albumToAdd) {
        for(Album album : this.albums) {
            if (album.equals(albumToAdd)) {
                return;
            }
        }

        this.albums.add(albumToAdd);
    }
}
