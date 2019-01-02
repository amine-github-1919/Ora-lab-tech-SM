package com.oracle.techtrial.notebook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedHeaders("*")
                        .allowedMethods("POST", "OPTIONS")
                        .allowedOrigins("*")
                        .exposedHeaders("SESSION-ID");
            }
        };
    }
}
