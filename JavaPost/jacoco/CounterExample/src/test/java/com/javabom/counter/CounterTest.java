package com.javabom.counter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CounterTest {

    @Test
    void getCounter() {
        Counter counter = new Counter("LINE");

        int value = counter.getCounter();

        assertThat(value).isEqualTo(1);
    }
}