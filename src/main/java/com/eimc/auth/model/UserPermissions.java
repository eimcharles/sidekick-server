package com.eimc.auth.model;

/**
 *      UserPermissions defines permissions
 *      for a given role within the application.
 *
 *      Each permission is represented as a
 *      "resources:action" which follows the
 *      standard convention for Spring
 *      Security authorities.
 * */

public enum UserPermissions {

    EMPLOYEE_READ("employee:read"),
    EMPLOYEE_WRITE("employee:write"),
    EMPLOYEE_UPDATE("employee:update"),
    EMPLOYEE_DELETE("employee:delete");

    private final String permission;

    UserPermissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
