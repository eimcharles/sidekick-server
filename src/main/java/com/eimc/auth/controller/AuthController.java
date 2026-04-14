package com.eimc.auth.controller;

import com.eimc.auth.dto.LoginRequest;
import com.eimc.common.domain.HttpResponse;
import com.eimc.employee.dto.response.EmployeeResponse;
import com.eimc.employee.model.Employee;
import com.eimc.employee.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final SecurityContextRepository securityContextRepository;
    private final RememberMeServices rememberMeServices;
    private final EmployeeService employeeService;

    public AuthController(AuthenticationManager authManager,
                          SecurityContextRepository securityContextRepository,
                          RememberMeServices rememberMeServices,
                          EmployeeService employeeService) {
        this.authManager = authManager;
        this.securityContextRepository = securityContextRepository;
        this.rememberMeServices = rememberMeServices;
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response){

        /// Validate Credentials against the database
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        /// Clear any existing security context
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        /// Attach the authenticated user to the security context
        context.setAuthentication(auth);

        /// Store the context in the SecurityContextHolder for the current thread
        SecurityContextHolder.setContext(context);

        /// Persist the authenticated session to the repository (JSESSIONID)
        securityContextRepository.saveContext(context, request, response);

        /// Generate a rememberMe cookie if requested
        if (loginRequest.rememberMe()) {
            rememberMeServices.loginSuccess(request, response, auth);
        }

        /// Fetch authenticated user profile for the response
        Employee employee = employeeService.getEmployeeByEmail(loginRequest.username());

        return ResponseEntity.ok().body(HttpResponse.builder()
                .timeStamp(Instant.now())
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("Successfully logged in")
                .path(request.getRequestURI())
                .requestMethod(request.getMethod())
                .data(Map.of("Employee", EmployeeResponse.mapToResponse(employee)))
                .build());
    }

}
