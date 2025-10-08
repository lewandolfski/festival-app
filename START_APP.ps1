# Festival App - Quick Start Script
# Run this to start everything in ~5 minutes

Write-Host "Starting Festival Platform..." -ForegroundColor Cyan
Write-Host "================================`n" -ForegroundColor Cyan

# 1. Start databases
Write-Host "1. Starting databases..." -ForegroundColor Yellow
docker start festival-postgres review-mongodb
Start-Sleep -Seconds 5

# 2. Check if databases are running
$postgresRunning = docker ps --filter "name=festival-postgres" --filter "status=running" -q
$mongoRunning = docker ps --filter "name=review-mongodb" --filter "status=running" -q

if ($postgresRunning -and $mongoRunning) {
    Write-Host "   Databases running!" -ForegroundColor Green
} else {
    Write-Host "   Database startup failed!" -ForegroundColor Red
    exit 1
}

# 3. Start Festival App Backend (in new window)
Write-Host "`n2. Starting Festival App backend..." -ForegroundColor Yellow
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
Start-Process powershell -ArgumentList "-NoExit", "-Command", `
    "Write-Host 'Festival App Backend' -ForegroundColor Cyan; " +
    "cd 'C:\dev\cap\festival-app\festival-app\festival-app'; " +
    "`$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21'; " +
    ".\mvnw.cmd spring-boot:run -pl festival-app"

# 4. Start Review Service (in new window)
Write-Host "3. Starting Review Service..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", `
    "Write-Host 'Review Service' -ForegroundColor Cyan; " +
    "cd 'C:\dev\cap\festival-app\festival-app\festival-app'; " +
    "`$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21'; " +
    ".\mvnw.cmd spring-boot:run -pl review-service"

# 5. Wait for backends to start
Write-Host "`nWaiting for backends to start (60 seconds)..." -ForegroundColor Yellow
Start-Sleep -Seconds 60

# 6. Start Frontend (in new window)
Write-Host "`n4. Starting Frontend..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", `
    "Write-Host 'Festival Frontend' -ForegroundColor Cyan; " +
    "cd 'C:\dev\cap\festival-app\festival-app\frontend\festival-platform'; " +
    "npm start"

# 7. Wait for frontend to compile
Write-Host "`nWaiting for frontend to compile (60 seconds)..." -ForegroundColor Yellow
Start-Sleep -Seconds 60

# 8. Open browser
Write-Host "`n5. Opening browser..." -ForegroundColor Yellow
Start-Process "http://localhost:4200"

Write-Host "`nFestival Platform is starting!" -ForegroundColor Green
Write-Host "`nServices:" -ForegroundColor Cyan
Write-Host "   - Frontend:      http://localhost:4200" -ForegroundColor White
Write-Host "   - Festival API:  http://localhost:9090" -ForegroundColor White
Write-Host "   - Review API:    http://localhost:8080" -ForegroundColor White
Write-Host "`nTip: Keep the PowerShell windows open!" -ForegroundColor Yellow
Write-Host "`nEnjoy your festival platform!" -ForegroundColor Green
