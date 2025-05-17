package com.mylstech.product.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final FileStorageProperties fileStorageProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get ( fileStorageProperties.getUploadDir ( ) ).toAbsolutePath ( ).normalize ( );

        registry.addResourceHandler ( "/uploads/images/**" )
                .addResourceLocations ( "file:" + uploadDir.toString ( ) + "/" )
                .setCachePeriod ( 3600 );
    }
}
