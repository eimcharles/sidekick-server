package com.eimc.auth.security;

import com.eimc.auth.service.ApplicationUserService;
import com.eimc.auth.filter.CsrfHeaderFilter;
import com.eimc.auth.model.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfigurationSource;

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

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final CorsConfigurationSource corsConfigurationSource;
    private final SecurityContextRepository securityContextRepository;

    public SecurityConfig(DaoAuthenticationProvider daoAuthenticationProvider,
                          CorsConfigurationSource corsConfigurationSource,
                          SecurityContextRepository securityContextRepository) { // Inject it here
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.corsConfigurationSource = corsConfigurationSource;
        this.securityContextRepository = securityContextRepository;
    }

    /**
     *      filterChain
     *      defines security
     *      configuration for
     *      endpoint access.
     * */

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            RememberMeServices rememberMeServices) throws Exception {

        http
                /// Enable Cross-Origin resource sharing
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                /// Persist user sessions across requests
                .securityContext(context ->
                        context.securityContextRepository(securityContextRepository))

                /// Return 401 instead of logic page direct
                .exceptionHandling(e ->
                                e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

                /// Disable CSRF protection for auth endpoints to allow the initial login handshake
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/v1/auth/login**")

                        /// Use a cookie-based repository that React can access via JavaScript
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                        /// Link the CsrfToken to request attributes so the doFilterInternal method can provide it to the React client
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))

                ///  Secured endpoints and whitelisted resources
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home/**","/images/**",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
                                ,"/api/v1/auth/login**").permitAll()
                        .requestMatchers("/management/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.ADMIN_TRAINEE.name())
                        .requestMatchers( "/api/v1/auth/logout", "/api/v1/profile/**").authenticated()
                        .anyRequest().authenticated())

                /// Remember-me cookie expiration to 30 days (in seconds)
                .rememberMe(remember ->
                        remember.rememberMeServices(rememberMeServices))

                /// Ensure the CSRF token is updated in the header/cookie after authentication
                .addFilterAfter(new CsrfHeaderFilter(), UsernamePasswordAuthenticationFilter.class)
                /// Register the daoAuthenticationProvider to the authentication manager to enable database-backed user validation.
                .authenticationProvider(daoAuthenticationProvider);

        return http.build();
    }

    @Bean
    public RememberMeServices rememberMeServices(ApplicationUserService userDetailsService) {

        TokenBasedRememberMeServices rememberMe =
                new TokenBasedRememberMeServices("uniqueAndSecret", userDetailsService);

        /// Create the rememberMe cookie
        rememberMe.setAlwaysRemember(true);
        /// Cookie validity 30 days
        rememberMe.setTokenValiditySeconds(2592000);
        /// Parameter for frontend
        rememberMe.setParameter("rememberMe");
        return rememberMe;

    }

}