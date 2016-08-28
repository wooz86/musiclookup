package com.wooz86.musiclookup.coverartarchive;

import java.util.UUID;

public interface CoverArtArchiveApi {
    String getImageUrlByMBID(UUID mbid) throws CoverArtArchiveApiException;
}
