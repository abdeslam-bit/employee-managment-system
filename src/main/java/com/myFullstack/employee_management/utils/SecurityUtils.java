package com.myFullstack.employee_management.utils;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.myFullstack.employee_management.repositories.UserRequestRepo;

@Component("securityUtils")
public class SecurityUtils {

    private final UserRequestRepo userRequestRepo;

    public SecurityUtils(UserRequestRepo userRequestRepo) {
        this.userRequestRepo = userRequestRepo;
    }

    /**
     * Returns true if:
     *  - the authenticated user has the ADMIN role, OR
     *  - the authenticated user's account is linked to the given employeeId
     */
    public boolean isOwner(UUID employeeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // ADMIN can access any employee
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return true;
        }

        String username = authentication.getName();        
        // Query the DB: does a user_account row exist with this username AND this employee_id?
        return userRequestRepo.existsByUsernameAndEmployeeId(username, employeeId);
    }
}

