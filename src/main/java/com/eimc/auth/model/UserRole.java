package com.eimc.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.eimc.auth.model.UserPermissions.*;

/**
 *      UserRole defines the application roles
 *      and their associated permissions.
 *
 *      UserRole maps high-level roles
 *      to permissions using sets for
 *      unique authorities.
 *
 *      This allows Spring Security to
 *      verify the specific role and
 *      individual permission.
 * */

public enum UserRole {

    ADMIN(Set.of(EMPLOYEE_READ, EMPLOYEE_WRITE, EMPLOYEE_UPDATE, EMPLOYEE_DELETE)),
    ADMIN_TRAINEE(Set.of(EMPLOYEE_READ, EMPLOYEE_UPDATE)),
    USER(Set.of());

    private final Set<UserPermissions> userPermissions;

    UserRole(Set<UserPermissions> userPermissions) {
        this.userPermissions = userPermissions;
    }

    public Set<UserPermissions> getUserPermissions() {
        return userPermissions;
    }

    /**
     *      getGrantedAuthorities transforms
     *      the user's role and associated permissions
     *      into a set of SimpleGrantedAuthority objects
     *      compatible with Spring Security.
     * */

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){

        /// Map each UserPermissions enum to a SimpleGrantedAuthority
        Set<SimpleGrantedAuthority> authorities = getUserPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        /// Adds the prefix "ROLE" to for user roles
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
