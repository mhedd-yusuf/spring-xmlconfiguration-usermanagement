# Spring User Management Application

A user management system built with **Spring Framework**, **Hibernate**, and **XML configuration**. Provides both **REST API** and **JSP web interface**. Can be deployed on **Tomcat** or **WebSphere Liberty (Docker)**.

## 🚀 Quick Start

### Prerequisites
- Java 11+
- Maven 3.6+
- Docker & Docker Compose (for Liberty deployment)
- Apache Tomcat 9.0+ (for Tomcat deployment)

---

## 📦 Option 1: Deploy with Docker Compose (WebSphere Liberty)

### Step 1: Build the Application

```bash
# Build the WAR file
mvn clean package
```

### Step 2: Create Docker Directory Structure

```bash
# Create necessary directories
mkdir -p docker/liberty
mkdir -p docker/apps
mkdir -p docker/lib
mkdir -p docker/logs

# Copy WAR file
cp target/user-management.war docker/apps/

# Download MySQL JDBC driver
curl -o docker/lib/mysql-connector-java-8.0.33.jar \
  https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar
```

### Step 3: Create Liberty Configuration Files

**Create `docker/liberty/server.xml`:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<server description="User Management Liberty Server">

    <featureManager>
        <feature>servlet-4.0</feature>
        <feature>jsp-2.3</feature>
        <feature>jstl-1.2</feature>
        <feature>jdbc-4.2</feature>
        <feature>beanValidation-2.0</feature>
        <feature>jsonp-1.1</feature>
    </featureManager>

    <httpEndpoint id="defaultHttpEndpoint"
                  host="*"
                  httpPort="9080"
                  httpsPort="9443"/>

    <webApplication id="userManagement"
                    location="/config/apps/user-management.war"
                    contextRoot="/user-management">
        <classloader delegation="parentLast">
            <privateLibrary>
                <fileset dir="/opt/ibm/wlp/usr/shared/resources/mysql" includes="*.jar"/>
            </privateLibrary>
        </classloader>
    </webApplication>

    <logging consoleLogLevel="INFO"
             consoleFormat="simple"
             traceSpecification="*=info"/>

</server>
```

**Create `docker/liberty/jvm.options`:**

```properties
-Xms512m
-Xmx1024m
-XX:+UseG1GC
-Duser.timezone=UTC
-Dfile.encoding=UTF-8
```

**Create `docker/liberty/bootstrap.properties`:**

```properties
default.http.port=9080
default.https.port=9443
com.ibm.ws.logging.console.format=simple
com.ibm.ws.logging.console.log.level=INFO
```

### Step 4: Create docker-compose.yml

**Create `docker-compose.yml` in project root:**

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: user-management-mysql
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: userdb
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - app-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-u", "root", "-prootpassword"]
      interval: 10s
      timeout: 5s
      retries: 5
    command: --default-authentication-plugin=mysql_native_password

  liberty:
    image: icr.io/appcafe/open-liberty:full-java11-openj9-ubi
    container_name: user-management-liberty
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      DB_HOST: mysql
      DB_PORT: 3306
      DB_NAME: userdb
      DB_USER: root
      DB_PASSWORD: rootpassword
    ports:
      - "9080:9080"
      - "9443:9443"
    volumes:
      - ./docker/liberty/server.xml:/config/server.xml
      - ./docker/liberty/jvm.options:/config/jvm.options
      - ./docker/liberty/bootstrap.properties:/config/bootstrap.properties
      - ./docker/apps:/config/apps
      - ./docker/lib:/opt/ibm/wlp/usr/shared/resources/mysql
      - ./docker/logs:/logs
    networks:
      - app-network
    restart: unless-stopped

networks:
  app-network:
    driver: bridge

volumes:
  mysql-data:
    driver: local
```

### Step 5: Configure applicationContext.xml for Docker

**Update `src/main/resources/applicationContext.xml`:**

Make sure your DataSource is configured for Docker MySQL:

```xml
<bean id="dataSource" class="org.apache.commons.dbcp2.BasicDataSource" 
      destroy-method="close">
    <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://mysql:3306/userdb?useSSL=false&amp;serverTimezone=UTC&amp;allowPublicKeyRetrieval=true"/>
    <property name="username" value="root"/>
    <property name="password" value="rootpassword"/>
    <property name="initialSize" value="5"/>
    <property name="maxTotal" value="20"/>
</bean>

<bean id="sessionFactory" 
      class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="packagesToScan" value="com.usermanagement.model"/>
    <property name="hibernateProperties">
        <props>
            <prop key="hibernate.dialect">org.hibernate.dialect.MySQL8Dialect</prop>
            <prop key="hibernate.hbm2ddl.auto">update</prop>
            <prop key="hibernate.show_sql">true</prop>
            <!-- CRITICAL: Disable JTA for Liberty -->
            <prop key="hibernate.transaction.coordinator_class">jdbc</prop>
            <prop key="hibernate.transaction.jta.platform">org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform</prop>
            <prop key="hibernate.current_session_context_class">org.springframework.orm.hibernate5.SpringSessionContext</prop>
        </props>
    </property>
</bean>
```

