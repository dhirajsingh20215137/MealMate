package com.malemate.demo.security;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class MultipartConfig {

    // Bean to configure file upload settings (max file size, max request size, etc.)
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(5));  // Set max file size to 5MB
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));  // Set max request size to 10MB
        return factory.createMultipartConfig();
    }
}
