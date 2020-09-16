package io.javable.beanconfiguration.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBeanConfiguration {

    @Bean
    public MyCustomBean myCustomBean() {
        return new MyCustomBean();
    }
}
