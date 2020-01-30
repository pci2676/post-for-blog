package com.tistory.javabom.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "riot")
public class RiotProperties {

    private Token token = new Token();

    public String getKey() {
        return Token.KEY;
    }

    public String getValue() {
        return token.value;
    }

    @Getter
    @Setter
    public static class Token {

        public static final String KEY = "X-Riot-Token";
        private String value = "";
    }

}