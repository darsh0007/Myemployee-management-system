package com.example.employee_management.service;

import com.example.employee_management.model.Employee;
import com.example.employee_management.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    // constructor injection from 2030 notes
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }
    // get all employees
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // get an employee by id
    public Optional<Employee> getEmployeeById(Long id) {
        return repository.findById(id);
    }

    // create or save employee
    public Employee saveEmployee(Employee employee) {
        return repository.save(employee);
    }

    // update existing employee
    public Employee updateEmployee(Long id, Employee updated) {
        return repository.findById(id)
                .map(emp -> {
                    emp.setName(updated.getName());
                    emp.setEmail(updated.getEmail());
                    emp.setDepartment(updated.getDepartment());
                    return repository.save(emp);
                })
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // delete an employee
    public void deleteEmployee(Long id) {
        repository.deleteById(id);
    }
}
