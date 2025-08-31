package com.cosmo.cosmo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter) throws Exception {
        return http
            // Disable CSRF for stateless REST APIs
            .csrf(AbstractHttpConfigurer::disable)

            // Enforce stateless session management for JWT-based authentication
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Define authorization rules - Phase 2: Hardening
            .authorizeHttpRequests(authorize -> authorize
                // Only authentication endpoints are public
                .requestMatchers("/auth/signin", "/auth/refresh/**").permitAll()

                // All user management endpoints require authentication
                .requestMatchers("/api/users/**").authenticated()

                // All other endpoints require authentication
                .anyRequest().authenticated()
            )

            // Integrate the custom JWT filter before UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)

            .build();
    }
}
