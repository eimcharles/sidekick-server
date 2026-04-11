package com.eimc.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *      PasswordConfig defines
 *      the password encoding strategy
 *      using Bcrypt.
 * */

@Configuration
public class PasswordConfig {

    /**
     *      BCryptPasswordEncoder is an
     *      implementation of the
     *      PasswordEncoder interface.
     *
     *      Setting the strength to
     *      10 makes is computationally
     *      expensive to break, yet
     *      fast for user verification.
     *
     * */

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
