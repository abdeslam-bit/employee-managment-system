package com.myFullstack.employee_management.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record EmployeeUpdate(
    @NotNull(message = "first name is required")
    @Size(min = 2, max = 50, message = "first name must be between 2 and 50 chars")
    String firstName,
    @NotNull(message = "last name is required")
    @Size(min = 2, max = 50, message = "last name must be between 2 and 50 chars")
    String lastName,
    @NotBlank(message = "phoneNumber is required")
    String phoneNumber,
    @NotBlank(message = "position is required")
    @Size(min = 2, max = 20, message = "position must be between 2 and 20 chars")
    String position
) {
    
}
