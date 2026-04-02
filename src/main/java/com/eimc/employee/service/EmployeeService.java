package com.eimc.employee.service;

import com.eimc.auth.ApplicationUser;
import com.eimc.employee.model.Employee;
import com.eimc.employee.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
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
    public Employee createEmployee(Employee employee, String password, Set<String> grantedAuthorities) {

        Employee createdEmployee = new Employee(
                employee.getEmployeePosition(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail()
        );

        ApplicationUser createdAccount = new ApplicationUser(
                createdEmployee,
                passwordEncoder.encode(password),
                grantedAuthorities
        );

        createdEmployee.setApplicationUser(createdAccount);
        employeeRepository.save(createdEmployee);
        return createdEmployee;
    }

    public Employee getEmployeeByEmployeeId(UUID employeeId){
        return employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new NoSuchElementException(String.format("EmployeeId: %s not found", employeeId)));
    }

    public List<Employee> getEmployees(){
        return employeeRepository.findAll();
    }

}
