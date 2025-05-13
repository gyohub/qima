# QIMAtech Fullstack Product Management App
#### Author: Gyowanny Pessatto Queiroz `gyowanny@gmail.com`

---

This is a fullstack web application developed as part of a technical assignment for a Fullstack Developer role at QIMAtech.

It includes:
- A **React + TypeScript** frontend with Ant Design UI
- A **Spring Boot** backend with JPA/Hibernate, Spring Security, and MySQL
- Docker support for local development

---

## 🧩 Technologies Used

### Frontend:
- [React](https://reactjs.org/)
- [TypeScript](https://www.typescriptlang.org/)
- [Ant Design](https://ant.design/) – UI components
- [Axios](https://axios-http.com/) – HTTP client
- [React Router](https://reactrouter.com/) – Routing

### Backend:
- [Spring Boot 3](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [MySQL 8](https://www.mysql.com/)
- [Hibernate](https://hibernate.org/)

### Dev Tools:
- [Docker](https://www.docker.com/)
- [Maven](https://maven.apache.org/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) (recommended)

---

## ✍️ Features Implemented

✅ Product Management
- List, Add, Edit, Delete products
- Sortable and searchable AntD table
- Category selection with hierarchy support

✅ Category Management
- Category drill-down using Ant Design Tree
- Backend supports nested parent-child relationships

✅ Authentication
- Custom React login form
- Protected routes for managing products
- Logout from Ant Design sidebar


## 📦 Project Structure

```
src/
├── main/
│   ├── java/com/gyowanny/qima/products     # Spring Boot application code
│   ├── resources/application.yaml          # Spring config
│   └── frontend/                           # React frontend (TypeScript + Ant Design)
docker/
├── mysql/                                  # MySQL Docker config + seed data
```

---

## 🚀 How to Run Locally (Dev Setup)

1. Build and start the MySQL database container:

```bash
docker-compose up --build
```

2. Run the whole app (UI and backend)
```bash
./mvnw spring-boot:run
```

### ⚙️ If you want to run Backend and Frontend separately (Dev Mode)

> 💡 Add this to package.json:
```json
"proxy": "http://localhost:8080"
```

## 🔐 Authentication
- Simple login form (React + AntD)
- Spring Security-based session authentication
- Default credentials:

```
Username: admin
Password: admin
```
> These are seeded via init.sql. Passwords are bcrypt-hashed.

## 🧪 Testing Notes
- MySQL container is seeded via init.sql
- Product and category tables support hierarchy and validation
- Use DBeaver or TablePlus to connect to MySQL on localhost:3306 with: