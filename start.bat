@echo off
echo ===================================================
echo   Starting PLUS33 Coffee ERP Platform
echo ===================================================

echo [1/2] Starting Spring Boot Java Backend...
start "PLUS33 Java Backend (Port 8080)" cmd /k "mvnw.cmd spring-boot:run -DskipTests -Dspring-boot.run.jvmArguments=\"-Dspring.datasource.url=jdbc:postgresql://localhost:5432/plus33_erp -Dspring.datasource.username=postgres -Dspring.datasource.password=crazy@8\""

echo [2/2] Compiling and Starting Frontend Web Server...
javac frontend\WebServer.java
start "PLUS33 Web Server (Port 3000)" cmd /k "java -cp frontend WebServer"

echo.
echo ---------------------------------------------------
echo  Both servers are launching in separate windows!
echo  Once startup completes, view the platform here:
echo  👉 http://localhost:3000
echo ---------------------------------------------------
echo.
pause
