# Festival App - Setup Script for New PC
# Run this after unzipping on your other PC

Write-Host "🎵 Festival App - Setup Wizard" -ForegroundColor Cyan
Write-Host "================================`n" -ForegroundColor Cyan

# Check prerequisites
Write-Host "📋 Checking prerequisites..." -ForegroundColor Yellow

# Check Java
try {
    $javaVersion = java -version 2>&1 | Select-String "version" | ForEach-Object { $_ -replace '.*version "([^"]*)".*', '$1' }
    Write-Host "  ✅ Java found: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "  ❌ Java not found! Please install Java 21" -ForegroundColor Red
    exit 1
}

# Check Node
try {
    $nodeVersion = node --version
    Write-Host "  ✅ Node.js found: $nodeVersion" -ForegroundColor Green
} catch {
    Write-Host "  ❌ Node.js not found! Please install Node.js 20+" -ForegroundColor Red
    exit 1
}

# Check Docker
try {
    docker --version | Out-Null
    Write-Host "  ✅ Docker found" -ForegroundColor Green
} catch {
    Write-Host "  ⚠️  Docker not found - you'll need to install it for databases" -ForegroundColor Yellow
}

Write-Host "`n📦 Installing frontend dependencies..." -ForegroundColor Yellow
Write-Host "This may take 5-10 minutes...`n" -ForegroundColor Gray

$frontendPath = Join-Path $PSScriptRoot "festival-app\frontend\festival-platform"

if (Test-Path $frontendPath) {
    Set-Location $frontendPath
    npm install
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "`n✅ Frontend dependencies installed!" -ForegroundColor Green
    } else {
        Write-Host "`n❌ npm install failed!" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "❌ Frontend path not found: $frontendPath" -ForegroundColor Red
    exit 1
}

Write-Host "`n🎉 Setup complete!" -ForegroundColor Green
Write-Host "`n📚 Next steps:" -ForegroundColor Cyan
Write-Host "  1. Start Docker Desktop" -ForegroundColor White
Write-Host "  2. Run: docker-compose up -d (in festival-app\festival-app folder)" -ForegroundColor White
Write-Host "  3. Load data using commands in TRANSFER_GUIDE.md" -ForegroundColor White
Write-Host "  4. Start backend services (see TRANSFER_GUIDE.md)" -ForegroundColor White
Write-Host "  5. Run: npm start (in this folder)" -ForegroundColor White
Write-Host "`n  Access at: http://localhost:4200" -ForegroundColor Yellow

Write-Host "`n📖 For detailed instructions, see TRANSFER_GUIDE.md" -ForegroundColor Cyan
