package com.tistory.javabom.resttemplate;

import com.tistory.javabom.domain.SummonerDto;

public class StubSummonerRestTemplate implements SummonerRestTemplate {
    @Override
    public SummonerDto findSummonerByName(String name) {
        return SummonerDto.builder()
                .name(name)
                .accountId("stubAccountId")
                .summonerId("stubSummonerId")
                .build();
    }
}
