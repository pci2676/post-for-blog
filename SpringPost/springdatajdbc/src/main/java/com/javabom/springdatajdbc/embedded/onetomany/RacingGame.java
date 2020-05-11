package com.javabom.springdatajdbc.embedded.onetomany;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

import java.util.Set;

public class RacingGame {
    @Id
    private Long id;

    @Embedded.Nullable
    private RacingCars racingCars;

    private RacingGame() {
    }

    public RacingGame(final RacingCars racingCars) {
        this.racingCars = racingCars;
    }

    public Long getId() {
        return id;
    }

    public Set<RacingCar> getRacingCars() {
        return racingCars.getRacingCars();
    }
}
