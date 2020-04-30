package com.javabom.springdatajdbc.oneton.list.mapped;

import org.springframework.data.annotation.Id;

public class Comment {
    @Id
    private Long id;

    private Long articleId;

    private String content;

    public Comment() {
    }

    public Comment(final String content) {
        this.articleId = articleId;
        this.content = content;
    }
}
