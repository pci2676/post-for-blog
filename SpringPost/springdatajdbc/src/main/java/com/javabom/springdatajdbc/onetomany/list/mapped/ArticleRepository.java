package com.javabom.springdatajdbc.onetomany.list.mapped;

import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Long> {
}
