package com.javabom.springdatajdbc.onetoone;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SuperOneTest {

    @Autowired
    private SuperOneRepository superOneRepository;
    @Autowired
    private SubOneRepository subOneRepository;

    @AfterEach
    void tearDown() {
        superOneRepository.deleteAll();
        superOneRepository.deleteAll();
    }

    @DisplayName("1:1 연관 관계에서 저장 하기")
    @Test
    void save1() {
        //given
        SuperOne superOne = new SuperOne("참조하는 객체", new SubOne("참조 당하는 객체"));

        //when
        superOne = superOneRepository.save(superOne);

        //then
        assertThat(superOne.getId()).isNotNull();
    }

}