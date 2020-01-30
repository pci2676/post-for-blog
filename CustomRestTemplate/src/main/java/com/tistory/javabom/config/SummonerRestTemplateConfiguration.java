package com.tistory.javabom.config;

import com.tistory.javabom.config.properties.RiotProperties;
import com.tistory.javabom.resttemplate.SummonerRestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class SummonerRestTemplateConfiguration {

    private static final Duration READ_TIME = Duration.ofSeconds(30);
    private static final Duration CONN_TIME = Duration.ofSeconds(30);

    private final RiotProperties riotProperties;

    @Bean
    public SummonerRestTemplate summonerRestTemplate() {
        RestTemplate restTemplate = RiotRestTemplateBuilder.get(riotProperties)
                .setReadTimeout(READ_TIME)
                .setConnectTimeout(CONN_TIME)
                .build();
        return new SummonerRestTemplate(restTemplate);
    }
}
