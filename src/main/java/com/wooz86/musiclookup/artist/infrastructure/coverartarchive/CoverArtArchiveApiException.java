package com.wooz86.musiclookup.artist.infrastructure.coverartarchive;

public class CoverArtArchiveApiException extends Throwable {

    public CoverArtArchiveApiException(String message) {
        super(message);
    }

    public CoverArtArchiveApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
