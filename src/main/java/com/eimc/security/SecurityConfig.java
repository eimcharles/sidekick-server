package com.eimc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

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

                        /// Whitelist the root, index.html and Swagger docs
                        .requestMatchers("/", "/index.html",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        /// Restricts management namespace to ADMIN role
                        .requestMatchers("/management/**").hasRole(UserRole.ADMIN.name())

                        /// Require credentials for the remaining endpoints
                        .anyRequest().authenticated())

                ///  Enable Basic Auth protocol (Base64 username:password in header)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     *      UserDetailsService defines
     *      custom in-memory administrative
     *      and standard accounts authorized
     *      to access the employee resources.
     */

    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))              ///  Password encoding
                .roles(UserRole.ADMIN.name())                                       ///  ROLE_ADMIN
                .build();

        UserDetails user = User.builder()
                .username("employee")
                .password(passwordEncoder.encode("password123"))       ///  Password encoding
                .roles(UserRole.USER.name())                                       ///  ROLE_USER
                .build();

        return new InMemoryUserDetailsManager(admin, user);

    }

}