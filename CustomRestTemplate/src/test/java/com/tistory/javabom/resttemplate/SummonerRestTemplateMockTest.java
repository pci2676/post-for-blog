package com.tistory.javabom.resttemplate;

import com.tistory.javabom.config.RiotRestTemplateBuilder;
import com.tistory.javabom.config.properties.RiotProperties;
import com.tistory.javabom.domain.SummonerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.util.UriEncoder;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class SummonerRestTemplateMockTest {
    private static final String SUMMONER_BY_NAME_URL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/";

    private SummonerRestTemplate summonerRestTemplate;

    private MockRestServiceServer mockServer;


    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = RiotRestTemplateBuilder.get(new RiotProperties()).build();
        summonerRestTemplate = new SummonerRestTemplate(restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @DisplayName("이름으로 사용자 찾기")
    @Test
    void findSummonerByName() {
        //given
        String name = "이상한새기";
        String encodedName = UriEncoder.encode(name);

        String expectBody = "{\n" +
                "    \"profileIconId\": 2095,\n" +
                "    \"name\": \"이상한새기\",\n" +
                "    \"puuid\": \"0unup92CswoD3CGo6qwydHzATD4pePmpi3XhZA-kX2urduks6nJke6nlnSpmJn0hEUPgHzuo0d5Tgg\",\n" +
                "    \"summonerLevel\": 98,\n" +
                "    \"accountId\": \"w94qxPIxhJ2ALZoRItVSwyN6R-CNMXOE1VJwesmrZdAv\",\n" +
                "    \"id\": \"zN1v1n2XlkIY9cYKj9XydSSKItQNRtDLVdJHEWIkVhN5fQ\",\n" +
                "    \"revisionDate\": 1570881947000\n" +
                "}";

        this.mockServer.expect(requestTo(SUMMONER_BY_NAME_URL + encodedName))
                .andRespond(withSuccess(expectBody, MediaType.APPLICATION_JSON));

        //when
        SummonerDto summonerDto = summonerRestTemplate.findSummonerByName(name);

        //then
        assertThat(summonerDto.getName()).isEqualTo(name);
        this.mockServer.verify();
    }

    @DisplayName("이름으로 사용자 찾기 실패")
    @Test
    public void findSummonerByName2() {
        //given
        String summonerName = "이상한새기";
        String encodedName = UriEncoder.encode(summonerName);

        String exceptBody = "{\n" +
                "    \"status\": {\n" +
                "        \"status_code\": 400,\n" +
                "        \"message\": \"BadRequest\"\n" +
                "    }\n" +
                "}";

        this.mockServer.expect(requestTo(SUMMONER_BY_NAME_URL + encodedName))
                .andRespond(withBadRequest()
                        .body(exceptBody)
                        .contentType(MediaType.APPLICATION_JSON));

        //when
        //then
        assertThatThrownBy(() -> summonerRestTemplate.findSummonerByName(summonerName))
                .isInstanceOf(NoSuchElementException.class);
        this.mockServer.verify();
    }
}