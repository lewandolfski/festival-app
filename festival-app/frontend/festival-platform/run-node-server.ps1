# PowerShell script to run Node.js server commands in Podman container with port mapping
param(
    [Parameter(Mandatory=$true)]
    [string]$Command,
    [Parameter(Mandatory=$false)]
    [string]$Port = "4200"
)

$CurrentDir = Get-Location
$ContainerPath = "/workspace"

# Run Node.js command in Podman container with volume mount and port mapping
podman run -it --rm `
    -v "${CurrentDir}:${ContainerPath}" `
    -w $ContainerPath `
    -p "${Port}:${Port}" `
    --entrypoint="" `
    node:22-alpine `
    sh -c "export PATH=/usr/local/lib/node_modules/.bin:`$PATH && $Command"