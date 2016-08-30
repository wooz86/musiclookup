package com.wooz86.musiclookup;

import com.wooz86.coverartarchive.CoverArtArchiveApi;
import com.wooz86.coverartarchive.impl.CoverArtArchiveApiConfiguration;
import com.wooz86.coverartarchive.impl.CoverArtArchiveApiImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestOperations;

@Configuration
//@PropertySource("classpath:application.properties")
public class CoverArtArchiveApiConfig {

    private Environment env;
    private RestOperations restOperations;

    @Autowired
    public CoverArtArchiveApiConfig(Environment env, RestOperations restOperations) {
        this.env = env;
        this.restOperations = restOperations;
    }

    @Bean
    public CoverArtArchiveApi coverArtArchiveApi() {
        CoverArtArchiveApiConfiguration configuration = getConfiguration();

        return new CoverArtArchiveApiImpl(restOperations, configuration);
    }

    private CoverArtArchiveApiConfiguration getConfiguration() {
        CoverArtArchiveApiConfiguration coverArtArchiveApiConfiguration = new CoverArtArchiveApiConfiguration();
        coverArtArchiveApiConfiguration.setBaseUrl(env.getProperty("coverartarchive.baseUrl"));
        coverArtArchiveApiConfiguration.setEndpoint(env.getProperty("coverartarchive.endpoint"));

        return coverArtArchiveApiConfiguration;
    }
}
