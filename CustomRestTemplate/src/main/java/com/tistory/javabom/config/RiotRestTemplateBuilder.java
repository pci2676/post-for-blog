package com.tistory.javabom.config;

import com.tistory.javabom.config.handler.RiotErrorHandler;
import com.tistory.javabom.config.interceptor.RiotTokenInterceptor;
import com.tistory.javabom.config.properties.RiotProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;

public class RiotRestTemplateBuilder {
    private static final String HOST = "https://kr.api.riotgames.com";

    public static RestTemplateBuilder get(RiotProperties riotProperties) {
        return new RestTemplateBuilder()
                .rootUri(HOST)
                .additionalInterceptors(new RiotTokenInterceptor(riotProperties))
                .errorHandler(new RiotErrorHandler());
    }
}
