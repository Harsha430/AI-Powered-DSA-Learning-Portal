# PostgreSQL Setup Instructions

## Option 1: Install PostgreSQL locally

1. Download PostgreSQL from https://www.postgresql.org/download/windows/
2. Install with default settings
3. Remember the password you set for the 'postgres' user
4. Update the password in `backend/src/main/resources/application.yml` if different from 'password'

## Option 2: Use Docker (if available)

1. Install Docker Desktop from https://www.docker.com/products/docker-desktop
2. Run: `docker run --name dsa_portal_postgres -e POSTGRES_DB=dsa_portal -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres:13`

## Option 3: Use H2 Database (Current)

If you prefer to continue with H2 database, the application is already configured to work with it.

## After PostgreSQL is running:

1. Start the backend: `cd backend && mvn spring-boot:run`
2. Start the frontend: `cd frontend && npm start`
3. Open http://localhost:3000 in your browser
