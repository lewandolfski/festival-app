========================================
  FESTIVAL APP - DEPLOYMENT GUIDE
========================================

CURRENT PC - BEFORE TRANSFER
-----------------------------

1. Clean up:
   .\cleanup-before-zip.ps1

2. Create ZIP:
   Compress-Archive -Path ".\festival-app" -DestinationPath ".\festival-app-transfer.zip" -Force

3. Transfer festival-app-transfer.zip to new PC


NEW PC - AFTER TRANSFER
------------------------

PREREQUISITES (Install these first):
- Java 21 JDK
- Node.js 20+
- Docker Desktop

SETUP STEPS:

1. Unzip:
   Expand-Archive -Path "festival-app-transfer.zip" -DestinationPath "C:\dev\festival-app"
   cd C:\dev\festival-app

2. First time setup (run ONCE):
   .\FIRST_TIME_SETUP.ps1
   
   This will:
   - Install npm dependencies (10 min)
   - Start Docker databases
   - Load 100+ DJs and 50+ performances
   - Start all services automatically

3. Access app:
   http://localhost:4200


DAILY USE (After first setup)
------------------------------

Just run:
   .\START_APP.ps1

Takes 2-3 minutes to start everything.


WHAT YOU GET
------------
- 103 DJs (Top DJs from DJ Mag 2025)
- 53 Festival Performances
- Review system
- Modern animated UI
- Full CRUD operations


SERVICES
--------
- Frontend:     http://localhost:4200
- Festival API: http://localhost:9090
- Review API:   http://localhost:8080


FILES NEEDED FOR TRANSFER
--------------------------
YES - Keep these:
  - festival-app/ folder (source code)
  - START_APP.ps1
  - FIRST_TIME_SETUP.ps1
  - cleanup-before-zip.ps1
  - All .md files

NO - Remove before zipping:
  - node_modules/
  - target/
  - .angular/
  - dist/
  - .git/
  - .vscode/


TROUBLESHOOTING
---------------

Port already in use:
  netstat -ano | findstr :4200
  Stop-Process -Id <PID> -Force

Docker won't start:
  Restart Docker Desktop
  docker-compose up -d

npm install fails:
  npm cache clean --force
  npm install


========================================
