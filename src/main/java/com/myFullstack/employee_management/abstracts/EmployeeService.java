package com.myFullstack.employee_management.abstracts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.myFullstack.employee_management.dtos.EmployeeCreate;
import com.myFullstack.employee_management.dtos.EmployeeUpdate;
import com.myFullstack.employee_management.entities.Employee;

public interface EmployeeService {
    public Employee findOne(UUID employeeId);
    List<Employee> findAll();
    public Employee createEmployee(EmployeeCreate employee);
    public void deleteEmployee(UUID employeeId);
    public Employee updateEmployee(UUID employeeId, EmployeeUpdate updatedEmployee);
}
