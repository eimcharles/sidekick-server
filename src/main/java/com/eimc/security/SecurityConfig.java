package com.eimc.security;

import com.eimc.auth.ApplicationUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
    private final ApplicationUserService applicationUserService;

    public SecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
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

                /// Write the CsrfToken into a cookie for browser to store
                .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                        /// Make the CsrfToken available as a request attribute.
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))

                /// Authorization requires the client to provide credentials
                .authorizeHttpRequests(auth -> auth

                        /// Whitelist the root, index.html and Swagger docs
                        .requestMatchers("/", "/index.html",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        /// Restricts management namespace to ADMIN and ADMIN_TRAINEE roles
                        .requestMatchers("/management/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.ADMIN_TRAINEE.name())

                        /// Require credentials for the remaining endpoints
                        .anyRequest().authenticated())

                /// Uncomment for basic Authentication protocol (Base64 username:password in header)
                /// .httpBasic(Customizer.withDefaults())

                /// Form Based Authentication with login
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        ///  Redirect to dashboard
                        .defaultSuccessUrl("/dashboard",true)
                        ///  Form parameter
                        .usernameParameter("username")
                        .passwordParameter("password"))

                ///  Sets remember-me cookie expiration to 30 days (in seconds)
                .rememberMe(remember -> remember
                        .tokenValiditySeconds(2592000)
                        /// Hardcoding the key ensures the cookie remains valid after server restarts
                        .key("uniqueAndSecret")
                        ///  Form parameter
                        .rememberMeParameter("remember-me")
                        /// Look up the user account in memory once the cookie is validated
                        .userDetailsService(applicationUserService))

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        ///  For logout success message in login.html
                        .logoutSuccessUrl("/login?logout")
                        ///  Clear data and invalidate session
                        .deleteCookies("JSESSIONID", "remember-me")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        ///  Allows all users to logout, with data cleared and session invalidated
                        .permitAll())

                ///  Uncomment for additional filter to trigger CsrfToken generation after basic auth
                /// .addFilterAfter(new CsrfHeaderFilter(), BasicAuthenticationFilter.class);

                /// Additional filter to trigger CsrfToken generation after form based auth
                .addFilterAfter(new CsrfHeaderFilter(), UsernamePasswordAuthenticationFilter.class)

                /// Explicitly register the custom DaoAuthenticationProvider for credential verification.
                .authenticationProvider(daoAuthenticationProvider());

        return http.build();
    }

    /**
     *      Configures the DaoAuthenticationProvider to use
     *      custom user retrieval and password encoding logic.
     *
     *      This bean is required to bridge the custom
     *      ApplicationUserService with Spring Security's
     *      authentication manager.
     *
     *      It ensures that when a user logs in, their
     *      credentials are fetched via the
     *      ApplicationUserDaoService and verified
     *      using the Bcrypt password encoder.
     * */

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }

}