package com.wooz86.musicbrainz;

public class MusicBrainzApiException extends Throwable {

    public MusicBrainzApiException(String message) {
        super(message);
    }

    public MusicBrainzApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
