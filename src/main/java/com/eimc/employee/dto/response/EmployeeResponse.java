package com.eimc.employee.dto.response;

import com.eimc.employee.model.Employee;

import java.util.UUID;

public record EmployeeResponse(

        UUID employeeId,
        String employeePosition,
        String firstName,
        String lastName,
        String email,
        String username

) implements ProfileView {

    public static EmployeeResponse mapToResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getEmployeeId(),
                employee.getEmployeePosition(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getApplicationUser().getUsername()
        );
    }

}
