# Festival Application

A Spring Boot REST API for managing festival DJs and performances with PostgreSQL database.

## 🚀 Quick Start

### Prerequisites
- Java 21+ installed
- Docker Desktop running

### Start Application
```bash
# One-command start (recommended)
.\start-festival.bat

# Manual start
.\mvnw.cmd clean package -DskipTests
docker compose up --build -d
```

### Run Tests
```bash
# Run tests with timeout protection
.\run-tests-simple.bat

# Manual test run
.\mvnw.cmd test
```

## 📊 What You Get

### Services
- **Festival Application**: http://localhost:9090
- **PostgreSQL Database**: localhost:5432
- **Sample Data**: 5 DJs and 5 performances pre-loaded

### API Endpoints
- **DJs**: http://localhost:9090/api/djs
- **Performances**: http://localhost:9090/api/performances
- **Health Check**: http://localhost:9090/actuator/health

### Database Access
- **Host**: localhost:5432
- **Database**: festival
- **Username**: festival_user
- **Password**: festival_pass

## 🔧 Management Commands

```bash
# View logs
docker compose logs -f

# Stop services
docker compose down

# Connect to database
docker exec -it festival-database psql -U festival_user -d festival

# Restart everything
docker compose down && docker compose up -d
```

## 🛠️ Troubleshooting

### JAVA_HOME Issues
If you get "JAVA_HOME is set to an invalid directory":
1. Install Java 21 from https://adoptium.net/temurin/releases/
2. Set JAVA_HOME to JDK directory (not java.exe)
3. Restart command prompt

### Docker Issues
If Docker commands fail:
1. Start Docker Desktop
2. Wait for it to fully start
3. Try again

### Test Issues
If tests run infinitely or fail:
1. Use `.\run-tests-simple.bat` (has timeout)
2. Tests use H2 in-memory database (separate from PostgreSQL)
3. Check test logs for specific errors

## 📋 Project Structure

```
src/
├── main/java/com/capgemini/festivalapplication/
│   ├── controller/     # REST API endpoints
│   ├── entity/         # JPA entities (DJ, Performance)
│   ├── repository/     # Data access layer
│   ├── service/        # Business logic
│   ├── dto/           # Data transfer objects
│   └── mapper/        # Entity-DTO conversion
└── test/              # Unit and integration tests
```

## 🎯 Features

- ✅ Complete CRUD operations for DJs and Performances
- ✅ PostgreSQL database with persistent storage
- ✅ Docker containerization with Docker Compose
- ✅ RESTful API with proper HTTP status codes
- ✅ Input validation and error handling
- ✅ Health checks and monitoring endpoints
- ✅ Comprehensive test suite
- ✅ Sample data for testing

## 📝 API Examples

```bash
# Get all DJs
curl http://localhost:9090/api/djs

# Create new DJ
curl -X POST -H "Content-Type: application/json" \
  -d '{"name":"New DJ","genre":"House","email":"newdj@example.com"}' \
  http://localhost:9090/api/djs

# Get all performances
curl http://localhost:9090/api/performances
```

---

**🎉 Ready to rock the festival!** 🎵
