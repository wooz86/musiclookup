package com.wooz86.musiclookup;

import com.wooz86.mediawiki.impl.MediaWikiApiConfiguration;
import com.wooz86.mediawiki.impl.MediaWikiApiImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MediaWikiApiConfig {

    private Environment env;

    @Autowired
    public MediaWikiApiConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public MediaWikiApiImpl mediaWikiApi()  {
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
