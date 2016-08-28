package com.wooz86.musiclookup.artist.infrastructure.coverartarchive;

import com.wooz86.musiclookup.artist.infrastructure.coverartarchive.dto.Image;
import com.wooz86.musiclookup.artist.infrastructure.coverartarchive.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.UUID;

@Service
public class CoverArtArchiveApi {

    private static final Logger log = LoggerFactory.getLogger(CoverArtArchiveApi.class);

    private RestOperations restClient;
    private String baseUrl = "http://coverartarchive.org/";
    private String endpoint = "release-group/";

    @Autowired
    public CoverArtArchiveApi(RestOperations restClient) {
        this.restClient = restClient;
    }

    public String getImageUrlByMBID(UUID mbid) throws CoverArtArchiveException {
        String requestUrl = buildRequestUrl(mbid);

        Response response = makeRequest(requestUrl, mbid);
        log.info(response.toString());

        List<Image> images = response.getImages();
        Image firstImage = images.get(0);
        String imageUrl = firstImage.getImage();

        return imageUrl;
    }

    private Response makeRequest(String requestUrl, UUID mbid) throws CoverArtArchiveException {
        try {
            return restClient.getForObject(requestUrl, Response.class);
        } catch (Exception e) {
            throw new CoverArtArchiveException("Could not find cover art for for album with MBID" + mbid.toString());
        }
    }

    private String buildRequestUrl(UUID mbid) {
        return baseUrl + endpoint + mbid.toString();
    }
}
