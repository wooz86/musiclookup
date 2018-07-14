package com.wooz86.coverartarchive;

public class CoverArtArchiveException extends RuntimeException {

    public CoverArtArchiveException(String message) {
        super(message);
    }

    public CoverArtArchiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
