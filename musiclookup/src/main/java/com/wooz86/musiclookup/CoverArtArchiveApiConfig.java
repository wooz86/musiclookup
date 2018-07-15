package com.wooz86.musiclookup;

import com.wooz86.coverartarchive.CoverArtArchiveApi;
import com.wooz86.coverartarchive.CoverArtArchiveException;
import com.wooz86.coverartarchive.impl.Configuration;
import com.wooz86.coverartarchive.impl.CoverArtArchiveApiImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestOperations;

@org.springframework.context.annotation.Configuration
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
    public CoverArtArchiveApi coverArtArchiveApi() throws CoverArtArchiveException {
        Configuration configuration = getConfiguration();

        return new CoverArtArchiveApiImpl(configuration);
    }

    private Configuration getConfiguration() {
        Configuration coverArtArchiveApiConfiguration = new Configuration();
        coverArtArchiveApiConfiguration.setBaseUrl(env.getProperty("coverartarchive.baseUrl"));
        coverArtArchiveApiConfiguration.setEndpoint(env.getProperty("coverartarchive.endpoint"));

        return coverArtArchiveApiConfiguration;
    }
}
