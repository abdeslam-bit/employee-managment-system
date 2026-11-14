package com.myFullstack.employee_management.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "username is required")
    String username,

    @NotBlank(message = "password is required")
    String password
) {
}
