package com.wooz86.musiclookup.artist.infrastructure;

import com.wooz86.musiclookup.artist.domain.model.Album;
import com.wooz86.musiclookup.artist.domain.model.Artist;
import com.wooz86.musiclookup.artist.infrastructure.coverartarchive.CoverArtArchiveApi;
import com.wooz86.musiclookup.artist.infrastructure.coverartarchive.CoverArtArchiveException;
import com.wooz86.musiclookup.artist.infrastructure.mediawiki.MediaWikiApi;
import com.wooz86.musiclookup.artist.infrastructure.mediawiki.MediaWikiApiException;
import com.wooz86.musiclookup.artist.infrastructure.mediawiki.MediaWikiPage;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.MusicBrainzApi;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.MusicBrainzException;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto.MusicBrainzArtist;
import com.wooz86.musiclookup.artist.infrastructure.musicbrainz.dto.ReleaseGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
class ArtistRemoteService {

    private static final Logger log = LoggerFactory.getLogger(ArtistRemoteService.class);

    private ExecutorService executorService;
    private MusicBrainzApi musicBrainzApi;
    private MediaWikiApi wikipediaService;
    private CoverArtArchiveApi coverArtArchiveService;

    @Autowired
    public ArtistRemoteService(
            MusicBrainzApi musicBrainzService,
            MediaWikiApi wikipediaService,
            CoverArtArchiveApi coverArtArchiveService
    ) {
        this.executorService = Executors.newWorkStealingPool(); // @todo Dependency inject this
        this.musicBrainzApi = musicBrainzService;
        this.wikipediaService = wikipediaService;
        this.coverArtArchiveService = coverArtArchiveService;
    }

    public Artist getByMBID(UUID mbid) throws MediaWikiApiException, MusicBrainzException, ExecutionException, InterruptedException {
        return getArtistByMBIDAsync(mbid);

//        MusicBrainzArtist musicBrainzArtist = musicBrainzApi.getByMBId(mbid);
//        String pageTitle = musicBrainzApi.getMediaWikiPageTitle(musicBrainzArtist);
//
//        CompletableFuture<String> description = getDescription(musicBrainzArtist);
////        CompletableFuture<List<Album>> albums = getAlbums(musicBrainzArtist);
//
//        // return getArtistByMBID(mbid)
////            .thenCombine(description, (String description, List<Album> albums) -> {
//
////            });
    }

    @Async
    private Artist getArtistByMBIDAsync(UUID mbid) throws MusicBrainzException, MediaWikiApiException, InterruptedException, ExecutionException {

        MusicBrainzArtist musicBrainzArtist = musicBrainzApi.getByMBId(mbid);
        String pageTitle = musicBrainzApi.getMediaWikiPageTitle(musicBrainzArtist);

        String description = getDescription(pageTitle).get(); // @todo Fix this blocking call
        List<Album> albums = getAlbums(musicBrainzArtist, mbid);

        return new Artist(mbid, description, albums);
    }

    private CompletableFuture<String> getDescription(String pageTitle) throws MediaWikiApiException {
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

    private List<Album> getAlbums(MusicBrainzArtist musicBrainzArtist, UUID mbid) throws ExecutionException, InterruptedException, MediaWikiApiException, MusicBrainzException {
        List<ReleaseGroup> musicBrainzAlbums = getMusicBrainzAlbums(musicBrainzArtist);

        List<CompletableFuture<Album>> collect = musicBrainzAlbums.stream()
                .map(this::buildAlbum)
                .collect(Collectors.toList());

        CompletableFuture<List<Album>> sequence = sequence(collect);

        List<Album> alba = sequence.get();

        return alba;
    }

    private List<ReleaseGroup> getMusicBrainzAlbums(MusicBrainzArtist musicBrainzArtist) {
        return musicBrainzArtist.getReleaseGroups()
                .stream()
                .filter(p -> p.getPrimaryType().equals("Album"))
                .collect(Collectors.toList());
    }

    private CompletableFuture<Album> buildAlbum(ReleaseGroup album) {
        return CompletableFuture.supplyAsync(() -> {
            String albumCoverUrl = getCoverImageUrl(album);
            log.info("Thread " + Thread.currentThread().getId() + ": " + albumCoverUrl); //@todo Remove debug code
            return new Album(album.getId(), album.getTitle(), albumCoverUrl);
        }, executorService);
    }

    private String getCoverImageUrl(ReleaseGroup album) {
        try {
            return coverArtArchiveService.getImageUrlByMBID(album.getId());
        } catch (CoverArtArchiveException e) {
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


