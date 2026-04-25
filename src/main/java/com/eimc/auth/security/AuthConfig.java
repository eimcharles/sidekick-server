package com.eimc.auth.security;

import com.eimc.auth.service.ApplicationUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class AuthConfig {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;

    public AuthConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
    }

    /**
     *      daoAuthenticationProvider
     *      uses a database for user
     *      retrieval and password verification
     *      for user authentication.
     * */

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    /**
     *      authenticationManager
     *      exposes the interface
     *      that validates user
     *      credentials using
     *      the data retrieved by the
     *      DaoAuthenticationProvider.
     * */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     *      securityContextRepository
     *      manages the persistence
     *      of authenticated users
     *      identity and roles
     *      using HTTP sessions.
     * */

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }


    /**
     *      securityContextLogoutHandler
     *      performs the steps for
     *      logging a user out,
     *      invalidating the session and
     *      clearing the SecurityContext.
     */

    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }

}
