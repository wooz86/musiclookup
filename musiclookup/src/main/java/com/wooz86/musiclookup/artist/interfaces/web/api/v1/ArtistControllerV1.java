package com.wooz86.musiclookup.artist.interfaces.web.api.v1;

import com.wooz86.musiclookup.artist.interfaces.facade.ArtistServiceFacade;
import com.wooz86.musiclookup.artist.interfaces.facade.dto.ArtistDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        ArtistDTO artistDTO = artistServiceFacade.getArtistByMBID(mbid);

        if (artistDTO == null) {
            throw new ResourceNotFoundException("Artist with MBID " + mbid + " not found.");
        }

        return artistDTO;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Throwable ex) {
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse error = new ErrorResponse();
        error.setCode(internalServerError.value());
        error.setMessage("Internal server error.");

        return new ResponseEntity<>(error, internalServerError);
    }

}
