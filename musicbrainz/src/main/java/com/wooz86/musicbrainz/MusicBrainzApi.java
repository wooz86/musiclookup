package com.wooz86.musicbrainz;

import java.util.UUID;

public interface MusicBrainzApi<T> {
    T getByMBID(UUID mbid) throws MusicBrainzException;
}
