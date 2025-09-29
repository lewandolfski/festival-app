# 🎵 Review Service - Festival Application Microservice

## 📋 Overview

The **Review Service** is a microservice that handles reviews for DJs and Performances from the Festival Application. It's built with Spring Boot and uses MongoDB for data storage, demonstrating a complete microservices architecture with inter-service communication.

## 🏗️ Architecture

### **Microservice Communication**
- **Review Service** (Port 8080) - MongoDB
- **Festival Application** (Port 9090) - PostgreSQL
- **WebClient** for HTTP communication between services

### **Technology Stack**
- **Framework**: Spring Boot 3.1.0
- **Database**: MongoDB 7.0
- **Communication**: Spring WebFlux WebClient
- **Containerization**: Docker & Docker Compose
- **Testing**: JUnit 5, Mockito, Embedded MongoDB

## 🚀 Quick Start

### **Prerequisites**
- Java 21
- Docker Desktop
- Festival Application running on port 9090

### **1. Start Review Service**
```bash
.\start-review-service.bat
```

### **2. Verify Service is Running**
```bash
# Health check
curl http://localhost:8080/api/reviews/health

# Get all reviews
curl http://localhost:8080/api/reviews
```

## 📡 API Endpoints

### **Review Management**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/reviews` | Create a new review |
| `GET` | `/api/reviews` | Get all reviews |
| `GET` | `/api/reviews/{id}` | Get review by ID |
| `PUT` | `/api/reviews/{id}` | Update review |
| `DELETE` | `/api/reviews/{id}` | Delete review |

### **Subject-Specific Reviews**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/reviews/subject/DJ/{djId}` | Get all reviews for a DJ |
| `GET` | `/api/reviews/subject/PERFORMANCE/{perfId}` | Get all reviews for a Performance |
| `GET` | `/api/reviews/type/DJ` | Get all DJ reviews |
| `GET` | `/api/reviews/type/PERFORMANCE` | Get all Performance reviews |

### **Statistics & Analytics**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/reviews/stats/{type}/{id}` | Get average rating and review count |
| `GET` | `/api/reviews/rating/{rating}` | Get reviews by rating |
| `GET` | `/api/reviews/rating/min/{minRating}` | Get reviews with minimum rating |
| `GET` | `/api/reviews/reviewer/{name}` | Get reviews by reviewer name |

## 📝 Example API Usage

### **Create a Review**
```bash
curl -X POST http://localhost:8080/api/reviews \
  -H "Content-Type: application/json" \
  -d '{
    "subjectId": "dj-1",
    "subjectType": "DJ",
    "reviewerName": "John Doe",
    "rating": 5,
    "comment": "Amazing performance!"
  }'
```

### **Get DJ Statistics**
```bash
curl http://localhost:8080/api/reviews/stats/DJ/dj-1
```

**Response:**
```json
{
  "subjectId": "dj-1",
  "subjectType": "DJ",
  "averageRating": 4.5,
  "reviewCount": 3
}
```

## 🗄️ Database Schema

### **Review Document (MongoDB)**
```javascript
{
  "_id": "ObjectId",
  "subject_id": "dj-1",           // DJ or Performance ID
  "subject_type": "DJ",           // "DJ" or "PERFORMANCE"
  "reviewer_name": "John Doe",
  "rating": 5,                    // 1-5 stars
  "comment": "Great show!",
  "created_at": "2025-01-01T10:00:00Z",
  "updated_at": "2025-01-01T10:00:00Z"
}
```

## 🔧 Development Commands

### **Build & Test**
```bash
.\build-review-service.bat      # Build the application
.\test-review-service.bat       # Run tests
```

### **Docker Commands**
```bash
docker-compose up -d            # Start services
docker-compose logs -f          # View logs
docker-compose down             # Stop services
```

## 🌐 Microservice Communication

The Review Service validates that DJs and Performances exist by calling the Festival Application:

```java
// Validate DJ exists
GET http://localhost:9090/api/djs/{djId}

// Validate Performance exists  
GET http://localhost:9090/api/performances/{performanceId}
```

## 🧪 Testing

### **Unit Tests**
- Service layer tests with Mockito
- Controller tests with MockMvc
- Repository tests with Embedded MongoDB

### **Integration Tests**
- Full application context tests
- MongoDB integration tests
- WebClient communication tests

## 📊 Sample Data

The service comes with sample reviews:
- 2 reviews for DJ-1 (ratings: 5, 4)
- 1 review for Performance-1 (rating: 5)
- 1 review for DJ-2 (rating: 3)
- 1 review for Performance-2 (rating: 4)

## 🔒 Validation Rules

- **Subject ID**: Required, must exist in Festival Application
- **Subject Type**: Must be "DJ" or "PERFORMANCE"
- **Reviewer Name**: 2-100 characters
- **Rating**: Integer 1-5
- **Comment**: Optional, max 1000 characters

## 🐳 Docker Configuration

### **Services**
- **review-database**: MongoDB 7.0
- **review-service**: Spring Boot application

### **Networking**
- Custom bridge network for microservice communication
- Persistent MongoDB volume for data storage

## 🎯 Exercise 6 Requirements

✅ **Microservice Architecture**: Separate Spring Boot application  
✅ **MongoDB Database**: NoSQL database instead of PostgreSQL  
✅ **Same Package Structure**: Mirrors Festival Application structure  
✅ **WebClient Communication**: HTTP calls to Festival Application  
✅ **Docker Containerization**: Full containerized deployment  
✅ **RESTful API**: Complete CRUD operations  
✅ **Validation**: Input validation and error handling  
✅ **Testing**: Unit and integration tests  

## 🎵 Ready to Rock!

Your Review Service microservice is ready to handle reviews for your Festival Application! The microservices can communicate with each other and provide a complete review system for DJs and Performances.

**Start both services and begin reviewing those amazing festival performances!** 🎉
