package com.myFullstack.employee_management.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LeaveRequestCreate(
    @NotNull(message = "start date is required")
    @FutureOrPresent(message = "Start date should be now or in the future")   
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate startDate,

    @NotNull(message = "end date is required")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @FutureOrPresent(message = "End date should be now or in the future")
    LocalDate endDate,

    @NotNull(message = "reason is required")
    @Size(min = 2, max = 100, message = "min is 2 characters and max is 100 characters")
    String reason
) {

}
