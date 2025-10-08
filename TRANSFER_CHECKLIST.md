# ✅ Transfer Checklist

## On Current PC (Before Transfer)

### 1. Cleanup Project
```powershell
cd C:\dev\cap\festival-app
.\cleanup-before-zip.ps1
```

**What this removes:**
- ❌ `node_modules/` folders (~500 MB)
- ❌ `target/` folders (Maven builds)
- ❌ `.angular/` cache
- ❌ `dist/` build outputs
- ❌ IDE files (.vscode, .idea)
- ❌ Log files

**What stays:**
- ✅ Source code
- ✅ Configuration files
- ✅ Data files (SQL scripts)
- ✅ Documentation
- ✅ package.json & pom.xml

### 2. Create ZIP
```powershell
Compress-Archive -Path "C:\dev\cap\festival-app\*" -DestinationPath "C:\dev\cap\festival-app-transfer.zip" -Force
```

**Expected ZIP size:** 50-100 MB

### 3. Transfer ZIP
- Copy `festival-app-transfer.zip` to USB drive or cloud storage
- Transfer to other PC

---

## On New PC (After Transfer)

### 1. Prerequisites Check
Install these BEFORE unzipping:

- [ ] **Java 21 JDK** installed
  - Download: https://adoptium.net/
  - Verify: `java -version`

- [ ] **Node.js 20+** installed
  - Download: https://nodejs.org/
  - Verify: `node --version`

- [ ] **Docker Desktop** installed
  - Download: https://www.docker.com/products/docker-desktop
  - Verify: `docker --version`

- [ ] **Git** (optional, for version control)
  - Download: https://git-scm.com/

### 2. Unzip Project
```powershell
Expand-Archive -Path "festival-app-transfer.zip" -DestinationPath "C:\dev\festival-app"
cd C:\dev\festival-app
```

### 3. Run Setup Script
```powershell
.\setup-on-new-pc.ps1
```

This will:
- ✅ Check prerequisites
- ✅ Install npm dependencies (~5-10 minutes)
- ✅ Show next steps

### 4. Start Databases
```powershell
cd festival-app\festival-app
docker-compose up -d
```

Wait 30 seconds for initialization.

### 5. Load Data (First Time Only)
```powershell
# Load DJs
Get-Content "festival-app\festival-app\src\main\resources\data-djs-top100.sql" | docker exec -i festival-postgres psql -U festival_user -d festival_db

# Load Performances
Get-Content "festival-app\festival-app\src\main\resources\data-performances-festivals.sql" | docker exec -i festival-postgres psql -U festival_user -d festival_db
```

### 6. Start Backend Services

**Terminal 1 - Festival App:**
```powershell
cd festival-app\festival-app
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
.\mvnw.cmd spring-boot:run -pl festival-app
```

**Terminal 2 - Review Service:**
```powershell
cd festival-app\festival-app
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
.\mvnw.cmd spring-boot:run -pl review-service
```

### 7. Start Frontend

**Terminal 3 - Angular:**
```powershell
cd festival-app\frontend\festival-platform
npm start
```

### 8. Verify Everything Works

Open browser: **http://localhost:4200**

- [ ] Frontend loads successfully
- [ ] Can see DJs page (95 DJs)
- [ ] Can see Performances page (44 performances)
- [ ] Can see Reviews page
- [ ] Can create a new DJ
- [ ] Can create a new performance
- [ ] Can submit a review

---

## 🎯 Quick Start (After Initial Setup)

Once everything is set up, you only need:

```powershell
# Terminal 1 - Databases (if not running)
cd festival-app\festival-app
docker-compose up -d

# Terminal 2 - Festival App
cd festival-app\festival-app
.\mvnw.cmd spring-boot:run -pl festival-app

# Terminal 3 - Review Service
cd festival-app\festival-app
.\mvnw.cmd spring-boot:run -pl review-service

# Terminal 4 - Frontend
cd festival-app\frontend\festival-platform
npm start
```

---

## 📊 What You'll Have

After successful setup:

- **95 Top DJs** from DJ Mag 2025
- **44 Festival Performances** from major Dutch festivals
- **Full CRUD operations** for DJs and Performances
- **Review system** with MongoDB
- **Modern UI** with animations

---

## 🐛 Common Issues

### Port Already in Use
```powershell
netstat -ano | findstr :4200
Stop-Process -Id <PID> -Force
```

### Docker Won't Start
1. Restart Docker Desktop
2. Wait for it to fully start
3. Retry `docker-compose up -d`

### Maven Build Fails
```powershell
# Clean and rebuild
.\mvnw.cmd clean install -DskipTests
```

### npm install Fails
```powershell
npm cache clean --force
Remove-Item package-lock.json
npm install
```

---

## 📞 Need Help?

Check these files:
- `README.md` - Full documentation
- `TRANSFER_GUIDE.md` - Detailed transfer instructions
- Backend logs in terminal windows
- Browser console (F12) for frontend errors

---

**You're ready to transfer! 🚀**
