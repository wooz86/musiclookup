package com.wooz86.musiclookup.artist.infrastructure;

import com.wooz86.musiclookup.artist.domain.model.Album;
import com.wooz86.musiclookup.artist.domain.model.Artist;
import com.wooz86.musiclookup.artist.infrastructure.coverartarchive.CoverArtArchiveException;
import com.wooz86.musiclookup.artist.infrastructure.coverartarchive.CoverArtArchiveService;
import com.wooz86.musiclookup.artist.infrastructure.mediawiki.MediaWikiApi;
import com.wooz86.musiclookup.artist.infrastructure.mediawiki.MediaWikiApiException;
import com.wooz86.musiclookup.artist.infrastructure.mediawiki.MediaWikiPage;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.MusicBrainzException;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.MusicBrainzService;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto.ReleaseGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ArtistRemoteService {

    private MusicBrainzService musicBrainzService;
    private MediaWikiApi wikipediaService;
    private CoverArtArchiveService coverArtArchiveService;

    @Autowired
    public ArtistRemoteService(
            MusicBrainzService musicBrainzService,
            MediaWikiApi wikipediaService,
            CoverArtArchiveService coverArtArchiveService
    ) {
        this.musicBrainzService = musicBrainzService;
        this.wikipediaService = wikipediaService;
        this.coverArtArchiveService = coverArtArchiveService;
    }

    public Artist getByMBID(UUID mbid) throws MediaWikiApiException, MusicBrainzException {
        com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto.Artist MBArtist = getArtistByMBId(mbid);


        String pageTitleForArtist = musicBrainzService.getWikipediaPageTitleForArtist(MBArtist);
        String artistDescription = getArtistDescription(pageTitleForArtist);
        List<ReleaseGroup> albums = getAlbums(MBArtist);

        List<Album> als = new ArrayList<>();

        Artist artist = new Artist(mbid, artistDescription);

        for(ReleaseGroup album : albums) {
            String albumCover = getAlbumCoverUrlByMBID(album.getId());
            Album newAl = new Album(album.getId(), album.getTitle(), albumCover);
            artist.addAlbum(newAl);
        }

        return artist;
    }

    private List<ReleaseGroup> getAlbums(com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto.Artist artistByMBId) {
        List<ReleaseGroup> albums = artistByMBId.getReleaseGroups()
                .stream()
                .filter(p -> p.getPrimaryType().equals("Album"))
                .collect(Collectors.toList());

        return albums;
    }

    private com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto.Artist getArtistByMBId(UUID mbid) throws MusicBrainzException {
        com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto.Artist byMBId = musicBrainzService.getByMBId(mbid);

        return byMBId;
    }

    private String getArtistDescription(String pageTitle) throws MediaWikiApiException {
        MediaWikiPage wikipediaPage = wikipediaService.getPageByTitle(pageTitle);
        return wikipediaPage.getExtract();
    }

    private String getAlbumCoverUrlByMBID(UUID mbid) {
        try {
            return coverArtArchiveService.getImageUrlByMBID(mbid);
        } catch (CoverArtArchiveException e) {
            return null;
        }
    }
}
