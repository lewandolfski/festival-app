# Festival Platform - Unified Repository

A complete festival management platform built with Spring Boot microservices architecture. This repository contains both the main Festival Application and the Review Microservice in separate folders for easy management and deployment.

## 🏗️ Architecture

```
festival-platform/
├── festival-app/          # Main Festival Application (Spring Boot + PostgreSQL)
├── review-service/        # Review Microservice (Spring Boot + MongoDB)
├── docker/               # Database initialization scripts
├── docker-compose.yml    # Unified container orchestration
├── pom.xml              # Parent POM for multi-module build
└── build-and-deploy.bat # One-click deployment script
```

## 🚀 Quick Start

### Prerequisites
- Java 21 (JDK)
- Docker Desktop
- Maven (or use included Maven wrapper)

### One-Click Deployment
```bash
# Windows
.\build-and-deploy.bat

# Manual steps (cross-platform)
mvn clean package -DskipTests
docker-compose up -d
```

## 📋 Services Overview

### Festival Application (Port 8080)
- **Technology**: Spring Boot 3.1.0 + PostgreSQL + JPA
- **Features**: DJ and Performance management with full CRUD operations
- **Database**: PostgreSQL with persistent storage
- **API Base**: `http://localhost:8080/api`

### Review Service (Port 8081)
- **Technology**: Spring Boot 3.1.0 + MongoDB + WebFlux
- **Features**: Review management for DJs and Performances
- **Database**: MongoDB with document storage
- **API Base**: `http://localhost:8081/api`

## 🔗 API Endpoints

### Festival Application APIs

#### DJs
- `GET /api/djs` - Get all DJs
- `POST /api/djs` - Create new DJ
- `GET /api/djs/{id}` - Get DJ by ID
- `PUT /api/djs/{id}` - Update DJ
- `DELETE /api/djs/{id}` - Delete DJ
- `GET /api/djs/{id}/reviews` - Get reviews for DJ
- `GET /api/djs/{id}/rating` - Get average rating for DJ
- `GET /api/djs/{id}/review-count` - Get review count for DJ

#### Performances
- `GET /api/performances` - Get all performances
- `POST /api/performances` - Create new performance
- `GET /api/performances/{id}` - Get performance by ID
- `PUT /api/performances/{id}` - Update performance
- `DELETE /api/performances/{id}` - Delete performance
- `GET /api/performances/{id}/reviews` - Get reviews for performance
- `GET /api/performances/{id}/rating` - Get average rating for performance

### Review Service APIs

#### Reviews
- `GET /api/reviews` - Get all reviews
- `POST /api/reviews` - Create new review
- `GET /api/reviews/{id}` - Get review by ID
- `PUT /api/reviews/{id}` - Update review
- `DELETE /api/reviews/{id}` - Delete review
- `GET /api/reviews/subject/{id}/type/{type}` - Get reviews by subject
- `GET /api/reviews/subject/{id}/type/{type}/average` - Get average rating
- `GET /api/reviews/subject/{id}/type/{type}/count` - Get review count

## 🐳 Docker Configuration

### Services
- **festival-db**: PostgreSQL 15 Alpine
- **review-db**: MongoDB 7 Jammy
- **festival-app**: Main application container
- **review-service**: Review microservice container

### Networks
- **festival-platform-network**: Custom bridge network for service communication

### Volumes
- **festival_postgres_data**: Persistent PostgreSQL data
- **review_mongodb_data**: Persistent MongoDB data

## 🛠️ Development

### Building Individual Services

#### Festival Application
```bash
cd festival-app
mvn clean package
```

#### Review Service
```bash
cd review-service
mvn clean package
```

### Building All Services
```bash
# From root directory
mvn clean package
```

### Running Services Locally

#### Festival Application (Development)
```bash
cd festival-app
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Review Service (Development)
```bash
cd review-service
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## 🔧 Configuration Profiles

### Development Profile (`dev`)
- H2/Embedded databases for testing
- Detailed logging enabled
- Auto-reload configurations

### Docker Profile (`docker`)
- PostgreSQL and MongoDB connections
- Optimized for containerized deployment
- Service-to-service communication configured

### Production Profile (`prod`)
- Production-ready configurations
- Security optimizations
- Performance tuning

## 📊 Monitoring & Health Checks

### Health Endpoints
- Festival App: `http://localhost:8080/actuator/health`
- Review Service: `http://localhost:8081/actuator/health`

### Metrics
- Festival App: `http://localhost:8080/actuator/metrics`
- Review Service: `http://localhost:8081/actuator/metrics`

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Tests for Specific Service
```bash
# Festival Application
cd festival-app && mvn test

# Review Service
cd review-service && mvn test
```

## 🚀 Deployment Options

### Local Docker Deployment
```bash
docker-compose up -d
```

### Kubernetes Deployment
```bash
# Build images
docker-compose build

# Deploy to Kubernetes (requires k8s cluster)
kubectl apply -f k8s-deployment.yaml
```

### Cloud Deployment
The containerized services can be deployed to:
- AWS ECS/EKS
- Google Cloud Run/GKE
- Azure Container Instances/AKS
- Any Docker-compatible platform

## 🔍 Troubleshooting

### Common Issues

#### Java Environment
If you encounter Java-related build issues:
```bash
# Run the Java environment fix script
.\festival-app\fix-java-env.bat
```

#### Docker Issues
```bash
# Clean Docker environment
docker-compose down -v
docker system prune -f

# Rebuild and restart
.\build-and-deploy.bat
```

#### Port Conflicts
- Festival App: Change port in `application.properties` (default: 8080)
- Review Service: Change port in `application.properties` (default: 8081)
- PostgreSQL: Change port in `docker-compose.yml` (default: 5432)
- MongoDB: Change port in `docker-compose.yml` (default: 27017)

### Logs
```bash
# View all service logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f festival-app
docker-compose logs -f review-service
```

## 📝 Sample Data

Both services include sample data initialization:
- **Festival App**: 5 DJs and 5 Performances
- **Review Service**: 5 sample reviews

## 🤝 Inter-Service Communication

The Festival Application communicates with the Review Service via:
- **WebClient**: Reactive HTTP client for async communication
- **Service Discovery**: Direct service-to-service calls in Docker network
- **Circuit Breaker**: Graceful degradation when review service is unavailable

## 📚 Technology Stack

- **Framework**: Spring Boot 3.1.0
- **Java Version**: 21
- **Databases**: PostgreSQL 15, MongoDB 7
- **Build Tool**: Maven 3.9+
- **Containerization**: Docker & Docker Compose
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Documentation**: OpenAPI/Swagger (available at `/swagger-ui.html`)

## 🔐 Security Features

- Non-root container execution
- Input validation with Bean Validation
- SQL injection prevention with JPA
- CORS configuration for frontend integration
- Health check endpoints for monitoring

## 📈 Performance Optimizations

- Connection pooling (HikariCP)
- JPA batch operations
- MongoDB indexing
- Container resource limits
- JVM optimization for containers

---

**Happy Coding! 🎉**

For issues and contributions, please refer to the project documentation or contact the development team.
