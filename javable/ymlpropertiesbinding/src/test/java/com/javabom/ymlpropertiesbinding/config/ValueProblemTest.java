package com.javabom.ymlpropertiesbinding.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ValueProblemTest {
    @Value("${problem.value}")
    private String stringValue;

    @Value("${problem.value}")
    private boolean booelanValue;

    @Test
    void problemTest() {
        assertThat(stringValue).isEqualTo("true");
        assertThat(booelanValue).isEqualTo(true);
    }
}
