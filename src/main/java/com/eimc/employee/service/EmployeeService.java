package com.eimc.employee.service;

import com.eimc.auth.ApplicationUser;
import com.eimc.common.exception.BadCredentialsException;
import com.eimc.common.exception.DuplicateResourceException;
import com.eimc.common.exception.PasswordMismatchException;
import com.eimc.common.exception.ResourceNotFoundException;
import com.eimc.employee.model.Employee;
import com.eimc.employee.repository.EmployeeRepository;
import com.eimc.security.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Employee createEmployee(Employee employee, String rawPassword, UserRole role) {

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
    public Employee updatePassword(String email, String oldPassword, String newPassword, String newPasswordConfirmed){

        if (!newPassword.equals(newPasswordConfirmed)){
            throw new PasswordMismatchException("New password and confirmation password do not match");
        }

        Employee employee = getEmployeeByEmail(email);
        ApplicationUser userAccount = employee.getApplicationUser();

        if (!passwordEncoder.matches(oldPassword, userAccount.getPassword())){
            throw new BadCredentialsException("The current password is incorrect");
        }

        userAccount.setPassword(passwordEncoder.encode(newPassword));
        return employeeRepository.save(employee);
    }

    public List<Employee> getEmployees(){
        return employeeRepository.findAll();
    }

}
