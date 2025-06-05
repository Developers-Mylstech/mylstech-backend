package com.mylstech.product.config;

import com.mylstech.product.security.JwtAuthenticationEntryPoint;
import com.mylstech.product.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    public static final String API_V_1_BANNERS = "/api/v1/banners/**";
    public static final String API_V_1_CLIENTS = "/api/v1/clients/**";
    public static final String API_V_1_SERVICES = "/api/v1/services/**";
    public static final String API_V_1_IMAGES = "/api/v1/images/**";
    public static final String API_V_1_CONTACT= "/api/v1/contact/**";
    public static final String API_V_1_PLANS= "/api/v1/plans/**";
    public static final String ADMIN = "ADMIN";
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Value("${zrok.url}")
    public String zrokUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors ( cors -> cors.configurationSource ( corsConfigurationSource ( ) ) )
                .csrf ( AbstractHttpConfigurer::disable )
                .exceptionHandling ( exception -> exception.authenticationEntryPoint ( jwtAuthenticationEntryPoint ) )
                .sessionManagement ( session -> session.sessionCreationPolicy ( SessionCreationPolicy.STATELESS ) )
                .authorizeHttpRequests ( auth -> auth
                        .requestMatchers ( HttpMethod.GET,
                                API_V_1_BANNERS,API_V_1_CLIENTS, API_V_1_SERVICES
                                ,API_V_1_IMAGES,API_V_1_CONTACT,API_V_1_PLANS ).permitAll ( )
                        .requestMatchers ( HttpMethod.POST,
                                API_V_1_BANNERS, API_V_1_CLIENTS, API_V_1_SERVICES
                                ,API_V_1_IMAGES,API_V_1_PLANS ).hasRole ( ADMIN )
                        .requestMatchers ( HttpMethod.PUT,
                                API_V_1_BANNERS, API_V_1_CLIENTS, API_V_1_SERVICES,
                                API_V_1_CONTACT ,API_V_1_PLANS).hasRole ( ADMIN )
                        .requestMatchers ( HttpMethod.DELETE,
                                API_V_1_BANNERS, API_V_1_CLIENTS, API_V_1_SERVICES,
                                API_V_1_IMAGES,API_V_1_PLANS ).hasRole ( ADMIN )
                        .requestMatchers ( "/api/v1/auth/**" ).permitAll ( )
                        .requestMatchers ( "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/upload.html" ).permitAll ( )
                        .requestMatchers ( "/uploads/images/**" ).permitAll ( )
                        .anyRequest ( ).authenticated ( )
                );

        http.addFilterBefore ( jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class );

        return http.build ( );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration ( );
        configuration.setAllowedOrigins (
                Arrays.asList ( zrokUrl,
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "http://javatest.mylstech.com:8080" ) ); // <-- replace with your real zrok URL
        configuration.setAllowedMethods ( Arrays.asList (
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS",
                "PATCH" ) );
        configuration.setAllowedHeaders ( List.of ( "*" ) );
        configuration.setAllowCredentials ( true );
        configuration.setMaxAge ( 3600L );

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource ( );
        source.registerCorsConfiguration ( "/**", configuration );
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager ( );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder ( );
    }
}