#Requires -Version 5.1
<#
.SYNOPSIS  Strips BOM from all Java files that were incorrectly written with UTF-8 BOM.
           Also rewrites them using UTF-8 without BOM going forward.
#>

$BackendSrc  = Join-Path $PSScriptRoot "src\main\java\com\plus33\erp"
$UTF8NoBOM   = New-Object System.Text.UTF8Encoding($false)  # UTF-8 without BOM
$fixed       = 0
$clean       = 0

Write-Host "Scanning Java files for BOM..." -ForegroundColor Cyan

$javaFiles = Get-ChildItem -Path $BackendSrc -Filter "*.java" -Recurse

foreach ($file in $javaFiles) {
    try {
        # Read raw bytes to detect BOM
        $bytes = [System.IO.File]::ReadAllBytes($file.FullName)

        # BOM for UTF-8 is: 0xEF 0xBB 0xBF
        if ($bytes.Length -ge 3 -and $bytes[0] -eq 0xEF -and $bytes[1] -eq 0xBB -and $bytes[2] -eq 0xBF) {
            # Strip BOM: read as string then rewrite without BOM
            $content = [System.Text.Encoding]::UTF8.GetString($bytes, 3, $bytes.Length - 3)
            [System.IO.File]::WriteAllText($file.FullName, $content, $UTF8NoBOM)
            $fixed++
        } else {
            $clean++
        }
    } catch {
        Write-Warning "FAIL $($file.Name): $_"
    }
}

Write-Host ""
Write-Host "Files with BOM fixed  : $fixed" -ForegroundColor Green
Write-Host "Files already clean   : $clean" -ForegroundColor Gray
Write-Host "Total Java files      : $($javaFiles.Count)" -ForegroundColor Cyan
Write-Host ""
Write-Host "BOM fix complete. Run: mvnw compile" -ForegroundColor Yellow
