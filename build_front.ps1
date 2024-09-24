$path = Get-Location
Set-Location "../../WebstormProjects/vpn-game-changer-front"

yarn install
yarn build

if (Test-Path "./dist/spa/") {
    Copy-Item -Force -Recurse "./dist/spa/*" "$path/src/main/resources/static"
} else {
    Write-Error "Building error"
    exit 1
}
Set-Location $path