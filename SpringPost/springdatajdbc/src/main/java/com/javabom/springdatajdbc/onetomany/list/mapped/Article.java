package com.javabom.springdatajdbc.onetomany.list.mapped;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.time.LocalDateTime;
import java.util.List;

public class Article {
    @Id
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @MappedCollection(idColumn = "article_id", keyColumn = "article_key")
    private List<Comment> comments;

    private Article() {
    }

    public Article(final List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(final Comment comment) {
        this.comments.add(comment);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
