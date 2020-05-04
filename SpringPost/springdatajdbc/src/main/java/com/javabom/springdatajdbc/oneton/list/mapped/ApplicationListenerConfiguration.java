package com.javabom.springdatajdbc.oneton.list.mapped;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@EnableJdbcAuditing
@Configuration
public class ApplicationListenerConfiguration {

    @Bean
    public ApplicationListener<BeforeSaveEvent> createdAtSaveTime() {
        return event -> {
            Object entity = event.getEntity();
            if (!(entity instanceof Article)) {
                return;
            }

            Article article = (Article) entity;
            Field[] declaredFields = Article.class.getDeclaredFields();
            for (Field field : declaredFields) {
                roundThreeDecimal(article, field);
            }
        };
    }

    private void roundThreeDecimal(final Article article, final Field field) {
        if (!LocalDateTime.class.equals(field.getType())) {
            return;
        }

        field.setAccessible(true);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String format = article.getCreatedAt().format(dateTimeFormatter);
        try {
            field.set(article, LocalDateTime.parse(format, dateTimeFormatter));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
