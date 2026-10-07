@echo off
title Hotel Reservation System
set "PROJ=C:\Users\G Benerji\OneDrive\Desktop\hotelreservationsystem"
set "JAVA_HOME=C:\Program Files\Java\jdk-26.0.1"
set "MAVEN_HOME=C:\Users\G Benerji\.m2\wrapper\dists\apache-maven-3.9.6-bin\3311e1d4\apache-maven-3.9.6"
set "PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%"
cd /d "%PROJ%"
echo.
echo  Starting Hotel Reservation System...
echo  Open http://localhost:8080 once you see "Started HotelReservationApplication"
echo  Login: admin / admin123
echo.
"%JAVA_HOME%\bin\java.exe" -classpath "%MAVEN_HOME%\boot\plexus-classworlds-2.7.0.jar" -Dclassworlds.conf="%MAVEN_HOME%\bin\m2.conf" -Dmaven.home="%MAVEN_HOME%" -Dmaven.multiModuleProjectDirectory="%PROJ%" org.codehaus.plexus.classworlds.launcher.Launcher spring-boot:run
pause
