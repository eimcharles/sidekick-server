package com.eimc.employee.repository;

import com.eimc.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeId(UUID employeeId);
    Optional<Employee> findByEmail(String email);
    boolean existsByEmail(String email);
}
