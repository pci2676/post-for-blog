package com.javabom.ymlpropertiesbinding.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@RequiredArgsConstructor
@Configuration
public class ExternalProperties {
    private final Api api;
    @Value("${external.record-year}")
    private String recordYear;

    @Getter
    @Configuration
    public static class Api {
        @Value("${external.api.name}")
        private String apiName;
        @Value("${external.api.key}")
        private Integer apiKey;
    }
}
