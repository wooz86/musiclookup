package com.wooz86.musiclookup.artist.domain.model;

import java.util.UUID;

public class Album {
    private UUID id;
    private String title;
    private String image;

    public Album(UUID id, String title) {
        this.id = id;
        this.title = title;
    }

    public Album(UUID id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
}
