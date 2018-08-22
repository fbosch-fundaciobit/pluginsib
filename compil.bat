
cd enforcer
call mvn clean install -DskipTests
if %ERRORLEVEL% NEQ 0 (cd ..) else (	
cd ..
cd builder
call mvn install -DskipTests
cd ..)
