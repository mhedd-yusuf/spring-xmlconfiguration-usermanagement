# Spring User Management Application

A user management system built with **Spring Framework**, **Hibernate**, and **XML configuration**. Provides both **REST API** and **JSP web interface**.

## 🚀 Quick Start

### Prerequisites
- Java 11+
- Maven 3.6+
- Apache Tomcat 9.0+

### Build & Run

1. **Build the project:**
   ```bash
   mvn clean package
   ```

2. **Deploy to Tomcat:**
   ```bash
   cp target/user-management.war $TOMCAT_HOME/webapps/
   ```

3. **Start Tomcat:**
   ```bash
   # Linux/Mac
   $TOMCAT_HOME/bin/startup.sh
   
   # Windows
   %TOMCAT_HOME%\bin\startup.bat
   ```

4. **Access the application:**
    - REST API: `http://localhost:8080/user-management/api/users`
    - Web Interface: `http://localhost:8080/user-management/users`

## 📡 API Examples

### Create User
```bash
curl -X POST http://localhost:8080/user-management/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "1234567890",
    "status": "ACTIVE",
    "role": "USER"
  }'
```

### Get All Users
```bash
curl http://localhost:8080/user-management/api/users
```

### Get User by ID
```bash
curl http://localhost:8080/user-management/api/users/1
```

### Update User
```bash
curl -X PUT http://localhost:8080/user-management/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john.updated@example.com",
    "firstName": "John",
    "lastName": "Doe Updated",
    "phoneNumber": "9876543210",
    "status": "ACTIVE",
    "role": "ADMIN"
  }'
```

### Delete User
```bash
curl -X DELETE http://localhost:8080/user-management/api/users/1
```

## 🌐 Web Interface

The application also provides JSP-based web pages for managing users through a browser:

- **Home Page:** `http://localhost:8080/user-management/`
- **User List:** `http://localhost:8080/user-management/users`
- **Create User:** Click "Create New User" button
- **Edit/Delete:** Available from the user list page

## 🛠 Using IntelliJ IDEA

1. Open project in IntelliJ
2. Configure Tomcat server (Run → Edit Configurations → Tomcat Server)
3. Set Application context to `/user-management`
4. Run the application
5. Access: `http://localhost:8080/user-management/`

## ⚙️ Database Configuration

**Default:** H2 in-memory database (no setup required)

**For MySQL:**
1. Create database: `CREATE DATABASE userdb;`
2. Update `applicationContext.xml` with MySQL credentials
3. Change hibernate dialect to `MySQL8Dialect`

## 📝 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

---

**Note:** This is a pure Spring (non-Boot) application with XML configuration, demonstrating traditional Spring MVC architecture. The application built with Claude to demonstrate the xml configuration