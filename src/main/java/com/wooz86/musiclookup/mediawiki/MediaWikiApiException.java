package com.wooz86.musiclookup.mediawiki;

public class MediaWikiApiException extends Throwable {

    public MediaWikiApiException(String message) {
        super(message);
    }

    public MediaWikiApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
