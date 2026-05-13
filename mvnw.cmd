@echo off
setlocal

set MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6-bin\apache-maven-3.9.6
set MVN_EXE=%MAVEN_HOME%\bin\mvn.cmd

if exist "%MVN_EXE%" goto run_maven

echo Downloading Apache Maven 3.9.6...
mkdir "%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6-bin" 2>nul
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip' -OutFile '%USERPROFILE%\.m2\wrapper\apache-maven-3.9.6-bin.zip'}"
powershell -Command "Expand-Archive -Path '%USERPROFILE%\.m2\wrapper\apache-maven-3.9.6-bin.zip' -DestinationPath '%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6-bin' -Force"

:run_maven
"%MVN_EXE%" %*
