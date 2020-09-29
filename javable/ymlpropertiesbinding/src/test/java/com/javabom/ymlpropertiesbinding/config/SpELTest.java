package com.javabom.ymlpropertiesbinding.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SpELTest {

    @Value("#{1 eq 1}")
    private boolean spelBoolean;

    @Value("#{externalService.apiName eq 'kakao'}")
    private String spelNameString;

    @Test
    void spelTest() {
        assertThat(spelBoolean).isTrue();
        assertThat(spelNameString).isEqualTo("true");
    }
}