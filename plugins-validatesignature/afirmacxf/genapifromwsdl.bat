REM -b ./wsdl/binding.xml 
call wsconsume -k  http://des-afirma.redsara.es/afirmaws/services/DSSAfirmaVerify?wsdl  -s src/main/java -n -p org.fundaciobit.plugins.validatesignature.afirmacxf.validarfirmaapi
