package com.eimc.employee.dto;

import com.eimc.security.UserRole;


public record EmployeeRegistrationDTO(

        String employeePosition,
        String firstName,
        String lastName,
        String email,
        String password,
        UserRole role

) {
}
