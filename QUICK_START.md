# ⚡ Quick Start - Festival Platform

## 🎯 Push to Git (Do This First)

### 1. Create a repository on GitHub/GitLab
- GitHub: https://github.com/new
- GitLab: https://gitlab.com/projects/new

### 2. Push your code
```powershell
# Replace YOUR_USERNAME and YOUR_REPO with your actual values
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git
git push -u origin main
```

## 📥 On Your Other PC

### 1. Clone the repository
```powershell
git clone https://github.com/YOUR_USERNAME/YOUR_REPO.git
cd festival-app
```

### 2. Run setup script
```powershell
.\setup-on-new-pc.ps1
```

This will:
- ✅ Check Java, Node.js, Docker
- ✅ Install all npm dependencies
- ✅ Show you next steps

### 3. Start the application

**Terminal 1 - Databases:**
```powershell
cd festival-app\festival-app
docker-compose up -d
```

**Terminal 2 - Backend:**
```powershell
cd festival-app\festival-app
.\mvnw.cmd spring-boot:run -pl festival-app
```

**Terminal 3 - Review Service:**
```powershell
cd festival-app\festival-app
.\mvnw.cmd spring-boot:run -pl review-service
```

**Terminal 4 - Frontend:**
```powershell
cd festival-app\frontend\festival-platform
npm start
```

### 4. Load sample data (first time only)
```powershell
# Load 95 DJs
Get-Content "festival-app\festival-app\festival-app\src\main\resources\data-djs-top100.sql" | docker exec -i festival-postgres psql -U festival_user -d festival_db

# Load 44 performances
Get-Content "festival-app\festival-app\festival-app\src\main\resources\data-performances-festivals.sql" | docker exec -i festival-postgres psql -U festival_user -d festival_db
```

### 5. Open in browser
http://localhost:4200

## 📚 Full Documentation

- **SETUP.md** - Detailed setup instructions
- **README.md** - Full project documentation
- **GIT_PUSH_INSTRUCTIONS.md** - Git hosting options
- **TRANSFER_GUIDE.md** - Alternative transfer methods

## ✅ What's Ready

- ✅ Git repository initialized
- ✅ All code committed
- ✅ .gitignore configured
- ✅ Setup scripts ready
- ✅ Documentation complete
- ⏳ Ready to push to remote

## 🎵 Features

- 95 top DJs from DJ Mag 2025
- 44 festival performances
- Review system with MongoDB
- Modern Angular 18 UI
- Spring Boot microservices
- Docker-based databases

---

**Next step:** Push to GitHub/GitLab and clone on your other PC!
