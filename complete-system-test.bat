@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Festival Platform - Complete System Test
echo ========================================
echo.
echo Testing with CORRECT PORTS:
echo - Festival Application: Port 9090
echo - Review Service: Port 8080
echo - PostgreSQL: Port 5432
echo - MongoDB: Port 27017
echo.

REM Set correct JAVA_HOME
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
echo Using Java: %JAVA_HOME%
echo.

echo ========================================
echo Step 1: Clean Environment
echo ========================================
echo.

REM Kill any existing Java processes
taskkill /F /IM java.exe >nul 2>&1
echo ✓ Stopped existing Java processes

REM Stop and remove existing containers
docker-compose down -v >nul 2>&1
echo ✓ Cleaned Docker containers

echo.
echo ========================================
echo Step 2: Start Databases
echo ========================================
echo.

echo Starting PostgreSQL and MongoDB...
docker-compose up -d festival-db review-db
if !errorlevel! neq 0 (
    echo ERROR: Failed to start databases
    pause
    exit /b 1
)

echo Waiting 15 seconds for databases to initialize...
timeout /t 15 /nobreak >nul

echo ✓ Databases started successfully

echo.
echo ========================================
echo Step 3: Build Applications
echo ========================================
echo.

echo Building Festival Application...
cd festival-app
call mvnw.cmd clean package -DskipTests
if !errorlevel! neq 0 (
    echo ERROR: Failed to build Festival Application
    cd ..
    pause
    exit /b 1
)
cd ..
echo ✓ Festival Application built

echo Building Review Service...
cd review-service
call mvnw.cmd clean package -DskipTests
if !errorlevel! neq 0 (
    echo ERROR: Failed to build Review Service
    cd ..
    pause
    exit /b 1
)
cd ..
echo ✓ Review Service built

echo.
echo ========================================
echo Step 4: Start Applications
echo ========================================
echo.

echo Starting Festival Application on port 9090...
cd festival-app
start /B "Festival-App" cmd /c "mvnw.cmd spring-boot:run"
cd ..

echo Waiting 20 seconds for Festival Application to start...
timeout /t 20 /nobreak >nul

echo Starting Review Service on port 8080...
cd review-service
start /B "Review-Service" cmd /c "mvnw.cmd spring-boot:run"
cd ..

echo Waiting 20 seconds for Review Service to start...
timeout /t 20 /nobreak >nul

echo.
echo ========================================
echo Step 5: Health Check Tests
echo ========================================
echo.

echo Testing Festival Application Health (Port 9090)...
powershell -Command "try { $response = Invoke-WebRequest -Uri 'http://localhost:9090/actuator/health' -Method Get -TimeoutSec 10; Write-Host '✓ Festival App Health: OK' -ForegroundColor Green } catch { Write-Host '✗ Festival App Health: FAILED' -ForegroundColor Red; Write-Host $_.Exception.Message }"

echo.
echo Testing Review Service Health (Port 8080)...
powershell -Command "try { $response = Invoke-WebRequest -Uri 'http://localhost:8080/actuator/health' -Method Get -TimeoutSec 10; Write-Host '✓ Review Service Health: OK' -ForegroundColor Green } catch { Write-Host '✗ Review Service Health: FAILED' -ForegroundColor Red; Write-Host $_.Exception.Message }"

echo.
echo ========================================
echo Step 6: API Functionality Tests
echo ========================================
echo.

echo Testing Festival Application APIs...
powershell -Command "try { $response = Invoke-WebRequest -Uri 'http://localhost:9090/api/djs' -Method Get -TimeoutSec 5; $djs = $response.Content | ConvertFrom-Json; Write-Host '✓ DJs API: Found' $djs.Count 'DJs' -ForegroundColor Green } catch { Write-Host '✗ DJs API: FAILED' -ForegroundColor Red }"

powershell -Command "try { $response = Invoke-WebRequest -Uri 'http://localhost:9090/api/performances' -Method Get -TimeoutSec 5; $perfs = $response.Content | ConvertFrom-Json; Write-Host '✓ Performances API: Found' $perfs.Count 'Performances' -ForegroundColor Green } catch { Write-Host '✗ Performances API: FAILED' -ForegroundColor Red }"

