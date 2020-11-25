package com.coldkreap.reactor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * This is the base setup of a Spring Boot application.
 */
@SpringBootApplication
@EnableConfigurationProperties
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
