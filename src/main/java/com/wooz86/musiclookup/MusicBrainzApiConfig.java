package com.wooz86.musiclookup;

import com.wooz86.musicbrainz.MusicBrainzApi;
import com.wooz86.musicbrainz.impl.MusicBrainzApiConfiguration;
import com.wooz86.musicbrainz.impl.MusicBrainzApiImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestOperations;

@Configuration
public class MusicBrainzApiConfig {

    private Environment env;
    private RestOperations restOperations;

    @Autowired
    public MusicBrainzApiConfig(Environment env, RestOperations restOperations) {
        this.env = env;
        this.restOperations = restOperations;
    }

    @Bean
    public MusicBrainzApi musicBrainzApi() {
        MusicBrainzApiConfiguration configuration = getConfiguration();

        return new MusicBrainzApiImpl(restOperations, configuration);
    }

    private MusicBrainzApiConfiguration getConfiguration() {
        MusicBrainzApiConfiguration configuration = new MusicBrainzApiConfiguration();
        configuration.setBaseUrl(env.getProperty("musicbrainz.baseUrl"));
        configuration.setEndpoint(env.getProperty("musicbrainz.endpoint"));
        configuration.setQueryString(env.getProperty("musicbrainz.queryString"));

        return configuration;
    }
}