package com.javabom.jacoco.question.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionTest {

    @Test
    void createTest() {
        Question question = new Question("하라는대로 했는데 안되면 보통은 내가 뭘 잘못했더라..");

        assertThat(question.getId()).isNull();
        assertThat(question.getContents()).isEqualTo("하라는대로 했는데 안되면 보통은 내가 뭘 잘못했더라..");
    }
}