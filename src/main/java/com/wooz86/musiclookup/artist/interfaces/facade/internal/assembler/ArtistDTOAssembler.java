package com.wooz86.musiclookup.artist.interfaces.facade.internal.assembler;

import com.wooz86.musiclookup.artist.domain.model.Artist;
import com.wooz86.musiclookup.artist.interfaces.facade.dto.AlbumDTO;
import com.wooz86.musiclookup.artist.interfaces.facade.dto.ArtistDTO;

import java.util.List;

public class ArtistDTOAssembler {

    public ArtistDTO toDTO(Artist artist) {
        List<AlbumDTO> albumDTOs = AlbumDTOAssembler.toDTOList(artist.getAlbums());

        return new ArtistDTO(artist.getId(), artist.getDescription(), albumDTOs);
    }
}
