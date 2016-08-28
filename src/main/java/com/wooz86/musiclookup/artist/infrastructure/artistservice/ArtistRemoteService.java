package com.wooz86.musiclookup.artist.infrastructure.artistservice;

import com.wooz86.musiclookup.artist.domain.model.Album;
import com.wooz86.musiclookup.artist.domain.model.Artist;
import com.wooz86.musiclookup.coverartarchive.impl.CoverArtArchiveApiImpl;
import com.wooz86.musiclookup.coverartarchive.CoverArtArchiveApiException;
import com.wooz86.musiclookup.mediawiki.MediaWikiApi;
import com.wooz86.musiclookup.mediawiki.MediaWikiApiException;
import com.wooz86.musiclookup.mediawiki.MediaWikiPage;
import com.wooz86.musiclookup.mediawiki.impl.MediaWikiApiImpl;
import com.wooz86.musiclookup.musicbrainz.MusicBrainzAlbum;
import com.wooz86.musiclookup.musicbrainz.MusicBrainzApi;
import com.wooz86.musiclookup.musicbrainz.MusicBrainzApiException;
import com.wooz86.musiclookup.musicbrainz.MusicBrainzArtist;
import com.wooz86.musiclookup.musicbrainz.impl.MusicBrainzApiImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ArtistRemoteService {

    private static final Logger log = LoggerFactory.getLogger(ArtistRemoteService.class);

    private ExecutorService executorService;
    private MusicBrainzApi musicBrainzApi;
    private MediaWikiApi wikipediaService;
    private CoverArtArchiveApiImpl coverArtArchiveService;

    @Autowired
    public ArtistRemoteService(
            MusicBrainzApiImpl musicBrainzService,
            MediaWikiApiImpl wikipediaService,
            CoverArtArchiveApiImpl coverArtArchiveService
    ) {
        this.executorService = Executors.newWorkStealingPool(); // @todo Dependency inject this
        this.musicBrainzApi = musicBrainzService;
        this.wikipediaService = wikipediaService;
        this.coverArtArchiveService = coverArtArchiveService;
    }

    public Artist getByMBID(UUID mbid) throws ArtistRemoteServiceException {
        return getArtistByMBIDAsync(mbid);

//        MusicBrainzArtist musicBrainzArtist = musicBrainzApi.getArtistByMBID(mbid);
//        String pageTitle = musicBrainzApi.getMediaWikiPageTitle(musicBrainzArtist);
//
//        CompletableFuture<String> description = getDescription(musicBrainzArtist);
////        CompletableFuture<List<Album>> albums = getReleaseGroups(musicBrainzArtist);
//
//        // return getArtistByMBID(mbid)
////            .thenCombine(description, (String description, List<Album> albums) -> {
//
////            });
    }

    private Artist getArtistByMBIDAsync(UUID mbid) throws ArtistRemoteServiceException {
        MusicBrainzArtist musicBrainzArtist = getArtistFromMusicBrainz(mbid);
        String description = getArtistDescription(musicBrainzArtist);
        List<Album> albums = getAlbums(musicBrainzArtist);

        return new Artist(mbid, description, albums);
    }

    private MusicBrainzArtist getArtistFromMusicBrainz(UUID mbid) throws ArtistRemoteServiceException {
        try {
            return musicBrainzApi.getArtistByMBID(mbid);
        } catch (MusicBrainzApiException e) {
            throw new ArtistRemoteServiceException("Failed not load artist.", e);
        }
    }

    private String getArtistDescription(MusicBrainzArtist musicBrainzArtist) {
        String wikipediaPageUrl = musicBrainzArtist.getWikipediaPageUrl();

        try {
            String pageTitle = getPageTitleFromUrl(wikipediaPageUrl);
            return getDescription(pageTitle).get();
        } catch (Exception e) {
            return null;
        }
    }

    private String getPageTitleFromUrl(String url) throws Exception { // @todo Cleanup
        String[] parts = url.split("/");

        if (parts.length == 0) {
            throw new Exception("Could not parse page title from URL."); // @todo Move this code?
        }

        return parts[parts.length-1];
    }

    private CompletableFuture<String> getDescription(String pageTitle) {
        return CompletableFuture.supplyAsync(() -> {
            MediaWikiPage mediaWikiPage = null;
            try {
                mediaWikiPage = wikipediaService.getPageByTitle(pageTitle);
                return mediaWikiPage.getExtract();
            } catch (MediaWikiApiException e) {
                return null;
            }
        });
    }

    private List<Album> getAlbums(MusicBrainzArtist musicBrainzArtist) throws ArtistRemoteServiceException {
        List<MusicBrainzAlbum> musicBrainzAlbums = musicBrainzArtist.getAlbums();

        List<CompletableFuture<Album>> collect = musicBrainzAlbums.stream()
                .map(this::buildAlbum)
                .collect(Collectors.toList());

        CompletableFuture<List<Album>> sequence = sequence(collect);

        try {
            return sequence.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ArtistRemoteServiceException("Failed to load albums.", e);
        }
    }

    private CompletableFuture<Album> buildAlbum(MusicBrainzAlbum album) {
        return CompletableFuture.supplyAsync(() -> {
            String albumCoverUrl = getCoverImageUrl(album);
            log.info("Thread " + Thread.currentThread().getId() + ": " + albumCoverUrl); //@todo Remove debug code
            return new Album(album.getId(), album.getTitle(), albumCoverUrl);
        }, executorService);
    }

    private String getCoverImageUrl(MusicBrainzAlbum album) {
        try {
            return coverArtArchiveService.getImageUrlByMBID(album.getId());
        } catch (CoverArtArchiveApiException e) {
            return null;
        }
    }

    private static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));

        return allDoneFuture.thenApply(results ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );
    }
}


