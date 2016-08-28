package com.wooz86.musiclookup.artist.domain.model;

import com.wooz86.musiclookup.artist.infrastructure.ArtistRemoteServiceException;
import com.wooz86.musiclookup.artist.infrastructure.mediawiki.MediaWikiApiException;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.MusicBrainzApiException;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface ArtistRepository {
    Artist getByMBID(UUID mbid) throws MediaWikiApiException, MusicBrainzApiException, ExecutionException, InterruptedException, ArtistRemoteServiceException;
}
