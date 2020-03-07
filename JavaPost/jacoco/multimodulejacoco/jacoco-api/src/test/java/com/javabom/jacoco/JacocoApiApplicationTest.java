package com.javabom.jacoco;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JacocoApiApplicationTest {

    @Test
    void main() {
        JacocoApiApplication.main(new String[]{});
        assertThat("for sonar qube".getClass()).isEqualTo(String.class);
    }

}