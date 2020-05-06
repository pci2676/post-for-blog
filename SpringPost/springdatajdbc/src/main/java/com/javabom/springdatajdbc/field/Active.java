package com.javabom.springdatajdbc.field;

public enum Active {
    Y(true),
    N(false);

    private final boolean value;

    Active(final boolean value) {
        this.value = value;
    }

    public boolean value() {
        return value;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
