package com.myFullstack.employee_management.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myFullstack.employee_management.abstracts.DepartmentService;
import com.myFullstack.employee_management.dtos.DepartmentCreate;
import com.myFullstack.employee_management.entities.Department;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/departments")
public class DepartmentController {
    DepartmentService departmentService;
    // dependecy injection
    // constructor injection
    public DepartmentController(DepartmentService departmentService){
        this.departmentService = departmentService;
    }
    @GetMapping("")
    public ResponseEntity<List<Department>> findAll() {
        List<Department> department = departmentService.findAll();
        return new ResponseEntity<List<Department>>(department,HttpStatus.OK);
    }
    
    @GetMapping("/{departmentId}")
    public ResponseEntity<Department> findOne(@PathVariable UUID departmentId) {
        Department department = departmentService.findOne(departmentId);
        return new ResponseEntity<Department>(department,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Department> create(@RequestBody @Valid DepartmentCreate departmetnCreate) {
        Department department = departmentService.create(departmetnCreate);
        return new ResponseEntity<Department>(department,HttpStatus.CREATED);
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID departmentId) {
        departmentService.deleteDepartment(departmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
