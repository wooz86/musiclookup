package com.wooz86.coverartarchive.impl;

import com.wooz86.coverartarchive.CoverArtArchiveApi;
import com.wooz86.coverartarchive.CoverArtArchiveApiException;
import com.wooz86.coverartarchive.impl.dto.Image;
import com.wooz86.coverartarchive.impl.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.UUID;

public class CoverArtArchiveApiImpl implements CoverArtArchiveApi {

    private static final Logger log = LoggerFactory.getLogger(CoverArtArchiveApiImpl.class);

    private RestOperations restClient;
    private CoverArtArchiveApiConfiguration configuration;

    public CoverArtArchiveApiImpl(RestOperations restClient, CoverArtArchiveApiConfiguration configuration) {
        this.restClient = restClient;
        this.configuration = configuration;
    }

    public String getImageUrlByMBID(UUID mbid) throws CoverArtArchiveApiException {
        String requestUrl = buildRequestUrl(mbid);

        Response response = makeRequest(requestUrl, mbid);
        log.info(response.toString());

        List<Image> images = response.getImages();
        Image firstImage = images.get(0);
        String imageUrl = firstImage.getImage();

        return imageUrl;
    }

    private Response makeRequest(String requestUrl, UUID mbid) throws CoverArtArchiveApiException {
        try {
            return restClient.getForObject(requestUrl, Response.class);
        } catch (Exception e) {
            throw new CoverArtArchiveApiException("Could not find cover art for for album with MBID" + mbid.toString());
        }
    }

    private String buildRequestUrl(UUID mbid) {
        return configuration.getBaseUrl() + configuration.getEndpoint() + mbid.toString();
    }
}
