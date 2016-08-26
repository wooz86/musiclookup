package com.wooz86.musiclookup.artist.interfaces.facade;

import com.wooz86.musiclookup.artist.interfaces.facade.dto.ArtistDTO;

import java.rmi.RemoteException;
import java.util.UUID;

public interface ArtistServiceFacade {
    ArtistDTO getArtistByMBID(UUID mbid) throws RemoteException;
}
