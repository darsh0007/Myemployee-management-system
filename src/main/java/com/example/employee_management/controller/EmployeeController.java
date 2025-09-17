package com.example.employee_management.controller;

import com.example.employee_management.model.Employee;
import com.example.employee_management.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.employee_management.exception.EmployeeNotFoundException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api/employees")   // all endpoints start with /api/employees - refer IBM utube vid
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }
    //tried them all in postman and it works.
    // GET /api/employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return service.getAllEmployees();
    }

    // GET /api/employees/{id}
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return service.getEmployeeById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    // POST /api/employees
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody @Valid Employee employee) {
        Employee created = service.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201
    }

    // PUT /api/employees/{id}
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody @Valid Employee employee) {
        return service.updateEmployee(id, employee); //200
    }
    // DELETE /api/employees/{id}
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
            service.deleteEmployee(id);
            return ResponseEntity.noContent().build(); // 204
        }
}
