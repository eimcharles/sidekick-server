package com.eimc.employee.dto.request;

public record EmployeeUpdateRequest(

        String employeePosition,
        String firstName,
        String lastName

) {
}
