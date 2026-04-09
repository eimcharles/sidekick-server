package com.eimc.employee.controller;

import com.eimc.employee.dto.request.PasswordUpdateRequest;
import com.eimc.employee.dto.response.ProfileResponse;
import com.eimc.employee.model.Employee;
import com.eimc.employee.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 *      EmployeeProfileController provides self-service endpoints
 *      for the currently authenticated user to manage their own profile.
 * */

@RestController
@RequestMapping("api/v1/profile")
public class EmployeeProfileController {

    private final EmployeeService employeeService;

    public EmployeeProfileController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> getEmployeeProfile(Authentication authentication){
        String employeeUsername = authentication.getName();
        Employee employee = employeeService.getEmployeeByEmail(employeeUsername);
        return ResponseEntity.ok(ProfileResponse.mapToResponse(employee));
    }

    @PatchMapping
    public ResponseEntity<ProfileResponse> updatePassword(
            Authentication authentication,
            @RequestBody PasswordUpdateRequest request){

        String employeeUsername = authentication.getName();
        Employee employee = employeeService.updatePassword(employeeUsername,
                request.oldPassword(), request.newPassword(), request.newPasswordConfirmed());
        return ResponseEntity.ok(ProfileResponse.mapToResponse(employee));
    }

}
