package com.eimc.employee.model;

import com.eimc.auth.ApplicationUser;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private ApplicationUser applicationUser;

    @Column(nullable = false, unique = true)
    private UUID employeeId;

    @Column(nullable = false)
    private String employeePosition;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    public Employee() {}

    public Employee(Long id,
                    UUID employeeId,
                    String employeePosition,
                    String firstName,
                    String lastName,
                    String email) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeePosition = employeePosition;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Employee(String employeePosition,
                    String firstName,
                    String lastName,
                    String email) {
        this.employeePosition = employeePosition;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @PrePersist
    public void initializeUUID() {
        if (this.employeeId == null) {
            this.employeeId = UUID.randomUUID();
        }
    }

    public Long getId() {
        return id;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
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

    public void setId(Long id) {
        this.id = id;
    }

    /**
     *      Synchronizes the bidirectional
     *      relationship between Employee
     *      and ApplicationUser by linking
     *      them in memory.
     * */

    public void setApplicationUser(ApplicationUser user) {
        this.applicationUser = user;
        if (user != null && user.getEmployee() != this) {
            user.setEmployee(this);
        }
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(employeeId, employee.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }

}
