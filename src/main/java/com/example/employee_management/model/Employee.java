package com.example.employee_management.model;

import jakarta.persistence.*;  // JPA annotations (Jakarta namespace in Spring Boot 3+)

@Entity
@Table(name = "employees")    // setting the table name explicitly so JPA will map this class to employee table
public class Employee {

    @Id   // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // for auto-increment
    private Long id;

    @Column(nullable = false)  // NOT NULL in database
    private String name;

    @Column(nullable = false, unique = true)  // NOT NULL & UNIQUE key constraint
    private String email;

    @Column(nullable = false)  // NOT NULL
    private String department;

    public Employee() { } //no args constructor studied in 2030 java course

    // parameterized constructor from 2030 course
    public Employee(String name, String email, String department) {
        this.name = name;
        this.email = email;
        this.department = department;
    }

    // generate getters & setters, used by JPA and later by JSON serialization
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}

