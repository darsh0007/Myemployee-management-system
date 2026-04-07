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

----
# CardVault - System Design Document

**Course:** EECS 3311 - Software Design  
**Project:** CardVault - Contact Database  
**Team Members:** Sri Kanagalingam, Ruchin Patel, Kunal Goswami, Darshan Praveen Kumar Jain, Bisrat Mulugeta  
**Date:** April 6, 2026 (Updated from March 23, 2026)

<div style="page-break-after: always;"></div>

## Table of Contents

1. [Introduction](#1-introduction)
2. [CRC Cards](#2-crc-cards)
3. [Software Architecture Diagram](#3-software-architecture-diagram)
4. [Component Descriptions](#4-component-descriptions)

## 1. Introduction

CardVault is a web-based contact management application that allows users to store, organize, tag, and search professional contacts obtained from business cards. The system is built as a client-server application with a React single-page application on the frontend and an Express REST API backed by PostgreSQL on the backend.

Since the initial design, the system has been extended with:
- **Google OAuth 2.0 authentication** with JWT-based session management
- **AI-powered business card scanning** using OpenAI GPT-4o vision to extract contact data from uploaded card images
- **Favorites system** allowing users to toggle favorite status on contacts across all views
- **Contact deletion** with confirmation dialogs
- **Centralized API client** with automatic Bearer token injection for all authenticated requests
- **Dashboard navigation** with quick-action routing to other pages

This document describes the system design through CRC (Class-Responsibility-Collaborator) cards and a software architecture diagram.

## 2. CRC Cards

### 2.1 Frontend Classes

#### AuthProvider
| | |
|---|---|
| **Class Name** | AuthProvider (context/AuthContext.tsx) |
| **Responsibilities** | Manages global authentication state via React Context. On mount, checks for an existing JWT in localStorage and validates it via the `/auth/me` endpoint. Exposes `login(credential)` (calls Google OAuth flow and stores JWT), `logout()` (clears token and user state), and reactive `user`, `isAuthenticated`, and `isLoading` values to all child components. |
| **Collaborators** | AuthAPI, AppLayout, LoginPage |

#### App
| | |
|---|---|
| **Class Name** | App (App.tsx) |
| **Responsibilities** | Root component. Reads authentication state from AuthProvider to conditionally render the LoginPage or the authenticated AppLayout. Manages the active page ID and passes a navigation callback to AppLayout and Dashboard. |
| **Collaborators** | AuthProvider, AppLayout, LoginPage, Dashboard, Contacts, ScanCard, Favorites, Tags, Settings |

#### AppLayout
| | |
|---|---|
| **Class Name** | AppLayout |
| **Responsibilities** | Renders the application shell including sidebar navigation, top header with search and theme toggle, and page content area. Manages sidebar collapse state and dark mode toggle. Routes user navigation to the correct page. Displays authenticated user info from AuthProvider. |
| **Collaborators** | AuthProvider, Dashboard, Contacts, ScanCard, Favorites, Tags, Settings |

#### LoginPage
| | |
|---|---|
| **Class Name** | LoginPage |
| **Responsibilities** | Renders the Google OAuth sign-in screen using `@react-oauth/google`. On successful Google authentication, passes the access token to `AuthProvider.login()` which exchanges it for a JWT via the backend. |
| **Collaborators** | AuthProvider, AuthAPI |

#### Dashboard
| | |
|---|---|
| **Class Name** | Dashboard |
| **Responsibilities** | Displays a summary overview of the user's contact vault including stat cards (total contacts, scanned this month, favorites, tags used), recently added contacts, and quick action buttons. Supports navigation callbacks to route users to Contacts, ScanCard, and Favorites pages. Allows toggling favorite status on recent contacts. |
| **Collaborators** | ContactsAPI, AuthProvider |

#### Contacts
| | |
|---|---|
| **Class Name** | Contacts |
| **Responsibilities** | Displays all contacts in a searchable, filterable table/grid. Supports filtering, pagination, and view toggles. Provides view, edit, and delete action buttons. Supports toggling favorite status inline. Displays a confirmation dialog before deleting a contact. |
| **Collaborators** | ContactsAPI, TagsAPI, AuthProvider |

#### ScanCard
| | |
|---|---|
| **Class Name** | ScanCard |
| **Responsibilities** | Provides an interface for uploading business card images via drag-and-drop or file picker. Sends the uploaded image (as base64) to the backend `/contacts/scan` endpoint, which uses OpenAI GPT-4o to extract contact fields. Displays the extracted contact after creation. |
| **Collaborators** | ContactsAPI, AuthProvider |

#### Favorites
| | |
|---|---|
| **Class Name** | Favorites |
| **Responsibilities** | Displays contacts marked as favorites in a card layout showing initials, name, role, company, and associated tag badges. Supports toggling favorite status (unfavoriting removes from view). Displays a confirmation dialog before deleting a contact. |
| **Collaborators** | ContactsAPI, AuthProvider |

#### Tags
| | |
|---|---|
| **Class Name** | Tags |
| **Responsibilities** | Displays all user-defined tags in a grid layout with tag name, color-coded icon, and the number of contacts associated with each tag. Supports creating, updating, and deleting tags. |
| **Collaborators** | TagsAPI, AuthProvider |

#### Settings
| | |
|---|---|
| **Class Name** | Settings |
| **Responsibilities** | Renders a tabbed settings interface with sub-pages for Profile, Appearance, OCR, Data, and About. Manages active tab state. Profile tab allows updating user name via the UsersAPI. |
| **Collaborators** | AppLayout, UsersAPI, AuthProvider |

#### APIClient
| | |
|---|---|
| **Class Name** | APIClient (api/client.ts) |
| **Responsibilities** | Centralized HTTP client wrapping `fetch()`. Automatically injects the JWT Bearer token from localStorage into every request. Provides a typed `apiFetch<T>()` function used by all other API modules. Handles JSON parsing and error extraction. |
| **Collaborators** | AuthProvider (reads token from localStorage) |

#### AuthAPI
| | |
|---|---|
| **Class Name** | AuthAPI (api/auth.ts) |
| **Responsibilities** | Provides typed functions for authentication: `googleLogin(accessToken)` exchanges a Google access token for a CardVault JWT and user object; `getMe()` retrieves the current user from the JWT. Defines the `User` interface. |
| **Collaborators** | APIClient, AuthRouter |

#### ContactsAPI
| | |
|---|---|
| **Class Name** | ContactsAPI (api/contacts.ts) |
| **Responsibilities** | Provides typed fetch functions for contacts CRUD, stats retrieval, business card scanning, and favorite toggling. Defines the `Contact`, `Tag`, and `ContactStats` interfaces. |
| **Collaborators** | APIClient, ContactsRouter |

#### TagsAPI
| | |
|---|---|
| **Class Name** | TagsAPI (api/tags.ts) |
| **Responsibilities** | Provides typed fetch functions for tag CRUD operations (list, create, update, delete). Defines the `TagWithCount` interface. |
| **Collaborators** | APIClient, TagsRouter |

#### UsersAPI
| | |
|---|---|
| **Class Name** | UsersAPI (api/users.ts) |
| **Responsibilities** | Provides a typed function to update user profile data (name, avatar). |
| **Collaborators** | APIClient, UsersRouter |

### 2.2 Backend Classes

#### ExpressApp
| | |
|---|---|
| **Class Name** | ExpressApp (index.ts) |
| **Responsibilities** | Initializes the Express server with CORS and JSON middleware (20 MB limit for base64 card images). Mounts route handlers for auth, users, contacts, and tags. Provides a health endpoint and a global error handler. |
| **Collaborators** | AuthRouter, UsersRouter, ContactsRouter, TagsRouter, Database |

#### AuthRouter
| | |
|---|---|
| **Class Name** | AuthRouter (routes/auth.ts) |
| **Responsibilities** | Handles Google OAuth authentication. `POST /google` exchanges a Google access token for user info via the Google userinfo API, upserts the user record in the database, and returns a signed JWT. `GET /me` (protected) returns the currently authenticated user from the JWT payload. |
| **Collaborators** | AuthMiddleware, Database, Schema |

#### AuthMiddleware
| | |
|---|---|
| **Class Name** | AuthMiddleware (middleware/auth.ts) |
| **Responsibilities** | Express middleware that verifies JWT Bearer tokens from the `Authorization` header. Attaches the decoded `AuthPayload` (containing `userId`) to `req.auth`. Also exports `signToken()` for generating JWTs with a 7-day expiry. |
| **Collaborators** | AuthRouter, ContactsRouter (potential future use) |

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
| **Responsibilities** | Handles CRUD operations for contacts scoped to a user. Supports listing, creating, updating, deleting, toggling favorites (`PATCH /:id/favorite`), and managing tag assignments (`GET/POST/DELETE /:id/tags`). Provides a `/stats` endpoint for aggregate counts. Includes a `POST /scan` endpoint that sends a base64-encoded business card image to OpenAI GPT-4o for contact field extraction, then persists the result. Validates all input with Zod schemas. |
| **Collaborators** | Database, Schema, OpenAI API (GPT-4o) |

#### TagsRouter
| | |
|---|---|
| **Class Name** | TagsRouter |
| **Responsibilities** | Handles CRUD operations for tags scoped to a user. Supports listing tags with contact counts, retrieving tag contacts, creating, updating, and deleting. |
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

An **authentication layer** spans client and server: the frontend obtains a Google OAuth access token, the backend exchanges it for user info and issues a JWT, and all subsequent API requests include the JWT as a Bearer token.

An **external AI service** (OpenAI GPT-4o) is called server-side for business card OCR during the scan workflow.

```text
[EXTERNAL SERVICES]
Google OAuth 2.0 API  <──  LoginPage (access token)
OpenAI GPT-4o API     <──  ContactsRouter /scan (base64 image)

[CLIENT (Browser) - View Layer]
GoogleOAuthProvider -> AuthProvider -> App
       |                                |
       v                                v
  LoginPage                    AppLayout -> Renders pages
                                   |
                                   v
                    Dashboard / Contacts / ScanCard / Favorites / Tags / Settings
                                   |
                                   v
              APIClient (api/client.ts) -> Injects JWT Bearer token
                    |          |         |          |
                    v          v         v          v
              AuthAPI   ContactsAPI   TagsAPI   UsersAPI
                    \__________|__________|________/
                               |
                         [HTTP JSON over REST]
                               |
                               v
[SERVER (Node.js) - Controller Layer]
ExpressApp -> Mounts -> AuthRouter / ContactsRouter / TagsRouter / UsersRouter
                            |
                   AuthMiddleware (JWT verify)
                            |
                            v
[SERVER (Node.js) - Model Layer]
Database (Drizzle ORM) -> Maps to -> Schema
       |
       v
[PostgreSQL Database] -> Tables: users, contacts, tags, contact_tags
```

**Architecture Reference:** This follows the standard MVC pattern as described in:
* Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). *Design Patterns: Elements of Reusable Object-Oriented Software.* Addison-Wesley.
* MDN Web Docs - [MVC Architecture](https://developer.mozilla.org/en-US/docs/Glossary/MVC)

## 4. Component Descriptions

### Frontend (View)

| Component | File | Purpose |
|-----------|------|---------|
| main | `main.tsx` | Entry point — wraps App with GoogleOAuthProvider, QueryClientProvider, and AuthProvider |
| App | `App.tsx` | Root component, reads auth state to show LoginPage or AppLayout, manages active page routing |
| AuthProvider | `context/AuthContext.tsx` | Global auth state via React Context — login, logout, token persistence, user hydration |
| AppLayout | `components/AppLayout.tsx` | Shell layout with sidebar, header, theme toggle, sign-out, user info display |
| LoginPage | `pages/LoginPage.tsx` | Google OAuth sign-in screen using `@react-oauth/google` |
| Dashboard | `pages/Dashboard.tsx` | Overview with stats, recent contacts, quick actions, navigation callbacks, inline favorite toggle |
| Contacts | `pages/Contacts.tsx` | Searchable/filterable contact table and grid with inline favorite toggle, delete with confirmation |
| ScanCard | `pages/ScanCard.tsx` | Business card image upload (drag-and-drop) → sends base64 to backend for AI extraction |
| Favorites | `pages/Favorites.tsx` | Starred contacts display with unfavorite toggle and delete with confirmation |
| Tags | `pages/Tags.tsx` | Tag CRUD management with contact counts and color-coded display |
| Settings | `pages/Settings.tsx` | Tabbed settings (Profile, Appearance, OCR, Data, About) |
| ProfileSettings | `pages/settings/ProfileSettings.tsx` | User profile editing (name, avatar) via UsersAPI |
| APIClient | `api/client.ts` | Centralized `apiFetch<T>()` with automatic JWT Bearer token injection |
| AuthAPI | `api/auth.ts` | `googleLogin()`, `getMe()` — Google OAuth token exchange and session validation |
| ContactsAPI | `api/contacts.ts` | Contacts CRUD, stats, scan (OCR), favorite toggle — typed fetch functions |
| TagsAPI | `api/tags.ts` | Tag CRUD — list, create, update, delete with `TagWithCount` type |
| UsersAPI | `api/users.ts` | User profile update |

### Backend (Controller + Model)

| Component | File | Purpose |
|-----------|------|---------|
| ExpressApp | `src/index.ts` | Server initialization, CORS, JSON middleware (20 MB limit), route mounting, error handler |
| AuthRouter | `src/routes/auth.ts` | `POST /google` (OAuth token exchange + JWT issuance), `GET /me` (current user from JWT) |
| AuthMiddleware | `src/middleware/auth.ts` | JWT Bearer token verification, `signToken()` helper (7-day expiry) |
| UsersRouter | `src/routes/users.ts` | User CRUD endpoints with Zod validation |
| ContactsRouter | `src/routes/contacts.ts` | Contact CRUD, `POST /scan` (OpenAI GPT-4o OCR), `PATCH /:id/favorite`, tag assignment endpoints, `/stats` aggregate |
| TagsRouter | `src/routes/tags.ts` | Tag CRUD with contact counts |
| Database | `src/db/index.ts` | Drizzle ORM + PostgreSQL connection |
| Schema | `src/db/schema.ts` | Table definitions (users, contacts, tags, contact_tags) and Drizzle relations |
| Seed | `src/db/seed.ts` | Sample data population script |
