package com.wooz86.musiclookup.artist.infrastructure.musicbrainz;

public class MusicBrainzApiException extends Throwable {

    public MusicBrainzApiException(String message) {
        super(message);
    }

    public MusicBrainzApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
