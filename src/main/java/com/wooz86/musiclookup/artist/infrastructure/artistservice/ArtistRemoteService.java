package com.wooz86.musiclookup.artist.infrastructure.artistservice;

import com.wooz86.coverartarchive.CoverArtArchiveApi;
import com.wooz86.coverartarchive.CoverArtArchiveApiException;
import com.wooz86.mediawiki.MediaWikiApi;
import com.wooz86.mediawiki.MediaWikiApiException;
import com.wooz86.mediawiki.MediaWikiPage;
import com.wooz86.musicbrainz.MusicBrainzAlbum;
import com.wooz86.musicbrainz.MusicBrainzApi;
import com.wooz86.musicbrainz.MusicBrainzApiException;
import com.wooz86.musicbrainz.MusicBrainzArtist;
import com.wooz86.musiclookup.artist.domain.model.Album;
import com.wooz86.musiclookup.artist.domain.model.Artist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class ArtistRemoteService {

    private static final Logger log = LoggerFactory.getLogger(ArtistRemoteService.class);

    private ExecutorService executorService;
    private MusicBrainzApi musicBrainzApi;
    private MediaWikiApi mediaWikiApi;
    private CoverArtArchiveApi coverArtArchiveApi;

    @Autowired
    public ArtistRemoteService(
            ExecutorService executorService,
            MusicBrainzApi musicBrainzService,
            MediaWikiApi wikipediaService,
            CoverArtArchiveApi coverArtArchiveService
    ) {
        this.executorService = executorService;
        this.musicBrainzApi = musicBrainzService;
        this.mediaWikiApi = wikipediaService;
        this.coverArtArchiveApi = coverArtArchiveService;
    }

    public Artist getByMBID(UUID mbid) throws ArtistRemoteServiceException {
        MusicBrainzArtist musicBrainzArtist = getArtistFromMusicBrainzApi(mbid);
        List<Album> albums = getAlbums(musicBrainzArtist);
        String description = getArtistDescription(musicBrainzArtist);

        return new Artist(mbid, description, albums);
    }

    private MusicBrainzArtist getArtistFromMusicBrainzApi(UUID mbid) throws ArtistRemoteServiceException {
        try {
            return musicBrainzApi.getArtistByMBID(mbid);
        } catch (MusicBrainzApiException e) {
            log.info("Failed to load artist from MusicBrainz API.", e);
            throw new ArtistRemoteServiceException("Failed to load artist.", e);
        }
    }

    private String getArtistDescription(MusicBrainzArtist musicBrainzArtist) {
        String description = null;

        String wikipediaPageUrl = musicBrainzArtist.getWikipediaPageUrl();
        String pageTitle = getPageTitleFromUrl(wikipediaPageUrl);

        if (pageTitle != null) {
            description = getDescriptionFromMediaWikiApi(pageTitle);
        }

        return description;
    }

    private String getPageTitleFromUrl(String url) {
        String pageTitle = null;

        String[] parts = url.split("/");

        if (parts.length != 0) {
            pageTitle = parts[parts.length - 1];
        }

        return pageTitle;
    }

    private String getDescriptionFromMediaWikiApi(String pageTitle) {
        try {
            MediaWikiPage mediaWikiPage = mediaWikiApi.getPageByTitle(pageTitle);
            return mediaWikiPage.getExtract();
        } catch (MediaWikiApiException e) {
            log.info("Failed to load artist description from MediaWiki API.");
            return null;
        }
    }

    private List<Album> getAlbums(MusicBrainzArtist musicBrainzArtist) throws ArtistRemoteServiceException {
        List<MusicBrainzAlbum> musicBrainzAlbums = musicBrainzArtist.getAlbums();

        List<CompletableFuture<Album>> futures = musicBrainzAlbums.stream()
                .map(this::buildAlbum)
                .collect(Collectors.toList());

        CompletableFuture<List<Album>> albumsFuture = mergeCompletableFutures(futures);

        try {
            return albumsFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ArtistRemoteServiceException("Failed to load albums for artist.", e);
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
            return coverArtArchiveApi.getImageUrlByMBID(album.getId());
        } catch (CoverArtArchiveApiException e) {
            return null;
        }
    }

    private static <T> CompletableFuture<List<T>> mergeCompletableFutures(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));

        return allDoneFuture.thenApply(results ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );
    }
}


