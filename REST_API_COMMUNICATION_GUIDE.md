# 🔗 REST API Communication Between Services

## 📋 **Service Architecture Overview**

```
┌─────────────────────┐    HTTP REST API    ┌─────────────────────┐
│  Festival App       │◄──────────────────►│  Review Service     │
│  Port: 9090         │                     │  Port: 8080         │
│  PostgreSQL DB      │                     │  MongoDB            │
└─────────────────────┘                     └─────────────────────┘
```

## 🎯 **Port Configuration (CORRECTED)**

- **Festival Application**: `http://localhost:9090`
- **Review Service**: `http://localhost:8080`
- **PostgreSQL**: `localhost:5432`
- **MongoDB**: `localhost:27017`

## 🔄 **How REST API Communication Works**

### **1. WebClient Configuration**

The Festival Application uses Spring WebFlux's `WebClient` to communicate with the Review Service:

```java
@Service
public class ReviewServiceClient {
    private final WebClient webClient;
    
    public ReviewServiceClient(WebClient.Builder webClientBuilder,
                             @Value("${review.service.url:http://localhost:8080}") String reviewServiceUrl) {
        this.webClient = webClientBuilder
                .baseUrl(reviewServiceUrl)
                .build();
    }
}
```

### **2. Communication Flow**

#### **Festival App → Review Service**
```
1. User requests DJ reviews: GET /api/djs/dj-001/reviews
2. Festival App calls ReviewServiceClient.getReviewsForDJ("dj-001")
3. WebClient makes HTTP call: GET http://localhost:8080/api/reviews/subject/dj-001/type/DJ
4. Review Service processes request and returns JSON
5. Festival App returns the reviews to the user
```

#### **Review Service → Festival App (Validation)**
```
1. User creates review: POST /api/reviews
2. Review Service validates DJ/Performance exists
3. WebClient makes HTTP call: GET http://localhost:9090/api/djs/dj-001
4. Festival App confirms DJ exists
5. Review Service saves the review
```

### **3. API Endpoints for Inter-Service Communication**

#### **Festival App Exposes (Port 9090):**
- `GET /api/djs/{id}` - Validate DJ exists
- `GET /api/performances/{id}` - Validate Performance exists
- `GET /api/djs/{id}/reviews` - Get reviews for DJ (calls Review Service)
- `GET /api/djs/{id}/rating` - Get average rating for DJ
- `GET /api/performances/{id}/reviews` - Get reviews for Performance

#### **Review Service Exposes (Port 8080):**
- `GET /api/reviews` - Get all reviews
- `POST /api/reviews` - Create new review
- `GET /api/reviews/subject/{id}/type/{type}` - Get reviews by subject
- `GET /api/reviews/subject/{id}/type/{type}/average` - Get average rating
- `GET /api/reviews/subject/{id}/type/{type}/count` - Get review count

### **4. Configuration Properties**

#### **Festival App (`application.properties`):**
```properties
server.port=9090
review.service.url=http://localhost:8080
```

#### **Review Service (`application.properties`):**
```properties
server.port=8080
festival.service.url=http://localhost:9090
```

### **5. Docker Network Communication**

In Docker, services communicate using service names:
```yaml
# Festival App calls Review Service
REVIEW_SERVICE_URL: http://review-service:8080

# Review Service calls Festival App  
FESTIVAL_SERVICE_URL: http://festival-app:9090
```

## 🧪 **Testing REST API Communication**

### **Test 1: Festival App Health**
```bash
curl http://localhost:9090/actuator/health
```

### **Test 2: Review Service Health**
```bash
curl http://localhost:8080/actuator/health
```

### **Test 3: Get All DJs**
```bash
curl http://localhost:9090/api/djs
```

### **Test 4: Get Reviews for DJ (Cross-Service Call)**
```bash
curl http://localhost:9090/api/djs/dj-001/reviews
```

### **Test 5: Create Review (Validates DJ exists)**
```bash
curl -X POST http://localhost:8080/api/reviews \
  -H "Content-Type: application/json" \
  -d '{
    "subjectId": "dj-001",
    "subjectType": "DJ", 
    "reviewerName": "John Doe",
    "rating": 5,
    "comment": "Amazing performance!"
  }'
```

### **Test 6: Get Average Rating**
```bash
curl http://localhost:9090/api/djs/dj-001/rating
```

## 🔧 **Error Handling & Resilience**

### **Circuit Breaker Pattern**
```java
public String getReviewsForDJ(Long djId) {
    try {
        return webClient.get()
                .uri("/api/reviews/subject/{subjectId}/type/{subjectType}", djId, "DJ")
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10))
                .block();
    } catch (WebClientResponseException.NotFound e) {
        return "[]"; // Return empty array if no reviews found
    } catch (Exception e) {
        System.err.println("Error fetching DJ reviews: " + e.getMessage());
        return "[]"; // Graceful degradation
    }
}
```

### **Timeout Configuration**
- **Connection Timeout**: 5 seconds
- **Response Timeout**: 10 seconds
- **Retry Logic**: 3 attempts with exponential backoff

## 📊 **Data Flow Examples**

### **Example 1: Get DJ with Reviews**
```
1. GET /api/djs/dj-001 → Festival App
2. Festival App fetches DJ from PostgreSQL
3. Festival App calls Review Service: GET /api/reviews/subject/dj-001/type/DJ
4. Review Service queries MongoDB for reviews
5. Review Service returns JSON array of reviews
6. Festival App combines DJ data with reviews
7. Returns enriched response to client
```

### **Example 2: Create Review with Validation**
```
1. POST /api/reviews → Review Service
2. Review Service validates request data
3. Review Service calls Festival App: GET /api/djs/dj-001
4. Festival App checks if DJ exists in PostgreSQL
5. Festival App returns DJ data or 404
6. Review Service saves review to MongoDB
7. Returns created review to client
```

## 🚀 **Performance Considerations**

1. **Async Communication**: WebClient is non-blocking
2. **Connection Pooling**: Reuses HTTP connections
3. **Caching**: Can implement response caching
4. **Load Balancing**: Ready for multiple service instances
5. **Monitoring**: Health checks and metrics exposed

## 🔐 **Security Considerations**

1. **Service-to-Service Authentication**: Can add JWT tokens
2. **Network Security**: Services communicate within Docker network
3. **Input Validation**: Both services validate incoming requests
4. **Error Handling**: No sensitive data in error responses

---

This architecture provides **loose coupling** between services while maintaining **data consistency** and **high availability** through proper error handling and timeouts.
