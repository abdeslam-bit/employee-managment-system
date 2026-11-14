package com.myFullstack.employee_management.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
    @NotBlank(message = "username is required")
    @Size(min = 2, max = 100, message = "username must be between 2 and 100 chars")
    String username,

    @NotBlank(message = "password is required")
    @Size(min = 8, max = 100, message = "password must be between 8 and 100 chars")
    String password
) {
}


