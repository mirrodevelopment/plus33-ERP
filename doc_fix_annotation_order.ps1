#Requires -Version 5.1
<#
.SYNOPSIS
    Fixes Java files where a JavaDoc block is positioned AFTER a method annotation
    instead of BEFORE it.

    Correct Java/Javadoc convention:
        /** JavaDoc comment */   <-- 1st
        @Transactional           <-- 2nd
        @Override                <-- 3rd
        public void method() {}  <-- 4th

    This script detects and corrects the wrong ordering:
        @Transactional           <-- was inserted before JavaDoc
        /** JavaDoc comment */
        public void method() {}

    SAFETY: Only reorders comment blocks. Does NOT change any business logic.
#>

$BackendSrc = Join-Path $PSScriptRoot "src\main\java\com\plus33\erp"
$UTF8NoBOM  = New-Object System.Text.UTF8Encoding($false)
$fixedFiles = 0
$cleanFiles = 0
$failedFiles= 0

Write-Host "`n=========================================================" -ForegroundColor Cyan
Write-Host "  JavaDoc Placement Fix (annotations -> before JavaDoc)  " -ForegroundColor Cyan
Write-Host "=========================================================" -ForegroundColor Cyan

function Fix-File($filePath) {
    try {
        $lines  = [IO.File]::ReadAllLines($filePath, $UTF8NoBOM)
        $out    = New-Object System.Collections.Generic.List[string]
        $changed = $false

        $i = 0
        while ($i -lt $lines.Count) {
            $line    = $lines[$i]
            $trimmed = $line.TrimStart()

            # Detect: one or more @Annotation lines at current position
            # followed by a /** ... */ JavaDoc block
            # followed by a public/protected/private method declaration
            # --> reorder to: JavaDoc first, then annotations, then method

            if ($trimmed.StartsWith('@')) {
                # Collect consecutive annotation lines
                $annotations = New-Object System.Collections.Generic.List[string]
                $j = $i
                while ($j -lt $lines.Count -and $lines[$j].TrimStart().StartsWith('@')) {
                    $annotations.Add($lines[$j])
                    $j++
                }

                # Skip blank lines between annotations and possible JavaDoc
                while ($j -lt $lines.Count -and $lines[$j].Trim() -eq '') { $j++ }

                # Check if next block is a JavaDoc /** ... */
                if ($j -lt $lines.Count -and $lines[$j].TrimStart().StartsWith('/**')) {
                    # Collect the JavaDoc block
                    $javadoc = New-Object System.Collections.Generic.List[string]
                    while ($j -lt $lines.Count) {
                        $javadoc.Add($lines[$j])
                        if ($lines[$j].TrimStart().Contains('*/')) { $j++; break }
                        $j++
                    }

                    # Skip blanks
                    while ($j -lt $lines.Count -and $lines[$j].Trim() -eq '') { $j++ }

                    # Check if next is a method/class declaration
                    if ($j -lt $lines.Count) {
                        $nextT = $lines[$j].TrimStart()
                        $isMethod = $nextT -match '^(public|protected|private)\s+' -and $nextT -match '\w+\s*\('
                        $isClass  = $nextT -match '^(public|protected|private|abstract|final)?\s*(class|interface|enum|record)\s+\w+'

                        if ($isMethod -or $isClass) {
                            # REORDER: JavaDoc → Annotations → Declaration
                            foreach ($jl in $javadoc)     { $out.Add($jl) }
                            foreach ($al in $annotations) { $out.Add($al) }
                            # Continue from $j (method line processed in next iteration)
                            $i = $j
                            $changed = $true
                            continue
                        }
                    }

                    # Not followed by a method - emit as-is
                    foreach ($al in $annotations) { $out.Add($al) }
                    foreach ($jl in $javadoc)     { $out.Add($jl) }
                    $i = $j
                    continue
                }

                # No JavaDoc follows - emit annotations as-is
                foreach ($al in $annotations) { $out.Add($al) }
                $i = $j
                continue
            }

            $out.Add($line)
            $i++
        }

        if ($changed) {
            [IO.File]::WriteAllText($filePath, ($out -join "`r`n"), $UTF8NoBOM)
            $script:fixedFiles++
            return $true
        }
        $script:cleanFiles++
        return $false
    } catch {
        Write-Warning ("  FAIL " + [IO.Path]::GetFileName($filePath) + ": " + $_)
        $script:failedFiles++
        return $false
    }
}

$javaFiles = Get-ChildItem -Path $BackendSrc -Filter "*.java" -Recurse
Write-Host ("  Scanning " + $javaFiles.Count + " Java files...") -ForegroundColor Gray

$done = 0
foreach ($file in $javaFiles) {
    Fix-File $file.FullName | Out-Null
    $done++
    if ($done % 500 -eq 0) {
        Write-Host ("  " + $done + " / " + $javaFiles.Count + "...") -ForegroundColor Gray
    }
}

Write-Host ""
Write-Host ("  Placement corrected : " + $fixedFiles)  -ForegroundColor Green
Write-Host ("  Already correct     : " + $cleanFiles)  -ForegroundColor Gray
Write-Host ("  Failed              : " + $failedFiles) -ForegroundColor Red
Write-Host ""
Write-Host "  Run: mvnw compile" -ForegroundColor Yellow
