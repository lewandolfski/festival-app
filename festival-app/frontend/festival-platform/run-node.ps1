# PowerShell script to run Node.js/Angular commands in Podman container
param(
    [Parameter(Mandatory=$true)]
    [string]$Command
)

$CurrentDir = Get-Location
$ContainerPath = "/workspace"

# Run Node.js command in Podman container with volume mount
podman run -it --rm `
    -v "${CurrentDir}:${ContainerPath}" `
    -w $ContainerPath `
    --entrypoint="" `
    node:22-alpine `
    sh -c "export PATH=/workspace/node_modules/.bin:`$PATH && $Command"