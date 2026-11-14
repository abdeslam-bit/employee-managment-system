package com.myFullstack.employee_management.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myFullstack.employee_management.entities.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, UUID> {
    // Finds an employee by their signup invite token
    Optional<Employee> findByInviteToken(String inviteToken);
}

