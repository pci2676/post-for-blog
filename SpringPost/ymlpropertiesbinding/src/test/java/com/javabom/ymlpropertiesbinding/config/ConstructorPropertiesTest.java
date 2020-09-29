package com.javabom.ymlpropertiesbinding.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConstructorPropertiesTest {

    @Autowired
    private ConstructorProperties constructorProperties;

    @DisplayName("yml properties 생성자 주입")
    @Test
    void bindProperties() {
        assertThat(constructorProperties.getRecordYear()).isEqualTo("2020");
        assertThat(constructorProperties.getApi().getKey()).isEqualTo(123123);
        assertThat(constructorProperties.getApi().getName()).isEqualTo("kakao");
    }

}
