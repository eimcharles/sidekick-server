package com.eimc.employee.dto.request;

import com.eimc.security.UserRole;

public record RegistrationRequest(

        String employeePosition,
        String firstName,
        String lastName,
        String email,
        String password,
        UserRole role

) {
}
