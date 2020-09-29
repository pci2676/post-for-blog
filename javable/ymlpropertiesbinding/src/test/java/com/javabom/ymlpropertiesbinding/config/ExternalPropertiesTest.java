package com.javabom.ymlpropertiesbinding.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ExternalPropertiesTest {

    @Autowired
    private ExternalProperties externalProperties;

    @Test
    void externalConfigurationTest() {
        assertThat(externalProperties.getRecordYear()).isEqualTo("2020");
        assertThat(externalProperties.getApi().getApiKey()).isEqualTo(123123);
    }

}