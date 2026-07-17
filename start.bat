@echo off
echo ===================================================
echo   Starting PLUS33 Coffee ERP Platform
echo ===================================================

echo [0/4] Killing old processes on ports 8080 and 3000...
powershell -NoProfile -Command "Get-NetTCPConnection -LocalPort 8080,3000 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess | Unique | ForEach-Object { Stop-Process -Id $_ -Force -ErrorAction SilentlyContinue }"
taskkill /IM java.exe /F >nul 2>&1
taskkill /IM javaw.exe /F >nul 2>&1
echo   Waiting for ports to clear...
ping 127.0.0.1 -n 4 >nul

echo [1/4] Skipping clean compile (classes already generated)...
REM call "%~dp0mvnw.cmd" clean compile "-Dmaven.test.skip=true" -q

echo [2/4] Skipping copying dependencies (already copied)...
REM call "%~dp0mvnw.cmd" dependency:copy-dependencies "-DoutputDirectory=target/dependency" "-DincludeScope=runtime" -q

echo [3/4] Starting Spring Boot Backend (Port 8080)...
start "PLUS33 Java Backend (Port 8080)" /D "%~dp0" cmd /k "java -Dspring.config.location=file:target/classes/application.properties -cp target/classes;target/dependency/* com.plus33.erp.Plus33ErpApplication"

echo.
echo Waiting for Java backend to start on Port 8080 (this may take up to 60 seconds)...
:wait_loop
netstat -ano | findstr /R /C:":8080" | findstr LISTENING >nul 2>&1
if errorlevel 1 (
    <nul set /p =.
    ping 127.0.0.1 -n 2 >nul
    goto wait_loop
)
echo.
echo [SUCCESS] Backend is connected and listening on Port 8080!
echo.

echo [4/4] Starting Frontend Web Server (Port 3000)...
javac "%~dp0frontend\WebServer.java"
start "PLUS33 Web Server (Port 3000)" /D "%~dp0" cmd /k "java -cp frontend WebServer"

echo.
echo ---------------------------------------------------
echo  Backend is ready! Web Server is running on Port 3000!
echo  Open your browser at: http://localhost:3000
echo ---------------------------------------------------
echo.
REM pause
