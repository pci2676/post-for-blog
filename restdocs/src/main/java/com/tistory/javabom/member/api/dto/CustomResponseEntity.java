package com.tistory.javabom.member.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomResponseEntity<T> {
    private int code;
    private String message;
    private T data;

    @Builder
    public CustomResponseEntity(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CustomResponseEntity<T> ok(T data) {
        return ok(null, data);
    }

    public static <T> CustomResponseEntity<T> ok(String message, T data) {
        return CustomResponseEntity.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }
}
