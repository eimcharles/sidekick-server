package com.eimc.employee.controller;

import com.eimc.common.domain.HttpResponse;
import com.eimc.employee.dto.request.PasswordUpdateRequest;
import com.eimc.employee.dto.response.EmployeeResponse;
import com.eimc.employee.model.Employee;
import com.eimc.employee.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

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
    public ResponseEntity<HttpResponse> getEmployeeProfile(
            Authentication authentication,
            HttpServletRequest request){

        String employeeUsername = authentication.getName();
        Employee employee = employeeService.getEmployeeByEmail(employeeUsername);

        return ResponseEntity.ok().body(HttpResponse.builder()
                        .timeStamp(Instant.now())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .path(request.getRequestURI())
                        .requestMethod(request.getMethod())
                        .message("Profile retrieved successfully")
                        .data(Map.of("Employee", EmployeeResponse.mapToResponse(employee)))
                        .build());
    }

    @PatchMapping("/update-password")
    public ResponseEntity<HttpResponse> updatePassword(
            Authentication authentication,
            HttpServletRequest request,
            @RequestBody PasswordUpdateRequest passwordUpdateRequest){

        String employeeUsername = authentication.getName();
        employeeService.updatePassword(employeeUsername,
                passwordUpdateRequest.oldPassword(),
                passwordUpdateRequest.newPassword(),
                passwordUpdateRequest.newPasswordConfirmed());

        return ResponseEntity.ok().body(HttpResponse.builder()
                .timeStamp(Instant.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .path(request.getRequestURI())
                .requestMethod(request.getMethod())
                .message("Password updated successfully")
                .build());
    }

}
