package com.eimc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity

/**
 *      @EnableWebSecurity disables
 *      Spring Security default
 *      settings and enables
 *      developer defined security
 *      configurations for endpoints.
 *
 *      @EnableMethodSecurity enables
 *      Spring Security's method-level Security.
 *      Allows for @PreAuthorize annotations for
 *      authorities on controller methods.
 *
 * */

public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     *      filterChain
     *      defines security
     *      configuration for
     *      endpoint access.
     * */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http

                /// 3rd: write the CsrfToken into a cookie for browser to store
                .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                        /// Make the CsrfToken available as a request attribute.
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))


                /// 4th: Authorization requires the client to provide credentials
                .authorizeHttpRequests(auth -> auth

                        /// Whitelist the root, index.html and Swagger docs
                        .requestMatchers("/", "/index.html",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        /// Restricts management namespace to ADMIN and ADMIN_TRAINEE roles
                        .requestMatchers("/management/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.ADMIN_TRAINEE.name())

                        /// Require credentials for the remaining endpoints
                        .anyRequest().authenticated())

                /// Basic Authentication protocol (Base64 username:password in header)
                /// .httpBasic(Customizer.withDefaults())

                /// Form Based Authentication with Login redirect
                .formLogin(form -> form.loginPage("/login")
                        .permitAll())

                /// 2nd: Additional filter to trigger CsrfToken generation after basic auth
                /// .addFilterAfter(new CsrfHeaderFilter(), BasicAuthenticationFilter.class);

                /// 2nd: Additional filter to trigger CsrfToken generation after form based auth
                .addFilterAfter(new CsrfHeaderFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     *      userDetailsService defines
     *      custom in-memory administrative
     *      and standard accounts authorized
     *      to access the employee resources.
     */

    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))              ///  Password encoding
                .authorities(UserRole.ADMIN.getGrantedAuthorities())                ///  ROLES + PERMISSIONS
                .build();

        UserDetails adminTrainee = User.builder()
                .username("adminTrainee")
                .password(passwordEncoder.encode("admin"))              ///  Password encoding
                .authorities(UserRole.ADMIN_TRAINEE.getGrantedAuthorities())        ///  ROLES + PERMISSIONS
                .build();

        UserDetails user = User.builder()
                .username("employee")
                .password(passwordEncoder.encode("password123"))       ///  Password encoding
                .authorities(UserRole.USER.getGrantedAuthorities())                ///  ROLES + PERMISSIONS
                .build();

        return new InMemoryUserDetailsManager(admin, adminTrainee ,user);

    }

}