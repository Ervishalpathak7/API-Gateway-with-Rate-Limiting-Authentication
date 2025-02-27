# API Gateway with Rate Limiting & Authentication

### 🏗️ **Project Overview:**

This project implements a powerful API Gateway using **Spring Boot**, **Spring Cloud Gateway**, and **JWT-based
authentication**. It handles centralized authentication, authorization, rate limiting, and load balancing — providing a
secure, optimized entry point for microservice-based systems.

---

## 🚀 **Features:**

- **JWT Authentication & Authorization:**
    - Secure API access with JSON Web Tokens (JWT).
    - Role-based access control for different routes.

- **API Routing & Load Balancing:**
    - Route requests to multiple backend services.
    - Balance load across multiple instances.

- **Rate Limiting & Throttling:**
    - Protect services from abuse with request limits.

- **Global Filters & Custom Filters:**
    - Add pre/post processing to requests.
    - JWT validation, logging, and error handling.

- **Spring WebFlux & Reactive Programming:**
    - Non-blocking I/O for high performance.


---

## 🛠️ **Tech Stack:**

- **Backend:** Java, Spring Boot, Spring Cloud Gateway, Spring Security, Spring WebFlux
- **Authentication:** JWT (JSON Web Tokens)
- **Database:** MongoDB
- **Caching & Rate Limiting:** Redis (for token blacklisting, etc.)
- **Containerization:** Docker
- **Build Tool:** Maven

---

## ⚡ **Architecture:**

- API Gateway routes requests to microservices.
- **Auth Service:** Issues JWT tokens.
- **JWT Filter:** Validates tokens, sets security context.
- **Rate Limiter:** Limits API calls based on IP or user ID.
- **MongoDB:** Stores application data.

---

## 🏁 **Setup & Running the Project:**

1. Clone the repository:
   ```bash
   git clone https://github.com/Ervishalpathak7/API-Gateway-with-Rate-Limiting-Authentication.git
   cd API-Gateway-with-Rate-Limiting-Authentication
   ```

2. Configure the **`application.yml`:**
   ```yaml
   server:
     port: 8080

   jwt:
     secret: "mySecretKey"

   spring:
     autoconfigure:
       exclude: org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration

     data:
       mongodb:
         uri: Database uri for your user service
         database: Database name for your user service

     main:
       allow-bean-definition-overriding: true

     cloud:
       gateway:
         routes:
           - id: "Service name"
             uri: "Service URL"
             predicates:
               - Path="user path"
         routes:
           - id: "Service name"
             uri: "Service URL"
             predicates:
               - Path="user path"

   ```

3. Build and run the project:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. Access the API Gateway at:
   ```
   http://localhost:8080
   ```

---

## 🛡️ **Example API Routes:**

- **Login:** `/auth/login` (POST)
- **User Service:** `/user/**`
- **Admin Service:** `/admin/**`

Use the **JWT token** from the login response in the **Authorization** header:

```
Authorization: Bearer <your_jwt_token>
```

---

## 📘 **To-Do:**

- ✅ JWT Authentication & Role-Based Access
- ✅ Routing & Load Balancing
- ✅ Rate Limiting with Redis
- 🔧 Token Blacklisting (upcoming)
- 🔧 API Documentation (Swagger)

---

## 🚀 **Final Goal:**

A fully functional, production-ready API Gateway that ensures **security**, **scalability**, and **reliability** for
your microservices architecture.

Let’s finish strong — I’m here to help you every step of the way! 💪✨

