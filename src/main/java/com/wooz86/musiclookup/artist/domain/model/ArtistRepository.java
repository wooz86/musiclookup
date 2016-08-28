package com.wooz86.musiclookup.artist.domain.model;

import java.util.UUID;

public interface ArtistRepository {
    Artist getByMBID(UUID mbid);
}
