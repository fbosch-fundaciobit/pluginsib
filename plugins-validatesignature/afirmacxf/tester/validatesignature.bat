
call mvn exec:java -Dgenapp_run=true -Dexec.mainClass="org.fundaciobit.plugins.validatesignature.afirmacxf.ValidateSignatureMain" -Dexec.args="%*"
