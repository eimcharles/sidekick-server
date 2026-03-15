package com.eimc.employee.controller;

import com.eimc.employee.model.Employee;
import com.eimc.employee.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee){
        Employee createdEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.ok(createdEmployee);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<?> getEmployeeByEmployeeId(@PathVariable UUID employeeId){
        Employee employee = employeeService.getEmployeeByEmployeeId(employeeId);
        return ResponseEntity.ok(employee);
    }

    @GetMapping
    public ResponseEntity<?> getEmployees(){
        List<Employee> employees = employeeService.getEmployees();
        return ResponseEntity.ok(employees);
    }

}
