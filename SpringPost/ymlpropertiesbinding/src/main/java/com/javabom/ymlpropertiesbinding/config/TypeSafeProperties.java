package com.javabom.ymlpropertiesbinding.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("type-safe")
public class TypeSafeProperties {
    private String recordYear;
    private Api api;

    @Getter
    @Setter
    public static class Api {
        private String name;
        private Integer key;
    }
}
