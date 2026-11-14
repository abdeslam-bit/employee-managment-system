package com.myFullstack.employee_management.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myFullstack.employee_management.abstracts.EmployeeService;
import com.myFullstack.employee_management.dtos.EmployeeCreate;
import com.myFullstack.employee_management.dtos.EmployeeUpdate;
import com.myFullstack.employee_management.entities.Department;
import com.myFullstack.employee_management.entities.Employee;
import com.myFullstack.employee_management.repositories.DepartmentRepo;
import com.myFullstack.employee_management.repositories.EmployeeRepo;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    ArrayList<Employee> employees = new ArrayList<>();

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private EmailService emailService;

    @Override
    public Employee findOne(UUID employeeId) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        return employee;
    }

    @Override
    public List<Employee> findAll(){
        return employeeRepo.findAll();
    }

    @Override
    public Employee createEmployee(EmployeeCreate employeeCreate){
        Employee employee = new Employee();

        Department department = departmentRepo.findById(employeeCreate.departmentId()).orElse(null);

        if(department == null){
            throw new RuntimeException("Department not found with id: " + employeeCreate.departmentId());
        }

        // Generate a unique invite token for the signup link
        String inviteToken = UUID.randomUUID().toString();
        try {
        employee.setFirstName(employeeCreate.firstName());
        employee.setLastName(employeeCreate.lastName());
        employee.setEmail(employeeCreate.email());
        employee.setPhoneNumber(employeeCreate.phoneNumber());
        employee.setHireDate(employeeCreate.hireDate());
        employee.setPosition(employeeCreate.position());
        employee.setDepartmentId(department);
        employee.setInviteToken(inviteToken);
        employee.setTokenValid(true);

        Employee savedEmployee = employeeRepo.save(employee);

        // Send the signup invitation email asynchronously
        emailService.sendInvitationEmail(
            savedEmployee.getEmail(),
            savedEmployee.getFirstName(),
            inviteToken
        );

        return savedEmployee;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create employee: " + e.getMessage());
            
        }
        
    }

    @Override
    public void deleteEmployee(UUID employeeId){
        if(employeeRepo.existsById(employeeId)){
            employeeRepo.deleteById(employeeId);
        } else {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }
    }

    @Override
    public Employee updateEmployee(UUID employeeId, EmployeeUpdate updatedEmployee){
        Employee employee = employeeRepo.findById(employeeId).orElse(null);

            if (employee != null) {
                employee.setFirstName(updatedEmployee.firstName());
                employee.setLastName(updatedEmployee.lastName());
                employee.setPhoneNumber(updatedEmployee.phoneNumber());
                employee.setPosition(updatedEmployee.position());
            }
            employeeRepo.save(employee);
            return employee;
    }
}
