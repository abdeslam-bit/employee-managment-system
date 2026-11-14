package com.myFullstack.employee_management.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myFullstack.employee_management.entities.Department;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, UUID> {

}
