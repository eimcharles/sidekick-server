package com.eimc.employee.controller;

import com.eimc.employee.dto.request.RegistrationRequest;
import com.eimc.employee.dto.response.RegistrationResponse;
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
    public ResponseEntity<RegistrationResponse> createEmployee(@RequestBody RegistrationRequest request){

        Employee newEmployee = new Employee(
                request.employeePosition(),
                request.firstName(),
                request.lastName(),
                request.email());

        Employee createdEmployee = employeeService
                .createEmployee(newEmployee, request.password(), request.role());

        return ResponseEntity.ok(RegistrationResponse.mapToResponse(createdEmployee));
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('employee:read')")
    public ResponseEntity<RegistrationResponse> getEmployeeByEmployeeId(@PathVariable UUID employeeId){
        Employee employee = employeeService.getEmployeeByEmployeeId(employeeId);
        return ResponseEntity.ok(RegistrationResponse.mapToResponse(employee));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('employee:read')")
    public ResponseEntity<List<RegistrationResponse>> getEmployees(){
        List<Employee> employees = employeeService.getEmployees();
        List<RegistrationResponse> response = employees
                .stream()
                .map(RegistrationResponse::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}
