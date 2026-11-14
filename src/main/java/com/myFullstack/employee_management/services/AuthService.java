package com.myFullstack.employee_management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myFullstack.employee_management.config.JwtHelper;
import com.myFullstack.employee_management.dtos.LoginRequest;
import com.myFullstack.employee_management.dtos.SignUpRequest;
import com.myFullstack.employee_management.entities.Employee;
import com.myFullstack.employee_management.entities.UserAccount;
import com.myFullstack.employee_management.repositories.EmployeeRepo;
import com.myFullstack.employee_management.repositories.UserRequestRepo;

@Service
public class AuthService {

    @Autowired
    private UserRequestRepo userRequestRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void signup(SignUpRequest signUpRequest, String token) {
        // 1. Find the employee linked to this invite token
        Employee employee = employeeRepo.findByInviteToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid signup token."));

        // 2. Ensure the token has not already been used
        if (!employee.isTokenValid()) {
            throw new RuntimeException("This signup link has already been used or has expired.");
        }

        // 3. Create the user account
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(signUpRequest.username());
        userAccount.setPassword(passwordEncoder.encode(signUpRequest.password()));
        userAccount.setEmployee(employee);

        userRequestRepo.save(userAccount);

        // 4. Invalidate the token so it cannot be reused
        employee.setTokenValid(false);
        employeeRepo.save(employee);
    }


    /**
     * Authenticates the user with the provided credentials and returns a signed JWT token.
     *
     * @param request contains username and password
     * @return JWT token string
     */
    public String login(LoginRequest request) {
        // 1. Delegate credential checking to Spring Security
        //    (throws BadCredentialsException if username/password are wrong)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // 2. Get the authenticated UserDetails principal
        UserDetails user = (UserDetails) authentication.getPrincipal();

        // 3. Generate and return the JWT token
        return jwtHelper.generateToken(user);
    }
}
