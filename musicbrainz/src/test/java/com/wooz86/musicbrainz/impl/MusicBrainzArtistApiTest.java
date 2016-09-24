package com.wooz86.musicbrainz.impl;

import com.wooz86.musicbrainz.MusicBrainzApi;
import com.wooz86.musicbrainz.MusicBrainzArtist;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class MusicBrainzArtistApiTest {
    private MusicBrainzApi<MusicBrainzArtist> subject;
    private MockRestServiceServer mockRestServiceServer;

    @Before
    public void setup() {
        RestTemplate restTemplate = new RestTemplate();
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();

        MusicBrainzApiConfiguration configuration = new MusicBrainzApiConfiguration();
        configuration.setBaseUrl("http://localhost/");
        configuration.setEndpoint("artist/");

        subject = new MusicBrainzArtistApi(restTemplate, configuration);
    }

    @Test
    public void testGetArtist() {

    }


}
