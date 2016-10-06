package com.wooz86.mediawiki;

public class MediaWikiException extends Throwable {

    public MediaWikiException(String message) {
        super(message);
    }

    public MediaWikiException(String message, Throwable cause) {
        super(message, cause);
    }
}
