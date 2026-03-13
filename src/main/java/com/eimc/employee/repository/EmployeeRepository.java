package com.eimc.employee.repository;

import com.eimc.employee.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class EmployeeRepository {

    private final List<Employee> EMPLOYEE_LIST = new ArrayList<>(List.of(

            ///  Resources to be accessed by Admin
            new Employee(UUID.fromString("8f3c2b1a-5d9e-4a6b-bc7d-ef0123456789"), "Developer",
                    "Charles", "Eimer", "c.eimer@me.com", "password123"),

            new Employee(UUID.fromString("2d4e6f8a-1c3b-4a5e-9d7c-8b6a43210f9e"), "Developer",
                    "James", "Bond", "jbond@me.com", "bond123"),

            new Employee(UUID.fromString("a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d"),"Developer",
                    "Kobe", "Bryant", "mamba@me.com", "mamba8")

    ));

    public List<Employee> getEmployeeList() {
        return EMPLOYEE_LIST;
    }
}
