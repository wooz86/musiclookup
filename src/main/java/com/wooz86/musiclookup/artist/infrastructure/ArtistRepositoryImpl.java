package com.wooz86.musiclookup.artist.infrastructure;

import com.wooz86.musiclookup.artist.domain.model.Artist;
import com.wooz86.musiclookup.artist.domain.model.ArtistRepository;
import com.wooz86.musiclookup.artist.infrastructure.mediawiki.MediaWikiApiException;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.MusicBrainzException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
public class ArtistRepositoryImpl implements ArtistRepository {

    private ArtistRemoteService artistRemoteService;

    @Autowired
    public ArtistRepositoryImpl(ArtistRemoteService artistRemoteService) {
        this.artistRemoteService = artistRemoteService;
    }

    @Override
    @Cacheable(value="artists")
    public Artist getByMBID(UUID mbid) throws MediaWikiApiException, MusicBrainzException, ExecutionException, InterruptedException {
        return artistRemoteService.getByMBID(mbid);
    }
}
