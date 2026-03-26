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
                employee.getEmployeePosition(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPassword()
        );

        employeeRepository.save(createdEmployee);
        return createdEmployee;
    }

    public Employee getEmployeeByEmployeeId(UUID employeeId){
        return employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));
    }

    public List<Employee> getEmployees(){
        return employeeRepository.findAll();
    }

}
