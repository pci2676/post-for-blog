package com.javabom.jacoco.book.service;

import com.javabom.jacoco.book.domain.Book;
import com.javabom.jacoco.book.dto.BookResponseDto;
import com.javabom.jacoco.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<BookResponseDto> getAll() {
        List<Book> books = bookRepository.findAll();

        return books.stream()
                .map(BookResponseDto::from)
                .collect(Collectors.toList());
    }
}
