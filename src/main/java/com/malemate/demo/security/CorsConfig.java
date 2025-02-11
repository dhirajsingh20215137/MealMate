package com.malemate.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Allowing all routes from your frontend URL
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")  // Frontend URL
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("Authorization", "Content-Type")  // Allowing necessary headers
                        .allowCredentials(true);  // Required for cookies

                // Allow specific access to /uploads/ route for image fetching
                registry.addMapping("/uploads/**")
                        .allowedOrigins("http://localhost:5173")  // Frontend URL
                        .allowedMethods("GET")  // Allow only GET requests to fetch images
                        .allowedHeaders("Content-Type");  // Allow content type header
            }
        };
    }
}
