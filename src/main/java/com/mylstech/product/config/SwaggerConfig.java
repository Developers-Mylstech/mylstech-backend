package com.mylstech.product.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Value("${zrok.url}")
    public String zrokUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI ( )
                .info ( new Info ( )
                        .title ( "My API Documentation" )
                        .version ( "v1" )
                        .description ( "API documentation for My Spring Boot application" )
                        .contact ( new Contact ( )
                                .name ( "MyLS Tech" )
                                .url ( "https://javatest.mylstech.com" )
                                .email ( "support@mylstech.com" ) ) )
                .addServersItem ( new io.swagger.v3.oas.models.servers.Server ( ).url ( zrokUrl ) )
                .addServersItem ( new io.swagger.v3.oas.models.servers.Server ( ).url ( "https://javatest.mylstech.com" ) )
                .addServersItem ( new io.swagger.v3.oas.models.servers.Server ( ).url ( "http://localhost:8081" ) )
                // Add security scheme
                .components ( new Components ( )
                        .addSecuritySchemes ( securitySchemeName,
                                new SecurityScheme ( )
                                        .type ( SecurityScheme.Type.HTTP )
                                        .scheme ( "bearer" )
                                        .bearerFormat ( "JWT" )
                                        .description ( "Enter JWT token with Bearer prefix, e.g. 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'" )
                        ) )
                // Apply security globally to all operations
                .addSecurityItem ( new SecurityRequirement ( ).addList ( securitySchemeName ) )
                // Add external documentation for image upload
                .externalDocs ( new ExternalDocumentation ( )
                        .description ( "Image Upload UI" )
                        .url ( "/upload.html" ) );
    }
}

