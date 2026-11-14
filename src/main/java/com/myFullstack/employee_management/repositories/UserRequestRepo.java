package com.myFullstack.employee_management.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myFullstack.employee_management.entities.UserAccount;

@Repository
public interface UserRequestRepo extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findByUsername(String username);

    // Checks that the authenticated user's username is linked to the given employeeId
    // Spring Data derives: SELECT COUNT(*) FROM user_account WHERE username = ? AND employee_id = ?
    boolean existsByUsernameAndEmployeeId(String username, UUID employeeId);
}

