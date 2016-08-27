package com.wooz86.musiclookup.artist.domain.model;

import com.wooz86.musiclookup.artist.infrastructure.mediawiki.MediaWikiApiException;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.MusicBrainzException;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface ArtistRepository {
    Artist getByMBID(UUID mbid) throws MediaWikiApiException, MusicBrainzException, ExecutionException, InterruptedException;
}
