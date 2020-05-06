package com.javabom.springdatajdbc.embadded.onetomany;

import org.springframework.data.annotation.Id;

public class RacingCar {
    @Id
    private Long id;
    private String carName;

    protected RacingCar() {
    }

    public RacingCar(final String carName) {
        this.carName = carName;
    }

    public Long getId() {
        return id;
    }

    public String getCarName() {
        return carName;
    }
}
