package com.wooz86.musiclookup.artist.infrastructure;

import com.wooz86.musiclookup.artist.domain.model.Artist;
import com.wooz86.musiclookup.artist.domain.model.ArtistId;
import com.wooz86.musiclookup.artist.domain.model.ArtistRepository;
import com.wooz86.musiclookup.artist.infrastructure.artistservice.ArtistRemoteService;
import com.wooz86.musiclookup.artist.infrastructure.artistservice.ArtistRemoteServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.UUID;

@Component
public class ArtistRepositoryImpl implements ArtistRepository {

    private ArtistRemoteService artistRemoteService;

    @Autowired
    public ArtistRepositoryImpl(ArtistRemoteService artistRemoteService) {
        this.artistRemoteService = artistRemoteService;
    }

    @Override
    @Cacheable(value = "artists")
    public Artist getByMBID(UUID mbid) throws URISyntaxException {
        Artist artist = tryGetByMBID(mbid);

        if (artist != null && artist.getArtistId() == null) {
            artist = new Artist(getNextArtistId(), artist.getMBID(), artist.getDescription(), artist.getAlbums());
        }

        return artist;
    }

    private Artist tryGetByMBID(UUID mbid) throws URISyntaxException {
        try {
            return artistRemoteService.getByMBID(mbid);
        } catch (ArtistRemoteServiceException e) {
            return null;
        }
    }

    @Override
    public ArtistId getNextArtistId() {
        final String randomUuidString = UUID.randomUUID().toString().toUpperCase();
        return new ArtistId(randomUuidString);
    }
}
