package com.javabom.springdatajdbc.field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class EnumEntityTest {

    @Autowired
    private EnumEntityRepository enumEntityRepository;

    @AfterEach
    void tearDown() {
        enumEntityRepository.deleteAll();
    }

    @DisplayName("enum을 필드로 사용하고 저장, 꺼낼수 있다.")
    @Test
    void save() {
        //given
        EnumEntity enumEntity = new EnumEntity(Active.Y);
        EnumEntity save = enumEntityRepository.save(enumEntity);

        //when
        EnumEntity entity = enumEntityRepository.findById(save.getId()).orElseThrow(NoSuchElementException::new);

        //then
        assertThat(entity.getActive()).isEqualTo(Active.Y);
    }

    @DisplayName("Enum의 name으로 저장되어 있다.")
    @Test
    void load() {
        //given
        EnumEntity enumEntity = new EnumEntity(Active.Y);
        EnumEntity save = enumEntityRepository.save(enumEntity);

        //when
        List<EnumEntity> allByActive = enumEntityRepository.findAllByActive(Active.Y.name());

        //then
        assertThat(allByActive).hasSize(1);
    }
}