# 🎵 Festival Platform Application

A modern, full-stack festival management platform with microservices architecture, featuring DJ management, performance scheduling, and review functionality.

## 🚀 Features

### Frontend (Angular 18)
- **Modern UI** with gradient backgrounds, glassmorphism effects, and smooth animations
- **DJ Management** - Browse, search, and filter 95+ top DJs from DJ Mag 2025
- **Performance Scheduling** - View 44+ festival performances from major Dutch festivals
- **Review System** - Submit and browse reviews for DJs and performances
- **Responsive Design** - Beautiful card-based layouts with stagger animations
- **Real-time Updates** - Live data from backend services

### Backend Services
- **Festival App (Spring Boot)** - Main application managing DJs and performances
- **Review Service (Spring Boot)** - Microservice for reviews with MongoDB
- **PostgreSQL** - Relational database for festival data
- **MongoDB** - NoSQL database for reviews

## 📦 Tech Stack

### Frontend
- Angular 18
- Angular Material
- TypeScript
- SCSS with animations
- RxJS

### Backend
- Java 21
- Spring Boot 3.1
- PostgreSQL 16
- MongoDB 7
- Docker & Docker Compose

## 🎯 Quick Start

### Prerequisites
- Docker & Docker Compose
- Java 21
- Node.js 20+
- Maven

### 1. Start Backend Services

```bash
cd festival-app/festival-app
docker-compose up -d
```

This starts:
- PostgreSQL (port 5432)
- MongoDB (port 27017)

### 2. Start Festival Application

```bash
cd festival-app/festival-app
./mvnw.cmd spring-boot:run -pl festival-app
```

Runs on: http://localhost:9090

### 3. Start Review Service

```bash
cd festival-app/festival-app
./mvnw.cmd spring-boot:run -pl review-service
```

Runs on: http://localhost:8080

### 4. Start Frontend

```bash
cd festival-app/frontend/festival-platform
npm install
npm start  # Starts festival-app (main UI)
```

Access at: http://localhost:4200

**Note:** This workspace contains two apps:
- `festival-app` - Main application (default, runs with `npm start`)
- `review-app` - Standalone review app (runs with `npm run start:review`)

## 📊 Database

### Current Data
- **95 DJs** - Top DJs from DJ Mag 2025 including David Guetta, Martin Garrix, Armin Van Buuren, Charlotte de Witte, etc.
- **44 Performances** - Major Dutch festivals:
  - Amsterdam Dance Event
  - Awakenings Festival
  - Defqon.1
  - Mysteryland
  - Lowlands
  - Dekmantel
  - And many more!

### Seed Data Files
Located in `festival-app/src/main/resources/`:
- `data-djs-top100.sql` - Top 100 DJs
- `data-performances-festivals.sql` - Festival performances

## 🎨 UI Features

### Animations
- **Fade-in animations** on page load
- **Staggered card animations** (0.1s delay per card)
- **Floating logo** in navigation
- **Shimmer effect** on navbar
- **Rotating gradient backgrounds**
- **Smooth hover effects** with scale and shadow

### Components
- DJ List with search and genre filtering
- Performance List with time badges
- Review List with star ratings
- Interactive forms for creating/editing data

## 🔧 Configuration

### Festival App
- Port: 9090
- Database: PostgreSQL (festival_db)
- User: festival_user / festival_pass

### Review Service
- Port: 8080
- Database: MongoDB (review_db)
- Auth: review_user / review_pass

### Frontend
- Port: 4200
- API Proxy: Configured for CORS

## 📝 API Endpoints

### Festival App (http://localhost:9090)
- `GET /api/djs` - List all DJs
- `GET /api/djs/{id}` - Get DJ by ID
- `POST /api/djs` - Create DJ
- `PUT /api/djs/{id}` - Update DJ
- `DELETE /api/djs/{id}` - Delete DJ
- `GET /api/performances` - List all performances
- `GET /api/performances/{id}` - Get performance by ID
- `POST /api/performances` - Create performance
- `PUT /api/performances/{id}` - Update performance
- `DELETE /api/performances/{id}` - Delete performance

### Review Service (http://localhost:8080)
- `GET /api/reviews` - List all reviews
- `GET /api/reviews/{id}` - Get review by ID
- `POST /api/reviews` - Create review
- `PUT /api/reviews/{id}` - Update review
- `DELETE /api/reviews/{id}` - Delete review
- `GET /api/reviews/subject/{type}/{id}` - Get reviews for subject
- `GET /api/reviews/health` - Health check

## 🎭 Top DJs Included

1. David Guetta (EDM)
2. Martin Garrix (Progressive House)
3. Alok (Bass House)
4. Dimitri Vegas & Like Mike (Big Room)
5. Armin Van Buuren (Trance)
6. Timmy Trumpet (Melbourne Bounce)
7. FISHER (Tech House)
8. Afrojack (Electro House)
9. Charlotte de Witte (Techno)
10. Anyma (Melodic Techno)
... and 85 more!

## 🎪 Featured Festivals

- **Amsterdam Dance Event** - World's largest electronic music conference
- **Awakenings Festival** - Leading techno festival
- **Defqon.1** - Hardstyle mega festival
- **Mysteryland** - Oldest electronic music festival in NL
- **Lowlands** - Multi-genre music and arts festival
- **Dekmantel** - Electronic music and arts festival
- **North Sea Jazz** - Largest indoor jazz festival
- **Pinkpop** - Longest running annual festival in NL
... and many more!

## 🐛 Troubleshooting

### MongoDB Authentication Issues
If you see "Command insert requires authentication":
```bash
docker restart review-mongodb
```

### Port Already in Use
```bash
# Check what's using the port
netstat -ano | findstr :8080
# Kill the process
Stop-Process -Id <PID> -Force
```

### Frontend Not Loading
1. Clear browser cache
2. Restart Angular dev server
3. Check console for errors

## 📄 License

This project is for educational purposes.

## 🤝 Contributing

This is a demonstration project showcasing modern web development practices with Angular and Spring Boot microservices.

---

**Built with ❤️ for the festival community** 🎵✨
