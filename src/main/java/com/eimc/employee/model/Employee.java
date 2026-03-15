package com.eimc.employee.model;

import java.util.Objects;
import java.util.UUID;

public class Employee {

    private UUID employeeId;
    private String employeePosition;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public Employee(UUID employeeId, String employeePosition, String firstName, String lastName, String email, String password) {
        this.employeeId = employeeId;
        this.employeePosition = employeePosition;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(employeeId, employee.employeeId)
                && Objects.equals(employeePosition, employee.employeePosition)
                && Objects.equals(firstName, employee.firstName)
                && Objects.equals(lastName, employee.lastName)
                && Objects.equals(email, employee.email)
                && Objects.equals(password, employee.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, employeePosition, firstName, lastName, email, password);
    }

}