**Rebuild after changes:**

```bash
mvn clean package
cp target/user-management.war docker/apps/
```

### Step 6: Start the Application

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f liberty

# Wait for this message:
# [AUDIT] CWWKZ0001I: Application userManagement started
```

### Step 7: Verify Application

**Test REST API:**

```bash
# Get all users
curl http://localhost:9080/user-management/api/users

# Create a user
curl -X POST http://localhost:9080/user-management/api/users \
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

# Get user by ID
curl http://localhost:9080/user-management/api/users/1
```

**Test Web Interface:**

Open browser:
- Home: `http://localhost:9080/user-management/`
- Users List: `http://localhost:9080/user-management/users`

### Step 8: Connect to MySQL and Verify Data

**Method 1: Using Docker exec**

```bash
# Connect to MySQL container
docker exec -it user-management-mysql mysql -u root -prootpassword userdb

# Inside MySQL:
mysql> SHOW TABLES;
+------------------+
| Tables_in_userdb |
+------------------+
| users            |
+------------------+

mysql> SELECT * FROM users;
+----+----------+------------------+----------+-----------+----------+--------------+--------+------+
| id | username | email            | password | firstName | lastName | phoneNumber  | status | role |
+----+----------+------------------+----------+-----------+----------+--------------+--------+------+
|  1 | johndoe  | john@example.com | ******   | John      | Doe      | 1234567890   | ACTIVE | USER |
+----+----------+------------------+----------+-----------+----------+--------------+--------+------+

mysql> DESCRIBE users;

mysql> EXIT;
```

**Method 2: Using MySQL Client from Host**

```bash
# Install MySQL client if not available:
# Mac: brew install mysql-client
# Ubuntu: sudo apt-get install mysql-client
# Windows: Download from mysql.com

# Connect from host machine
mysql -h 127.0.0.1 -P 3306 -u root -prootpassword userdb
```

**Method 3: Using MySQL Workbench or DBeaver**

- Host: `localhost` or `127.0.0.1`
- Port: `3306`
- Username: `root`
- Password: `rootpassword`
- Database: `userdb`

### Step 9: Stop the Application

```bash
# Stop all containers
docker-compose down

# Stop and remove volumes (WARNING: deletes database data!)
docker-compose down -v
```

---

## 📦 Option 2: Deploy on Tomcat (Traditional)

### Build & Deploy

```bash
# Build
mvn clean package

# Copy to Tomcat
cp target/user-management.war $TOMCAT_HOME/webapps/

# Start Tomcat
$TOMCAT_HOME/bin/startup.sh  # Linux/Mac
%TOMCAT_HOME%\bin\startup.bat  # Windows
```

### Update applicationContext.xml for Local MySQL

```xml
<property name="url" value="jdbc:mysql://localhost:3306/userdb?useSSL=false&amp;serverTimezone=UTC"/>
```

### Access Application

- REST API: `http://localhost:8080/user-management/api/users`
- Web Interface: `http://localhost:8080/user-management/users`

---

## 📡 API Examples

### Create User
```bash
curl -X POST http://localhost:9080/user-management/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "janedoe",
    "email": "jane@example.com",
    "password": "password123",
    "firstName": "Jane",
    "lastName": "Doe",
    "phoneNumber": "9876543210",
    "status": "ACTIVE",
    "role": "ADMIN"
  }'
```

### Get All Users
```bash
curl http://localhost:9080/user-management/api/users
```

### Get User by ID
```bash
curl http://localhost:9080/user-management/api/users/1
```

### Update User
```bash
curl -X PUT http://localhost:9080/user-management/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "username": "janedoe",
    "email": "jane.updated@example.com",
    "firstName": "Jane",
    "lastName": "Doe Updated",
    "phoneNumber": "1111111111",
    "status": "ACTIVE",
    "role": "USER"
  }'
```

### Delete User
```bash
curl -X DELETE http://localhost:9080/user-management/api/users/1
```

### Filter by Status
```bash
curl http://localhost:9080/user-management/api/users?status=ACTIVE
```

---

## 🗂️ Git Repository Guidelines

### What to Commit to Git

**✅ DO commit these files:**

```
.gitignore
pom.xml
README.md
src/                          # All source code
docker/
  ├── liberty/
  │   ├── server.xml          # ✅ Configuration
  │   ├── jvm.options         # ✅ Configuration  
  │   └── bootstrap.properties # ✅ Configuration
  ├── Dockerfile              # ✅ If you created one
  └── .gitkeep                # ✅ To preserve empty directories
docker-compose.yml            # ✅ Docker orchestration
```

**❌ DO NOT commit these:**

```
docker/
  ├── apps/
  │   └── user-management.war  # ❌ Generated file (built by Maven)
  ├── lib/
  │   └── mysql-connector-*.jar # ❌ Downloaded dependency
  └── logs/                    # ❌ Runtime logs
target/                        # ❌ Maven build output
.idea/                         # ❌ IDE settings
*.iml                          # ❌ IDE files
.DS_Store                      # ❌ Mac files
```

