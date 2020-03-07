package com.javabom.jacoco.book.repository;

import com.javabom.jacoco.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
