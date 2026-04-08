package com.eimc.employee.dto;

import com.eimc.employee.model.Employee;

import java.util.UUID;

public record EmployeeProfileDTO(

        UUID employeeId,
        String employeePosition,
        String firstName,
        String lastName,
        String email,
        String username

) {

    public static EmployeeProfileDTO mapToResponse(Employee employee) {
        return new EmployeeProfileDTO(
                employee.getEmployeeId(),
                employee.getEmployeePosition(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getApplicationUser().getUsername()
        );
    }

}
