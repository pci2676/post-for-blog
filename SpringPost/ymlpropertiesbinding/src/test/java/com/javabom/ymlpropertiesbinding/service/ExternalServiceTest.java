package com.javabom.ymlpropertiesbinding.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ExternalServiceTest {
    @Autowired
    private ExternalService externalService;

    @DisplayName("@Value 를 이용한 properties bind")
    @Test
    void valueBindTest() {
        assertThat(externalService.getRecordYear()).isEqualTo("2020");
        assertThat(externalService.getApiKey()).isEqualTo(123123);
        assertThat(externalService.getApiName()).isEqualTo("kakao");
    }
}
