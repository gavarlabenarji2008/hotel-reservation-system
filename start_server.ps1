$env:MAVEN_HOME = 'C:\Users\G Benerji\.m2\wrapper\dists\apache-maven-3.9.6-bin\3311e1d4\apache-maven-3.9.6'
$env:PATH = "$env:MAVEN_HOME\bin;" + $env:PATH
Set-Location 'C:\Users\G Benerji\OneDrive\Desktop\hotelreservationsystem'
& "$env:MAVEN_HOME\bin\mvn.cmd" '-Dmaven.multiModuleProjectDirectory=C:\Users\G Benerji\OneDrive\Desktop\hotelreservationsystem' spring-boot:run | Out-File -FilePath 'C:\Users\G Benerji\OneDrive\Desktop\hotelreservationsystem\out.log'
