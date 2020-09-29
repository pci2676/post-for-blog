package com.javabom.ymlpropertiesbinding.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TypeSafePropertiesTest {

    @Autowired
    private TypeSafeProperties typeSafeProperties;

    @DisplayName("getter, setter 를 이용한 properties bind")
    @Test
    void typeSafeBindTest() {
        assertThat(typeSafeProperties.getRecordYear()).isEqualTo("2020");
        assertThat(typeSafeProperties.getApi().getKey()).isEqualTo(123123);
        assertThat(typeSafeProperties.getApi().getName()).isEqualTo("kakao");
    }

}
