Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Festival Platform - Project Cleanup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Removing unnecessary files for production deployment..." -ForegroundColor Yellow
Write-Host ""

# Remove unnecessary test files
Write-Host "Removing unnecessary test files..." -ForegroundColor Green
$testFilesToRemove = @(
    "festival-app\src\test\java\com\capgemini\festivalapplication\Exercise4VerificationTest.java",
    "festival-app\src\test\java\com\capgemini\festivalapplication\entity\DjTest.java",
    "festival-app\src\test\java\com\capgemini\festivalapplication\entity\PerformanceTest.java",
    "festival-app\src\test\java\com\capgemini\festivalapplication\mapper\DjMapperTest.java",
    "festival-app\src\test\java\com\capgemini\festivalapplication\mapper\PerformanceMapperTest.java",
    "festival-app\src\test\java\com\capgemini\festivalapplication\repository\DjRepositoryTest.java",
    "festival-app\src\test\java\com\capgemini\festivalapplication\repository\PerformanceRepositoryTest.java",
    "review-service\src\test\java\com\capgemini\reviewservice\controller\ReviewControllerTest.java"
)

foreach ($file in $testFilesToRemove) {
    if (Test-Path $file) {
        Remove-Item $file -Force
        Write-Host "  Removed: $file" -ForegroundColor Gray
    }
}

# Remove temporary batch files
Write-Host "Removing temporary batch files..." -ForegroundColor Green
$batchFilesToRemove = @(
    "quick-start.bat",
    "start-festival-app.bat", 
    "start-review-service.bat",
    "start-with-postgres.bat",
    "test-local-services.bat",
    "test-postgres-setup.bat",
    "test-spring-boot-run.bat",
    "verify-setup.bat",
    "start-microservices.bat",
    "festival-app\build-app.bat",
    "festival-app\docker-compose-postgres.bat",
    "festival-app\start-festival.bat",
    "festival-app\test-app.bat",
    "festival-app\verify-all.bat",
    "review-service\build-review-service.bat",
    "review-service\fix-java-env.bat",
    "review-service\refresh-ide-project.bat",
    "review-service\start-review-service.bat",
    "review-service\test-review-service.bat"
)

foreach ($file in $batchFilesToRemove) {
    if (Test-Path $file) {
        Remove-Item $file -Force
        Write-Host "  Removed: $file" -ForegroundColor Gray
    }
}

# Remove redundant documentation
Write-Host "Removing redundant documentation..." -ForegroundColor Green
$docsToRemove = @(
    "EXERCISE_6_MICROSERVICES_COMPLETE.md",
    "POSTGRESQL_SETUP_COMPLETE.md", 
    "VERIFICATION_SUMMARY.md",
    "festival-app\POSTGRESQL_MIGRATION_COMPLETE.md",
    "festival-app\WORKING_COMMANDS.md"
)

foreach ($file in $docsToRemove) {
    if (Test-Path $file) {
        Remove-Item $file -Force
        Write-Host "  Removed: $file" -ForegroundColor Gray
    }
}

# Remove development-specific files
Write-Host "Removing development-specific files..." -ForegroundColor Green
$devFilesToRemove = @(
    "festival-app\src\main\resources\application-dev.properties",
    "festival-app\src\main\resources\data-dev.sql"
)

foreach ($file in $devFilesToRemove) {
    if (Test-Path $file) {
        Remove-Item $file -Force
        Write-Host "  Removed: $file" -ForegroundColor Gray
    }
}

# Remove build artifacts
Write-Host "Removing build artifacts..." -ForegroundColor Green
if (Test-Path "festival-app\target") {
    Remove-Item "festival-app\target" -Recurse -Force
    Write-Host "  Removed: festival-app\target" -ForegroundColor Gray
}
if (Test-Path "review-service\target") {
    Remove-Item "review-service\target" -Recurse -Force
    Write-Host "  Removed: review-service\target" -ForegroundColor Gray
}

# Remove cleanup scripts
Remove-Item "cleanup-project.bat" -Force -ErrorAction SilentlyContinue
Remove-Item "cleanup-project.ps1" -Force -ErrorAction SilentlyContinue

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Cleanup Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Project is now clean and ready for Git!" -ForegroundColor Yellow
