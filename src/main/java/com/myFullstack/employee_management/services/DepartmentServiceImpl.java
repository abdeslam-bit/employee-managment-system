package com.myFullstack.employee_management.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myFullstack.employee_management.abstracts.DepartmentService;
import com.myFullstack.employee_management.dtos.DepartmentCreate;
import com.myFullstack.employee_management.entities.Department;
import com.myFullstack.employee_management.repositories.DepartmentRepo;


@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    DepartmentRepo departmentRepo;

    @Override
    public List<Department> findAll(){
        return departmentRepo.findAll();
    };

    @Override
    public Department findOne(UUID departmentId){
        Department department = departmentRepo.findById(departmentId).orElse(null);
        return department;
    };

    @Override
    public Department create(DepartmentCreate departmentCreate){
        Department department = new Department();
        department.setName(departmentCreate.name());
        departmentRepo.save(department);
        return department;
    }

    @Override
    public void deleteDepartment(UUID departmentId){
        if(departmentRepo.existsById(departmentId)){
            departmentRepo.deleteById(departmentId);
        } else {
            throw new RuntimeException("Department not found with id: " + departmentId);
        }
    }
    
}
