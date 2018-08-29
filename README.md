

# ![Logo](https://github.com/GovernIB/maven/raw/binaris/pluginsib/projectinfo_Attachments/icon.jpg) Repositori de Plugins per les Aplicacions del KitAnibal  (pluginsib-2.0)

**Descripció**

Repositori per compilar els Plugins per les Aplicacions del KitAnibal i qualsevol aplicació que vulgui reutilitzar funcionalitats configurables emprant un plugin. A més de compilar els Plugins com a conjunt, conté un script que t'indica quins plugins fan falta per tenir la llista completa. Els plugins que compila són els següents:

Repositori | URL | Nom | Descripció
------------ | ------------- | ------------- | -------------
pluginsib-core | [pluginsib-core-2.0](https://github.com/GovernIB/pluginsib-core/tree/pluginsib-core-2.0) | Core | Classes comuns i d'utilitat de PluginsIB. No conté cap llibreria de tercers. 
pluginsib-utils | [pluginsib-utils-2.0](https://github.com/GovernIB/pluginsib-utils/tree/pluginsib-utils-2.0) | Utilitats de pluginsib | Paquet que conté utilitats pels pluginsIB. Aquestes utilitats fan referència a _.jar_ de tercers.
pluginsib-userinformation | [pluginsib-userinformation-2.0](https://github.com/GovernIB/pluginsib-userinformation/tree/pluginsib-userinformation-2.0) | Informació d'usuaris | Informació de les dades d'un usuari a partir del seu username o identificador administratiu (NIF) i/o informació de Roles: BBDD o LDAP.
pluginsib-barcode | [pluginsib-barcode-2.0](https://github.com/GovernIB/pluginsib-barcode/tree/pluginsib-barcode-2.0) |  Generació de Codi de barres | barcode128, pdf417 i qrcode
pluginsib-exportdata | [pluginsib-exportdata-2.0](https://github.com/GovernIB/pluginsib-exportdata/tree/pluginsib-exportdata-2.0) | Exportació de Dades | Exporta una matriu de dades a formats com csv, excel o ods.
pluginsib-validatecertificate | [pluginsib-validatecertificate-2.0](https://github.com/GovernIB/pluginsib-validatecertificate/tree/pluginsib-validatecertificate-2.0) | Validació de Certificats Digitals | Sencill i @firma
pluginsib-csvgenerator | [pluginsib-csvgenerator-2.0](https://github.com/GovernIB/pluginsib-csvgenerator/tree/pluginsib-csvgenerator-2.0) | Generador de csv | Genera codis segurs de verificació en diferents formats.
pluginsib-documentconverter | [pluginsib-documentconverter-2.0](https://github.com/GovernIB/pluginsib-documentconverter/tree/pluginsib-documentconverter-2.0) | Conversió de Format de Documents | OpenOffice en mode servei
pluginsib-loginmodulejboss | [pluginsib-loginmodulejboss-2.0](https://github.com/GovernIB/pluginsib-loginmodulejboss/tree/pluginsib-loginmodulejboss-2.0) | Mòduls de loggin per jboss | Mòduls de loggin per jboss.
pluginsib-validatesignature | [pluginsib-validatesignature-2.0](https://github.com/GovernIB/pluginsib-validatesignature/tree/pluginsib-validatesignature-2.0) |  Informacioó i Validació de Firmes | Integr@ i @Firma
pluginsib-paymentweb | [pluginsib-paymentweb-2.0](https://github.com/GovernIB/pluginsib-paymentweb/tree/pluginsib-paymentweb-2.0) | Pagament web | Eina per fer pagants vía web.
pluginsib-timestamp | [pluginsib-timestamp-2.0](https://github.com/GovernIB/pluginsib-timestamp/tree/pluginsib-timestamp-2.0) | Timestamp | Generadors de Segell de Temps.
pluginsib-documentcustody | [pluginsib-documentcustody-2.0](https://github.com/GovernIB/pluginsib-documentcustody/tree/pluginsib-documentcustody-2.0) | Custòdia de Documents | sistema de fitxers, alfresco, Custòdia CAIB i Arxiu Digital CAIB.
pluginsib-scanweb | [pluginsib-scanweb-2.0](https://github.com/GovernIB/pluginsib-scanweb/tree/pluginsib-scanweb-2.0) | Escaneig de Documents via Web | Dynamic Web Twain, Digitalitzacio Corporativa CAIB i Applet/JNLP
pluginsib-arxiu | [pluginsib-arxiu-2.0](https://github.com/GovernIB/pluginsib-arxiu/tree/pluginsib-arxiu-2.0) | Arxiu | Gestió documental orientada a expedients

Altres PLugins que es troben per raons històriques dins el projecte PortaFIB són:

   Repositori | URL | Nom | Descripció
------------ | ------------- | ------------- | -------------
plugins-signatureserver | [plugins-signatureserver](https://github.com/GovernIB/portafib/tree/portafib-2.0/plugins-signatureserver) | Plugins de Firma en Servidor | MiniApplet com a llibreria i @firma federat
plugins-signatureweb | [plugins-signatureweb](https://github.com/GovernIB/portafib/tree/portafib-2.0/plugins-signatureweb) | Plugins de Firma via Web | MiniApplet com applet/JNLP, SIA, AutoFirma&Cliente Firma Móvil, MiniApplet en servidor, Cl@veFirma i Passarel·la PortaFIB



**Indicacions**

Primer de tot, dins un directori principal anomenat _PluginsIB_ s'hi ha de descarregar [pluginsib v2.0](https://github.com/GovernIB/pluginsib/tree/pluginsib-2.0) amb la següent comanda desde terminal:

`git clone https://github.com/GovernIB/pluginsib.git`

Al compilar-lo a través de l'script _compil.bat_ si s'està treballant amb Windows i _compil.sh_ si es treballa amb Linux, apareixerà un missatge que indicarà quins plugins falten. 

A la següent captura es veu el missatge que apareix que informa de quins plugins fan falta descarregar en cas de no tenir-los tots. 

![captura plugins](https://github.com/GovernIB/pluginsib/blob/pluginsib-2.0/resources/captura.png)

Els plugins (core, barcode, utils...) s'hauran d'anar descarregant al mateix directori on hi ha _pluginsibv2.0_. S'han de descarregar les branques 2.0 executant el següent (per exemple el _pluginib-core_):

`git clone https://github.com/GovernIB/pluginsib-core.git`


S'han de descarregar al directori principal _PluginsIB_ de la següent manera:

![arbre](https://github.com/GovernIB/pluginsib/blob/pluginsib-2.0/resources/arbre.png)

Una vegada descarregats tots els plugins, amb el mateix script, es tornarà a executar el _compil_ fins que ja no doni errors.. 



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
 



