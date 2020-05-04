package com.javabom.springdatajdbc.chess.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ChessGameTest {

    @Autowired
    private ChessGameRepository chessGameRepository;

    @AfterEach
    void tearDown() {
        chessGameRepository.deleteAll();
    }

    @DisplayName("단일 엔티티 저장 테스트")
    @Test
    void save() {
        //given
        ChessGame chessGame = new ChessGame();

        //when
        ChessGame save = chessGameRepository.save(chessGame);

        //then
        assertThat(save).isNotNull();
    }
}