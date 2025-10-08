# Festival App - First Time Setup on New PC
# Run this ONCE after unzipping on your new PC

Write-Host "Festival App - First Time Setup" -ForegroundColor Cyan
Write-Host "================================`n" -ForegroundColor Cyan

# Check prerequisites
Write-Host "Checking prerequisites..." -ForegroundColor Yellow

# Check Java
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "  Java found" -ForegroundColor Green
} catch {
    Write-Host "  Java NOT found! Install Java 21 first" -ForegroundColor Red
    exit 1
}

# Check Node
try {
    $nodeVersion = node --version
    Write-Host "  Node.js found: $nodeVersion" -ForegroundColor Green
} catch {
    Write-Host "  Node.js NOT found! Install Node.js 20+ first" -ForegroundColor Red
    exit 1
}

# Check Docker
try {
    docker --version | Out-Null
    Write-Host "  Docker found" -ForegroundColor Green
} catch {
    Write-Host "  Docker NOT found! Install Docker Desktop first" -ForegroundColor Red
    exit 1
}

Write-Host "`nAll prerequisites OK!`n" -ForegroundColor Green

# Step 1: Install npm dependencies
Write-Host "1. Installing npm dependencies (5-10 minutes)..." -ForegroundColor Yellow
Write-Host "   This only happens once!" -ForegroundColor Gray
$frontendPath = Join-Path $PSScriptRoot "festival-app\frontend\festival-platform"

if (-not (Test-Path $frontendPath)) {
    Write-Host "  ERROR: Frontend path not found!" -ForegroundColor Red
    Write-Host "  Expected: $frontendPath" -ForegroundColor Yellow
    exit 1
}

Set-Location $frontendPath
npm install

if ($LASTEXITCODE -ne 0) {
    Write-Host "  npm install failed!" -ForegroundColor Red
    exit 1
}
Write-Host "  npm dependencies installed!" -ForegroundColor Green

# Step 2: Build shared library
Write-Host "`n2. Building shared library..." -ForegroundColor Yellow
npx ng build shared-lib

if ($LASTEXITCODE -ne 0) {
    Write-Host "  Shared library build failed!" -ForegroundColor Red
    exit 1
}
Write-Host "  Shared library built!" -ForegroundColor Green

# Step 3: Start Docker containers
Write-Host "`n3. Starting Docker containers..." -ForegroundColor Yellow
Set-Location (Join-Path $PSScriptRoot "festival-app\festival-app")
docker-compose up -d

Start-Sleep -Seconds 10

# Check if containers are running
$postgresRunning = docker ps --filter "name=festival-postgres" --filter "status=running" -q
$mongoRunning = docker ps --filter "name=review-mongodb" --filter "status=running" -q

if ($postgresRunning -and $mongoRunning) {
    Write-Host "  Databases started!" -ForegroundColor Green
} else {
    Write-Host "  Database startup failed!" -ForegroundColor Red
    exit 1
}

# Step 4: Load data
Write-Host "`n4. Loading data..." -ForegroundColor Yellow

# Load DJs
Get-Content "festival-app\src\main\resources\data-djs-top100.sql" | docker exec -i festival-postgres psql -U festival_user -d festival_db
Write-Host "  DJs loaded (100+)" -ForegroundColor Green

# Load Performances
Get-Content "festival-app\src\main\resources\data-performances-festivals.sql" | docker exec -i festival-postgres psql -U festival_user -d festival_db
Write-Host "  Performances loaded (50+)" -ForegroundColor Green

Write-Host "`nFirst time setup complete!" -ForegroundColor Green
Write-Host "`nNext steps:" -ForegroundColor Cyan
Write-Host "  1. Run: .\START_APP.ps1" -ForegroundColor White
Write-Host "  2. Wait 2-3 minutes for services to start" -ForegroundColor White
Write-Host "  3. Browser will open automatically at http://localhost:4200" -ForegroundColor White
Write-Host "`nPress any key to start the app now..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

# Start the app
Set-Location $PSScriptRoot
.\START_APP.ps1
