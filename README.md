# JWT Authentication & Authorization — Spring Boot

A REST API implementing stateless authentication and authorization using JSON Web Tokens (JWT), built with Spring Boot 4 and Spring Security 7.

---

## Tech Stack

| Technology | Version |
|---|---|
| Java | 17 |
| Spring Boot | 4.0.3 |
| Spring Security | 7.x |
| PostgreSQL | 18 |
| JJWT | latest |
| Lombok | latest |
| Maven | wrapper included |

---

## Project Structure

```
src/main/java/com/koech/security/
├── auth/
│   ├── AuthenticationController.java   # Register & authenticate endpoints
│   ├── AuthenticationService.java      # Business logic for auth
│   ├── AuthenticateRequest.java        # Login request body
│   ├── RegisterRequest.java            # Registration request body
│   └── AuthenticationResponse.java     # JWT token response
├── config/
│   ├── ApplicationConfig.java          # Beans: UserDetailsService, AuthProvider, PasswordEncoder
│   ├── JwtAuthenticationFilter.java    # Intercepts requests and validates JWT
│   ├── JwtService.java                 # JWT generation, validation, and claim extraction
│   └── SecurityConfiguration.java      # Security filter chain configuration
├── demo/
│   └── DemoController.java             # Protected endpoint example
└── user/
    ├── User.java                        # User entity implementing UserDetails
    ├── UserRepository.java              # JPA repository
    └── Role.java                        # USER, ADMIN roles
```

---

## Prerequisites

- Java 17+
- PostgreSQL 18 running on `localhost:5432`
- Maven (or use the included `mvnw.cmd` wrapper)

---

## Database Setup

Create the database in PostgreSQL before running the app:

```powershell
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -c "CREATE DATABASE security;"
```

---

## Configuration

`src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/security
    username: postgres
    password: password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

> **Note:** `ddl-auto: update` keeps your data between restarts. Use `create-drop` only during initial development.

---

## Running the App

```powershell
.\mvnw.cmd spring-boot:run
```

The app starts on `http://localhost:8080`.

---

## API Endpoints

### Register

```
POST /api/v1/auth/register
```

Request body:
```json
{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "password123"
}
```

Response:
```json
{
    "token": "<JWT_TOKEN>"
}
```

---

### Authenticate

```
POST /api/v1/auth/authenticate
```

Request body:
```json
{
    "email": "john@example.com",
    "password": "password123"
}
```

Response:
```json
{
    "token": "<JWT_TOKEN>"
}
```

---

### Demo (Protected Endpoint)

```
GET /api/v1/demo-controller
```

Requires JWT token in the `Authorization` header:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## How It Works

1. **Registration** — user details are saved to the database with a BCrypt-hashed password. A JWT token is returned.
2. **Authentication** — credentials are verified against the database. A JWT token is returned on success.
3. **Subsequent requests** — the `JwtAuthenticationFilter` intercepts every request, extracts the token from the `Authorization: Bearer <token>` header, validates it, and sets the security context.
4. **Protected routes** — any route not under `/api/v1/auth/**` requires a valid JWT token.

---

## Security

- Passwords are hashed using **BCrypt**
- JWT tokens are signed with **HMAC-SHA256**
- Sessions are **stateless** (no server-side session storage)
- CSRF is disabled (stateless API)
- Token expiration is enforced on every request
