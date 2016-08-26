package com.wooz86.musiclookup.artist.interfaces.facade.internal.assembler;

import com.wooz86.musiclookup.artist.domain.model.Album;
import com.wooz86.musiclookup.artist.interfaces.facade.dto.AlbumDTO;

import java.util.ArrayList;
import java.util.List;

public class AlbumDTOAssembler {

    public static List<AlbumDTO> toDTOList(List<Album> albums) {
        List<AlbumDTO> dtoList = new ArrayList<>();

        for(Album album : albums) {
            AlbumDTO albumDTO = new AlbumDTO(album.getId(), album.getTitle(), album.getImage());
            dtoList.add(albumDTO);
        }

        return dtoList;
    }
}
