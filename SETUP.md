# 🚀 Quick Setup Guide

## Prerequisites
- **Java 21** - [Download](https://adoptium.net/)
- **Node.js 20+** - [Download](https://nodejs.org/)
- **Docker Desktop** - [Download](https://www.docker.com/products/docker-desktop/)
- **Git** - [Download](https://git-scm.com/)

## 📥 Clone Repository

```powershell
git clone <YOUR_REPO_URL>
cd festival-app
```

## 🎯 One-Command Setup

Run this script to check prerequisites and install dependencies:

```powershell
.\setup-on-new-pc.ps1
```

## 🚀 Start Application (3 Terminals)

### Terminal 1: Start Databases
```powershell
cd festival-app\festival-app
docker-compose up -d
```

Wait 30 seconds for databases to initialize.

### Terminal 2: Start Backend Services

**Festival App (Main Service):**
```powershell
cd festival-app\festival-app
.\mvnw.cmd spring-boot:run -pl festival-app
```

**Review Service (in a new terminal):**
```powershell
cd festival-app\festival-app
.\mvnw.cmd spring-boot:run -pl review-service
```

### Terminal 3: Start Frontend
```powershell
cd festival-app\frontend\festival-platform
npm start
```

## 🌐 Access Application

Open browser: **http://localhost:4200**

## 📊 Load Sample Data (First Time Only)

After starting databases and backend:

```powershell
# Load DJs (95 top DJs)
Get-Content "festival-app\festival-app\festival-app\src\main\resources\data-djs-top100.sql" | docker exec -i festival-postgres psql -U festival_user -d festival_db

# Load Performances (44 festival performances)
Get-Content "festival-app\festival-app\festival-app\src\main\resources\data-performances-festivals.sql" | docker exec -i festival-postgres psql -U festival_user -d festival_db
```

## ✅ Verify Everything Works

- [ ] Frontend loads at http://localhost:4200
- [ ] DJs page shows 95 DJs
- [ ] Performances page shows 44 performances
- [ ] Can submit reviews
- [ ] Can create new DJs and performances

## 🛑 Stop Everything

```powershell
# Stop frontend: Ctrl+C in Terminal 3
# Stop backend services: Ctrl+C in Terminal 2
# Stop databases:
cd festival-app\festival-app
docker-compose down
```

## 🐛 Common Issues

### Port Already in Use
```powershell
# Find process using port
netstat -ano | findstr :4200

# Kill process
Stop-Process -Id <PID> -Force
```

### Docker Not Starting
1. Open Docker Desktop
2. Wait for it to fully start
3. Retry `docker-compose up -d`

### Maven Build Fails
```powershell
# Set JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

# Clean build
.\mvnw.cmd clean install -DskipTests
```

### npm install Fails
```powershell
# Clear cache
npm cache clean --force

# Retry
npm install
```

## 📚 More Information

- **Full Documentation**: See [README.md](README.md)
- **Transfer Guide**: See [TRANSFER_GUIDE.md](TRANSFER_GUIDE.md)
- **API Endpoints**: See README.md section "API Endpoints"

---

**Quick Start Summary:**
1. Clone repo
2. Run `.\setup-on-new-pc.ps1`
3. Start databases: `docker-compose up -d`
4. Start backends: `.\mvnw.cmd spring-boot:run -pl festival-app` (and review-service)
5. Start frontend: `npm start`
6. Open http://localhost:4200
