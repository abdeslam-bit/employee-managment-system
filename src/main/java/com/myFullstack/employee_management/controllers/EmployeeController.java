package com.myFullstack.employee_management.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.myFullstack.employee_management.abstracts.EmployeeService;
import com.myFullstack.employee_management.abstracts.LeaveRequestService;
import com.myFullstack.employee_management.dtos.EmployeeCreate;
import com.myFullstack.employee_management.dtos.EmployeeUpdate;
import com.myFullstack.employee_management.dtos.LeaveRequestCreate;
import com.myFullstack.employee_management.entities.Employee;
import com.myFullstack.employee_management.entities.LeaveRequest;
import com.myFullstack.employee_management.repositories.EmployeeRepo;
import com.myFullstack.employee_management.shared.GlobalResponse;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final LeaveRequestService leaveRequestService;
    // ArrayList<Employee> employees = new ArrayList<>();

    public EmployeeController(EmployeeService employeeService, LeaveRequestService leaveRequestService) {
        this.employeeService = employeeService;
        this.leaveRequestService = leaveRequestService;
    }

    @GetMapping("")
    public ResponseEntity<List<Employee>> findAll() {
        List<Employee> employees = employeeService.findAll();
        return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("@securityUtils.isOwner(#employeeId)")
    public ResponseEntity<Employee> findById(@PathVariable UUID employeeId) {
        Employee employee = employeeService.findOne(employeeId);
        if (employee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Employee>(employee, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody @Valid EmployeeCreate employee) {
        Employee savedEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<Employee>(savedEmployee, HttpStatus.CREATED);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and authentication.principal.employee.id == #employeeId)")
    public ResponseEntity<Employee> updateEmployee(@PathVariable UUID employeeId,
            @RequestBody @Valid EmployeeUpdate updatedEmployee) {
        {
            Employee employee = employeeService.updateEmployee(employeeId, updatedEmployee);
            if (employee == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Employee>(employee, HttpStatus.OK);
        }
    }

    @PostMapping("/{employeeId}/leave-requests")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and authentication.principal.employee.id == #employeeId)")
    public ResponseEntity<LeaveRequest> leaveRequest(@RequestBody @Valid LeaveRequestCreate leaveRequestCreate,@PathVariable UUID employeeId){
        LeaveRequest leaveReqest = leaveRequestService.createOne(leaveRequestCreate, employeeId);
        return new ResponseEntity<LeaveRequest>(leaveReqest,HttpStatus.CREATED);
    }
    
    @GetMapping("/{employeeId}/leave-requests")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and authentication.principal.employee.id == #employeeId)")
    public ResponseEntity<List<LeaveRequest>> LeaveRequestsByEmployeeId(
        @PathVariable UUID employeeId
    ) {
        List<LeaveRequest> leaveRequests = leaveRequestService.getAllByEmployeeId(employeeId);        
        return new ResponseEntity<List<LeaveRequest>>(leaveRequests, HttpStatus.OK);
    }
    
}
