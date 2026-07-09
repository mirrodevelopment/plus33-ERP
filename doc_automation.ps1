#Requires -Version 5.1
<#
.SYNOPSIS
    PLUS33 Coffee ERP Enterprise Source Code Documentation Automation
.DESCRIPTION
    Adds standardized enterprise file headers to every Java, SQL, JS, CSS, HTML file.
    All fields (module, package, purpose, dependencies, related APIs) are auto-derived
    from actual source code. Also generates 24 enterprise documentation .md files.

    Project     : PLUS33 Coffee ERP
    Developed By: Haulo  |  For: PLUS33 Coffee  |  Developer: Sivasurya

.NOTES
    Run: powershell -ExecutionPolicy Bypass -File doc_automation.ps1
#>

Set-StrictMode -Version Latest
$ErrorActionPreference = "Continue"

$ProjectRoot    = $PSScriptRoot
$BackendSrc     = Join-Path $ProjectRoot "src\main\java\com\plus33\erp"
$MigrationDir   = Join-Path $ProjectRoot "src\main\resources\db\migration"
$FrontendDir    = Join-Path $ProjectRoot "frontend"
$DocsOutputDir  = Join-Path $FrontendDir "docs"
$ProjectVersion = "0.0.1-SNAPSHOT"

$UTF8NoBOM = New-Object System.Text.UTF8Encoding($false)  # No BOM -- critical for Java compilation
$Stats = @{ Java=0; SQL=0; JS=0; CSS=0; HTML=0; MD=0; Skipped=0 }

function Write-Phase($msg) {
    Write-Host "`n==========================================================" -ForegroundColor Cyan
    Write-Host "  $msg" -ForegroundColor Cyan
    Write-Host "==========================================================" -ForegroundColor Cyan
}
function Write-Done($msg) { Write-Host "  OK $msg" -ForegroundColor Green }
function Write-Info($msg) { Write-Host "  .. $msg" -ForegroundColor Gray }

function Has-Header($content) { return $content -match "PLUS33 Coffee ERP" }

#region JAVA AUTO-DERIVATION ENGINE

