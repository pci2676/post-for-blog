package com.tistory.javabom.resttemplate;

import com.tistory.javabom.domain.SummonerDto;

public interface SummonerRestTemplate {
    SummonerDto findSummonerByName(String name);
}
