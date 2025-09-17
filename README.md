# Employee Management System (Spring Boot)

A CRUD REST API built with **Spring Boot 3**, **Spring Data JPA**, and **H2 Database**.  
It demonstrates clean architecture (Controller → Service → Repository → Database) and includes validation + global error handling.

---

## Features
- **CRUD Operations**: Create, Read, Update, Delete employees
- **Validation**: Ensures required fields (name, email, department) and valid email format
- **Error Handling**: Centralized with `@RestControllerAdvice` for consistent JSON errors
- **Database**: H2 in-memory DB (easy for dev/demo, can swap to MySQL)
- **REST API**: Follows proper REST conventions (`GET`, `POST`, `PUT`, `DELETE`)

---

## Tech Stack
- Java 17
- Spring Boot 3
- Spring Data JPA (Hibernate)
- H2 Database
- Maven

---

## Getting Started

### 1. Clone the repo
```bash
git clone https://github.com/darsh0007/Myemployee-management-system.git
cd Myemployee-management-system
