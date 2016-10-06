package com.wooz86.musicbrainz;

public class MusicBrainzException extends Throwable {

    public MusicBrainzException(String message) {
        super(message);
    }

    public MusicBrainzException(String message, Throwable cause) {
        super(message, cause);
    }
}
