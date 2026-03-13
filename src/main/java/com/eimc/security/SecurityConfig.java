package com.eimc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

/**
 *      @EnableWebSecurity disables
 *      Spring Security default
 *      settings and enables
 *      developer defined security
 *      configurations for endpoints.
 * */

public class SecurityConfig {

    /**
     *      SecurityFilterChain
     *      defines security
     *      configuration for
     *      endpoint access.
     * */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                ///  Require the client to provide credentials
                .authorizeHttpRequests(auth -> auth

                        /// Whitelist the root and index file
                        .requestMatchers("/", "/index.html").permitAll()

                        /// Require credentials for the remaining endpoints
                        .anyRequest().authenticated())

                ///  Enable Basic Auth protocol (Base64 username:password in header)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}