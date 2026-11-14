package com.myFullstack.employee_management.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myFullstack.employee_management.abstracts.LeaveRequestService;
import com.myFullstack.employee_management.dtos.LeaveRequestCreate;
import com.myFullstack.employee_management.entities.Employee;
import com.myFullstack.employee_management.entities.LeaveRequest;
import com.myFullstack.employee_management.repositories.EmployeeRepo;
import com.myFullstack.employee_management.repositories.LeaveRequestRepo;

import jakarta.validation.Valid;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {
    
    @Autowired
    public LeaveRequestRepo leaveRequestRepo;

    @Autowired
    public EmployeeRepo employeeRepo; 


    @Override
    public LeaveRequest createOne(LeaveRequestCreate leaveRequestCreate, UUID employeeId){
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee == null){
            throw new RuntimeException("employee not found with id: " + employeeId);
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStartDate(leaveRequestCreate.startDate());
        leaveRequest.setEndDate(leaveRequestCreate.endDate());
        leaveRequest.setReason(leaveRequestCreate.reason());
        leaveRequest.setStatus("pending");
        leaveRequest.setEmployee(employee);
        leaveRequestRepo.save(leaveRequest);
        return leaveRequest;
    }

    public List<LeaveRequest> getAllByEmployeeId(UUID employeeId){
        return leaveRequestRepo.getAllByEmployeeId(employeeId);
    }
}