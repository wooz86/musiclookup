package com.wooz86.coverartarchive;

import java.util.UUID;

public interface CoverArtArchiveApi {
    String getImageUrlByMBID(UUID mbid) throws CoverArtArchiveException;
}
