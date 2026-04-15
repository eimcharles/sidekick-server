package com.eimc.employee.controller;

import com.eimc.common.domain.HttpResponse;
import com.eimc.employee.dto.request.EmployeeCreationRequest;
import com.eimc.employee.dto.request.EmployeeUpdateRequest;
import com.eimc.employee.dto.response.EmployeeAdminResponse;
import com.eimc.employee.model.Employee;
import com.eimc.employee.service.EmployeeManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *      EmployeeManagementController provides administrative
 *      endpoints for managing employees.
 * */

@RestController
@RequestMapping("management/api/v1/employees")
public class EmployeeManagementController {

    private final EmployeeManagementService employeeManagementService;

    public EmployeeManagementController(EmployeeManagementService employeeManagementService) {
        this.employeeManagementService = employeeManagementService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('employee:write')")
    public ResponseEntity<HttpResponse> createEmployee(
            @RequestBody EmployeeCreationRequest employeeCreationRequest,
            HttpServletRequest request){

        Employee newEmployee = new Employee(
                employeeCreationRequest.employeePosition(),
                employeeCreationRequest.firstName(),
                employeeCreationRequest.lastName(),
                employeeCreationRequest.email());

        Employee createdEmployee = employeeManagementService
                .createEmployee(newEmployee,
                        employeeCreationRequest.password(),
                        employeeCreationRequest.role());

        URI resourceLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEmployee.getEmployeeId())
                .toUri();

        return ResponseEntity.created(resourceLocation)
                .body(HttpResponse.builder()
                .timeStamp(Instant.now())
                .statusCode(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .message("Employee successfully created")
                .path(resourceLocation.getPath())
                .requestMethod(request.getMethod())
                .data(Map.of("Employee", EmployeeAdminResponse
                        .mapToResponse(createdEmployee)))
                .build());

    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('employee:read')")
    public ResponseEntity<HttpResponse> getEmployeeByEmployeeId(
            @PathVariable UUID employeeId,
            HttpServletRequest request){

        Employee employee = employeeManagementService.getEmployeeByEmployeeId(employeeId);
        return ResponseEntity.ok().body(HttpResponse.builder()
                .timeStamp(Instant.now())
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("Employee successfully retrieved")
                .path(request.getRequestURI())
                .requestMethod(request.getMethod())
                .data(Map.of("Employee", EmployeeAdminResponse
                        .mapToResponse(employee)))
                .build());

    }

    @PatchMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('employee:update')")
    public ResponseEntity<HttpResponse> updateEmployee(
            @PathVariable UUID employeeId,
            @RequestBody EmployeeUpdateRequest employeeUpdateRequest,
            HttpServletRequest request) {

        Employee updatedEmployee = employeeManagementService
                .updateEmployeeByEmployeeId(employeeId,
                        employeeUpdateRequest.employeePosition(),
                        employeeUpdateRequest.firstName(),
                        employeeUpdateRequest.lastName());

        return ResponseEntity.ok().body(HttpResponse.builder()
                .timeStamp(Instant.now())
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("Employee successfully updated")
                .path(request.getRequestURI())
                .requestMethod(request.getMethod())
                .data(Map.of("Employee", EmployeeAdminResponse
                        .mapToResponse(updatedEmployee)))
                .build());

    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('employee:delete')")
    public ResponseEntity<HttpResponse> deleteEmployee(
            @PathVariable UUID employeeId,
            HttpServletRequest request){

        employeeManagementService.deleteByEmployeeId(employeeId);
        return ResponseEntity.ok().body(HttpResponse.builder()
                .timeStamp(Instant.now())
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("Employee successfully deleted")
                .path(request.getRequestURI())
                .requestMethod(request.getMethod())
                .build());

    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('employee:read')")
    public ResponseEntity<HttpResponse> getActiveEmployees(HttpServletRequest request){

        List<Employee> employees = employeeManagementService.getActiveEmployees();

        return ResponseEntity.ok().body(HttpResponse.builder()
                .timeStamp(Instant.now())
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("Active employees successfully retrieved")
                .path(request.getRequestURI())
                .requestMethod(request.getMethod())
                .data(Map.of("Active employees", employees.stream()
                        .map(EmployeeAdminResponse::mapToResponse)
                        .toList()))
                .build());

    }

    @GetMapping("/inactive")
    @PreAuthorize("hasAuthority('employee:read')")
    public ResponseEntity<HttpResponse> getInactiveEmployees(HttpServletRequest request){

        List<Employee> employees = employeeManagementService.getInactiveEmployees();

        return ResponseEntity.ok().body(HttpResponse.builder()
                .timeStamp(Instant.now())
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("Inactive employees successfully retrieved")
                .path(request.getRequestURI())
                .requestMethod(request.getMethod())
                .data(Map.of("Inactive employees", employees.stream()
                        .map(EmployeeAdminResponse::mapToResponse)
                        .toList()))
                .build());

    }

}
