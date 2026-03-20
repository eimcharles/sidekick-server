package com.eimc.auth;

import com.eimc.security.UserRole;
import com.google.common.collect.Lists;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *      ApplicationUserDaoService serves as
 *      a mock data repository for testing
 *      authentication logic. It defines a
 *      list of users with encoded passwords
 *      and assigned roles.
 * */

@Repository("fake")

/**
 *      @Repository("fake") enables a Spring managed bean
 *      to be injected whenever @Qualifier("fake") is used.
 * */

public class ApplicationUserDaoService implements ApplicationUserDao {

    private final PasswordEncoder passwordEncoder;

    public ApplicationUserDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }

    public List<ApplicationUser> getApplicationUsers(){
        return Lists.newArrayList(

                new ApplicationUser("admin",
                        passwordEncoder.encode("admin"),
                        UserRole.ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true),

                new ApplicationUser("adminTrainee",
                        passwordEncoder.encode("admin"),
                        UserRole.ADMIN_TRAINEE.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true),

                new ApplicationUser("employee",
                        passwordEncoder.encode("password123"),
                        UserRole.USER.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true));

    }

}
