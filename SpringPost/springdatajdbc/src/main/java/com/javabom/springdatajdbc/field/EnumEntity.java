package com.javabom.springdatajdbc.field;

import org.springframework.data.annotation.Id;

public class EnumEntity {
    @Id
    private Long id;

    private Active active;

    public EnumEntity() {
    }

    public EnumEntity(final Active active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public Active getActive() {
        return active;
    }
}
