@echo off
set "PATH=C:\Users\G Benerji\.m2\wrapper\dists\apache-maven-3.9.6-bin\3311e1d4\apache-maven-3.9.6\bin;%PATH%"
call mvn -Dmaven.multiModuleProjectDirectory="%CD%" package -DskipTests
