#Requires -Version 5.1
<#
.SYNOPSIS  Applies enterprise headers to all frontend JavaScript files.
           Run after doc_automation.ps1 if JS phase failed.
#>

$FrontendDir    = Join-Path $PSScriptRoot "frontend"
$ProjectVersion = "0.0.1-SNAPSHOT"
$UTF8NoBOM      = New-Object System.Text.UTF8Encoding($false)  # No BOM
$count   = 0
$skipped = 0
$failed  = 0

function Has-Header($content) { return $content -match "PLUS33 Coffee ERP" }

$jsFiles = Get-ChildItem -Path $FrontendDir -Filter "*.js" -Recurse |
    Where-Object { $_.FullName -notmatch '\\node_modules\\' }

Write-Host "Found $($jsFiles.Count) JS files" -ForegroundColor Cyan

foreach ($file in $jsFiles) {
    try {
        $raw = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)
        if (Has-Header $raw) { $skipped++; continue }

        # Derive relative path and directory name
        $relPath    = $file.FullName -replace [regex]::Escape($FrontendDir + '\'), ''
        $parts      = $relPath -split '[/\\]'
        $dirName    = if ($parts.Count -ge 2) { $parts[0] } else { 'core' }
        $dirClean   = [string]($dirName -replace '-', ' ')
        $modName    = (Get-Culture).TextInfo.ToTitleCase($dirClean) + ' Module'

        # Detect file type
        $isPage   = $relPath -match 'pages[/\\]'
        $isSvc    = ($relPath -match 'services[/\\]') -or ($file.BaseName -match 'Service')
        $isStore  = ($relPath -match 'store[/\\]')    -or ($file.BaseName -match 'Store')
        $isRouter = $file.BaseName -match 'router|routes'
        $isCore   = $relPath -match 'core[/\\]|boot[/\\]|app[/\\]'
        $isAPI    = $relPath -match 'api[/\\]'

        $purpose = if     ($isPage)   { "Frontend page component for the $modName UI" }
                   elseif ($isSvc)    { "Frontend service wrapping backend REST APIs for $modName" }
                   elseif ($isStore)  { "Frontend state store managing $modName UI state" }
                   elseif ($isRouter) { "Client-side hash-based router defining application navigation" }
                   elseif ($isCore)   { "Core frontend infrastructure: $($file.BaseName)" }
                   elseif ($isAPI)    { "HTTP API client wrapper for backend communication" }
                   else               { "Frontend utility: $($file.BaseName) for PLUS33 Coffee ERP" }

        # Detect API calls
        $apiPattern = 'apiClient\.(get|post|put|patch|delete)\s*\(\s*[''"]([^''"]+)[''"]'
        $apiCalls   = [regex]::Matches($raw, $apiPattern)
        $relAPIs    = if ($apiCalls.Count -gt 0) {
            ($apiCalls | Select-Object -First 4 | ForEach-Object {
                $_.Groups[1].Value.ToUpper() + ' ' + $_.Groups[2].Value
            }) -join ', '
        } else { 'N/A' }

        # Detect imports
        $impPattern = "from\s+'([^']+)'"
        $importMts  = [regex]::Matches($raw, $impPattern)
        $imports    = ($importMts | ForEach-Object {
            $_.Groups[1].Value -replace '\.\./|\./', '' -replace '\.js$', ''
        } | Select-Object -Unique -First 5) -join ', '

        $relPathFwd = $relPath -replace '\\', '/'

        $header = "/******************************************************************************`r`n"
        $header += " * Project           : PLUS33 Coffee ERP`r`n"
        $header += " * Developed By      : Haulo`r`n"
        $header += " * Developed For     : PLUS33 Coffee`r`n"
        $header += " * Developer         : Sivasurya`r`n"
        $header += " *`r`n"
        $header += " * Module            : $modName`r`n"
        $header += " * File              : $($file.Name)`r`n"
        $header += " * Path              : frontend/$relPathFwd`r`n"
        $header += " * Purpose           : $purpose`r`n"
        $header += " * Version           : $ProjectVersion`r`n"
        $header += " *`r`n"
        $header += " * Related API       : $relAPIs`r`n"
        $header += " * Related CSS       : theme/variables.css, theme/coffee-dark.css`r`n"
        $header += " * Related HTML      : index.html`r`n"
        $header += " * Imports           : $imports`r`n"
        $header += " * Depends On        : $imports`r`n"
        $header += " *`r`n"
        $header += " * Description`r`n"
        $header += " * ---------------------------------------------------------------------------`r`n"
        $header += " * $purpose. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based`r`n"
        $header += " * routing, JWT authentication, and a premium glassmorphism design system.`r`n"
        $header += " ******************************************************************************/`r`n`r`n"

        [System.IO.File]::WriteAllText($file.FullName, $header + $raw, $UTF8NoBOM)
        $count++
    } catch {
        Write-Warning "FAIL $($file.Name): $_"
        $failed++
    }
}

Write-Host ""
Write-Host "JS headers added  : $count"  -ForegroundColor Green
Write-Host "Already documented: $skipped" -ForegroundColor Yellow
Write-Host "Failed            : $failed"  -ForegroundColor Red
Write-Host "Total JS files    : $($jsFiles.Count)" -ForegroundColor Cyan
