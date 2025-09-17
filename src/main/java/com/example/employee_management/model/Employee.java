package com.example.employee_management.model;

import jakarta.persistence.*;  // JPA annotations\
import jakarta.validation.constraints.Email;        // Bean Validation
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "employees")    // setting the table name explicitly so JPA will map this class to employee table
public class Employee {

    @Id   // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // for auto-increment
    private Long id;

    @NotBlank(message = "Name is required") //added for validation after first git commit
    @Column(nullable = false)  // NOT NULL in database
    private String name;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required") //same as above @notblank
    @Column(nullable = false, unique = true)  // NOT NULL & UNIQUE key constraint
    private String email;

    @NotBlank(message = "Department is required")
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

