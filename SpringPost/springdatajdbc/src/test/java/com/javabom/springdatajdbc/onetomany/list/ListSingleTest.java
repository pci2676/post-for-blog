package com.javabom.springdatajdbc.onetomany.list;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ListSingleTest {

    @Autowired
    private ListSingleRepository listSingleRepository;

    @Autowired
    private ListManyRepository listManyRepository;

    @AfterEach
    void tearDown() {
        listManyRepository.deleteAll();
        listSingleRepository.deleteAll();
    }

    @DisplayName("List의 경우 Map<Integer, entity> 처럼 동작한다고 보면된다.")
    @Test
    void save() {
        //given
        ListMany listMany1 = new ListMany();
        ListMany listMany2 = new ListMany();
        ListMany listMany3 = new ListMany();
        ListSingle listSingle = new ListSingle(Arrays.asList(listMany1, listMany2, listMany3));

        //when
        ListSingle save = listSingleRepository.save(listSingle);
        ListSingle load = listSingleRepository.findById(save.getId()).orElseThrow(NoSuchElementException::new);

        //then
        assertThat(load.getListManyList()).hasSize(3);
    }
}