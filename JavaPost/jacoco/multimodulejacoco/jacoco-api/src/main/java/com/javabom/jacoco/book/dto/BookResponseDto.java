package com.javabom.jacoco.book.dto;

import com.javabom.jacoco.book.domain.Book;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BookResponseDto {
    private String title;
    private String author;

    @Builder
    public BookResponseDto(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public static BookResponseDto from(Book book) {
        return BookResponseDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .build();
    }
}
