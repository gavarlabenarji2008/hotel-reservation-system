@echo off
cd /d "%~dp0"
set "MAVEN_HOME=C:\Users\G Benerji\.m2\wrapper\dists\apache-maven-3.9.6-bin\3311e1d4\apache-maven-3.9.6"
set "PATH=%MAVEN_HOME%\bin;%PATH%"
echo Starting Spring Boot Application... > server.log
"%MAVEN_HOME%\bin\mvn.cmd" -Dmaven.multiModuleProjectDirectory="%CD%" spring-boot:run >> server.log 2>&1
