package com.tistory.javabom.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SummonerDto {
    @JsonAlias("id")
    private String summonerId;
    private String accountId;
    private String name;

    @Builder
    public SummonerDto(String summonerId, String accountId, String name) {
        this.summonerId = summonerId;
        this.accountId = accountId;
        this.name = name;
    }
}
