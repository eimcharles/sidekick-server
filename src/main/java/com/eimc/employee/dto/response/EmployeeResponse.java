package com.eimc.employee.dto.response;

import com.eimc.employee.model.Employee;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record EmployeeResponse(

        UUID employeeId,
        String employeePosition,
        String firstName,
        String lastName,
        String email,
        String username,
        String userRole,
        Set<String> grantedAuthorities

) implements ProfileView {

    public static EmployeeResponse mapToResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getEmployeeId(),
                employee.getEmployeePosition(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getApplicationUser().getUsername(),
                employee.getApplicationUser().getUserRole().name(),
                employee.getApplicationUser()
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet())
        );
    }

}
