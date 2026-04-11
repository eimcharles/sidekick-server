package com.eimc.employee.dto.request;

import com.eimc.auth.model.UserRole;

public record EmployeeCreationRequest(

        String employeePosition,
        String firstName,
        String lastName,
        String email,
        String password,
        UserRole role

) {
}
