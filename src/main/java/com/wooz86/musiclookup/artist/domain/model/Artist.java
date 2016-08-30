package com.wooz86.musiclookup.artist.domain.model;

import com.wooz86.musiclookup.artist.domain.shared.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Artist implements Entity<Artist> {

    private ArtistId artistId;
    private UUID MBID;
    private String description;
    private List<Album> albums = new ArrayList<>();

    public Artist(ArtistId artistId, UUID mbid) {
        this.artistId = artistId;
        this.MBID = mbid;
    }

    public Artist(ArtistId artistId, UUID mbid, String description) {
        this.artistId = artistId;
        this.MBID = mbid;
        this.description = description;
    }

    public Artist(UUID mbid, String description, List<Album> albums) {
        this.MBID = mbid;
        this.description = description;
        this.albums = albums;
    }

    public Artist(ArtistId artistId, UUID mbid, String description, List<Album> albums) {
        this.artistId = artistId;
        this.MBID = mbid;
        this.description = description;
        this.albums = albums;
    }

    public ArtistId getArtistId() {
        return artistId;
    }

    public UUID getMBID() {
        return MBID;
    }

    public String getDescription() {
        return description;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        final Artist other = (Artist) object;

        return sameIdentityAs(other);
    }

//    @Override
//    public int hashCode() {
//        return artistId.hashCode();
//    }
//
//    @Override
//    public String toString() {
//        return artistId.toString();
//    }

    @Override
    public boolean sameIdentityAs(final Artist other) {
        return other != null && artistId.sameValueAs(other.artistId);
    }
}