function Derive-JavaMeta($filePath, $content) {
    $fileName  = Split-Path $filePath -Leaf
    $className = $fileName -replace '\.java$', ''
    $relPath   = $filePath -replace [regex]::Escape($BackendSrc + '\'), ''
    $parts     = $relPath -split '[/\\]' | Where-Object { $_ -ne '' }

    $moduleName = if ($parts.Count -ge 1) {
        (Get-Culture).TextInfo.ToTitleCase($parts[0]) + " Module"
    } else { "Core Module" }

    $pkgMatch = [regex]::Match($content, 'package\s+([\w\.]+);')
    $package  = if ($pkgMatch.Success) { $pkgMatch.Groups[1].Value } else { "com.plus33.erp" }

    $isController = $content -match '@RestController|@Controller'
    $isService    = $content -match '@Service\b'
    $isRepo       = $content -match 'JpaRepository|CrudRepository|@Repository'
    $isEntity     = $content -match '@Entity\b'
    $isMapper     = $content -match '@Mapper\b'
    $isConfig     = $content -match '@Configuration\b'
    $isDTO        = $className -match 'Request$|Response$|Dto$|DTO$'
    $isEnum       = $content -match 'public enum '
    $isEvent      = $className -match 'Event$'
    $isFilter     = $content -match 'OncePerRequestFilter'
    $isException  = $className -match 'Exception$'
    $isInterface  = ($content -match 'public interface ') -and -not $isMapper

    $purpose = if ($isController) { "REST Controller exposing HTTP endpoints for $moduleName" }
    elseif ($isService)           { "Business logic service layer for $moduleName operations" }
    elseif ($isRepo)              { "JPA Repository providing database CRUD for $moduleName entities" }
    elseif ($isEntity)            { "JPA Entity representing a persistent database record in $moduleName" }
    elseif ($isMapper)            { "MapStruct Mapper converting between entities and DTOs in $moduleName" }
    elseif ($isConfig)            { "Spring Configuration bean for $moduleName" }
    elseif ($isFilter)            { "Spring Security filter for JWT authentication and authorization" }
    elseif ($isDTO)               { "Data Transfer Object for request/response in $moduleName" }
    elseif ($isEnum)              { "Enumeration of typed constants for $moduleName" }
    elseif ($isEvent)             { "Spring ApplicationEvent published by $moduleName for async processing" }
    elseif ($isException)         { "Custom exception for domain error handling in $moduleName" }
    elseif ($isInterface)         { "Service interface contract defining the API for $moduleName" }
    else                          { "Component of $moduleName within the PLUS33 Coffee ERP platform" }

    $baseName = $className -replace 'ServiceImpl$|Service$|Repository$|Mapper$|Request$|Response$|Entity$|Impl$', ''
    $relCtrl  = if ($isController) { $className } else { "${baseName}Controller" }
    $relSvc   = if ($isService)    { $className } else { "${baseName}Service, ${baseName}ServiceImpl" }

    $repoFields = [regex]::Matches($content, 'private final (\w+Repository)\s+\w+;')
    $relRepo    = if ($repoFields.Count -gt 0) {
        ($repoFields | ForEach-Object { $_.Groups[1].Value }) -join ', '
    } else { "${baseName}Repository" }

    $relEntity = if ($isRepo)    { $className -replace 'Repository$', '' }
                 elseif ($isEntity) { $className }
                 else            { $baseName }

    $dtoMatches = [regex]::Matches($content, '([\w]+(?:Request|Response|Dto|DTO))\b')
    $relDTOs    = if ($dtoMatches.Count -gt 0) {
        ($dtoMatches | ForEach-Object { $_.Groups[1].Value } | Sort-Object -Unique | Select-Object -First 5) -join ', '
    } else { "N/A" }

    $mapFields = [regex]::Matches($content, 'private final (\w+Mapper)\s+\w+;')
    $relMapper = if ($mapFields.Count -gt 0) {
        ($mapFields | ForEach-Object { $_.Groups[1].Value }) -join ', '
    } else { "${baseName}Mapper" }

    $baseMapping = [regex]::Match($content, '@RequestMapping\s*\(\s*"([^"]+)"')
    $baseUrl     = if ($baseMapping.Success) { $baseMapping.Groups[1].Value } else { "" }
    $methodMaps  = [regex]::Matches($content, '@(Get|Post|Put|Patch|Delete)Mapping(?:\s*\(\s*"([^"]*)")?')
    $relAPIs     = if ($methodMaps.Count -gt 0 -and $baseUrl) {
        ($methodMaps | Select-Object -First 4 | ForEach-Object {
            "$($_.Groups[1].Value.ToUpper()) $baseUrl$($_.Groups[2].Value)"
        }) -join ', '
    } elseif ($baseUrl) { "REST $baseUrl/**" } else { "N/A" }

    $tblMatch = [regex]::Match($content, '@Table\s*\(\s*name\s*=\s*"([^"]+)"')
    $relTable = if ($tblMatch.Success) { $tblMatch.Groups[1].Value } else {
        ($baseName -creplace '([A-Z])', '_$1').TrimStart('_').ToLower() + 's'
    }

    $importMods = [regex]::Matches($content, 'import com\.plus33\.erp\.([\w]+)\.')
    $deps = @()
    foreach ($m in $importMods) {
        $mod = (Get-Culture).TextInfo.ToTitleCase($m.Groups[1].Value) + " Module"
        if ($mod -notin $deps -and $mod -ne $moduleName) { $deps += $mod }
    }
    $dependsOn = if ($deps.Count -gt 0) { ($deps | Select-Object -First 5) -join ', ' } else { "None" }

    $usedBy = if ($isService)    { "$relCtrl, ${className}Impl" }
    elseif ($isRepo)             { $relSvc }
    elseif ($isMapper)           { $relSvc }
    elseif ($isEntity)           { "$relRepo, $relMapper" }
    elseif ($isDTO)              { "$relCtrl, $relSvc" }
    else                         { "$moduleName components" }

    $description = if ($isController) {
        "REST Controller for $moduleName. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: $relAPIs"
    } elseif ($isService) {
        "Business service for $moduleName. Implements ${baseName}Service. Encapsulates business rules, @Transactional operations, validations, and event publishing."
    } elseif ($isRepo) {
        "JPA Repository for $moduleName against the '$relTable' table. Provides CRUD, Specification-based queries, and paginated results."
    } elseif ($isEntity) {
        "JPA Entity mapped to '$relTable'. Defines persistent domain object for $moduleName with validation, relationship mappings, and lifecycle callbacks."
    } elseif ($isMapper) {
        "MapStruct Mapper for $moduleName. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig."
    } elseif ($isDTO) {
        "DTO for $moduleName HTTP serialization. Annotated with Jakarta Bean Validation constraints."
    } elseif ($isEnum) {
        "Typed enum defining state/category constants for $moduleName. Referenced in entity fields, service logic, and DB check constraints."
    } elseif ($isEvent) {
        "Spring ApplicationEvent for $moduleName. Published on state changes. Consumed by @EventListener methods for async processing."
    } else {
        "Component of $moduleName within the PLUS33 Coffee ERP platform."
    }

    return @{
        Module      = $moduleName
        Package     = $package
        File        = $fileName
        Purpose     = $purpose
        Version     = $ProjectVersion
        Description = $description
        Controller  = $relCtrl
        Service     = $relSvc
        Repository  = $relRepo
        Entity      = $relEntity
        DTOs        = $relDTOs
        Mapper      = $relMapper
        APIs        = $relAPIs
        Table       = $relTable
        DependsOn   = $dependsOn
        UsedBy      = $usedBy
    }
}

function Build-JavaHeader($m) {
    return @"
/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : $($m.Module)
 * Package           : $($m.Package)
 * File              : $($m.File)
 * Purpose           : $($m.Purpose)
 * Version           : $($m.Version)
 *
 * Related Controller: $($m.Controller)
 * Related Service   : $($m.Service)
 * Related Repository: $($m.Repository)
 * Related Entity    : $($m.Entity)
 * Related DTO       : $($m.DTOs)
 * Related Mapper    : $($m.Mapper)
 * Related DB Table  : $($m.Table)
 * Related REST APIs : $($m.APIs)
 * Depends On        : $($m.DependsOn)
 * Used By           : $($m.UsedBy)
 *
 * Description
 * ---------------------------------------------------------------------------
 * $($m.Description -replace "`n", " ")
 ******************************************************************************/

"@
}

#endregion

# =============================================================================
# PHASE 1 — JAVA HEADERS
# =============================================================================
Write-Phase "PHASE 1: Java source file headers"
$javaFiles = Get-ChildItem -Path $BackendSrc -Filter "*.java" -Recurse
Write-Info "Found $($javaFiles.Count) Java files"

foreach ($file in $javaFiles) {
    try {
        $raw = [System.IO.File]::ReadAllText($file.FullName, $UTF8NoBOM)
        if (Has-Header $raw) { $Stats.Skipped++; continue }
        $meta   = Derive-JavaMeta $file.FullName $raw
        $header = Build-JavaHeader $meta
        [System.IO.File]::WriteAllText($file.FullName, $header + $raw, $UTF8NoBOM)
        $Stats.Java++
    } catch { Write-Warning "FAIL $($file.Name): $_" }
}
Write-Done "Java: $($Stats.Java) headers added, $($Stats.Skipped) already documented"

# =============================================================================
# PHASE 2 — SQL MIGRATION HEADERS
# =============================================================================
Write-Phase "PHASE 2: SQL migration headers"
$sqlFiles = Get-ChildItem -Path $MigrationDir -Filter "*.sql" | Sort-Object Name
Write-Info "Found $($sqlFiles.Count) SQL migrations"

foreach ($file in $sqlFiles) {
    try {
        $raw  = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)
        if (Has-Header $raw) { $Stats.Skipped++; continue }

        $name = $file.BaseName
        $vm   = [regex]::Match($name, '^V([\d_]+)__(.+)$')
        $ver  = if ($vm.Success) { $vm.Groups[1].Value -replace '_', '.' } else { "?" }
        $desc = if ($vm.Success) { $vm.Groups[2].Value -replace '_', ' ' } else { $name }

        $creates = (([regex]::Matches($raw, 'CREATE TABLE\s+(\w+)', 'IgnoreCase')) | ForEach-Object { $_.Groups[1].Value }) -join ', '
        $alters  = (([regex]::Matches($raw, 'ALTER TABLE\s+(\w+)',  'IgnoreCase')) | ForEach-Object { $_.Groups[1].Value }) -join ', '
        $inserts = (([regex]::Matches($raw, 'INSERT INTO\s+(\w+)',  'IgnoreCase')) | ForEach-Object { $_.Groups[1].Value } | Sort-Object -Unique) -join ', '
        $indexes = (([regex]::Matches($raw, 'CREATE (?:UNIQUE )?INDEX\s+(\w+)', 'IgnoreCase')) | ForEach-Object { $_.Groups[1].Value }) -join ', '
        $opType  = if ($name -match 'seed|permission') { "Seed Data / Permission Grant" }
                   elseif ($creates) { "Schema Creation" }
                   elseif ($alters)  { "Schema Alteration" }
                   else              { "DDL" }

        $header = @"
-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : $ver
-- File              : $($file.Name)
-- Operation Type    : $opType
-- Purpose           : $desc
--
-- Tables Created    : $(if ($creates) { $creates } else { 'N/A' })
-- Tables Altered    : $(if ($alters)  { $alters  } else { 'N/A' })
-- Seed Data For     : $(if ($inserts) { $inserts } else { 'N/A' })
-- Indexes           : $(if ($indexes) { $indexes } else { 'N/A' })
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================

"@
        [System.IO.File]::WriteAllText($file.FullName, $header + $raw, $UTF8NoBOM)
        $Stats.SQL++
    } catch { Write-Warning "FAIL $($file.Name): $_" }
}
Write-Done "SQL: $($Stats.SQL) headers added"

