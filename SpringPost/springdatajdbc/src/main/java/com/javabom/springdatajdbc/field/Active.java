package com.javabom.springdatajdbc.field;

public enum Active {
    TRUE("Y", true),
    FALSE("N", false);

    private final String message;
    private final boolean value;

    Active(final String message, final boolean value) {
        this.message = message;
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public boolean value() {
        return value;
    }
}
