package com.eimc.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    /**
     *      corsConfigurationSource
     *      defines cross-origin
     *      policies for frontend
     *      client/server communication .
     * */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        /// Restrict access to a specific origin
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        /// Specify the HTTP verbs permitted for cross-origin requests
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        /// Define which request headers are accepted from the client
        configuration.setAllowedHeaders(List.of("Content-Type", "X-XSRF-TOKEN"));

        /// Allows the browser to include security cookies and CSRF tokens in cross-origin requests.
        configuration.setExposedHeaders(List.of("X-XSRF-TOKEN"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
