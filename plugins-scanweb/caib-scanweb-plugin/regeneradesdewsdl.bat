
REM  https://proves.caib.es/digital/ws/ServicioCopiaAutentica?wsdl

call wsconsume -k src/main/resources/ServicioCopiaAutentica.wsdl -s src/main/java -b ./bindings/bindings.xjc -n -p es.caib.digital.ws.api.copiaautentica

call wsconsume -k src/main/resources/ServicioEntidades.wsdl -s src/main/java -n -p es.caib.digital.ws.api.entidades

call wsconsume -k src/main/resources/ServicioCaratulas.wsdl -s src/main/java -n -p es.caib.digital.ws.api.caratulas
