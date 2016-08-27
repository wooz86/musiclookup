package com.wooz86.musiclookup.artist.infrastructure;

import com.wooz86.musiclookup.artist.domain.model.Artist;
import com.wooz86.musiclookup.artist.infrastructure.coverartarchive.CoverArtArchiveService;
import com.wooz86.musiclookup.artist.infrastructure.mediawiki.MediaWikiApi;
import com.wooz86.musiclookup.artist.infrastructure.mediawiki.MediaWikiApiException;
import com.wooz86.musiclookup.artist.infrastructure.mediawiki.MediaWikiPage;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.MusicBrainzException;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.MusicBrainzService;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto.MusicBrainzArtist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
class ArtistRemoteService {

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

    public Artist getByMBID(UUID mbid) throws MediaWikiApiException, MusicBrainzException, ExecutionException, InterruptedException {
        return getArtistByMBIDAsync(mbid);
    }

    @Async
    private Artist getArtistByMBIDAsync(UUID mbid) throws MusicBrainzException, MediaWikiApiException, InterruptedException, ExecutionException {
        MusicBrainzArtist musicBrainzArtist = musicBrainzService.getByMBId(mbid);
        String pageTitle = musicBrainzService.getWikipediaPageTitleForArtist(musicBrainzArtist);

        Future<MediaWikiPage> mediaWikiPage = getMediaWikiPage(pageTitle);

        // Wait until they are all done
        while (!mediaWikiPage.isDone()) {
            Thread.sleep(10); //10-millisecond pause between each check
        }

        String description = mediaWikiPage.get().getExtract();

        return new Artist(mbid, description);
    }

    @Async
    private Future<MediaWikiPage> getMediaWikiPage(String pageTitle) throws MediaWikiApiException {
        return new AsyncResult<>(wikipediaService.getPageByTitle(pageTitle));
    }
}
