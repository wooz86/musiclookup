package com.wooz86.musiclookup;

import com.wooz86.mediawiki.MediaWikiException;
import com.wooz86.mediawiki.impl.MediaWikiApiConfiguration;
import com.wooz86.mediawiki.impl.MediaWikiApiImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestOperations;

@Configuration
public class MediaWikiApiConfig {

    private Environment env;
    private RestOperations restOperations;

    @Autowired
    public MediaWikiApiConfig(Environment env, RestOperations restOperations) {
        this.env = env;
        this.restOperations = restOperations;
    }

    @Bean
    public MediaWikiApiImpl mediaWikiApi() throws MediaWikiException {
        MediaWikiApiConfiguration configuration = getConfiguration();

        return new MediaWikiApiImpl(configuration);
    }

    private MediaWikiApiConfiguration getConfiguration() {
        MediaWikiApiConfiguration configuration = new MediaWikiApiConfiguration();
        configuration.setBaseUrl(env.getProperty("mediawiki.baseUrl"));
        configuration.setQueryString(env.getProperty("mediawiki.queryString"));

        return configuration;
    }
}
