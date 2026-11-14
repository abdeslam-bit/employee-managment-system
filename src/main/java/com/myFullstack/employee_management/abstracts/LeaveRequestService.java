package com.myFullstack.employee_management.abstracts;

import java.util.List;
import java.util.UUID;

import com.myFullstack.employee_management.dtos.LeaveRequestCreate;
import com.myFullstack.employee_management.entities.LeaveRequest;

public interface LeaveRequestService { 
    public LeaveRequest createOne(LeaveRequestCreate leaveRequestCreate, UUID employeeId);
    public List<LeaveRequest> getAllByEmployeeId(UUID employeeId);
}
