

# ![Logo](https://github.com/GovernIB/maven/raw/binaris/pluginsib/projectinfo_Attachments/icon.jpg) Repositori de Plugins per les Aplicacions del KitAnibal  (pluginsib-2.0)

**Descripció**

Repositori per compilar els Plugins per les Aplicacions del KitAnibal i qualsevol aplicació que vulgui reutilitzar funcionalitats configurables emprant un plugin. A més de compilar els Plugins com a conjunt, conté un script que t'indica quins plugins fan falta per tenir la llista completa. Els plugins que compila són els següents:

Repositori | URL | Nom | Descripció
------------ | ------------- | ------------- | -------------
pluginsib-core | [pluginsib-core-2.0](https://github.com/GovernIB/pluginsib-core/tree/pluginsib-core-2.0) | Core | Lo que sigui. 
pluginsib-utils | [pluginsib-utils-2.0](https://github.com/GovernIB/pluginsib-utils/tree/pluginsib-utils-2.0) | Utilitats de pluginsib | Paquet que conté utilitats pels pluginsIB.
pluginsib-userinformation | [pluginsib-userinformation-2.0](https://github.com/GovernIB/pluginsib-userinformation/tree/pluginsib-userinformation-2.0) | Informació d'usuaris | Informació de les dades d'un usuari a partir del seu username o identificador administratiu (NIF) i/o informació de Roles: BBDD o LDAP.
pluginsib-barcode | [pluginsib-barcode-2.0](https://github.com/GovernIB/pluginsib-barcode/tree/pluginsib-barcode-2.0) |  Generació de Codi de barres | barcode128, pdf417 i qrcode
pluginsib-exportdata | [pluginsib-exportdata-2.0](https://github.com/GovernIB/pluginsib-exportdata/tree/pluginsib-exportdata-2.0) | Exportació de Dades | Exporta una matriu de dades a formats com csv, excel o ods.
pluginsib-validatecertificate | [pluginsib-validatecertificate-2.0](https://github.com/GovernIB/pluginsib-validatecertificate/tree/pluginsib-validatecertificate-2.0) | Validació de Certificats Digitals | Sencill i @firma
pluginsib-csvgenerator | [pluginsib-csvgenerator-2.0](https://github.com/GovernIB/pluginsib-csvgenerator/tree/pluginsib-csvgenerator-2.0) | Generador de csv | Genera codis segurs de verificació en diferents formats.
pluginsib-documentconverter | [pluginsib-documentconverter-2.0](https://github.com/GovernIB/pluginsib-documentconverter/tree/pluginsib-documentconverter-2.0) | Conversió de Format de Documents | OpenOffice en mode servei
pluginsib-loginmodulejboss | [pluginsib-loginmodulejboss-2.0](https://github.com/GovernIB/pluginsib-loginmodulejboss/tree/pluginsib-loginmodulejboss-2.0) | Loginmodulejboss | Ni idea de què fa.
pluginsib-validatesignature | [pluginsib-validatesignature-2.0](https://github.com/GovernIB/pluginsib-validatesignature/tree/pluginsib-validatesignature-2.0) |  Informacio i Validació de Firmes | Integr@ i @Firma
pluginsib-paymentweb | [pluginsib-paymentweb-2.0](https://github.com/GovernIB/pluginsib-paymentweb/tree/pluginsib-paymentweb-2.0) | Pagament web | Eina per fer pagants vía web.
pluginsib-timestamp | [pluginsib-timestamp-2.0](https://github.com/GovernIB/pluginsib-timestamp/tree/pluginsib-timestamp-2.0) | Timestamp | Generadors de Segell de Temps.
pluginsib-documentcustody | [pluginsib-documentcustody-2.0](https://github.com/GovernIB/pluginsib-documentcustody/tree/pluginsib-documentcustody-2.0) | Custòdia de Documents | sistema de fitxers, alfresco, Custòdia CAIB i Arxiu Digital CAIB.
pluginsib-scanweb | [pluginsib-scanweb-2.0](https://github.com/GovernIB/pluginsib-scanweb/tree/pluginsib-scanweb-2.0) | Escaneig de Documents via Web | Dynamic Web Twain, Digitalitzacio Corporativa CAIB i Applet/JNLP
pluginsib-arxiu | [pluginsib-arxiu-2.0](https://github.com/GovernIB/pluginsib-arxiu/tree/pluginsib-arxiu-2.0) | Arxiu | Ni idea.

Altres PLugins que es troben per raons històriques dins el projecte PortaFIB són:

   Repositori | URL | Nom | Descripció
------------ | ------------- | ------------- | -------------
plugins-signatureserver | [plugins-signatureserver](https://github.com/GovernIB/portafib/tree/portafib-2.0/plugins-signatureserver) | Plugins de Firma en Servidor | MiniApplet com a llibreria i @firma federat
plugins-signatureweb | [plugins-signatureweb](https://github.com/GovernIB/portafib/tree/portafib-2.0/plugins-signatureweb) | Plugins de Firma via Web | MiniApplet com applet/JNLP, SIA, AutoFirma&Cliente Firma Móvil, MiniApplet en servidor, Cl@veFirma i Passarel·la PortaFIB



**Indicacions**

Al repositori principal hi ha dos _scripts_ iguals un per a linux (_.sh_) i l'altra per a windows (_.bat_) que si s'executen, compilen l'_enforcer_ i el _builder_ que es poden trobar dins el mateix repositori. Primer s'executarà l'_enforcer_ que indica si es troben tots els plugins que toca, i en cas de que no sigui així, indica quins fan falta amb un missatge d'error i el _builder_ directament, no es compila. 
En cas de tenir tots els plugins necessaris, seguit de l'_enforcer_ es compilarà el _builder_ i compilarà tot el projecte. 


**Característiques**

* Registered: 2014-08-04
* Project Labels: Plugin 
* Topic: Software Development :: Libraries
* License:  European Union Public License &  GNU General Public License version 3.0 (GPLv3)
* Database Environment: No Database Environment
* Development Status: Production/Stable
* Intended Audience: Developers & Government
* Operating System: OS Independent (Written in an interpreted language)
* Programming Language: Java & JSP


**Captures de pantalla**

![Exportació de dades a cvs,xls, ods, ...](https://github.com/GovernIB/maven/raw/binaris/pluginsib/projectinfo_Attachments/screenshots/exportdata1.png)
<br/>Exportació de dades a cvs,xls, ods, ...
 



