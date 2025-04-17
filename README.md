# 🔐 Fullstack JWT Auth System – Spring Boot + React + PostgreSQL

A full-featured secure system with:

- 🔐 JWT Authentication + Refresh Token
- 🛂 Role-based Access Control (RBAC)
- ♻️ Auto Access Token Refresh (React + Redux)
- ⚛️ React + Redux frontend
- ☕ Spring Boot backend (with Spring Security & JPA)
- 🐘 PostgreSQL Database
- ☁️ Cloud-ready for Render, Railway, Fly.io

---

## 📦 Tech Stack

| Layer      | Tech                            |
|------------|---------------------------------|
| Frontend   | React + Redux + Axios           |
| Backend    | Spring Boot + Spring Security   |
| Auth       | JWT + Refresh Token (HttpOnly)  |
| Database   | PostgreSQL                      |
| Deploy     | Docker                          |

---

## 🧱 System Architecture Diagram

```mermaid
graph TD
  React["React Frontend (Redux + JWT)"]
  API["Spring Boot Backend API"]
  DB["PostgreSQL Database"]
  Auth["JWT Middleware"]

  React --> API
  API --> DB
  API --> Auth
```

---

## 🔐 JWT Auth Flow

```mermaid
sequenceDiagram
  participant C as Client (React)
  participant B as Backend (Spring Boot)
  C->>B: POST /auth/login
  B-->>B: Validate Credentials
  B-->>C: Access Token + Refresh Token (Cookie)
  C-->>C: Save Access Token in Redux/localStorage
```

---

## 🛂 Role-Based Access Flow

```mermaid
sequenceDiagram
  participant User
  participant Backend
  User->>Backend: GET /admin/dashboard (with JWT)
  Backend-->>Backend: Decode JWT
  Backend-->>Backend: Check Role == ADMIN?
  alt Valid Admin
    Backend-->>User: 200 OK + Data
  else Invalid Role
    Backend-->>User: 403 Forbidden
  end
```

---

## ♻️ Refresh Token Lifecycle

```mermaid
sequenceDiagram
  participant Client
  participant Backend
  Client-->>Client: JWT Timer Trigger (before expiry)
  Client->>Backend: POST /auth/refresh (with cookie)
  Backend-->>Backend: Validate refresh token
  Backend-->>Client: New access token
  Client-->>Client: Update Redux + LocalStorage
```

---

## ⚙️ CI/CD Deployment (Render)

```mermaid
graph TD
  Dev[Developer Push Code] --> GitHub[GitHub Repo]
  GitHub --> RenderCI[Render Deploy Hook]
  RenderCI --> Build[Render: Build Backend + Frontend]
  Build --> Deploy[Deployed Services]
  Deploy --> BackendAPI[Spring Boot API]
  Deploy --> ReactUI[React Frontend]
  BackendAPI --> DB[(Managed PostgreSQL)]
```

---

## 🚀 Local Dev Setup

```bash
git clone https://github.com/your-username/fullstack-auth-app.git
cd fullstack-auth-app

# Run with Docker
docker-compose up --build
```

### 💻 Access URLs

| Service   | URL                         |
|-----------|-----------------------------|
| Frontend  | http://localhost:3000       |
| Backend   | http://localhost:8080       |
| Swagger   | http://localhost:8080/swagger-ui.html |

---

## 🔑 API Endpoints

| Method | Endpoint           | Auth     | Description            |
|--------|--------------------|----------|-------------------------|
| POST   | `/auth/register`   | ❌ Public | Register new user      |
| POST   | `/auth/login`      | ❌ Public | Login, get token       |
| POST   | `/auth/refresh`    | ✅ Cookie | Refresh access token   |
| POST   | `/auth/logout`     | ✅ Cookie | Logout, clear cookie   |
| GET    | `/users`           | ✅ USER   | Get all users          |
| GET    | `/admin/dashboard` | ✅ ADMIN  | Admin only route       |

---

## 📁 Project Structure

```bash
springboot-postgres-docker/
├── backend/
│   ├── src/main/java/com/example/demo/
│   │   ├── controller/
│   │   ├── entity/
│   │   ├── dto/
│   │   ├── repository/
│   │   ├── service/
│   │   ├── security/
│   │   └── DemoApplication.java
│   ├── resources/
│   │   └── application.properties
│   └── Dockerfile
├── frontend/
│   ├── src/
│   │   ├── store/
│   │   ├── App.jsx
│   │   ├── Login.jsx
│   │   ├── Register.jsx
│   │   └── UserList.jsx
│   └── Dockerfile
├── docker-compose.yml
└── README.md
```