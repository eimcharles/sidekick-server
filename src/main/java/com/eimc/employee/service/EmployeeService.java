package com.eimc.employee.service;

import com.eimc.auth.model.ApplicationUser;
import com.eimc.common.exception.InactiveResourceException;
import com.eimc.common.exception.InvalidCurrentPasswordException;
import com.eimc.common.exception.PasswordMismatchException;
import com.eimc.common.exception.ResourceNotFoundException;
import com.eimc.employee.model.Employee;
import com.eimc.employee.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    public void updatePassword(String email,
                               String oldPassword,
                               String newPassword,
                               String newPasswordConfirmed){

        if (!newPassword.equals(newPasswordConfirmed)){
            throw new PasswordMismatchException("New password and confirmation password do not match");
        }

        Employee employee = getEmployeeByEmail(email);
        ApplicationUser userAccount = employee.getApplicationUser();

        if (!userAccount.isEnabled()) {
            throw new InactiveResourceException(String.format("Employee with username %s is inactive", userAccount.getUsername()));
        }

        if (!passwordEncoder.matches(oldPassword, userAccount.getPassword())){
            throw new InvalidCurrentPasswordException("The current password is incorrect");
        }

        userAccount.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);
    }

    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with email %s not found", email)));
    }

}
