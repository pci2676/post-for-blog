package com.javabom.springdatajdbc.embadded.onetomany;

import java.util.HashSet;
import java.util.Set;

public class RacingCars {
    private Set<RacingCar> racingCars;

    public RacingCars(final Set<RacingCar> racingCars) {
        this.racingCars = racingCars;
    }

    public Set<RacingCar> getRacingCars() {
        return new HashSet<>(racingCars);
    }
}
