

COM USAR EL TESTER
==================

Prerequisits:
    (a) Java 1.6 o superior
    (b) Maven
    (c) Connexió a Internet i accés a GitHub


Configuració:    
    
(1) Modificar ruta a les plantilles de transformació: Editar el fitxer plugin.properties i modificar la propietat "org.fundaciobit.exemple.base.plugins.validatesignature.afirmacxf.TransformersTemplatesPath" per fer-la apuntar al directori "transformersTemplates". Aquest directori es pot descarregar d'aqui: https://github.com/GovernIB/pluginsib/tree/pluginsib-1.0/plugins-validatesignature/afirmacxf/config

(2) Editar les propietats de connexió del fitxer plugin.properties

(3) Executar el batch "validatesignature.bat" passat-li 3 parametres:
              - [filePropertiesPath] Ruta al plugin.properties
              - [packageBase] Package emprat per les propietats del plugin.properties
              - [signaturePath] Ruta a la firma 
              
   Exemple: validatesignature.bat plugin.properties org.fundaciobit.exemple.base. firma.pdf 