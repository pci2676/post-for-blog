package com.javabom.springdatajdbc.oneton.set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class SetSingleTest {

    @Autowired
    private SetSingleRepository setSingleRepository;
    @Autowired
    private SetManyRepository setManyRepository;

    @AfterEach
    void tearDown() {
        setManyRepository.deleteAll();
        setSingleRepository.deleteAll();
    }

    @DisplayName("일 대 다 연관 관계는 기본적으로 Set을 지원한다.")
    @Test
    void save() {
        //given
        SetMany setMany1 = new SetMany();
        SetMany setMany2 = new SetMany();
        SetMany setMany3 = new SetMany();

        SetSingle setSingle = new SetSingle(new HashSet<>(Arrays.asList(setMany1, setMany2, setMany3)));

        //when
        SetSingle save = setSingleRepository.save(setSingle);
        SetSingle load = setSingleRepository.findById(save.getId()).orElseThrow(NoSuchElementException::new);

        //then
        assertThat(load.getManies()).hasSize(3);

        SetMany setMany = load.getManies().stream().findFirst().orElseThrow(NoSuchElementException::new);
        assertThat(setMany.getSetSingle()).isEqualTo(save.getId());
    }
}