# Festival Platform - Production Backend

## 🎯 Overview

Production-ready microservices platform for festival management with DJs, performances, and reviews. Clean architecture with Spring Boot 3.1.0 and modern design patterns.

## 🏗️ Architecture

```
┌─────────────────────┐    REST API     ┌─────────────────────┐
│  Festival App       │◄──────────────►│  Review Service     │
│  Port: 9090         │                 │  Port: 8080         │
│  PostgreSQL         │                 │  MongoDB            │
└─────────────────────┘                 └─────────────────────┘
```

### Services
- **Festival Application** (9090) - DJ and Performance management
- **Review Service** (8080) - Reviews and ratings microservice

### Databases  
- **PostgreSQL** - Festival data (DJs, Performances)
- **MongoDB** - Review data (Reviews, Ratings)

## 🚀 Quick Start

### Docker Deployment (Recommended)
```bash
docker-compose up -d
```

### Local Development
```bash
# Terminal 1: Festival App
cd festival-app && mvn spring-boot:run

# Terminal 2: Review Service  
cd review-service && mvn spring-boot:run
```

## 📊 API Endpoints

### Festival Application (9090)
```
GET    /api/djs                    # List DJs
POST   /api/djs                    # Create DJ
GET    /api/djs/{id}               # Get DJ
PUT    /api/djs/{id}               # Update DJ
DELETE /api/djs/{id}               # Delete DJ
GET    /api/djs/{id}/reviews       # DJ reviews
GET    /api/djs/{id}/rating        # DJ rating

GET    /api/performances           # List performances
POST   /api/performances           # Create performance
GET    /api/performances/{id}      # Get performance
PUT    /api/performances/{id}      # Update performance
DELETE /api/performances/{id}      # Delete performance
```

### Review Service (8080)
```
GET    /api/reviews                # List reviews
POST   /api/reviews                # Create review
GET    /api/reviews/{id}           # Get review
PUT    /api/reviews/{id}           # Update review
DELETE /api/reviews/{id}           # Delete review
```

## 🗄️ Data Models

### DJ Entity
```json
{
  "id": "dj-001",
  "name": "DJ Thunderbolt", 
  "genre": "Electronic",
  "email": "thunderbolt@festival.com"
}
```

### Performance Entity
```json
{
  "id": "perf-001",
  "title": "Electric Night Opening",
  "description": "Opening ceremony with electronic beats",
  "startTime": "2024-07-15T18:00:00",
  "endTime": "2024-07-15T20:00:00", 
  "djId": "dj-001"
}
```

### Review Entity
```json
{
  "id": "review-001",
  "subjectId": "dj-001",
  "subjectType": "DJ",
  "reviewerName": "John Doe",
  "rating": 5,
  "comment": "Amazing performance!",
  "createdAt": "2024-07-15T20:30:00"
}
```

## 🔧 Configuration

### Environment Variables
```bash
# Festival Application
SERVER_PORT=9090
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/festival_db
REVIEW_SERVICE_URL=http://localhost:8080

# Review Service  
SERVER_PORT=8080
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/review_db
FESTIVAL_SERVICE_URL=http://localhost:9090
```

## 🧪 Health Checks

```bash
curl http://localhost:9090/actuator/health
curl http://localhost:8080/actuator/health
```

## 📦 Project Structure

```
festival-app/
├── Dockerfile.festival-app         # Festival app container
├── Dockerfile.review-service       # Review service container  
├── docker-compose.yml              # Multi-service orchestration
├── docker/                         # Database initialization
├── festival-app/                   # Main application
│   ├── src/main/java/             # Source code
│   └── src/main/resources/        # Configuration
└── review-service/                 # Review microservice
    ├── src/main/java/             # Source code
    └── src/main/resources/        # Configuration
```

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.1.0, Java 21
- **Databases**: PostgreSQL 15, MongoDB 6  
- **Build**: Maven 3.9
- **Containers**: Docker, Docker Compose
- **Communication**: REST API, WebClient

## 📋 Sample Data

The platform includes sample data:
- **5 DJs** with different genres
- **5 Performances** linked to DJs
- **5 Reviews** for testing

## 🔄 Inter-Service Communication

Services communicate via REST API:
- Festival App calls Review Service for DJ/Performance reviews
- Review Service validates DJs/Performances with Festival App
- WebClient handles HTTP communication with timeouts and error handling

---

**Production-ready microservices platform for festival management** 🎉
