package com.urlshortner.urlshortner.Bean;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UrlBean {

    public @Bean CqlSession session() {

        return CqlSession.builder().withKeyspace("urlspace").build();


    }

}
