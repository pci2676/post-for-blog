package com.tistory.javabom.resttemplate;

import com.tistory.javabom.domain.SummonerDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SummonerRestTemplateTest {

    @Autowired
    private SummonerRestTemplate summonerRestTemplate;

    @DisplayName("이름으로 사용자 찾기")
    @Test
    void findSummonerByName() {
        //given
        String name = "이상한새기";

        //when
        SummonerDto summonerDto = summonerRestTemplate.findSummonerByName(name);

        //then
        assertThat(summonerDto.getName()).isEqualTo(name);
    }

}