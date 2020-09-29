package com.javabom.ymlpropertiesbinding.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("constructor")
public final class ConstructorProperties {
    private final String recordYear;
    private final Api api;

    @Getter
    @RequiredArgsConstructor
    public static final class Api {
        private final String name;
        private final Integer key;
    }
}
