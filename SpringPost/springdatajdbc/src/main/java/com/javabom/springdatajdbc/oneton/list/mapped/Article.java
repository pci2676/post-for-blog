package com.javabom.springdatajdbc.oneton.list.mapped;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.time.LocalDateTime;
import java.util.List;

public class Article {
    @Id
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @MappedCollection(idColumn = "article_id", keyColumn = "article_key")
    private List<Comment> comments;

    public Article() {
    }

    public Article(final List<Comment> comments) {
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Comment> getComments() {
        return comments;
    }

}
