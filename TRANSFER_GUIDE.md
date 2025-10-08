# 📦 Festival App - Transfer Guide

## 🗑️ Step 1: Clean Up Before Zipping

Run this PowerShell script to remove all build artifacts and dependencies:

```powershell
# Navigate to project root
cd C:\dev\cap\festival-app

# Remove node_modules folders (will be reinstalled)
Get-ChildItem -Path . -Recurse -Directory -Filter "node_modules" | Remove-Item -Recurse -Force

# Remove Maven target folders (will be rebuilt)
Get-ChildItem -Path . -Recurse -Directory -Filter "target" | Remove-Item -Recurse -Force

# Remove Angular build artifacts
Get-ChildItem -Path . -Recurse -Directory -Filter ".angular" | Remove-Item -Recurse -Force
Get-ChildItem -Path . -Recurse -Directory -Filter "dist" | Remove-Item -Recurse -Force

# Remove IDE files (optional)
Remove-Item -Path ".vscode" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path ".idea" -Recurse -Force -ErrorAction SilentlyContinue

Write-Host "✅ Cleanup complete! Project is ready to zip."
```

## 📦 Step 2: Create ZIP File

```powershell
# Create zip file
Compress-Archive -Path "C:\dev\cap\festival-app\*" -DestinationPath "C:\dev\cap\festival-app-transfer.zip" -Force

Write-Host "✅ ZIP file created: C:\dev\cap\festival-app-transfer.zip"
```

## 📥 Step 3: On Your Other PC

### Prerequisites
Install these before unzipping:
- ✅ **Java 21** (JDK)
- ✅ **Node.js 20+** (with npm)
- ✅ **Docker Desktop** (for databases)
- ✅ **Maven** (or use included mvnw)

### Unzip and Setup

```powershell
# 1. Unzip the file
Expand-Archive -Path "festival-app-transfer.zip" -DestinationPath "C:\dev\festival-app"

# 2. Navigate to project
cd C:\dev\festival-app

# 3. Install frontend dependencies
cd festival-app\frontend\festival-platform
npm install

# This will take 5-10 minutes - installs Angular, Material, etc.
```

## 🚀 Step 4: Start the Application

### Option A: Full Stack with Docker (Recommended)

```powershell
# 1. Start databases
cd C:\dev\festival-app\festival-app\festival-app
docker-compose up -d

# Wait 30 seconds for databases to initialize

# 2. Start Festival App (Terminal 1)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
.\mvnw.cmd spring-boot:run -pl festival-app

# 3. Start Review Service (Terminal 2)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
.\mvnw.cmd spring-boot:run -pl review-service

# 4. Start Frontend (Terminal 3)
cd C:\dev\festival-app\festival-app\frontend\festival-platform
npm start
```

### Option B: Quick Frontend Only (for UI testing)

```powershell
cd C:\dev\festival-app\festival-app\frontend\festival-platform
npm start
```

Access at: **http://localhost:4200**

## 📊 Step 5: Load Data (First Time Only)

```powershell
# Load DJs
Get-Content "C:\dev\festival-app\festival-app\festival-app\festival-app\src\main\resources\data-djs-top100.sql" | docker exec -i festival-postgres psql -U festival_user -d festival_db

# Load Performances
Get-Content "C:\dev\festival-app\festival-app\festival-app\festival-app\src\main\resources\data-performances-festivals.sql" | docker exec -i festival-postgres psql -U festival_user -d festival_db

Write-Host "✅ Data loaded! 95 DJs and 44 performances added."
```

## ✅ Verification Checklist

After setup, verify everything works:

- [ ] Frontend loads at http://localhost:4200
- [ ] Can see DJs list (95 DJs)
- [ ] Can see Performances list (44 performances)
- [ ] Can submit a review
- [ ] Can create a new DJ
- [ ] Can create a new performance

## 🐛 Troubleshooting

### Port Already in Use
```powershell
# Check what's using port 4200
netstat -ano | findstr :4200

# Kill the process
Stop-Process -Id <PID> -Force
```

### Docker Not Starting
```powershell
# Restart Docker Desktop
# Then retry:
docker-compose up -d
```

### Maven Build Fails
```powershell
# Set JAVA_HOME explicitly
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

# Clean and rebuild
.\mvnw.cmd clean install -DskipTests
```

### npm install Fails
```powershell
# Clear npm cache
npm cache clean --force

# Delete package-lock.json and retry
Remove-Item package-lock.json
npm install
```

## 📁 What's Included in ZIP

```
festival-app/
├── README.md                          # Main documentation
├── TRANSFER_GUIDE.md                  # This file
├── festival-app/
│   ├── festival-app/                  # Backend services
│   │   ├── festival-app/              # Main Spring Boot app
│   │   ├── review-service/            # Review microservice
│   │   ├── docker-compose.yml         # Database containers
│   │   └── pom.xml                    # Maven config
│   └── frontend/
│       └── festival-platform/         # Angular app
│           ├── projects/
│           │   ├── festival-app/      # Main app
│           │   └── shared-lib/        # Shared library
│           ├── package.json           # Dependencies
│           └── angular.json           # Angular config
└── Data Files:
    ├── data-djs-top100.sql            # 95 top DJs
    └── data-performances-festivals.sql # 44 performances
```

## 📊 Expected File Sizes

- **ZIP file**: ~50-100 MB (without node_modules/target)
- **After npm install**: ~500-800 MB
- **After Maven build**: ~200-300 MB

## 🎯 Quick Start Commands (Summary)

```powershell
# On new PC after unzip:
cd festival-app\frontend\festival-platform
npm install
npm start

# Access at: http://localhost:4200
```

---

**Need help?** Check the main README.md for detailed API documentation and troubleshooting.
