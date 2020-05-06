package com.javabom.springdatajdbc.onetomany.list.mapped;

import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
