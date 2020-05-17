package com.javabom.springdatajdbc.onetomany.list.mapped;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ArticleTest {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

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

    @DisplayName("자식 Entity는 Audit 기능을 사용할 수 없다.")
    @Test
    void save2() {
        //given
        Comment comment1 = new Comment("1");
        Comment comment2 = new Comment("2");

        Article article = new Article(Arrays.asList(comment1, comment2));

        //when
        article = articleRepository.save(article);

        //then
        assertThat(article.getComments().get(0).getCreatedAt()).isNull();
        assertThat(article.getComments().get(0).getUpdatedAt()).isNull();
        System.out.println(gson.toJson(article));
    }

    @DisplayName("자식 Entity가 추가될때 전부 DELETE 후 전부 INSERT를 진행한다. ")
    @Test
    void save3() {
        //given
        Comment comment1 = new Comment("1");
        Comment comment2 = new Comment("2");

        Article article = new Article(new ArrayList<>(Arrays.asList(comment1, comment2)));
        article = articleRepository.save(article);

        //when
        System.out.println("\n##################\n");
        Comment comment3 = new Comment("3");
        article.addComment(comment3);

        article = articleRepository.save(article);

        //then
        List<Comment> comments = article.getComments();
        assertThat(comments).hasSize(3);
    }
}