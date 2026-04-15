package com.eimc.employee.service;

import com.eimc.auth.model.ApplicationUser;
import com.eimc.auth.model.UserRole;
import com.eimc.common.exception.DuplicateResourceException;
import com.eimc.common.exception.ResourceNotFoundException;
import com.eimc.employee.model.Employee;
import com.eimc.employee.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeManagementService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeManagementService(EmployeeRepository employeeRepository,
                                     PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Employee createEmployee(Employee employee,
                                   String rawPassword,
                                   UserRole role) {

        if (employeeRepository.existsByEmail(employee.getEmail()))
            throw new DuplicateResourceException(String.format("Employee with email %s already exists", employee.getEmail()));

        ApplicationUser createdAccount = new ApplicationUser(employee,
                passwordEncoder.encode(rawPassword),
                role.getGrantedAuthoritiesAsStrings()
        );

        employee.setApplicationUser(createdAccount);
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeByEmployeeId(UUID employeeId){
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("EmployeeId %s not found", employeeId)));

        if (employee.isDeleted()) {
            throw new ResourceNotFoundException(String.format("EmployeeId %s is inactive", employeeId));
        }

        return employee;
    }


    @Transactional
    public Employee updateEmployeeByEmployeeId(UUID employeeId,
                                               String employeePosition,
                                               String firstName,
                                               String lastName) {

        Employee employeeToUpdate = getEmployeeByEmployeeId(employeeId);

        if (employeePosition != null && !employeePosition.isBlank()
                && !employeePosition.equals(employeeToUpdate.getEmployeePosition())) {
            employeeToUpdate.setEmployeePosition(employeePosition);
        }

        if (firstName != null && !firstName.isBlank()
                && !firstName.equals(employeeToUpdate.getFirstName())) {
            employeeToUpdate.setFirstName(firstName);
        }

        if (lastName != null && !lastName.isBlank()
                && !lastName.equals(employeeToUpdate.getLastName())) {
            employeeToUpdate.setLastName(lastName);
        }

        return employeeRepository.save(employeeToUpdate);
    }

    @Transactional
    public void deleteByEmployeeId(UUID employeeId) {

        Employee employeeToDelete = getEmployeeByEmployeeId(employeeId);

        employeeToDelete.setIsDeleted(true);
        employeeToDelete.setDeletedAt(Instant.now());
        employeeToDelete.getApplicationUser().setIsEnabled(false);

        employeeRepository.save(employeeToDelete);
    }

    public List<Employee> getActiveEmployees(){
        return employeeRepository.findAllByIsDeletedFalse();
    }

    public List<Employee> getInactiveEmployees(){
        return employeeRepository.findAllByIsDeletedTrue();
    }

}
