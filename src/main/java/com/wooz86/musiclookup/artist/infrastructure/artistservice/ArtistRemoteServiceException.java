package com.wooz86.musiclookup.artist.infrastructure.artistservice;

public class ArtistRemoteServiceException extends Throwable {
    public ArtistRemoteServiceException(String message) {
        super(message);
    }
    public ArtistRemoteServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
