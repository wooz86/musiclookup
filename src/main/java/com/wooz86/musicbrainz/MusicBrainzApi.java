package com.wooz86.musicbrainz;

import java.util.UUID;

public interface MusicBrainzApi {
    MusicBrainzArtist getArtistByMBID(UUID mbid) throws MusicBrainzApiException;
}
