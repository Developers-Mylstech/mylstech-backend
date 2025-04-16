package com.mylstech.product.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My API Documentation")
                        .version("v1")
                        .description("API documentation for My Spring Boot application"))
                .addServersItem(new io.swagger.v3.oas.models.servers.Server().url("https://2jx688rdxcnx.share.zrok.io") )
                .addServersItem(new io.swagger.v3.oas.models.servers.Server().url("http://javatest.mylstech.com:8080") )
                ;
    }
}

