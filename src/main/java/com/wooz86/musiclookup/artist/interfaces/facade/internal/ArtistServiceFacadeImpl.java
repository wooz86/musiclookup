package com.wooz86.musiclookup.artist.interfaces.facade.internal;

import com.wooz86.musiclookup.artist.domain.model.Artist;
import com.wooz86.musiclookup.artist.domain.model.ArtistRepository;
import com.wooz86.musiclookup.artist.interfaces.facade.ArtistServiceFacade;
import com.wooz86.musiclookup.artist.interfaces.facade.dto.ArtistDTO;
import com.wooz86.musiclookup.artist.interfaces.facade.internal.assembler.ArtistDTOAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.util.UUID;

@Component
public class ArtistServiceFacadeImpl implements ArtistServiceFacade {

    private ArtistRepository artistRepository;

    @Autowired
    public ArtistServiceFacadeImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public ArtistDTO getArtistByMBID(UUID mbid) throws RemoteException {
        Artist artist = getArtist(mbid);
        ArtistDTOAssembler artistDTOAssembler = new ArtistDTOAssembler();
        ArtistDTO artistDTO = artistDTOAssembler.toDTO(artist);

        return artistDTO;
    }

    private Artist getArtist(UUID mbid) throws RemoteException {
        try {
            return artistRepository.getByMBID(mbid);
        } catch (Throwable e) {
            throw new RemoteException("MusicBrainzArtist with MBID " + mbid.toString() + " not found.");
        }
    }
}
