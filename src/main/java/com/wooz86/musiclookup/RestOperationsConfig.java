package com.wooz86.musiclookup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestOperationsConfig {

    @Bean
    public RestOperations restTemplate() {
        return new RestTemplate();
    }
}
