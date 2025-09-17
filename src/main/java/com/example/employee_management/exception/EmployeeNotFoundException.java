package com.example.employee_management.exception;

public class EmployeeNotFoundException extends RuntimeException  {
    public EmployeeNotFoundException(Long id) {
        super("Employee " + id + " not found");
    }
}
