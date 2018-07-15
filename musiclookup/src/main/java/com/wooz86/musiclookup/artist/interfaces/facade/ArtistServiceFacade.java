package com.wooz86.musiclookup.artist.interfaces.facade;

import com.wooz86.musiclookup.artist.interfaces.facade.dto.ArtistDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface ArtistServiceFacade extends Remote {
    ArtistDTO getArtistByMBID(UUID mbid) throws RemoteException;
}
