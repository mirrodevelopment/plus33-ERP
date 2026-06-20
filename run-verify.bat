@echo off
set MAVEN_USER_HOME=D:\plus33\maven-home
echo === Maven Version === > verify-output.txt 2>&1
call mvnw.cmd -version >> verify-output.txt 2>&1

echo === Effective POM (surefire check) === >> verify-output.txt 2>&1
call mvnw.cmd help:effective-pom >> effective-pom.xml 2>&1

echo === Checking skipTests and test skip flags === >> verify-output.txt 2>&1
findstr /i "skipTests\|maven.test.skip\|surefire" effective-pom.xml >> verify-output.txt 2>&1

echo === Done === >> verify-output.txt 2>&1
