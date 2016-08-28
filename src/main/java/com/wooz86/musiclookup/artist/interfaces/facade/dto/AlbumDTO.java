package com.wooz86.musiclookup.artist.interfaces.facade.dto;

import java.io.Serializable;
import java.util.UUID;

public class AlbumDTO implements Serializable {
    private UUID id;
    private String title;
    private String image;

    public AlbumDTO(UUID id, String title) {
        this.id = id;
        this.title = title;
    }

    public AlbumDTO(UUID id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public UUID getId() {

        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
