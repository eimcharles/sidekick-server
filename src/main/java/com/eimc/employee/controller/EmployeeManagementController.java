package com.eimc.employee.controller;

import com.eimc.employee.dto.request.EmployeeCreationRequest;
import com.eimc.employee.dto.response.EmployeeAdminResponse;
import com.eimc.employee.model.Employee;
import com.eimc.employee.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *      EmployeeManagementController provides administrative
 *      endpoints for managing employees.
 * */

@RestController
@RequestMapping("management/api/v1/employees")
public class EmployeeManagementController {

    private final EmployeeService employeeService;

    public EmployeeManagementController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('employee:write')")
    public ResponseEntity<EmployeeAdminResponse> createEmployee(@RequestBody EmployeeCreationRequest request){

        Employee newEmployee = new Employee(
                request.employeePosition(),
                request.firstName(),
                request.lastName(),
                request.email());

        Employee createdEmployee = employeeService
                .createEmployee(newEmployee, request.password(), request.role());

        return ResponseEntity.ok(EmployeeAdminResponse.mapToResponse(createdEmployee));
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('employee:read')")
    public ResponseEntity<EmployeeAdminResponse> getEmployeeByEmployeeId(@PathVariable UUID employeeId){

        Employee employee = employeeService.getEmployeeByEmployeeId(employeeId);
        return ResponseEntity.ok(EmployeeAdminResponse.mapToResponse(employee));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('employee:read')")
    public ResponseEntity<List<EmployeeAdminResponse>> getEmployees(){

        List<Employee> employees = employeeService.getEmployees();

        List<EmployeeAdminResponse> response = employees
                .stream()
                .map(EmployeeAdminResponse::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}
