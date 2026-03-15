package com.eimc.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.eimc.security.UserPermissions.*;

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

    ADMIN(Sets.newHashSet(EMPLOYEE_READ, EMPLOYEE_WRITE)),
    ADMIN_TRAINEE(Sets.newHashSet(EMPLOYEE_READ)),
    USER(Sets.newHashSet(EMPLOYEE_READ));

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

        /// Adds prefix "ROLE" to for user roles
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
