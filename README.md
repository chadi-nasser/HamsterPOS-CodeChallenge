# HamsterPOS 🐹 – Point of Sale System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue.svg)](https://www.postgresql.org/)

A simple, robust Spring Boot POS backend focused on product management and order creation with automatic totals.

---

## ✨ Features

- **Spring Boot API** with secure JWT-based authentication (ADMIN / USER roles)
- **Product & Order Management**: CRUD operations, atomic stock management, automatic total calculation
- **Tests**: Minimal unit tests for core entities and services
- **Persistence**: H2 in-memory by default, PostgreSQL recommended for production
- **Clean Domain Model**: Totals calculated in entities (lifecycle callbacks)
- **Role Management**: Simple ADMIN/USER roles

---

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Gradle Wrapper (`gradlew` included)
- (Optional) Docker/Docker Compose for PostgreSQL

### 1. **Run with Gradle**

```
# Windows
gradlew.bat bootRun
# macOS/Linux
./gradlew bootRun
```

App launches at [http://localhost:8080](http://localhost:8080)  
Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 2. **Use PostgreSQL (optional)**

```
docker-compose up -d
# Edit src/main/resources/application.yml (or set ENV: DB_USER, DB_PASSWORD, etc.)
```

### 3. **Run Tests**

```
./gradlew test
```

---

## 🏗️ Architecture

- **Persistence**: Spring Data JPA + Hibernate
- **Database**: H2 in-memory (dev/tests), PostgreSQL (prod)
- **Security**: JWT-based, minimal multi-role system
- **Domain Model**: Entity lifecycle event-based total calculation

---

## 📦 Assumptions

- Single-tenant setup
- Products exist before orders; stock decreases on order
- Simple statuses: PENDING, COMPLETED, CANCELLED
- No payment or inventory reservation logic
- H2 for local/dev, PostgreSQL for production

---

## 📊 API & State

| Resource       | Storage (Dev) | Storage (Prod) |
| -------------- | ------------- | -------------- |
| Products       | H2            | PostgreSQL     |
| Orders         | H2            | PostgreSQL     |
| Users/Roles    | H2            | PostgreSQL     |

- Orders hold computed totals in entities—not provided by client.

---

## ⏱️ Time Spent

- Project setup: ~30m
- Models & mapping: ~60m
- Services/business logic: ~45m
- Tests & fixes: ~45m
- Docs & cleanup: ~30m

---

## ⚖️ Trade-offs

- **JPA/Hibernate:** Fast to build, but ORM abstraction overhead
- **H2:** Quick for dev, some SQL/behavior differences vs. Postgres
- **Entity Callback Totals:** Fewer DB round-trips, careful handling of callbacks required
- **Minimal Controllers/Errors:** Fast delivery, can be expanded with custom error DTOs/handlers

---

**Built with ❤️ using Spring Boot**