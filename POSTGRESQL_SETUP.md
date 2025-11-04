# PostgreSQL Setup Guide

## Current Status
✅ **Application is working with H2 Database** (in-memory)
✅ **Authentication is fixed and working**
✅ **Frontend and Backend are running**

## To Switch to PostgreSQL:

### 1. Install PostgreSQL
- Download from: https://www.postgresql.org/download/windows/
- Install with default settings
- Remember the password you set for 'postgres' user

### 2. Create Database
```sql
-- Connect to PostgreSQL as postgres user
-- Run this command in psql or pgAdmin:
CREATE DATABASE dsa_portal;
```

### 3. Update Configuration
In `backend/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/dsa_portal
    username: postgres
    password: YOUR_POSTGRES_PASSWORD
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

### 4. Update Dependencies
In `backend/pom.xml`:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 5. Restart Application
```bash
cd backend
mvn spring-boot:run
```

## Current Working Setup
- **Database**: H2 (in-memory)
- **Backend**: http://localhost:8080/api
- **Frontend**: http://localhost:3000
- **H2 Console**: http://localhost:8080/api/h2-console

## Test Credentials
- **Admin**: admin / admin
- **Test User**: testuser / testuser
