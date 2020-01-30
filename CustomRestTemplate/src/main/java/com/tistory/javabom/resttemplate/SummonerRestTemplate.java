package com.tistory.javabom.resttemplate;

import com.tistory.javabom.domain.SummonerDto;
import org.springframework.web.client.RestTemplate;

public class SummonerRestTemplate {
    private static final String SUMMONER_V4_FIND_BY_NAME_URL = "/lol/summoner/v4/summoners/by-name/{summonerName}";

    private final RestTemplate restTemplate;

    public SummonerRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SummonerDto findSummonerByName(String name) {
        return restTemplate.getForObject(SUMMONER_V4_FIND_BY_NAME_URL, SummonerDto.class, name);
    }

}
