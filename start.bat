@echo off
setlocal enabledelayedexpansion

echo ===================================================
echo   Starting PLUS33 Coffee ERP Platform
echo ===================================================

REM ---------------------------------------------------
REM [0/4] Kill old processes on ports 8080 and 3000
REM ---------------------------------------------------
echo [0/4] Killing old processes on ports 8080 and 3000...
powershell -NoProfile -Command "Get-NetTCPConnection -LocalPort 8080,3000 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess | Sort-Object -Unique | ForEach-Object { Stop-Process -Id $_ -Force -ErrorAction SilentlyContinue }"
echo   Waiting for ports to clear...
ping 127.0.0.1 -n 3 >nul

REM ---------------------------------------------------
REM [1/4] Compile backend if needed
REM ---------------------------------------------------
if not exist "%~dp0target\classes" (
    echo [1/4] target\classes missing. Compiling backend...
    call "%~dp0mvnw.cmd" compile "-Dmaven.test.skip=true" -q
    if errorlevel 1 (
        echo.
        echo [FAILED] Maven compile failed. Check the errors above.
        pause
        exit /b 1
    )
) else (
    echo [1/4] Skipping compile - classes already generated...
)

REM ---------------------------------------------------
REM [2/4] Copy dependencies if needed
REM ---------------------------------------------------
if not exist "%~dp0target\dependency" (
    echo [2/4] target\dependency missing. Restoring libraries...
    call "%~dp0mvnw.cmd" dependency:copy-dependencies "-DoutputDirectory=target/dependency" "-DincludeScope=runtime" -q
    if errorlevel 1 (
        echo.
        echo [FAILED] Dependency copy failed. Check the errors above.
        pause
        exit /b 1
    )
) else (
    echo [2/4] Skipping copying dependencies - already copied...
)

REM ---------------------------------------------------
REM [3/4] Start backend
REM ---------------------------------------------------
echo [3/4] Starting Spring Boot Backend (Port 8080)...
start "PLUS33 Backend" /D "%~dp0" cmd /k "java -XX:TieredStopAtLevel=1 -XX:+UseParallelGC -Dspring.jmx.enabled=false -Dspring.main.lazy-initialization=true -Dspring.main.banner-mode=off "-Dspring.config.location=file:target/classes/application.properties" -cp target/classes;target/dependency/* com.plus33.erp.Plus33ErpApplication"

REM ---------------------------------------------------
REM [4/4] Compile + start frontend
REM ---------------------------------------------------
echo [4/4] Starting Frontend Web Server (Port 3000)...
javac "%~dp0frontend\WebServer.java"
if errorlevel 1 (
    echo.
    echo [FAILED] WebServer.java failed to compile. Check the errors above.
    pause
    exit /b 1
)
start "PLUS33 Web Server" /D "%~dp0" cmd /k "java -cp frontend WebServer"

REM ---------------------------------------------------
REM Wait for backend health check (with timeout)
REM ---------------------------------------------------
echo.
echo Waiting for Java backend to start on Port 8080...
set /a attempts=0
set /a max_attempts=60

:wait_backend
powershell -NoProfile -Command "try { $null = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction Stop; exit 0 } catch { exit 1 }" >nul 2>&1
if %errorlevel% equ 0 goto backend_ready

set /a attempts+=1
if !attempts! geq !max_attempts! (
    echo.
    echo [FAILED] Backend did not respond on port 8080 after 2 minutes.
    echo   Check the "PLUS33 Backend" console window for stack traces.
    pause
    exit /b 1
)
<nul set /p =.
ping 127.0.0.1 -n 2 >nul
goto wait_backend

:backend_ready
echo.
echo [SUCCESS] Backend is connected and listening on Port 8080!

REM ---------------------------------------------------
REM Wait for frontend to respond (with timeout)
REM ---------------------------------------------------
echo.
echo Waiting for Web Server to start on Port 3000...
set /a attempts=0
set /a max_attempts=30

:wait_frontend
powershell -NoProfile -Command "try { $null = Get-NetTCPConnection -LocalPort 3000 -State Listen -ErrorAction Stop; exit 0 } catch { exit 1 }" >nul 2>&1
if %errorlevel% equ 0 goto frontend_ready

set /a attempts+=1
if !attempts! geq !max_attempts! (
    echo.
    echo [FAILED] Web server did not respond on port 3000 after 1 minute.
    echo   Check the "PLUS33 Web Server" console window for errors.
    pause
    exit /b 1
)
<nul set /p =.
ping 127.0.0.1 -n 2 >nul
goto wait_frontend

:frontend_ready
echo.
echo ---------------------------------------------------
echo  Both servers are up and responding!
echo  Backend  : http://localhost:8080  [OK]
echo  Frontend : http://localhost:3000  [OK]
echo  Opening browser at: http://localhost:3000/#login
echo ---------------------------------------------------
echo.

start chrome "http://localhost:3000/#login"

echo Done. If Chrome didn't open (not installed/not default), the default
echo browser was used instead, or open the link above manually.
pause