### Create .gitignore

**Create `.gitignore` in project root:**

```gitignore
# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml

# Docker - generated files
docker/apps/*.war
docker/lib/*.jar
docker/logs/

# IDE
.idea/
*.iml
*.iws
*.ipr
.vscode/
.settings/
.project
.classpath

# OS
.DS_Store
Thumbs.db

# Logs
*.log
```

### Preserve Empty Directories

Create `.gitkeep` files:

```bash
# Create placeholder files for empty directories
touch docker/apps/.gitkeep
touch docker/lib/.gitkeep
touch docker/logs/.gitkeep
```

### Git Commands

```bash
# Initialize repository
git init

# Add files
git add .gitignore
git add pom.xml
git add README.md
git add src/
git add docker/liberty/
git add docker-compose.yml
git add docker/*/.gitkeep

# Commit
git commit -m "Initial commit: Spring User Management with Liberty Docker support"

# Push to remote
git remote add origin <your-repo-url>
git push -u origin main
```

### Repository Structure in Git

```
your-repo/
├── .gitignore
├── README.md
├── pom.xml
├── docker-compose.yml
├── docker/
│   ├── liberty/
│   │   ├── server.xml
│   │   ├── jvm.options
│   │   └── bootstrap.properties
│   ├── apps/.gitkeep
│   ├── lib/.gitkeep
│   └── logs/.gitkeep
└── src/
    ├── main/
    │   ├── java/
    │   ├── resources/
    │   └── webapp/
    └── test/
```

---

## 🛠 Development Workflow

### Making Changes

```bash
# 1. Make code changes in src/

# 2. Rebuild
mvn clean package

# 3. Copy new WAR
cp target/user-management.war docker/apps/

# 4. Restart Liberty (picks up new WAR automatically)
docker-compose restart liberty

# 5. Watch logs
docker-compose logs -f liberty
```

### Updating Configuration

```bash
# 1. Edit docker/liberty/server.xml

# 2. Restart Liberty
docker-compose restart liberty

# No rebuild needed for config changes!
```

---

## 🔍 Useful Commands

### Docker Commands

```bash
# View running containers
docker-compose ps

# View logs
docker-compose logs -f liberty
docker-compose logs -f mysql

# Enter container shell
docker exec -it user-management-liberty bash
docker exec -it user-management-mysql bash

# Stop services
docker-compose stop

# Start services
docker-compose start

# Restart specific service
docker-compose restart liberty

# Remove everything (including volumes)
docker-compose down -v
```

### Database Commands

```bash
# Backup database
docker exec user-management-mysql mysqldump -u root -prootpassword userdb > backup.sql

# Restore database
docker exec -i user-management-mysql mysql -u root -prootpassword userdb < backup.sql

# View database size
docker exec user-management-mysql mysql -u root -prootpassword -e \
  "SELECT table_schema AS 'Database', ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)' FROM information_schema.TABLES WHERE table_schema='userdb';"
```

---

## 📝 Project Structure

```
spring-user-management/
├── docker/                    # Docker-related files
│   ├── liberty/              # Liberty configuration
│   │   ├── server.xml
│   │   ├── jvm.options
│   │   └── bootstrap.properties
│   ├── apps/                 # WAR files (not in git)
│   ├── lib/                  # External JARs (not in git)
│   └── logs/                 # Application logs (not in git)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/usermanagement/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dao/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       ├── model/
│   │   │       └── service/
│   │   ├── resources/
│   │   │   ├── applicationContext.xml
│   │   │   └── logback.xml
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml
│   │       │   ├── dispatcher-servlet.xml
│   │       │   └── views/
│   │       ├── resources/
│   │       └── index.jsp
│   └── test/
├── target/                   # Maven build output (not in git)
├── .gitignore
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## 🎓 Key Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 11+ | Programming Language |
| Spring Framework | 5.3.30 | Core Framework |
| Hibernate | 5.6.15 | ORM |
| MySQL | 8.0 | Database |
| Open Liberty | Latest | Application Server |
| Maven | 3.6+ | Build Tool |
| Docker | Latest | Containerization |

---

## 📞 Troubleshooting

### Application won't start

```bash
# Check logs
docker-compose logs liberty | grep -i error

# Verify files exist
ls -lh docker/apps/user-management.war
ls -lh docker/lib/mysql-connector-*.jar
```

### Can't connect to database

```bash
# Test MySQL connection
docker exec -it user-management-mysql mysql -u root -prootpassword

# Check network
docker exec -it user-management-liberty ping mysql
```

### Port already in use

```bash
# Change port in docker-compose.yml
ports:
  - "9081:9080"  # Use 9081 instead
```

---

**Note:** This is a pure Spring (non-Boot) application demonstrating traditional Spring MVC with XML configuration and Liberty deployment.


**Note2:** This application is generated with the help of Claude based on the requirment I have given to it, not just a random application created by Claude