package com.wooz86.musiclookup.artist.domain.shared;

public interface Entity<T> {
    boolean sameIdentityAs(T other);
}
