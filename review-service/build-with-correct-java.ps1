# PowerShell script to build the review service with correct JAVA_HOME
Write-Host "========================================" -ForegroundColor Green
Write-Host "Review Service Build Script" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Set the correct JAVA_HOME for this session
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

Write-Host "Setting JAVA_HOME to: $env:JAVA_HOME" -ForegroundColor Yellow
Write-Host ""

# Verify Java installation
if (Test-Path "$env:JAVA_HOME\bin\javac.exe") {
    Write-Host "✓ Java compiler found at: $env:JAVA_HOME\bin\javac.exe" -ForegroundColor Green
} else {
    Write-Host "✗ Java compiler not found. Please check your Java installation." -ForegroundColor Red
    exit 1
}

# Run Maven build
Write-Host "Running Maven build..." -ForegroundColor Yellow
Write-Host ""

try {
    & .\mvnw.cmd clean compile
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "========================================" -ForegroundColor Green
        Write-Host "BUILD SUCCESSFUL!" -ForegroundColor Green
        Write-Host "========================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "The ReviewMapper.java file should now be recognized by your IDE." -ForegroundColor Cyan
        Write-Host "If it's still not recognized, please restart your IDE." -ForegroundColor Cyan
    } else {
        Write-Host "Build failed with exit code: $LASTEXITCODE" -ForegroundColor Red
    }
} catch {
    Write-Host "Error running Maven build: $_" -ForegroundColor Red
}

Write-Host ""
Write-Host "To make JAVA_HOME permanent, run the fix-java-env.bat script." -ForegroundColor Yellow
