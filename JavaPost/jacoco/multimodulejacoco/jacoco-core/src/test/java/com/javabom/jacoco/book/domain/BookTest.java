package com.javabom.jacoco.book.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @Test
    void create() {
        Book book = new Book("좌충우돌 자코코", "찬인", 0L);

        assertThat(book.getId()).isNull();
        assertThat(book.getTitle()).isEqualTo("좌충우돌 자코코");
        assertThat(book.getAuthor()).isEqualTo("찬인");
        assertThat(book.getPrice()).isEqualTo(0);
    }
}