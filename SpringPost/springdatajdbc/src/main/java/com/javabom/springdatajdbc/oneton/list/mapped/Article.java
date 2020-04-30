package com.javabom.springdatajdbc.oneton.list.mapped;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.List;

public class Article {
    @Id
    private Long id;

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

    public List<Comment> getComments() {
        return comments;
    }
}
