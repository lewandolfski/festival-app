# Simple Deployment Guide

## On Current PC (Before Transfer)

### 1. Clean Up
```powershell
cd C:\dev\cap\festival-app
.\cleanup-before-zip.ps1
```

### 2. Create ZIP
```powershell
Compress-Archive -Path ".\festival-app" -DestinationPath ".\festival-app-transfer.zip" -Force
```

### 3. Transfer
Copy `festival-app-transfer.zip` to your other PC (USB, cloud, etc.)

---

## On New PC (After Transfer)

### Prerequisites (Install First)
- Java 21 JDK
- Node.js 20+
- Docker Desktop

### 1. Unzip
```powershell
Expand-Archive -Path "festival-app-transfer.zip" -DestinationPath "C:\dev\festival-app"
cd C:\dev\festival-app
```

### 2. Install Dependencies
```powershell
cd festival-app\frontend\festival-platform
npm install
```
Wait 5-10 minutes for npm to finish.

### 3. Start Everything
```powershell
cd C:\dev\festival-app
.\START_APP.ps1
```

This will:
- Start databases (Docker)
- Start Festival App backend
- Start Review Service backend
- Start Frontend
- Open browser automatically

**Total time: ~3 minutes**

### 4. Load Data (First Time Only)
```powershell
cd festival-app\festival-app

# Load DJs
Get-Content "festival-app\src\main\resources\data-djs-top100.sql" | docker exec -i festival-postgres psql -U festival_user -d festival_db

# Load Performances
Get-Content "festival-app\src\main\resources\data-performances-festivals.sql" | docker exec -i festival-postgres psql -U festival_user -d festival_db
```

---

## That's It!

Access at: **http://localhost:4200**

You'll have:
- 100+ DJs
- 50+ Performances
- Full review system
- Beautiful modern UI

---

## Quick Start (After First Setup)

Just run:
```powershell
.\START_APP.ps1
```

Done! 🎉
