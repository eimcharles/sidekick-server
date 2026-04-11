package com.eimc.employee.service;

import com.eimc.auth.model.ApplicationUser;
import com.eimc.common.exception.BadCredentialsException;
import com.eimc.common.exception.DuplicateResourceException;
import com.eimc.common.exception.PasswordMismatchException;
import com.eimc.common.exception.ResourceNotFoundException;
import com.eimc.employee.model.Employee;
import com.eimc.employee.repository.EmployeeRepository;
import com.eimc.auth.model.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository,
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
        return employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("EmployeeId %s not found", employeeId)));
    }

    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with %s not found", email)));
    }

    @Transactional
    public void updatePassword(String email,
                               String oldPassword,
                               String newPassword,
                               String newPasswordConfirmed){

        if (!newPassword.equals(newPasswordConfirmed)){
            throw new PasswordMismatchException("New password and confirmation password do not match");
        }

        Employee employee = getEmployeeByEmail(email);
        ApplicationUser userAccount = employee.getApplicationUser();

        if (!passwordEncoder.matches(oldPassword, userAccount.getPassword())){
            throw new BadCredentialsException("The current password is incorrect");
        }

        userAccount.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(UUID employeeId,
                                   String employeePosition,
                                   String firstName,
                                   String lastName) {

        Employee employeeToUpdate = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("EmployeeId %s not found", employeeId)));

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

        Employee employeeToDelete = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("EmployeeId %s not found", employeeId)));

        employeeToDelete.setIsDeleted(true);
        employeeToDelete.setDeletedAt(Instant.now());
        employeeToDelete.getApplicationUser().setIsEnabled(false);

        employeeRepository.save(employeeToDelete);
    }

    public List<Employee> getEmployees(){
        return employeeRepository.findAll();
    }

}
