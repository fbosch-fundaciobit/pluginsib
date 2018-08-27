
cd enforcer
call mvn clean 
if %ERRORLEVEL% NEQ 0 (cd ..) else (	
cd ..
cd builder
call mvn clean
cd ..)
