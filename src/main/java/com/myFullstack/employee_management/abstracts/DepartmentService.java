package com.myFullstack.employee_management.abstracts;

import java.util.List;
import java.util.UUID;

import com.myFullstack.employee_management.dtos.DepartmentCreate;
import com.myFullstack.employee_management.entities.Department;

public interface DepartmentService {
    public Department findOne(UUID departmentId);
    public List<Department> findAll();
    public Department create(DepartmentCreate department);
    public void deleteDepartment(UUID departmentId);
}
