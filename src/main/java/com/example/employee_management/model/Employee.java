package com.example.employee_management.model;

import jakarta.persistence.*;  // JPA annotations (Jakarta namespace in Spring Boot 3+)

/**
 * Domain model for an employee.
 * JPA will map this class to a DB table named "employees".
 */

@Entity                       // Marks this class as a JPA-managed entity (a table row model)
@Table(name = "employees")    // set the table name explicitly
public class Employee {

    @Id   // Primary key column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // IDENTITY = let the DB auto-increment the ID (easy & works with H2/MySQL)
    private Long id;

    @Column(nullable = false)                 // NOT NULL in the DB
    private String name;

    @Column(nullable = false, unique = true)  // NOT NULL + UNIQUE constraint (no duplicate emails)
    private String email;

    @Column(nullable = false)                 // NOT NULL
    private String department;

    // --- JPA requires a no-arg constructor ---
    public Employee() { }

    // parameterized constructor for manual creation/testing
    public Employee(String name, String email, String department) {
        this.name = name;
        this.email = email;
        this.department = department;
    }

    // --- Getters & setters (needed by JPA and later by JSON serialization) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}

