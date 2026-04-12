package com.eimc.auth.security;

import com.eimc.auth.service.ApplicationUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    /**
     *      authenticationManager
     *      exposes the interface
     *      that validates user
     *      credentials.
     * */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     *      securityContextRepository
     *      manages the storage
     *      of authenticated users
     *      within the session.
     * */

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

}
