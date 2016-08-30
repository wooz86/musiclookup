package com.wooz86.musiclookup.artist.domain.shared;

public interface ValueObject<T> {
    boolean sameValueAs(T other);
}
