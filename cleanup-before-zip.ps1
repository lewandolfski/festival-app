# Festival App - Cleanup Script for Transfer
# Run this before creating ZIP for transfer to another PC

Write-Host "Starting cleanup for transfer..." -ForegroundColor Cyan
Write-Host "This will remove build artifacts but keep source code" -ForegroundColor Gray

# Remove node_modules folders
Write-Host "`nRemoving node_modules folders..." -ForegroundColor Yellow
Write-Host "(Will be reinstalled on new PC with npm install)" -ForegroundColor Gray
Get-ChildItem -Path . -Recurse -Directory -Filter "node_modules" -ErrorAction SilentlyContinue | ForEach-Object {
    Write-Host "  Removing: $($_.FullName)" -ForegroundColor Gray
    Remove-Item $_.FullName -Recurse -Force -ErrorAction SilentlyContinue
}

# Remove Maven target folders
Write-Host "Removing Maven target folders..." -ForegroundColor Yellow
Get-ChildItem -Path . -Recurse -Directory -Filter "target" -ErrorAction SilentlyContinue | ForEach-Object {
    Write-Host "  Removing: $($_.FullName)" -ForegroundColor Gray
    Remove-Item $_.FullName -Recurse -Force -ErrorAction SilentlyContinue
}

# Remove Angular build artifacts
Write-Host "Removing Angular build artifacts..." -ForegroundColor Yellow
Get-ChildItem -Path . -Recurse -Directory -Filter ".angular" -ErrorAction SilentlyContinue | ForEach-Object {
    Write-Host "  Removing: $($_.FullName)" -ForegroundColor Gray
    Remove-Item $_.FullName -Recurse -Force -ErrorAction SilentlyContinue
}

Get-ChildItem -Path . -Recurse -Directory -Filter "dist" -ErrorAction SilentlyContinue | ForEach-Object {
    Write-Host "  Removing: $($_.FullName)" -ForegroundColor Gray
    Remove-Item $_.FullName -Recurse -Force -ErrorAction SilentlyContinue
}

# Remove IDE files
Write-Host "Removing IDE files..." -ForegroundColor Yellow
Remove-Item -Path ".vscode" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path ".idea" -Recurse -Force -ErrorAction SilentlyContinue

# Remove log files
Write-Host "Removing log files..." -ForegroundColor Yellow
Get-ChildItem -Path . -Recurse -Filter "*.log" -ErrorAction SilentlyContinue | ForEach-Object {
    Write-Host "  Removing: $($_.FullName)" -ForegroundColor Gray
    Remove-Item $_.FullName -Force -ErrorAction SilentlyContinue
}

Write-Host "Cleanup complete!" -ForegroundColor Green
Write-Host "Project is ready for git commit." -ForegroundColor Cyan
