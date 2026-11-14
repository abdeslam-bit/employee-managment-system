package com.myFullstack.employee_management.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myFullstack.employee_management.entities.LeaveRequest;

public interface LeaveRequestRepo extends JpaRepository<LeaveRequest,UUID>{
    public List<LeaveRequest> getAllByEmployeeId(UUID employeeId);
}
