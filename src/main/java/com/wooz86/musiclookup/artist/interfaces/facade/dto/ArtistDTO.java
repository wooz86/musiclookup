package com.wooz86.musiclookup.artist.interfaces.facade.dto;

import java.util.List;
import java.util.UUID;

public class ArtistDTO {

    private UUID mbid;
    private String description;
    private List<AlbumDTO> albums;

    public ArtistDTO() {
    }

    public ArtistDTO(UUID mbid, String description, List<AlbumDTO> albums) {

        this.mbid = mbid;
        this.description = description;
        this.albums = albums;
    }

    public UUID getMbid() {
        return mbid;
    }

    public void setMbid(UUID mbid) {
        this.mbid = mbid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AlbumDTO> getAlbums() {
        return albums;
    }

    public void setAlbums(List<AlbumDTO> albums) {
        this.albums = albums;
    }
}
