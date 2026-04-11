package com.eimc.auth.model;

import com.eimc.employee.model.Employee;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *      ApplicationUser is a custom implementation
 *      of UserDetails that represents an authenticated
 *      user. It allows Spring Security to perform
 *      authentication and authorization checks.
 * */

@Entity
@Table(name = "users")
public class ApplicationUser implements UserDetails {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Employee employee;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority")
    private Set<String> grantedAuthorities;

    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    public ApplicationUser() {}

    public ApplicationUser(Employee employee,
                           String username,
                           String password,
                           Set<String> grantedAuthorities,
                           boolean isAccountNonExpired,
                           boolean isAccountNonLocked,
                           boolean isCredentialsNonExpired,
                           boolean isEnabled) {
        this.employee = employee;
        this.username = username;
        this.password = password;
        this.grantedAuthorities = grantedAuthorities;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    public ApplicationUser(Employee employee,
                           String password,
                           Set<String> grantedAuthorities) {
        this.employee = employee;
        this.username = employee.getEmail();
        this.password = password;
        this.grantedAuthorities = grantedAuthorities;
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
    }

    /**
     *      getAuthorities transforms the persisted
     *      set of authority strings into a collection
     *      of SimpleGrantedAuthority objects.
     *
     *      It re-assembles the database strings
     *      into the security objects required
     *      by Spring Security for authorization.
     *
     *      MySQL -> Set<String> -> Set<SimpleGrantedAuthority>
     * */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public boolean isDeleted(){
        return this.employee.isDeleted();
    }

    public void setIsEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled && !isDeleted();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationUser that = (ApplicationUser) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
