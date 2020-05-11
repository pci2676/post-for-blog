package com.javabom.springdatajdbc.embedded.onetomany;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RacingGameTest {

    @Autowired
    private RacingGameRepository racingGameRepository;

    @Autowired
    private RacingCarRepository racingCarRepository;

    @Test
    void name() {
        //given
        RacingCar racingCar1 = new RacingCar("1");
        RacingCar racingCar2 = new RacingCar("2");
        RacingCars racingCars = new RacingCars(new HashSet<>(Arrays.asList(racingCar1, racingCar2)));

        RacingGame racingGame = new RacingGame(racingCars);

        //when
        racingGame = racingGameRepository.save(racingGame);
        RacingGame findRacingGame = racingGameRepository.findById(racingGame.getId()).orElseThrow(RuntimeException::new);

        //then
        assertThat(findRacingGame.getId()).isNotNull();
        assertThat(findRacingGame.getRacingCars()).hasSize(2);
    }

}