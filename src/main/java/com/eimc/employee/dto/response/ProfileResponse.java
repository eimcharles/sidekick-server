package com.eimc.employee.dto.response;

import com.eimc.employee.model.Employee;

import java.util.UUID;

public record ProfileResponse(

        UUID employeeId,
        String employeePosition,
        String firstName,
        String lastName,
        String email,
        String username

) {

    public static ProfileResponse mapToResponse(Employee employee) {
        return new ProfileResponse(
                employee.getEmployeeId(),
                employee.getEmployeePosition(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getApplicationUser().getUsername()
        );
    }

}
