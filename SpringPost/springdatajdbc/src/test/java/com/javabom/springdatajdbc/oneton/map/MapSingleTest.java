package com.javabom.springdatajdbc.oneton.map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MapSingleTest {

    @Autowired
    private MapManyRepository mapManyRepository;

    @Autowired
    private MapSingleRepository mapSingleRepository;

    @AfterEach
    void tearDown() {
        mapManyRepository.deleteAll();
        mapSingleRepository.deleteAll();
    }

    @DisplayName("맵으로 저장하기")
    @Test
    void save() {
        //given
        HashMap<String, MapMany> map = new HashMap<>();
        map.put("하나", new MapMany("내용1"));
        map.put("둘", new MapMany("내용2"));
        map.put("셋", new MapMany("내용3"));

        MapSingle mapSingle = new MapSingle(map);

        //when
        MapSingle save = mapSingleRepository.save(mapSingle);

        //then
        assertThat(save.getMapManyMap()).hasSize(3);
    }
}