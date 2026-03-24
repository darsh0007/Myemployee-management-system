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




# CardVault - System Design Document

**Course:** EECS 3311 - Software Design  
**Project:** CardVault - Contact Database  
**Team Members:** Sri Kanagalingam, Ruchin Patel, Kunal Goswami, Darshan Praveen Kumar Jain, Bisrat Mulugeta  
**Date:** March 23, 2026

<div style="page-break-after: always;"></div>

## Table of Contents

1. [Introduction](#1-introduction)
2. [CRC Cards](#2-crc-cards)
3. [Software Architecture Diagram](#3-software-architecture-diagram)
4. [Component Descriptions](#4-component-descriptions)

## 1. Introduction

CardVault is a web-based contact management application that allows users to store, organize, tag, and search professional contacts obtained from business cards. The system is built as a client-server application with a React single-page application on the frontend and an Express REST API backed by PostgreSQL on the backend.

This document describes the system design through CRC (Class-Responsibility-Collaborator) cards and a software architecture diagram.

## 2. CRC Cards

### 2.1 Frontend Classes

#### AppLayout
| | |
|---|---|
| **Class Name** | AppLayout |
| **Responsibilities** | Renders the application shell including sidebar navigation, top header with search and theme toggle, and page content area. Manages sidebar collapse state and dark mode toggle. Routes user navigation to the correct page. |
| **Collaborators** | LoginPage, Dashboard, Contacts, ScanCard, Favorites, Tags, Settings |

#### LoginPage
| | |
|---|---|
| **Class Name** | LoginPage |
| **Responsibilities** | Renders the authentication screen. Provides a sign-in entry point and triggers the login callback on successful authentication. |
| **Collaborators** | AppLayout |

#### Dashboard
| | |
|---|---|
| **Class Name** | Dashboard |
| **Responsibilities** | Displays a summary overview of the user's contact vault including stat cards, recently added contacts, and quick action buttons. |
| **Collaborators** | ContactsAPI |

#### Contacts
| | |
|---|---|
| **Class Name** | Contacts |
| **Responsibilities** | Displays all contacts in a searchable, filterable table/grid. Supports filtering, pagination, and view toggles. Provides view, edit, and delete action buttons. |
| **Collaborators** | ContactsAPI |

#### ScanCard
| | |
|---|---|
| **Class Name** | ScanCard |
| **Responsibilities** | Provides an interface for uploading business card images. Prepares uploaded images for future LLM-based contact extraction. |
| **Collaborators** | ContactsAPI |

#### Favorites
| | |
|---|---|
| **Class Name** | Favorites |
| **Responsibilities** | Displays contacts marked as favorites in a card layout showing initials, name, role, company, and associated tag badges. |
| **Collaborators** | ContactsAPI |

#### Tags
| | |
|---|---|
| **Class Name** | Tags |
| **Responsibilities** | Displays all user-defined tags in a grid layout with tag name, color-coded icon, and the number of contacts associated with each tag. |
| **Collaborators** | ContactsAPI |

#### Settings
| | |
|---|---|
| **Class Name** | Settings |
| **Responsibilities** | Renders a tabbed settings interface with sub-pages for Profile, Appearance, OCR, Data, and About. Manages active tab state. |
| **Collaborators** | AppLayout |

#### ContactsAPI
| | |
|---|---|
| **Class Name** | ContactsAPI (frontend API client) |
| **Responsibilities** | Provides typed fetch functions for communicating with the backend REST API. Defines the Contact interface. |
| **Collaborators** | ExpressApp, ContactsRouter |

### 2.2 Backend Classes

#### ExpressApp
| | |
|---|---|
| **Class Name** | ExpressApp (index.ts) |
| **Responsibilities** | Initializes the Express server with CORS and JSON middleware. Mounts route handlers, provides a health endpoint, and a global error handler. |
| **Collaborators** | UsersRouter, ContactsRouter, TagsRouter, Database |

#### UsersRouter
| | |
|---|---|
| **Class Name** | UsersRouter |
| **Responsibilities** | Handles CRUD operations for user accounts. Supports lookup by ID or email, user creation, and profile updates. Validates input with Zod schemas. |
| **Collaborators** | Database, Schema |

#### ContactsRouter
| | |
|---|---|
| **Class Name** | ContactsRouter |
| **Responsibilities** | Handles CRUD operations for contacts scoped to a user. Supports listing, creating, updating, toggling favorites, and managing tags. Validates input. |
| **Collaborators** | Database, Schema |

#### TagsRouter
| | |
|---|---|
| **Class Name** | TagsRouter |
| **Responsibilities** | Handles CRUD operations for tags scoped to a user. Supports listing tags, retrieving tag contacts, creating, updating, and deleting. |
| **Collaborators** | Database, Schema |

#### Database
| | |
|---|---|
| **Class Name** | Database (db/index.ts) |
| **Responsibilities** | Establishes the PostgreSQL connection. Creates and exports a Drizzle ORM instance configured with the full relational schema. |
| **Collaborators** | Schema |

#### Schema
| | |
|---|---|
| **Class Name** | Schema (db/schema.ts) |
| **Responsibilities** | Defines the database tables (users, contacts, tags, contact_tags) and their Drizzle ORM relations. Establishes foreign keys. |
| **Collaborators** | None (Data Definition) |

#### Seed
| | |
|---|---|
| **Class Name** | Seed (db/seed.ts) |
| **Responsibilities** | Populates the database with sample data for development and testing. Clears existing data before seeding. |
| **Collaborators** | Database, Schema |

## 3. Software Architecture Diagram

CardVault follows a **layered client-server architecture** consistent with the **Model-View-Controller (MVC)** pattern adapted for modern web applications:

* **View (Frontend):** React SPA handles all UI rendering and user interaction
* **Controller (API Routes):** Express route handlers process requests and orchestrate business logic
* **Model (ORM + Database):** Drizzle ORM schema definitions and PostgreSQL database store all persistent data

```text
[CLIENT (Browser) - View Layer]
AppLayout -> Renders -> Dashboard / Contacts / ScanCard
       |
       v
ContactsAPI -> fetch() calls -> [HTTP JSON over REST]

[SERVER (Node.js) - Controller Layer]
ExpressApp -> Routes to -> ContactsRouter / UsersRouter / TagsRouter
       |
       v
[SERVER (Node.js) - Model Layer]
Database (Drizzle ORM) -> Maps to -> Schema
       |
       v
[PostgreSQL Database] -> Stores Tables (users, contacts, tags)
