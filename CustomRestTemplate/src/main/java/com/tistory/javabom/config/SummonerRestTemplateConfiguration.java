package com.tistory.javabom.config;

import com.tistory.javabom.config.properties.RiotProperties;
import com.tistory.javabom.resttemplate.MajorSummonerRestTemplate;
import com.tistory.javabom.resttemplate.StubSummonerRestTemplate;
import com.tistory.javabom.resttemplate.SummonerRestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

public class SummonerRestTemplateConfiguration {

    private static final Duration READ_TIME = Duration.ofSeconds(30);
    private static final Duration CONN_TIME = Duration.ofSeconds(30);

    @Profile("major")
    @Configuration
    @RequiredArgsConstructor
    public static class MajorConfig {
        private final RiotProperties riotProperties;

        @Bean
        public SummonerRestTemplate summonerRestTemplate() {
            RestTemplate restTemplate = RiotRestTemplateBuilder.get(riotProperties)
                    .setReadTimeout(READ_TIME)
                    .setConnectTimeout(CONN_TIME)
                    .build();
            return new MajorSummonerRestTemplate(restTemplate);
        }
    }

    @Profile("local")
    @Configuration
    public static class LocalConfig {
        @Bean
        public SummonerRestTemplate summonerRestTemplate() {
            return new StubSummonerRestTemplate();
        }
    }

}