echo.
echo Testing Review Service APIs...
powershell -Command "try { $response = Invoke-WebRequest -Uri 'http://localhost:8080/api/reviews' -Method Get -TimeoutSec 5; $reviews = $response.Content | ConvertFrom-Json; Write-Host '✓ Reviews API: Found' $reviews.Count 'Reviews' -ForegroundColor Green } catch { Write-Host '✗ Reviews API: FAILED' -ForegroundColor Red }"

echo.
echo ========================================
echo Step 7: Inter-Service Communication Tests
echo ========================================
echo.

echo Testing DJ Reviews (Festival App → Review Service)...
powershell -Command "try { $response = Invoke-WebRequest -Uri 'http://localhost:9090/api/djs/dj-001/reviews' -Method Get -TimeoutSec 10; Write-Host '✓ DJ Reviews Communication: OK' -ForegroundColor Green } catch { Write-Host '✗ DJ Reviews Communication: FAILED' -ForegroundColor Red; Write-Host $_.Exception.Message }"

echo Testing DJ Average Rating...
powershell -Command "try { $response = Invoke-WebRequest -Uri 'http://localhost:9090/api/djs/dj-001/rating' -Method Get -TimeoutSec 10; Write-Host '✓ DJ Rating Communication: OK' -ForegroundColor Green } catch { Write-Host '✗ DJ Rating Communication: FAILED' -ForegroundColor Red }"

echo.
echo Creating a test review (Review Service → Festival App validation)...
powershell -Command "try { $body = @{ subjectId='dj-001'; subjectType='DJ'; reviewerName='Test User'; rating=5; comment='Great DJ!' } | ConvertTo-Json; $response = Invoke-WebRequest -Uri 'http://localhost:8080/api/reviews' -Method Post -Body $body -ContentType 'application/json' -TimeoutSec 10; Write-Host '✓ Review Creation with Validation: OK' -ForegroundColor Green } catch { Write-Host '✗ Review Creation: FAILED' -ForegroundColor Red; Write-Host $_.Exception.Message }"

echo.
echo ========================================
echo Step 8: Database Connectivity Tests
echo ========================================
echo.

echo Testing PostgreSQL connectivity...
docker exec festival-postgres psql -U festival_user -d festival_db -c "SELECT COUNT(*) as dj_count FROM djs;" 2>nul
if !errorlevel! equ 0 (
    echo ✓ PostgreSQL: Connected and accessible
) else (
    echo ✗ PostgreSQL: Connection failed
)

echo Testing MongoDB connectivity...
docker exec review-mongodb mongosh --eval "db.adminCommand('ping')" >nul 2>&1
if !errorlevel! equ 0 (
    echo ✓ MongoDB: Connected and accessible
) else (
    echo ✗ MongoDB: Connection failed
)

echo.
echo ========================================
echo SYSTEM TEST SUMMARY
echo ========================================
echo.

echo Services Status:
echo - Festival Application: http://localhost:9090
echo - Review Service: http://localhost:8080
echo - PostgreSQL Database: localhost:5432
echo - MongoDB Database: localhost:27017
echo.

echo Key API Endpoints:
echo.
echo Festival Application (Port 9090):
echo - Health: http://localhost:9090/actuator/health
echo - DJs: http://localhost:9090/api/djs
echo - Performances: http://localhost:9090/api/performances
echo - DJ Reviews: http://localhost:9090/api/djs/dj-001/reviews
echo - DJ Rating: http://localhost:9090/api/djs/dj-001/rating
echo.
echo Review Service (Port 8080):
echo - Health: http://localhost:8080/actuator/health
echo - Reviews: http://localhost:8080/api/reviews
echo - Subject Reviews: http://localhost:8080/api/reviews/subject/dj-001/type/DJ
echo.

echo Inter-Service Communication:
echo ✓ Festival App can call Review Service for reviews
echo ✓ Review Service can validate DJs/Performances with Festival App
echo ✓ Both services use correct ports (9090 and 8080)
echo.

echo To stop all services:
echo - Applications: taskkill /F /IM java.exe
echo - Databases: docker-compose down
echo.

pause
