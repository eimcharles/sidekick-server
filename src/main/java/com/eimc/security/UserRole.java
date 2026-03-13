package com.eimc.security;

import com.google.common.collect.Sets;

import java.util.Set;

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
    USER(Sets.newHashSet(EMPLOYEE_READ));

    private final Set<UserPermissions> userPermissions;

    UserRole(Set<UserPermissions> userPermissions) {
        this.userPermissions = userPermissions;
    }

    public Set<UserPermissions> getUserPermissions() {
        return userPermissions;
    }
}
