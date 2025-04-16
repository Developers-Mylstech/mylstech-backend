package com.mylstech.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

/**
 * Configuration class for Cross-Origin Resource Sharing (CORS) settings
 *
 * This configuration ensures that cross-origin requests are allowed from any origin,
 * which is essential when your frontend and backend are hosted on different domains.
 */
@Configuration
public class CorsConfig {

    /**
     * Global CORS configuration for the application using WebMvcConfigurer
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://2jx688rdxcnx.share.zrok.io", "http://localhost:5173","http://javatest.mylstech.com:8080") // <-- replace with your real zrok URL
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true); // Only if you’re using cookies/auth headers
            }
        };
    }
}
