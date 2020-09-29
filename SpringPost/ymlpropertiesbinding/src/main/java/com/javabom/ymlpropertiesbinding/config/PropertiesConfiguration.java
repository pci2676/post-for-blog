package com.javabom.ymlpropertiesbinding.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {ConstructorProperties.class})
public class PropertiesConfiguration {
}
