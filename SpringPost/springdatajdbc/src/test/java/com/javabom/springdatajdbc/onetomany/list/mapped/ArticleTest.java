package com.javabom.springdatajdbc.onetomany.list.mapped;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ArticleTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        articleRepository.deleteAll();
    }

    @DisplayName("컬럼기본전략과 데이터베이스의 컬럼이 상이한 경우에도 가능하다")
    @Test
    void save() {
        //given
        Comment comment1 = new Comment("asdf");
        Comment comment2 = new Comment("qwer");
        Comment comment3 = new Comment("zxcv");

        Article article1 = new Article(Arrays.asList(comment1, comment2, comment3));

        //when
        Article save = articleRepository.save(article1);
        Article load = articleRepository.findById(save.getId()).orElseThrow(NoSuchElementException::new);

        //then
        System.out.println(save.getCreatedAt());
        System.out.println(load.getCreatedAt());
        assertThat(save.getCreatedAt().equals(load.getCreatedAt()));
        assertThat(load.getComments()).hasSize(3);
    }
}