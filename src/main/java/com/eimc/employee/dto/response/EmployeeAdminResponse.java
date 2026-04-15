package com.eimc.employee.dto.response;

import com.eimc.employee.model.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


public record EmployeeAdminResponse(

        UUID employeeId,
        String employeePosition,
        String firstName,
        String lastName,
        String email,
        String username,
        Set<String> grantedAuthorities,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        Instant deletedAt,

        boolean isDeleted

) implements ProfileView {

    public static EmployeeAdminResponse mapToResponse(Employee createdEmployee) {

        return new EmployeeAdminResponse(createdEmployee.getEmployeeId(),
                createdEmployee.getEmployeePosition(),
                createdEmployee.getFirstName(),
                createdEmployee.getLastName(),
                createdEmployee.getEmail(),
                createdEmployee.getApplicationUser().getUsername(),
                createdEmployee.getApplicationUser()
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()),
                createdEmployee.getDeletedAt(),
                createdEmployee.isDeleted()
        );
    }
}
