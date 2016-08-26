package com.wooz86.musiclookup.artist.interfaces.web.api.v1;

import com.wooz86.musiclookup.artist.interfaces.facade.ArtistServiceFacade;
import com.wooz86.musiclookup.artist.interfaces.facade.dto.ArtistDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.RemoteException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/artist")
public class ArtistControllerV1 {

    private ArtistServiceFacade artistServiceFacade;

    @Autowired
    public ArtistControllerV1(ArtistServiceFacade artistServiceFacade) {
        this.artistServiceFacade = artistServiceFacade;
    }

    @RequestMapping(value = "/{mbid}", method = RequestMethod.GET)
    public ArtistDTO getByMBID(@PathVariable UUID mbid) throws RemoteException {
        return artistServiceFacade.getArtistByMBID(mbid);
    }
}
