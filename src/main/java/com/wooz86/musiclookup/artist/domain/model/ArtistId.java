package com.wooz86.musiclookup.artist.domain.model;

import com.wooz86.musiclookup.artist.domain.shared.ValueObject;

public class ArtistId implements ValueObject<ArtistId> {
    
    private String id;

    public String getId() {
        return id;
    }

    public ArtistId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArtistId other = (ArtistId) o;

        return sameValueAs(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean sameValueAs(ArtistId other) {
        return other != null && this.id.equals(other.id);
    }
}
