package com.myFullstack.employee_management.shared;

import java.util.List;

import lombok.Getter;

@Getter
public class GlobalResponse<T> {
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    private String status;
    private T data;
    private List<ErrorItem> errors;

    public GlobalResponse(List<ErrorItem> errors){
        this.status = ERROR;
        this.errors = errors;
        this.data = null;
    }

    public GlobalResponse(T data){
        this.status = SUCCESS;
        this.data = data;
        this.errors = null;
    }
    public record ErrorItem(String message) {}
}