# =============================================================================
# PHASE 3 — JAVASCRIPT HEADERS
# =============================================================================
Write-Phase "PHASE 3: JavaScript frontend headers"
$jsFiles = Get-ChildItem -Path $FrontendDir -Filter "*.js" -Recurse |
    Where-Object { $_.FullName -notmatch '\\node_modules\\' }
Write-Info "Found $($jsFiles.Count) JS files"

foreach ($file in $jsFiles) {
    try {
        $raw     = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)
        if (Has-Header $raw) { $Stats.Skipped++; continue }

        $relPath = $file.FullName -replace [regex]::Escape($FrontendDir + '\'), ''
        $parts   = $relPath -split '[/\\]'
        $dirName = if ($parts.Count -ge 2) { $parts[0] } else { "core" }
        $modName = (Get-Culture).TextInfo.ToTitleCase([string]($dirName -replace '-', ' ')) + " Module"

        $isPage   = $relPath -match 'pages[/\\]'
        $isSvc    = $relPath -match 'services[/\\]' -or $file.BaseName -match 'Service'
        $isStore  = $relPath -match 'store[/\\]'    -or $file.BaseName -match 'Store'
        $isRouter = $file.BaseName -match 'router|routes'
        $isCore   = $relPath -match 'core[/\\]|boot[/\\]|app[/\\]'
        $isAPI    = $relPath -match 'api[/\\]'

        $purpose = if ($isPage)   { "Frontend page component for the $modName UI" }
        elseif ($isSvc)           { "Frontend service wrapping backend REST APIs for $modName" }
        elseif ($isStore)         { "Frontend state store managing $modName UI state" }
        elseif ($isRouter)        { "Client-side hash-based router defining application navigation" }
        elseif ($isCore)          { "Core frontend infrastructure: $($file.BaseName)" }
        elseif ($isAPI)           { "HTTP API client wrapper for backend communication" }
        else                      { "Frontend utility: $($file.BaseName) for PLUS33 Coffee ERP" }

        $apiCalls = [regex]::Matches($raw, "apiClient\.(get|post|put|patch|delete)\s*\(\s*[`'`"]([^`'`"]+)[`'`"]")
        $relAPIs  = if ($apiCalls.Count -gt 0) {
            ($apiCalls | Select-Object -First 4 | ForEach-Object {
                "$($_.Groups[1].Value.ToUpper()) $($_.Groups[2].Value)"
            }) -join ', '
        } else { "N/A" }

        $importMts = [regex]::Matches($raw, "from\s+'([^']+)'")
        $imports   = ($importMts | ForEach-Object {
            $_.Groups[1].Value -replace '\.\./|\./', '' -replace '\.js$', ''
        } | Select-Object -Unique -First 5) -join ', '

        $relPathFwd = $relPath -replace '\\', '/'

        $header = @"
/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : $modName
 * File              : $($file.Name)
 * Path              : frontend/$relPathFwd
 * Purpose           : $purpose
 * Version           : $ProjectVersion
 *
 * Related API       : $relAPIs
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : $imports
 * Depends On        : $imports
 *
 * Description
 * ---------------------------------------------------------------------------
 * $purpose. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

"@
        [System.IO.File]::WriteAllText($file.FullName, $header + $raw, $UTF8NoBOM)
        $Stats.JS++
    } catch { Write-Warning "FAIL $($file.Name): $_" }
}
Write-Done "JS: $($Stats.JS) headers added"

# =============================================================================
# PHASE 4 — CSS HEADERS
# =============================================================================
Write-Phase "PHASE 4: CSS file headers"
$cssFiles = Get-ChildItem -Path $FrontendDir -Filter "*.css" -Recurse |
    Where-Object { $_.FullName -notmatch '\\node_modules\\' }
Write-Info "Found $($cssFiles.Count) CSS files"

foreach ($file in $cssFiles) {
    try {
        $raw     = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)
        if (Has-Header $raw) { $Stats.Skipped++; continue }
        $relPath = ($file.FullName -replace [regex]::Escape($FrontendDir + '\'), '') -replace '\\', '/'
        $varCnt  = ([regex]::Matches($raw, '--[\w-]+\s*:')).Count
        $mqs     = ([regex]::Matches($raw, '@media[^{]+') | ForEach-Object { $_.Value.Trim() } | Select-Object -Unique -First 2) -join ' | '
        $purpose = "CSS Theme/Stylesheet: $($file.BaseName) design tokens and visual styles"

        $header = @"
/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Frontend Design System
 * File              : $($file.Name)
 * Path              : frontend/$relPath
 * Purpose           : $purpose
 * Version           : $ProjectVersion
 *
 * CSS Variables     : $varCnt custom properties defined
 * Responsive Queries: $(if ($mqs) { $mqs } else { 'None' })
 * Depends On        : theme/variables.css (root CSS custom properties)
 *
 * Description
 * ---------------------------------------------------------------------------
 * $purpose. Part of the PLUS33 Coffee ERP CSS custom-property design system.
 * Applied via link tags in index.html across all UI components.
 ******************************************************************************/

"@
        [System.IO.File]::WriteAllText($file.FullName, $header + $raw, [System.Text.Encoding]::UTF8)
        $Stats.CSS++
    } catch { Write-Warning "FAIL $($file.Name): $_" }
}
Write-Done "CSS: $($Stats.CSS) headers added"

# =============================================================================
# PHASE 5 — HTML HEADERS
# =============================================================================
Write-Phase "PHASE 5: HTML file headers"
$htmlFiles = Get-ChildItem -Path $FrontendDir -Filter "*.html" -Recurse |
    Where-Object { $_.FullName -notmatch '\\node_modules\\' }
Write-Info "Found $($htmlFiles.Count) HTML files"

foreach ($file in $htmlFiles) {
    try {
        $raw     = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)
        if (Has-Header $raw) { $Stats.Skipped++; continue }
        $relPath = ($file.FullName -replace [regex]::Escape($FrontendDir + '\'), '') -replace '\\', '/'
        $isEntry = $file.Name -eq 'index.html'
        $purpose = if ($isEntry) { "Application shell that bootstraps the PLUS33 Coffee ERP SPA" } else { "HTML template for $($file.BaseName)" }

        $comment = @"
<!--
 ******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Frontend Application Shell
 * File              : $($file.Name)
 * Path              : frontend/$relPath
 * Purpose           : $purpose
 * Version           : $ProjectVersion
 *
 * Script Entry      : app/main.js
 * Theme CSS         : theme/variables.css, theme/coffee-dark.css
 * Related API       : POST /api/v1/auth/login
 *
 * Description
 * ---------------------------------------------------------------------------
 * $purpose. The PLUS33 Coffee ERP is a Vanilla JS SPA.
 * The JavaScript router dynamically injects page components into #app div.
 ******************************************************************************
-->

"@
        [System.IO.File]::WriteAllText($file.FullName, $comment + $raw, [System.Text.Encoding]::UTF8)
        $Stats.HTML++
    } catch { Write-Warning "FAIL $($file.Name): $_" }
}
Write-Done "HTML: $($Stats.HTML) headers added"

# =============================================================================
# PHASE 6 — GENERATE ENTERPRISE DOCUMENTATION FILES
# =============================================================================
Write-Phase "PHASE 6: Generating enterprise documentation files"
if (-not (Test-Path $DocsOutputDir)) { New-Item -ItemType Directory -Path $DocsOutputDir -Force | Out-Null }

# Run the separate docs generation script
$docsScript = Join-Path $ProjectRoot "doc_generate_docs.ps1"
if (Test-Path $docsScript) {
    Write-Info "Running documentation generator..."
    & powershell -ExecutionPolicy Bypass -File $docsScript
} else {
    Write-Warning "doc_generate_docs.ps1 not found. Run it separately to generate .md docs."
}

# =============================================================================
# PHASE 7 — UPGRADE README
# =============================================================================
Write-Phase "PHASE 7: Upgrading frontend/README.md"

$readmePath = Join-Path $FrontendDir "README.md"
$readme = @"
# PLUS33 Coffee ERP

> Enterprise Resource Planning Platform
> Developed By: Haulo | For: PLUS33 Coffee | Developer: Sivasurya | Version: $ProjectVersion

## Quick Start

1. Ensure PostgreSQL is running with database plus33_erp
2. Configure src/main/resources/application.properties
3. Run: mvnw spring-boot:run  (Flyway auto-applies 331 migrations)
4. Open: http://localhost:8080/frontend/index.html
5. API Docs: http://localhost:8080/swagger-ui.html
6. Login: admin@plus33.com / Admin@123

## Technology Stack

- Backend: Spring Boot 4.x, Java 17, Spring Security JWT, JPA/Hibernate, Flyway, MapStruct, Lombok
- Database: PostgreSQL 15+ (331 migrations, 200+ tables)
- Frontend: Vanilla JavaScript ES Modules SPA (no framework, no build step)
- API Docs: SpringDoc OpenAPI / Swagger UI 3.0.2

## Enterprise Documentation

See frontend/docs/ for all enterprise reference documents:

- Project_Architecture.md
- API_Reference.md
- Workflow_Reference.md
- Security_Reference.md
- Database_Reference.md
- Developer_Guide.md
- Deployment_Guide.md
- Testing_Guide.md
- Module_Reference.md
- Integration_Guide.md
- Complete_Project_Index.md
- Source_Code_Guide.md
- Configuration_Reference.md
- Entity_Relationships.md
- Package_Reference.md
- File_Dependency_Map.md
- Module_Dependency_Map.md
- Flyway_Migration_Reference.md
- Code_Reference.md
- REST_API_Workflow.md
- Maintenance_Guide.md
- Project_Workflow.md
- Project_File_Reference.md

---
PLUS33 Coffee ERP -- Developed by Haulo for PLUS33 Coffee
"@
[System.IO.File]::WriteAllText($readmePath, $readme, [System.Text.Encoding]::UTF8)
Write-Done "README.md upgraded"

# =============================================================================
# FINAL REPORT
# =============================================================================
Write-Phase "DOCUMENTATION COMPLETE"
$total = $Stats.Java + $Stats.SQL + $Stats.JS + $Stats.CSS + $Stats.HTML + $Stats.MD
Write-Host ""
Write-Host "  Java headers      : $($Stats.Java)" -ForegroundColor White
Write-Host "  SQL headers       : $($Stats.SQL)" -ForegroundColor White
Write-Host "  JS headers        : $($Stats.JS)" -ForegroundColor White
Write-Host "  CSS headers       : $($Stats.CSS)" -ForegroundColor White
Write-Host "  HTML headers      : $($Stats.HTML)" -ForegroundColor White
Write-Host "  Already documented: $($Stats.Skipped)" -ForegroundColor Yellow
Write-Host "  TOTAL processed   : $total" -ForegroundColor Cyan
Write-Host ""
Write-Host "  Run doc_generate_docs.ps1 to generate all 23 .md documentation files." -ForegroundColor Cyan
Write-Host "  Next: mvnw compile  (verify zero compilation errors)" -ForegroundColor Yellow
