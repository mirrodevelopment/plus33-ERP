#Requires -Version 5.1
<#
.SYNOPSIS  PLUS33 Coffee ERP -- Enterprise Self-Documentation Engine
           Adds class JavaDoc, method JavaDoc, JS JSDoc, SQL inline comments.
           Does NOT change business logic.
#>
param([string]$Module = "")   # Optional: process only one module folder

$BackendSrc   = Join-Path $PSScriptRoot "src\main\java\com\plus33\erp"
$FrontendDir  = Join-Path $PSScriptRoot "frontend"
$MigrationDir = Join-Path $PSScriptRoot "src\main\resources\db\migration"
$UTF8NoBOM    = New-Object System.Text.UTF8Encoding($false)
$ProjectVer   = "0.0.1-SNAPSHOT"

$Stats = @{ ClassDoc=0; MethodDoc=0; JSDoc=0; SQLDoc=0; Skipped=0; Failed=0 }

function Write-Phase($msg) {
    Write-Host ("`n" + "="*58) -ForegroundColor Cyan
    Write-Host "  $msg" -ForegroundColor Cyan
    Write-Host ("="*58) -ForegroundColor Cyan
}

# =============================================================================
# UTILITIES
# =============================================================================

function Get-ModName($filePath) {
    $rel   = $filePath -replace [regex]::Escape($BackendSrc + '\'), ''
    $parts = $rel -split '[/\\]'
    if ($parts.Count -ge 1 -and $parts[0]) {
        $m = $parts[0]
        return ($m.Substring(0,1).ToUpper() + $m.Substring(1)) + " Module"
    }
    return "Core Module"
}

function Get-ClassType($content) {
    if ($content -match '@RestController|@Controller\b')       { return "Controller"    }
    if ($content -match '@Service\b')                          { return "Service"       }
    if ($content -match 'JpaRepository|@Repository\b')         { return "Repository"    }
    if ($content -match '@Entity\b')                           { return "Entity"        }
    if ($content -match '@Mapper\b')                           { return "Mapper"        }
    if ($content -match '@Configuration\b')                    { return "Configuration" }
    if ($content -match 'OncePerRequestFilter')                { return "Filter"        }
    if ($content -match 'ApplicationEvent\b' -and
        $content -notmatch 'ApplicationEventPublisher')        { return "Event"         }
    if ($content -match '@Scheduled\b')                        { return "Scheduler"     }
    if ($content -match 'public enum ')                        { return "Enum"          }
    if ($content -match 'public record ')                      { return "Record"        }
    if ($content -match 'extends.*Exception\b')                { return "Exception"     }
    if ($content -match 'Request\b|Response\b')                { return "DTO"           }
    if ($content -match '@Component\b')                        { return "Component"     }
    return "Class"
}

function Get-MethodDesc($methodName, $noun, $classType) {
    $v = ($methodName -creplace '([A-Z])', ' $1').Trim().ToLower() -split ' '
    $verb = if ($v.Count -gt 0) { $v[0] } else { "" }

    switch -Regex ($verb) {
        '^create|^add|^register|^new'      { return "Creates a new $noun and persists it to the database." }
        '^save|^persist|^store'            { return "Persists the $noun entity to the database." }
        '^update|^modify|^edit|^patch'     { return "Updates an existing $noun record in the database." }
        '^delete|^remove|^purge'           { return "Permanently deletes the $noun from the database." }
        '^get|^find|^load|^fetch|^retrieve' {
            if ($methodName -match 'All|List|Page|Search') { return "Retrieves a paginated list of $noun records." }
            if ($methodName -match 'ById|ByCode|ByEmail')  { return "Retrieves a single $noun by its identifier." }
            return "Retrieves $noun data from the database."
        }
        '^list|^search|^filter|^query'     { return "Returns a filtered paginated list of $noun records." }
        '^approve|^confirm'                { return "Approves the $noun, transitions to APPROVED status, and posts GL journal entries." }
        '^submit'                          { return "Submits the $noun for approval. Transitions DRAFT to SUBMITTED status." }
        '^cancel'                          { return "Cancels the $noun and posts reversing GL entries. Restores reserved resources." }
        '^void'                            { return "Permanently voids the $noun. This action cannot be undone." }
        '^process'                         { return "Processes the $noun business workflow end-to-end." }
        '^calculate|^compute'              { return "Calculates $noun totals including subtotal, tax, discounts, and net amount." }
        '^validate|^check|^verify'         { return "Validates business rules and constraints for $noun." }
        '^generate|^build|^construct'      { return "Generates the $noun based on input parameters and business rules." }
        '^map|^convert|^toEntity|^toDto|^toResponse' { return "Converts between Entity and DTO representations (MapStruct)." }
        '^publish|^emit|^dispatch'         { return "Publishes a domain event to notify dependent modules of the state change." }
        '^complete|^finish|^close'         { return "Completes the $noun workflow and finalizes the record status." }
        '^post'                            { return "Posts $noun entries to the General Ledger and updates financial balances." }
        '^pay|^settle|^reconcile'          { return "Processes payment for $noun and updates the outstanding balance." }
        '^login|^authenticate'             { return "Authenticates the user credentials and generates a signed JWT token." }
        '^doFilter'                        { return "JWT Security Filter: validates the Bearer token and sets the Spring SecurityContext." }
        '^handle|^on'                      { return "Handles the $noun event or exception in the business workflow." }
        '^configure'                       { return "Configures the $noun bean and registers it in the Spring ApplicationContext." }
        '^send|^notify|^alert'             { return "Sends a notification or alert for the $noun event." }
        '^reserve|^allocate'               { return "Reserves $noun resources (budget or stock) for downstream processing." }
        '^release|^restore'                { return "Releases previously reserved $noun resources back to the available pool." }
        '^init|^initialize|^setup'         { return "Initializes the $noun configuration and prepares default state." }
        '^export'                          { return "Exports $noun data as a report or downloadable file." }
        '^import'                          { return "Imports $noun data from an external source or file." }
        default { return "Performs the $methodName operation in this module." }
    }
}

# =============================================================================
# CLASS JAVADOC BUILDER
# =============================================================================

function Build-ClassDoc($className, $classType, $modName, $content) {
    # Package
    $pkgM  = [regex]::Match($content, 'package\s+([\w.]+);')
    $pkg   = if ($pkgM.Success) { $pkgM.Groups[1].Value } else { "com.plus33.erp" }

    # Base name
    $base  = $className -replace 'ServiceImpl$|Service$|Repository$|Controller$|Mapper$|Request$|Response$|Event$|Filter$|Exception$|Impl$', ''

    # DB table from @Table annotation
    $tblM  = [regex]::Match($content, '@Table\s*\(\s*(?:name\s*=\s*)?[''"]([^''"]+)[''"]')
    $table = if ($tblM.Success) { $tblM.Groups[1].Value }
             elseif ($base)    { ($base -creplace '([A-Z])', '_$1').TrimStart('_').ToLower() + 's' }
             else              { 'N/A' }

    # REST base URL
    $urlM  = [regex]::Match($content, '@RequestMapping\s*\(\s*[''"]([^''"]+)[''"]')
    $baseUrl = if ($urlM.Success) { $urlM.Groups[1].Value } else { '' }

    # REST endpoints
    $epM  = [regex]::Matches($content, '@(Get|Post|Put|Patch|Delete)Mapping(?:\s*\(\s*[''"]([^''"]*)[''"])?')
    $apis = if ($epM.Count -gt 0 -and $baseUrl) {
        ($epM | Select-Object -First 5 | ForEach-Object {
            $_.Groups[1].Value.ToUpper() + ' ' + $baseUrl + $_.Groups[2].Value
        }) -join ', '
    } elseif ($baseUrl) { "REST $baseUrl/**" } else { '' }

    # Dependencies from imports
    $impM = [regex]::Matches($content, 'import com\.plus33\.erp\.([\w]+)\.')
    $deps = @()
    foreach ($m in $impM) {
        $mod = $m.Groups[1].Value
        $mt  = $mod.Substring(0,1).ToUpper() + $mod.Substring(1)
        if ($mt -notin $deps) { $deps += $mt }
    }
    $depsStr = if ($deps.Count -gt 0) { ($deps | Select-Object -Unique -First 6) -join ', ' } else { 'None' }

    # Layer description
    $layer = switch ($classType) {
        "Controller"    { "REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to " + $base + "Service." }
        "Service"       { "Business Service: core logic, validation, and @Transactional operations for " + $modName + "." }
        "Repository"    { "JPA Repository: database access for table '" + $table + "' via Spring Data JPA." }
        "Entity"        { "JPA Entity: persistent domain object mapped to PostgreSQL table '" + $table + "'." }
        "Mapper"        { "MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection." }
        "Configuration" { "Spring Configuration: defines and registers beans for " + $modName + "." }
        "Filter"        { "Spring Security Filter: validates JWT Bearer token on every HTTP request." }
        "Event"         { "Spring ApplicationEvent: signals a state change for async processing." }
        "Scheduler"     { "Scheduled Task: runs periodic background jobs via @Scheduled annotation." }
        "Enum"          { "Enumeration: typed domain constants for " + $modName + " status and category fields." }
        "Record"        { "Java Record: immutable value object / data carrier for " + $modName + "." }
        "Exception"     { "Custom Exception: domain-specific error condition in " + $modName + "." }
        "DTO"           { "Data Transfer Object: serializes API request/response data for " + $modName + "." }
        "Component"     { "Spring Component: shared utility or infrastructure helper for " + $modName + "." }
        default         { "Component of " + $modName + " in the PLUS33 Coffee ERP platform." }
    }

    # Build JavaDoc as string list
    $out = New-Object System.Collections.Generic.List[string]
    $out.Add("/**")
    $out.Add(" * <b>PLUS33 Coffee ERP -- " + $modName + "</b>")
    $out.Add(" *")
    $out.Add(" * <p><b>Class  :</b> {@code " + $className + "}</p>")
    $out.Add(" * <p><b>Package:</b> {@code " + $pkg + "}</p>")
    $out.Add(" * <p><b>Layer  :</b> " + $layer + "</p>")
    $out.Add(" *")

    # Workflow block
    if ($classType -eq "Controller" -and $baseUrl) {
        $out.Add(" * <p><b>Request Flow:</b></p>")
        $out.Add(" * <pre>")
        $out.Add(" * HTTP Request")
        $out.Add(" *   --> JWT Auth Filter (validate Bearer token)")
        $out.Add(" *   --> @PreAuthorize (permission check)")
        $out.Add(" *   --> " + $className + ".endpoint()")
        $out.Add(" *   --> " + $base + "Service.method()")
        $out.Add(" *   --> " + $base + "Repository (PostgreSQL)")
        $out.Add(" *   --> ApiResponse wrapped in ResponseEntity")
        $out.Add(" *   --> JSON response to Frontend")
        $out.Add(" * </pre>")
        $out.Add(" *")
    } elseif ($classType -eq "Service") {
        $out.Add(" * <p><b>Service Flow:</b></p>")
        $out.Add(" * <pre>")
        $out.Add(" * " + $base + "Controller")
        $out.Add(" *   --> " + $className + " (this)")
        $out.Add(" *   --> Validate business rules")
        $out.Add(" *   --> " + $base + "Repository (read/write '" + $table + "')")
        $out.Add(" *   --> " + $base + "Mapper (Entity to DTO conversion)")
        $out.Add(" *   --> Publish domain event (analytics refresh)")
        $out.Add(" *   --> Return DTO response to Controller")
        $out.Add(" * </pre>")
        $out.Add(" *")
    } elseif ($classType -eq "Filter") {
        $out.Add(" * <p><b>JWT Security Flow:</b></p>")
        $out.Add(" * <pre>")
        $out.Add(" * Every HTTP Request")
        $out.Add(" *   --> JwtAuthFilter.doFilterInternal()")
        $out.Add(" *   --> Extract Authorization: Bearer token")
        $out.Add(" *   --> Decode and verify HMAC-SHA256 signature")
        $out.Add(" *   --> Extract subject (email) + authorities (permissions)")
        $out.Add(" *   --> SecurityContextHolder.setAuthentication(...)")
        $out.Add(" *   --> @PreAuthorize checks succeed --> Controller executes")
        $out.Add(" * </pre>")
        $out.Add(" *")
    }

    if ($apis) {
        $out.Add(" * <p><b>REST Endpoints    :</b> " + $apis + "</p>")
    }
    if ($table -and $table -ne 'N/A' -and $classType -in @("Repository","Entity","Service")) {
        $out.Add(" * <p><b>Database Table   :</b> {@code " + $table + "}</p>")
    }
    $out.Add(" * <p><b>Module Deps      :</b> " + $depsStr + "</p>")
    $out.Add(" *")
    $out.Add(" * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)")
    $out.Add(" * @version " + $ProjectVer)
    $out.Add(" */")
    return $out
}

# =============================================================================
# METHOD JAVADOC BUILDER
# =============================================================================

function Build-MethodDoc($sigLine, $className, $classType, $modName) {
    $t = $sigLine.TrimStart()

    # Extract method name
    $nameM = [regex]::Match($t, '(?:public|protected|private|static|\s)+\S+\s+(\w+)\s*\(')
    if (-not $nameM.Success) { return @() }
    $methodName = $nameM.Groups[1].Value

    # Skip constructors
    if ($methodName -ceq $className) { return @() }
    # Skip common trivial identifiers
    if ($methodName -in @('if','for','while','switch','catch','try')) { return @() }

    # Return type
    $retM = [regex]::Match($t, '(?:public|protected|private)(?:\s+static)?\s+([\w<>\[\],\s]+?)\s+\w+\s*\(')
    $returnType = if ($retM.Success) { $retM.Groups[1].Value.Trim() } else { "void" }

    # Parameter string
    $ps = $t.IndexOf('(')
    $pe = $t.LastIndexOf(')')
    $paramStr = if ($ps -ge 0 -and $pe -gt $ps) { $t.Substring($ps+1, $pe-$ps-1).Trim() } else { "" }

    # Parse params
    $params = @()
    if ($paramStr) {
        $clean = $paramStr -replace '@\w+(?:\([^)]*\))?\s+', ''
        foreach ($seg in ($clean -split ',\s*')) {
            $seg = $seg.Trim()
            $pm  = [regex]::Match($seg, '([\w<>\[\]]+)\s+(\w+)$')
            if ($pm.Success) {
                $params += @{ Type=$pm.Groups[1].Value; Name=$pm.Groups[2].Value }
            }
        }
    }

    # noun from method name words
    $words = ($methodName -creplace '([A-Z])', ' $1').Trim().ToLower() -split ' '
    $noun  = if ($words.Count -gt 1) { ($words[1..($words.Count-1)] -join ' ') } else { ($modName -replace ' Module','').ToLower() }

    $description = Get-MethodDesc $methodName $noun $classType

    # Context note
    $ctxNote = ""
    if ($classType -eq "Controller")  { $ctxNote = "Requires JWT authentication. Permission enforced via @PreAuthorize annotation." }
    if ($classType -eq "Service" -and ($methodName -match '^(create|update|delete|approve|submit|cancel|void|complete|post|pay)')) {
        $ctxNote = "@Transactional: rolled back on exception. Publishes domain event on success."
    }
    if ($classType -eq "Mapper")      { $ctxNote = "Generated at compile time by MapStruct processor. No runtime reflection." }

    # Build lines
    $out = New-Object System.Collections.Generic.List[string]
    $out.Add("    /**")
    $out.Add("     * " + $description)
    $out.Add("     *")
    if ($ctxNote) {
        $out.Add("     * <p><em>" + $ctxNote + "</em></p>")
        $out.Add("     *")
    }

    foreach ($p in $params) {
        $pd = switch ($p.Name) {
            "id"         { "the unique database ID of the resource" }
            "request"    { "the validated request DTO containing input data" }
            "pageable"   { "Spring Pageable (page, size, sort) from query parameters" }
            "companyId"  { "owning company ID for multi-tenant data isolation" }
            "status"     { "status filter for narrowing query results" }
            "startDate"  { "inclusive start date for date-range filtering" }
            "endDate"    { "inclusive end date for date-range filtering" }
            "userId"     { "authenticated user identifier" }
            "sessionId"  { "active session identifier" }
            "token"      { "JWT Bearer token string" }
            "source"     { "the source entity or DTO to convert" }
            default      { "the " + $p.Name + " input value" }
        }
        $out.Add("     * @param " + $p.Name + " " + $pd)
    }

    if ($returnType -ne "void" -and $returnType -ne "Void") {
        $rd = switch -Regex ($returnType) {
            'ResponseEntity'      { "HTTP ResponseEntity wrapping ApiResponse with status code and data" }
            'Page<'               { "Spring Page of matching records with pagination metadata" }
            'List<'               { "List of matching records" }
            'Optional<'           { "Optional containing the entity if found, empty if not" }
            'boolean|Boolean'     { "true if operation succeeded, false otherwise" }
            'String'              { "the result string value" }
            'long|Long|int|Integer' { "the numeric result value" }
            default               { "the " + ($returnType -replace '<.*>','') + " result" }
        }
        $out.Add("     * @return " + $rd)
    }

    # Common throws
    if ($description -match 'Retrieves|single') { $out.Add("     * @throws ResourceNotFoundException if the entity is not found") }
    if ($description -match 'Validates|Creates|Updates|Approves|Submits|Cancels') { $out.Add("     * @throws BusinessException if a business rule is violated") }

    $out.Add("     */")
    return $out
}

# =============================================================================
# JAVA FILE PROCESSOR
# =============================================================================

function Process-Java($filePath) {
    try {
        $content = [IO.File]::ReadAllText($filePath, $UTF8NoBOM)
        $className = [IO.Path]::GetFileNameWithoutExtension($filePath)
        $modName   = Get-ModName $filePath
        $classType = Get-ClassType $content

        $srcLines   = $content -split "`r?`n"
        $out        = New-Object System.Collections.Generic.List[string]
        $braceDepth = 0
        $inBlockCmt = $false
        $classDocDone = $false

        for ($i = 0; $i -lt $srcLines.Count; $i++) {
            $line    = $srcLines[$i]
            $trimmed = $line.TrimStart()

            # Track block comments (/*** header, /** javadoc, /* regular */)
            if (-not $inBlockCmt -and ($trimmed.StartsWith('/**') -or $trimmed.StartsWith('/*'))) {
                $inBlockCmt = $true
            }
            if ($inBlockCmt) {
                $out.Add($line)
                if ($trimmed.Contains('*/')) { $inBlockCmt = $false }
                continue
            }
            # Skip // line comments
            if ($trimmed.StartsWith('//')) { $out.Add($line); continue }

            # Brace counting (simplified: strip string literals first)
            $strippedLine = [regex]::Replace($line, '"(?:[^"\\]|\\.)*"', '""')
            $strippedLine = [regex]::Replace($strippedLine, "'(?:[^'\\]|\\.)*'", "''")
            $slashIdx = $strippedLine.IndexOf('//')
            if ($slashIdx -ge 0) { $strippedLine = $strippedLine.Substring(0, $slashIdx) }
            $opens  = ([regex]::Matches($strippedLine, '{')).Count
            $closes = ([regex]::Matches($strippedLine, '}')).Count

            # ── CLASS-LEVEL JAVADOC ─────────────────────────────────────────
            if (-not $classDocDone -and $braceDepth -eq 0 -and
                $trimmed -match '^(public|protected|private|)\s*(abstract\s+|final\s+)?(class|interface|enum|record)\s+\w+') {

                $hasDocAbove = $false
                for ($j = [Math]::Max(0,$i-6); $j -lt $i; $j++) {
                    if ($srcLines[$j].TrimStart().Contains('*/')) { $hasDocAbove = $true; break }
                }
                if (-not $hasDocAbove) {
                    $docLines = Build-ClassDoc $className $classType $modName $content
                    foreach ($dl in $docLines) { $out.Add($dl) }
                    $script:Stats.ClassDoc++
                }
                $classDocDone = $true
            }

            # ── METHOD-LEVEL JAVADOC ────────────────────────────────────────
            if ($classDocDone -and $braceDepth -eq 1 -and
                $trimmed -match '^(public|protected)\s+' -and
                $trimmed -match '\w+\s*\(' -and
                $trimmed -notmatch '^(public|protected)\s+(class|interface|enum|record)\s+') {

                $hasDocAbove = $false
                for ($j = [Math]::Max(0,$i-5); $j -lt $i; $j++) {
                    $prevT = $srcLines[$j].TrimStart()
                    if ($prevT.Contains('*/')) { $hasDocAbove = $true; break }
                }
                if (-not $hasDocAbove) {
                    $mdoc = Build-MethodDoc $trimmed $className $classType $modName
                    if ($mdoc.Count -gt 0) {
                        foreach ($dl in $mdoc) { $out.Add($dl) }
                        $script:Stats.MethodDoc++
                    }
                }
            }

            # Handle @Override - add method doc to NEXT method line
            if ($classDocDone -and $braceDepth -eq 1 -and $trimmed -eq '@Override') {
                # Peek ahead for the actual method
                $nextIdx = $i + 1
                while ($nextIdx -lt $srcLines.Count -and $srcLines[$nextIdx].Trim() -eq '') { $nextIdx++ }
                if ($nextIdx -lt $srcLines.Count) {
                    $nextT = $srcLines[$nextIdx].TrimStart()
                    if ($nextT -match '^(public|protected)\s+' -and $nextT -match '\w+\s*\(') {
                        $hasDocAbove = $false
                        for ($j = [Math]::Max(0,$i-4); $j -lt $i; $j++) {
                            if ($srcLines[$j].TrimStart().Contains('*/')) { $hasDocAbove = $true; break }
                        }
                        if (-not $hasDocAbove) {
                            $mdoc = Build-MethodDoc $nextT $className $classType $modName
                            if ($mdoc.Count -gt 0) {
                                foreach ($dl in $mdoc) { $out.Add($dl) }
                                $script:Stats.MethodDoc++
                            }
                        }
                    }
                }
            }

            $braceDepth += $opens - $closes
            if ($braceDepth -lt 0) { $braceDepth = 0 }

            $out.Add($line)
        }

        [IO.File]::WriteAllText($filePath, ($out -join "`r`n"), $UTF8NoBOM)
    } catch {
        Write-Warning ("  FAIL " + [IO.Path]::GetFileName($filePath) + ": " + $_)
        $script:Stats.Failed++
    }
}

# =============================================================================
# JS JSDOC ADDER
# =============================================================================

function Process-JS($filePath) {
    try {
        $content  = [IO.File]::ReadAllText($filePath, $UTF8NoBOM)
        $srcLines = $content -split "`r?`n"
        $out      = New-Object System.Collections.Generic.List[string]

        $rel     = ($filePath -replace [regex]::Escape($FrontendDir + '\'), '') -replace '\\', '/'
        $dirName = ($rel -split '/')[0]
        $modName = (Get-Culture).TextInfo.ToTitleCase([string]($dirName -replace '-', ' ')) + ' Module'

        $inBlockCmt = $false
        for ($i = 0; $i -lt $srcLines.Count; $i++) {
            $line    = $srcLines[$i]
            $trimmed = $line.TrimStart()

            if (-not $inBlockCmt -and $trimmed.StartsWith('/*')) { $inBlockCmt = $true }
            if ($inBlockCmt) { $out.Add($line); if ($trimmed.Contains('*/')) { $inBlockCmt = $false }; continue }
            if ($trimmed.StartsWith('//')) { $out.Add($line); continue }

            $isFunc = $trimmed -match '^(export\s+)?(async\s+)?function\s+\w+\s*\(' -or
                      $trimmed -match '^(export\s+)?(static\s+)?(async\s+)?\w+\s*\([^)]*\)\s*\{' -or
                      $trimmed -match '^(export\s+)?(const|let)\s+\w+\s*=\s*(async\s+)?\('

            if ($isFunc) {
                $hasDoc = $false
                for ($j = [Math]::Max(0,$i-3); $j -lt $i; $j++) {
                    if ($srcLines[$j].TrimStart().Contains('*/')) { $hasDoc = $true; break }
                }
                if (-not $hasDoc) {
                    $fnM    = [regex]::Match($trimmed, '(?:function\s+|const\s+|let\s+)(\w+)')
                    $fnName = if ($fnM.Success) { $fnM.Groups[1].Value } else { "fn" }
                    $noun   = ($fnName -creplace '([A-Z])', ' $1').Trim().ToLower()
                    $desc   = Get-MethodDesc $fnName $noun "JS"

                    $indent = $line.Length - $trimmed.Length
                    $pad    = " " * $indent
                    $out.Add($pad + "/**")
                    $out.Add($pad + " * " + $desc)
                    $out.Add($pad + " * @memberof " + $modName)
                    $out.Add($pad + " */")
                    $script:Stats.JSDoc++
                }
            }
            $out.Add($line)
        }

        [IO.File]::WriteAllText($filePath, ($out -join "`r`n"), $UTF8NoBOM)
    } catch {
        Write-Warning ("  FAIL JS " + [IO.Path]::GetFileName($filePath) + ": " + $_)
        $script:Stats.Failed++
    }
}

# =============================================================================
# SQL SECTION COMMENTER
# =============================================================================

function Process-SQL($filePath) {
    try {
        $content = [IO.File]::ReadAllText($filePath, $UTF8NoBOM)
        $cmtCnt  = ([regex]::Matches($content, '(?m)^\s*--')).Count
        if ($cmtCnt -gt 10) { $script:Stats.Skipped++; return }

        $srcLines = $content -split "`r?`n"
        $out      = New-Object System.Collections.Generic.List[string]

        foreach ($line in $srcLines) {
            $t = $line.TrimStart()
            if ($t -match '^CREATE TABLE\s+(\w+)', 'IgnoreCase') {
                $out.Add("")
                $out.Add("-- ----------------------------------------------------------------")
                $out.Add("-- Table: " + $Matches[1])
                $out.Add("-- ----------------------------------------------------------------")
                $script:Stats.SQLDoc++
            } elseif ($t -match '^ALTER TABLE\s+(\w+)', 'IgnoreCase') {
                $out.Add("-- ALTER: " + $Matches[1])
            } elseif ($t -match '^INSERT INTO\s+(\w+)', 'IgnoreCase') {
                $out.Add("-- Seed: " + $Matches[1])
            } elseif ($t -match '^CREATE\s+(UNIQUE\s+)?INDEX\s+(\w+)', 'IgnoreCase') {
                $out.Add("-- Index: " + $Matches[2])
            }
            $out.Add($line)
        }

        [IO.File]::WriteAllText($filePath, ($out -join "`r`n"), $UTF8NoBOM)
    } catch {
        Write-Warning ("  FAIL SQL " + [IO.Path]::GetFileName($filePath) + ": " + $_)
    }
}

# =============================================================================
# MAIN EXECUTION
# =============================================================================

Write-Phase "PHASE 1: Java Class + Method JavaDoc"
$searchRoot = if ($Module) { Join-Path $BackendSrc $Module } else { $BackendSrc }
$javaFiles  = Get-ChildItem -Path $searchRoot -Filter "*.java" -Recurse
Write-Host ("  " + $javaFiles.Count + " Java files to process") -ForegroundColor Gray
$done = 0
foreach ($file in $javaFiles) {
    Process-Java $file.FullName | Out-Null
    $done++
    if ($done % 250 -eq 0) {
        Write-Host ("  " + $done + " / " + $javaFiles.Count + " processed...") -ForegroundColor Gray
    }
}
Write-Host ("  Class JavaDoc  : " + $Stats.ClassDoc)  -ForegroundColor Green
Write-Host ("  Method JavaDoc : " + $Stats.MethodDoc) -ForegroundColor Green
Write-Host ("  Failed         : " + $Stats.Failed)    -ForegroundColor Red

Write-Phase "PHASE 2: JavaScript JSDoc"
$jsFiles = Get-ChildItem -Path $FrontendDir -Filter "*.js" -Recurse |
    Where-Object { $_.FullName -notmatch '\\node_modules\\' }
Write-Host ("  " + $jsFiles.Count + " JS files") -ForegroundColor Gray
foreach ($file in $jsFiles) { Process-JS $file.FullName }
Write-Host ("  JSDoc blocks : " + $Stats.JSDoc) -ForegroundColor Green

Write-Phase "PHASE 3: SQL Inline Section Comments"
$sqlFiles = Get-ChildItem -Path $MigrationDir -Filter "*.sql"
Write-Host ("  " + $sqlFiles.Count + " SQL migrations") -ForegroundColor Gray
foreach ($file in $sqlFiles) { Process-SQL $file.FullName }
Write-Host ("  SQL sections : " + $Stats.SQLDoc) -ForegroundColor Green

Write-Phase "COMPLETE"
Write-Host ""
Write-Host ("  Class JavaDoc  : " + $Stats.ClassDoc)  -ForegroundColor White
Write-Host ("  Method JavaDoc : " + $Stats.MethodDoc) -ForegroundColor White
Write-Host ("  JS JSDoc       : " + $Stats.JSDoc)     -ForegroundColor White
Write-Host ("  SQL sections   : " + $Stats.SQLDoc)    -ForegroundColor White
Write-Host ("  Skipped (OK)   : " + $Stats.Skipped)   -ForegroundColor Yellow
Write-Host ("  Failed         : " + $Stats.Failed)    -ForegroundColor Red
Write-Host ""
Write-Host "  Next: mvnw compile   (verify zero errors)" -ForegroundColor Yellow
