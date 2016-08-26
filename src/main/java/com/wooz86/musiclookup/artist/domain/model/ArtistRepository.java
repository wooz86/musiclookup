package com.wooz86.musiclookup.artist.domain.model;

import com.wooz86.musiclookup.artist.infrastructure.mediawiki.MediaWikiApiException;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.MusicBrainzException;

import java.util.UUID;

public interface ArtistRepository {
    Artist getByMBID(UUID mbid) throws MediaWikiApiException, MusicBrainzException;
}
