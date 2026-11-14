package com.myFullstack.employee_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myFullstack.employee_management.dtos.LoginRequest;
import com.myFullstack.employee_management.dtos.SignUpRequest;
import com.myFullstack.employee_management.entities.Employee;
import com.myFullstack.employee_management.repositories.EmployeeRepo;
import com.myFullstack.employee_management.services.AuthService;
import com.myFullstack.employee_management.shared.GlobalResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmployeeRepo employeeRepo;

    /**
     * Validates an invite token and returns the employee info.
     * This simulates the signup page load — the frontend would call this
     * with the token from the email link to display the employee's name/email.
     *
     * GET /auth/signup?token=<inviteToken>
     */
    @GetMapping("/signup")
    public ResponseEntity<GlobalResponse<Employee>> resolveSignupToken(@RequestParam String token) {
        Employee employee = employeeRepo.findByInviteToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired signup token."));

        if (!employee.isTokenValid()) {
            return ResponseEntity.status(HttpStatus.GONE)
                    .body(new GlobalResponse<>(employee));
        }

        return new ResponseEntity<>(new GlobalResponse<>(employee), HttpStatus.OK);
    }

    /**
     * POST /auth/signup?token=<inviteToken>
     * Body: { "username": "...", "password": "..." }
     * Token comes from the email link URL, body only needs credentials.
     */
    @PostMapping("/signup")
    public ResponseEntity<GlobalResponse<String>> signup(
            @RequestParam String token,
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        authService.signup(signUpRequest, token);
        return new ResponseEntity<>(new GlobalResponse<>("Signed Up"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<String>> login(
            @RequestBody @Valid LoginRequest loginRequest
    ) {
        String token = authService.login(loginRequest);
        return new ResponseEntity<>(new GlobalResponse<>(token), HttpStatus.OK);
    }
}

