package com.wooz86.musiclookup.artist.infrastructure;

import com.wooz86.musiclookup.artist.domain.model.Artist;
import com.wooz86.musiclookup.artist.domain.model.ArtistRepository;
import com.wooz86.musiclookup.artist.infrastructure.artistservice.ArtistRemoteService;
import com.wooz86.musiclookup.artist.infrastructure.artistservice.ArtistRemoteServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ArtistRepositoryImpl implements ArtistRepository {

    private ArtistRemoteService artistRemoteService;

    @Autowired
    public ArtistRepositoryImpl(ArtistRemoteService artistRemoteService) {
        this.artistRemoteService = artistRemoteService;
    }

    @Override
    @Cacheable(value="artists")
    public Artist getByMBID(UUID mbid) {
        try {
            return artistRemoteService.getByMBID(mbid);
        } catch (ArtistRemoteServiceException e) {
            return null; // @todo Return null or Exception? - this is not handled in the DTOAssembler/Controller as of now
        }
    }
}
