@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Festival Platform - Build and Deploy
echo ========================================
echo.

REM Set correct JAVA_HOME for this session
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
echo Setting JAVA_HOME to: %JAVA_HOME%
echo.

REM Check if Docker is running
echo Checking Docker status...
docker --version >nul 2>&1
if !errorlevel! neq 0 (
    echo ERROR: Docker is not installed or not running
    echo Please install Docker Desktop and ensure it's running
    pause
    exit /b 1
)

echo Docker is available
echo.

REM Clean previous builds
echo Step 1: Cleaning previous builds...
if exist "festival-app\target" rmdir /s /q "festival-app\target"
if exist "review-service\target" rmdir /s /q "review-service\target"
echo.

REM Build Festival Application
echo Step 2: Building Festival Application...
cd festival-app
call ..\mvnw.cmd clean package -DskipTests
if !errorlevel! neq 0 (
    echo ERROR: Failed to build Festival Application
    cd ..
    pause
    exit /b 1
)
cd ..
echo Festival Application built successfully
echo.

REM Build Review Service
echo Step 3: Building Review Service...
cd review-service
call ..\mvnw.cmd clean package -DskipTests
if !errorlevel! neq 0 (
    echo ERROR: Failed to build Review Service
    cd ..
    pause
    exit /b 1
)
cd ..
echo Review Service built successfully
echo.

REM Stop existing containers
echo Step 4: Stopping existing containers...
docker-compose down -v
echo.

REM Build Docker images
echo Step 5: Building Docker images...
docker-compose build --no-cache
if !errorlevel! neq 0 (
    echo ERROR: Failed to build Docker images
    pause
    exit /b 1
)
echo Docker images built successfully
echo.

REM Start the platform
echo Step 6: Starting Festival Platform...
docker-compose up -d
if !errorlevel! neq 0 (
    echo ERROR: Failed to start containers
    pause
    exit /b 1
)
echo.

REM Wait for services to be ready
echo Step 7: Waiting for services to be ready...
timeout /t 30 /nobreak >nul

REM Check service health
echo Step 8: Checking service health...
echo.

echo Checking Festival Application health...
curl -f http://localhost:8080/actuator/health 2>nul
if !errorlevel! equ 0 (
    echo ✓ Festival Application is healthy
) else (
    echo ✗ Festival Application health check failed
)

echo.
echo Checking Review Service health...
curl -f http://localhost:8081/actuator/health 2>nul
if !errorlevel! equ 0 (
    echo ✓ Review Service is healthy
) else (
    echo ✗ Review Service health check failed
)

echo.
echo ========================================
echo Deployment Complete!
echo ========================================
echo.
echo Services are running on:
echo - Festival Application: http://localhost:8080
echo - Review Service: http://localhost:8081
echo.
echo API Endpoints:
echo - Festival DJs: http://localhost:8080/api/djs
echo - Festival Performances: http://localhost:8080/api/performances
echo - Reviews: http://localhost:8081/api/reviews
echo.
echo To view logs: docker-compose logs -f
echo To stop services: docker-compose down
echo.

pause
