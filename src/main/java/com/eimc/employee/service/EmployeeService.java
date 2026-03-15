package com.eimc.employee.service;

import com.eimc.employee.model.Employee;
import com.eimc.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(Employee employee) {

        Employee createdEmployee = new Employee(
                UUID.randomUUID(),
                employee.getEmployeePosition(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPassword()
        );

        employeeRepository.addEmployee(createdEmployee);
        return createdEmployee;
    }

    public Employee getEmployeeByEmployeeId(UUID employeeId){
        return employeeRepository.getEmployeeList()
                .stream()
                .filter(employee -> employee.getEmployeeId().equals(employeeId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));
    }

    public List<Employee> getEmployees(){
        return employeeRepository.getEmployeeList();
    }

}
