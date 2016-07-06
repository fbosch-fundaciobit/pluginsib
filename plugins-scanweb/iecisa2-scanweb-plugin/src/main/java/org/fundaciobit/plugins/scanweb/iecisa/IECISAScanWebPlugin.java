package org.fundaciobit.plugins.scanweb.iecisa;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.fundaciobit.plugins.scanweb.api.AbstractScanWebPlugin;
import org.fundaciobit.plugins.scanweb.api.ScanWebConfig;
import org.fundaciobit.plugins.scanweb.api.ScanWebMode;
import org.fundaciobit.plugins.scanweb.api.ScanWebStatus;
import org.fundaciobit.plugins.scanweb.api.ScannedPlainFile;
import org.fundaciobit.plugins.scanweb.api.ScannedDocument;
import org.fundaciobit.plugins.utils.Metadata;

/**
 * 
 * @author anadal
 * 
 */
public class IECISAScanWebPlugin extends AbstractScanWebPlugin {

  private static final String PROPERTY_BASE = SCANWEB_BASE_PROPERTY + "iecisa.";

  public static final int HEIGHT = 350;

  /**
	 * 
	 */
  public IECISAScanWebPlugin() {
    super();
  }

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public IECISAScanWebPlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);
  }

  /**
   * @param propertyKeyBase
   */
  public IECISAScanWebPlugin(String propertyKeyBase) {
    super(propertyKeyBase);
  }


  // TODO XYZ
  public String getSubmitButton() {
    return getProperty(PROPERTY_BASE + "submitButton");
  }
  
  
  public boolean isShowInModal() {
    return "true".equals(getProperty(PROPERTY_BASE + "showInModal"));
  }
  
  
  public boolean isDebug() {
    return "true".equals(getProperty(PROPERTY_BASE + "debug"));
  }
  

  @Override
  public String getName(Locale locale) {
    return "IECISA ScanWeb";
  }


  @Override
  public boolean filter(HttpServletRequest request, ScanWebConfig config) {
    return super.filter(request, config);
  }

  @Override
  public String startScanWebTransaction(String absolutePluginRequestPath,
      String relativePluginRequestPath, HttpServletRequest request, 
      ScanWebConfig config) throws Exception {

    putTransaction(config);
    config.getStatus().setStatus(ScanWebStatus.STATUS_IN_PROGRESS);

    return relativePluginRequestPath + "/" + INDEX;
  }


  protected static final List<String> SUPPORTED_SCAN_TYPES = Collections
      .unmodifiableList(new ArrayList<String>(Arrays.asList(SCANTYPE_PDF)));

  @Override
  public List<String> getSupportedScanTypes() {
    return SUPPORTED_SCAN_TYPES;
  }

  protected static final Set<String> SUPPORTED_FLAG = Collections
      .unmodifiableSet(new HashSet<String>(Arrays.asList(FLAG_NON_SIGNED)));

  protected static final List<Set<String>> SUPPORTED_FLAGS = Collections
      .unmodifiableList(new ArrayList<Set<String>>(Arrays.asList(SUPPORTED_FLAG)));

  @Override
  public List<Set<String>> getSupportedFlagsByScanType(String scanType) {
    if (SCANTYPE_PDF.equals(scanType)) {
      return SUPPORTED_FLAGS;
    }
    return null;
  }
  
  protected static final Set<ScanWebMode> SUPPORTED_MODES = Collections
      .unmodifiableSet(new HashSet<ScanWebMode>(Arrays.asList(
          ScanWebMode.ASYNCHRONOUS, ScanWebMode.SYNCHRONOUS)));
  
  @Override
  public Set<ScanWebMode> getSupportedScanWebModes() {
    return SUPPORTED_MODES;
  }

  @Override
  public String getResourceBundleName() {
    return "iecisascanweb";
  }

  @Override
  public void requestGET(String absolutePluginRequestPath, String relativePluginRequestPath,
      long scanWebID, String query, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    requestGETPOST(absolutePluginRequestPath, relativePluginRequestPath, scanWebID, query,
        request, response, true);
  }

  @Override
  public void requestPOST(String absolutePluginRequestPath, String relativePluginRequestPath,
      long scanWebID, String query, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    requestGETPOST(absolutePluginRequestPath, relativePluginRequestPath, scanWebID, query,
        request, response, false);

  }

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------
  // ------------------- REQUEST GET-POST
  // ---------------------------------------
  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------

  /**
   * 
   */
  protected void requestGETPOST(String absolutePluginRequestPath,
      String relativePluginRequestPath, long scanWebID, String query,
      HttpServletRequest request, HttpServletResponse response, boolean isGet) {

    if (!absolutePluginRequestPath.endsWith("/")) {
      absolutePluginRequestPath = absolutePluginRequestPath + "/";
    }
    
    if (!relativePluginRequestPath.endsWith("/")) {
      relativePluginRequestPath = relativePluginRequestPath + "/";
    }
    
    ScanWebConfig fullInfo = getTransaction(scanWebID);

    if (fullInfo == null) {
      String titol = (isGet ? "GET" : "POST") + " " + getName(new Locale("ca"))
          + " PETICIO HA CADUCAT";

      requestTimeOutError(absolutePluginRequestPath, relativePluginRequestPath, query,
          String.valueOf(scanWebID), request, response, titol);

    } else {

      Locale languageUI = new Locale(fullInfo.getLanguageUI());

      if (query.startsWith(ISFINISHED_PAGE)) {
         isFinishedRequest(absolutePluginRequestPath, relativePluginRequestPath, scanWebID,
             query, request, response, fullInfo, languageUI);
      } else if (query.startsWith(INDEX)) {

        indexPage(absolutePluginRequestPath, relativePluginRequestPath, scanWebID, query,
            request, response, fullInfo, languageUI);

      } else if (query.startsWith(APPLET) || query.startsWith("img")) {

        retornarRecursLocal(absolutePluginRequestPath, relativePluginRequestPath, scanWebID,
            query, request, response, languageUI);

      } else if (query.startsWith(JNLP)) {

        jnlpPage(absolutePluginRequestPath, relativePluginRequestPath, scanWebID, query,
            request, response, languageUI);

      } else if (query.startsWith(UPLOAD_SCAN_FILE_PAGE)) {

        uploadPage(absolutePluginRequestPath, relativePluginRequestPath, scanWebID, query,
            request, response, fullInfo, languageUI);
      }  else if (query.startsWith(FINALPAGE)) {

        finalPage(absolutePluginRequestPath, relativePluginRequestPath, scanWebID, query,
            request, response, fullInfo, languageUI);
      } else {

        String titol = (isGet ? "GET" : "POST") + " " + getName(new Locale("ca"))
            + " DESCONEGUT";
        requestNotFoundError(titol, absolutePluginRequestPath, relativePluginRequestPath,
            query, String.valueOf(scanWebID), request, response, languageUI);
      }

    }

  }

  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // -------------------------------- INDEX ----------------------------------
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------

  public static final String INDEX = "index.html";

  protected void indexPage(String absolutePluginRequestPath, String relativePluginRequestPath,
      long scanWebID, String query, HttpServletRequest request, HttpServletResponse response,
      ScanWebConfig fullInfo, Locale languageUI) {

    boolean debug = isDebug();

    PrintWriter out;
    out = generateHeader(request, response, absolutePluginRequestPath,
        relativePluginRequestPath, languageUI);
    
    String tstyle = debug? "border: 2px solid red":"";
    
    out.println("  <table style=\"min-height:200px;width:100%;height:100%;" + tstyle + "\">");
    
    // ----------------  FILA DE INFORMACIO DE FITXERS ESCANEJATS
    
    out.println("  <tr valign=\"middle\">");
    out.println("    <td align=\"center\">");
    out.println("      <h3 style=\"padding:5px\">" + getTraduccio("llistatescanejats", languageUI) + "</h3>");
    
    out.println("    <table style=\"border: 2px solid black;\">");
    out.println("     <tr><td>");
    out.println("      <div id=\"escanejats\" style=\"width:400px;\">");
    out.println("        <img alt=\"Esperi\" style=\"vertical-align:middle;z-index:200\" src=\"" + absolutePluginRequestPath + "img/ajax-loader2.gif" + "\">");
    out.println("        &nbsp;&nbsp;<i>" +  getTraduccio("esperantservidor", languageUI) + "</i>");
    out.println("      </div>");
    out.println("     </td>");
    if (fullInfo.getMode() == ScanWebMode.SYNCHRONOUS) {
      out.println("<td>");
      //out.println("<br/><input type=\"button\" class=\"btn btn-success\" value=\"" + getTraduccio("final", languageUI) + "\" onclick=\"finalScanProcess()\" />");
      out.println("<button class=\"btn btn-success\" onclick=\"finalScanProcess()\">" + getTraduccio("final", languageUI) + "</button>");
      out.println("</td>");
    }
    out.println("     </tr></table>");
    

    out.println("      <br/>");
    //out.println("  <input type=\"button\" class=\"btn btn-primary\" onclick=\"gotoCancel()\" value=\"" + getTraduccio("cancel", locale) + "\">");
    out.println("    </td>");
    out.println("  </tr>");
    out.println("  <tr valign=\"middle\">");
    out.println("    <td align=\"center\">");
    
    
    
    // ------------------  APPLET O BOTO DE CARREGA D'APPLET
    
    String dstyle = debug? "border-style:double double double double;":"";
    

    out.println("<script src=\"https://www.java.com/js/deployJava.js\"></script>\n"
        + "<div style=\"" + dstyle + "\">");
    
    out.println("<center>"
        + "<script>\n"
        + "\n"
        //+ "    var isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;\n"
        + "    // Firefox 1.0+\n"
        + "    var isFirefox = typeof InstallTrigger !== 'undefined';\n"
        //+ "    // At least Safari 3+: \"[object HTMLElementConstructor]\"\n"
        //+ "    var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;\n"
        //+ "    // Internet Explorer 6-11\n"
        //+ "    var isIE = false || !!document.documentMode;\n"
        //+ "    // Edge 20+\n"
        //+ "    var isEdge = !isIE && !!window.StyleMedia;\n"
        + "    // Chrome 1+\n"
        + "    var isChrome = !!window.chrome && !!window.chrome.webstore;\n"
        //+ "    // Blink engine detection\n"
        //+ "    var isBlink = (isChrome || isOpera) && !!window.CSS;\n"
        + "\n"
        // + "    var home;\n"
        // +
        // "    home = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '"
        // + context + "';\n"
        + "\n"
        + "   function escanejarAmbFinestra() {\n"
        + "    var scan;       \n"
        + "    scan = document.getElementById('iecisa_scan');\n"
        + "    var result;\n"
        + "    result = scan.showInWindow();\n"
        + "    if (result) {\n"
        + "      alert(\"" + getTraduccio("error.nofinestra", languageUI)+ "\" + result);\n"
        + "    } else {\n" 
        + "      // OK\n"
        + "    }\n"
        + "  }\n"
        + "\n"
        + "  function downloadJNLP() {\n"
        + "     location.href=\"" + relativePluginRequestPath   + JNLP
        + "\";\n"
        + "  }\n"

        + "\n"
        // XYZ Falta 
        // + "  function enviarFitxer() {\n"
        // + "    var scan;       \n"
        // + "    scan = document.getElementById('iecisa_scan');\n"
        // + "    var result;\n"
        // + "    result = scan.upload('" + context + uploadPath + "','" +
        // fileFieldName + "');\n\n"
        // + "    if (result) {\n"
        // + "      if (result == '') {\n"
        // + "        alert(\"WARNING: No s'ha escanejat cap pagina\");\n"
        // + "      } else {\n"
        // + "        alert(\"ERROR: \" +result);\n"
        // + "      }\n"
        // + "    } else {\n"
        // + "      alert('OK');\n"
        // + "    }\n"
        // + "       \n"
        // + "  }\n"
        + "\n\n"
        // + "   alert('IS CROME? ' + isChrome);"
        + "  if (isFirefox && " + isShowInModal() + ") {\n"
        // XYZ 
        // + "    var scan;\n"
        // + "    scan = document.getElementById('iecisa_scan');\n"
        // + "    if (scan) { scan.style.display = 'none' }\n"
        + "    document.write('<input type=\"button\" value=\"" + getTraduccio("pitja", languageUI) + "\" onclick=\"escanejarAmbFinestra();\" /><br/>');\n"
        + "  }\n"
        + "\n\n"
        
        
        + "  if (!isChrome) {\n"
        + "  var attributes = {\n"
        + "    id:'iecisa_scan',\n"
        + "    code:'es.ieci.tecdoc.fwktd.applets.scan.applet.IdocApplet',\n"
        + "    archive:'"
        + absolutePluginRequestPath
        + "applet/plugin-scanweb-iecisascanweb-applet.jar',\n"
        + "    width: (isFirefox && " + isShowInModal() + ")? 1 : " + getWidth() + ",\n"        
        + "    height: (isFirefox && " + isShowInModal() + ")? 1 : " + HEIGHT  + "\n"
        + "  };\n"
        + "  var parameters = {\n"
        + "    servlet:'" + absolutePluginRequestPath + UPLOAD_SCAN_FILE_PAGE + "',\n"
        + "    fileFormName:'" + UPLOAD_SCANNED_FILE_PARAMETER
        + "'\n"
        + "  } ;\n"
        + "  deployJava.runApplet(attributes, parameters, '1.6');\n"
        + "  }; // Final Chrome\n"
        + "\n\n"

        + "  if (isChrome) {\n"
        //+ "    document.write('<br/><br/><input type=\"button\" value=\"" + getTraduccio("pitja", languageUI) + "\" onclick=\"downloadJNLP()\" />');\n"
        + "     document.write('<br/><br/><h4> S´està descarregant un arxiu jnlp."
        + " L´ha d´executar per obrir l´aplicació d´escaneig ... </h4><br/>');\n"
        + "     setTimeout(downloadJNLP, 1000);\n" // directament obrim el JNLP
        + "  }\n");
        // XYZ
        // + "  $( document ).ready(function() {\n"
        // + "    $('#" + getSubmitButton() + "')[0].onclick = null;\n"
        // + "    $('#" + getSubmitButton() + "').click(function() {  \n"
        // // TODO COM HO FEIM !!!!!!
        // + "          enviarFitxer();\n"
        // + "    });\n"
     if ((fullInfo.getMode() == ScanWebMode.SYNCHRONOUS))  { 
       out.println("  function finalScanProcess() {");
       out.println("    if (document.getElementById(\"escanejats\").innerHTML.indexOf(\"ajax\") !=-1) {");
       out.println("      if (!confirm('" + getTraduccio("noenviats", languageUI) +  "')) {");
       out.println("        return;");
       out.println("      };");
       out.println("    };");
       out.println("    location.href=\"" + relativePluginRequestPath   + FINALPAGE + "\";");
       out.println("  }\n");
     }
     
    out.println("</script>");

    //out.println("<br/><br/>");
    out.println("</center></div>");
        
    out.println("  </td></tr>");
    out.println("</table>");
    
    
    
    out.println("<script type=\"text/javascript\">");

    out.println();
    out.println("  var myTimer;");
    out.println("  myTimer = setInterval(function () {closeWhenSign()}, 20000);");
    out.println();
    out.println("  function closeWhenSign() {");
    out.println("    var request;");
    out.println("    if(window.XMLHttpRequest) {");
    out.println("        request = new XMLHttpRequest();");
    out.println("    } else {");
    out.println("        request = new ActiveXObject(\"Microsoft.XMLHTTP\");");
    out.println("    }");
    out.println("    request.open('GET', '" + absolutePluginRequestPath + ISFINISHED_PAGE + "', false);");
    out.println("    request.send();"); 
    out.println();
    out.println("    if ((request.status + '') == '" + HttpServletResponse.SC_OK + "') {");
    out.println("      clearTimeout(myTimer);");
    out.println("      myTimer = setInterval(function () {closeWhenSign()}, 4000);");
    out.println("      document.getElementById(\"escanejats\").innerHTML = 'Documents pujats:' + request.responseText;");
    out.println("    } else if ((request.status + '') == '" + HttpServletResponse.SC_REQUEST_TIMEOUT + "') {"); // 
    out.println("      clearTimeout(myTimer);");
    out.println("      window.location.href = '" + fullInfo.getUrlFinal() + "';");
    out.println("    } else {");
    out.println("      clearTimeout(myTimer);");
    out.println("      myTimer = setInterval(function () {closeWhenSign()}, 4000);");
    out.println("    }");
    out.println("  }");
    out.println();
    out.println();
    out.println("</script>");
    
    
    
    

    generateFooter(out);

  }

  public int getWidth() {
    return 550;
  }
  
  
  

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------
  // ------------------------------  IS_FINISHED   ------------------------------
  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------
  
  protected static final String ISFINISHED_PAGE = "isfinished";

  
  protected void isFinishedRequest(String absolutePluginRequestPath, String relativePluginRequestPath,
      long scanWebID, String query, HttpServletRequest request, HttpServletResponse response,
      ScanWebConfig fullInfo, Locale languageUI) {
    
    
    
    List<ScannedDocument> list = fullInfo.getScannedFiles();
    
    try {
    if (list.size() == 0) {
        response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
    } else {
      //  response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      
      if (list.size() == 1) {
        // "S'ha rebut <b>" +  list.size() + "</b> fitxer"
        response.getWriter().println(getTraduccio("rebut.1.fitxer", languageUI, String.valueOf(list.size())));
      } else {
        // "S'han rebut <b>" +  list.size() + "</b> fitxers"
        response.getWriter().println(
            getTraduccio("rebut.n.fitxers", languageUI, String.valueOf(list.size())));
      }
      response.setStatus(HttpServletResponse.SC_OK);
    }
    
    } catch (IOException e) {
      e.printStackTrace();
      try {
        response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      
      
    }
  }
  


  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // ---------------------- RECURSOS LOCALS ----------------------------------
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------

  public static final String APPLET = "applet/";
  
  
  public static final String IMG = "img/";
  


  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // ---------------------- JNLP ----------------------------------
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------

  public static final String JNLP = "jnlp/";

  protected void jnlpPage(String absolutePluginRequestPath, String relativePluginRequestPath,
      long scanWebID, String query, HttpServletRequest request, HttpServletResponse response,
      Locale languageUI) {

    String appletUrlBase = absolutePluginRequestPath + "applet/";

    log.info(" appletUrlBase = ]" + appletUrlBase + "[ ");

    String appletUrl = appletUrlBase + "plugin-scanweb-iecisascanweb-applet.jar";

    log.info(" appletUrl = ]" + appletUrl + "[ ");

    response.setContentType("application/x-java-jnlp-file");
    response.setHeader("Content-Disposition", "filename=\"ScanWebIECISA.jnlp\"");
    response.setCharacterEncoding("utf-8");

    PrintWriter out;
    try {
      out = response.getWriter();
    } catch (IOException e2) {
      log.error(e2.getMessage(), e2);
      return;
    }

    out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    out.println("<jnlp spec=\"1.0+\" codebase=\"" + appletUrl + "\" >");
    out.println("    <information>");
    out.println("        <title>ScanWeb Applet</title>");
    out.println("        <vendor>IECISA</vendor>");
    out.println("        <homepage href=\"http://www.fundaciobit.org/\" />");
    out.println("        <description>ScanWeb Applet de IECISA</description>");
    // out.println("         <icon href=\"" + absolutePluginRequestPath +
    // "/img/portafib.ico" + "\" />");
    out.println("    </information>");
    out.println("    <security>");
    out.println("        <all-permissions/>");
    out.println("    </security>");
    out.println("    <resources>");
    out.println("        <j2se version=\"1.6+\" java-vm-args=\"-Xmx1024m\" />");

    out.println("        <jar href=\"" + appletUrl + "\" main=\"true\" />");
    out.println("    </resources>");
    out.println("    <applet-desc");
    out.println("      documentBase=\"" + appletUrlBase + "\"");
    out.println("      name=\"ScanWeb Applet de IECISA\"");
    out.println("      main-class=\"es.ieci.tecdoc.fwktd.applets.scan.applet.IdocApplet\"");
    out.println("      width=\"" + getWidth() + " \"");
    out.println("      height=\"" + HEIGHT + "\">");
    out.println();

    // ---------------- GLOBALS ----------------

    out.println("       <param name=\"servlet\" value=\"" + absolutePluginRequestPath
        + UPLOAD_SCAN_FILE_PAGE + "\"/>");
    out.println("       <param name=\"fileFormName\" value=\"" + UPLOAD_SCANNED_FILE_PARAMETER
        + "\"/>");

    out.println("   </applet-desc>");
    out.println("</jnlp>");

    out.flush();

  }
  
  
  
  
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // --------------- FINAL PAGE (SINCRON MODE) -------------------------------
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------

  public static final String FINALPAGE = "finalPage";


  protected void finalPage(String absolutePluginRequestPath,
      String relativePluginRequestPath, long scanWebID, String query,
      HttpServletRequest request, HttpServletResponse response,
      ScanWebConfig fullInfo, Locale languageUI) {
    
    log.debug("Entra dins FINAL_PAGE(...");

    try {
      response.sendRedirect(fullInfo.getUrlFinal());
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  
  }
  

  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // ---------------------- UPLOAD PAGE --------------------------------------
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------

  public static final String UPLOAD_SCAN_FILE_PAGE = "upload/";

  public static final String UPLOAD_SCANNED_FILE_PARAMETER = "scannedfileparam";

  protected void uploadPage(String absolutePluginRequestPath,
      String relativePluginRequestPath, long scanWebID, String query,
      HttpServletRequest request, HttpServletResponse response,
      ScanWebConfig fullInfo, Locale languageUI) {

    log.debug("Entra dins uploadPage(...");

    Map<String, FileItem> map = super.readFilesFromRequest(request, response);

    if (map == null) {
      return;
    }

    FileItem fileItem = map.get(UPLOAD_SCANNED_FILE_PARAMETER);
    if (fileItem == null) {
      log.error(" No s'ha rebut cap fitxer amb paràmetre "
            + UPLOAD_SCANNED_FILE_PARAMETER);
      return;
    }

    byte[] data;
    try {
      data = IOUtils.toByteArray(fileItem.getInputStream());
    } catch (IOException e) {
      log.error(" No s'ha pogut llegir del request el fitxer amb paràmetre "
          + UPLOAD_SCANNED_FILE_PARAMETER);
      return;
    }

    String name = fileItem.getName();
    if (name != null) {
      name = FilenameUtils.getName(name);
    }
    String mime = fileItem.getContentType();
    if (mime == null) {
      mime = "application/pdf";
    }
    
    final Date date = new Date(System.currentTimeMillis());
    
    List<Metadata> metadatas = new ArrayList<Metadata>();
    metadatas.add(new Metadata("TipoDocumental", "TD99"));
    metadatas.add(new Metadata("EstadoElaboracion", "EE99"));
    metadatas.add(new Metadata("Identificador", Calendar.getInstance().get(Calendar.YEAR)
        + "_" + fullInfo.getScannedFiles().size() + scanWebID));
    metadatas.add(new Metadata("FechaCaptura", date));
    metadatas.add(new Metadata("VersionNTI", "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e"));
    

    ScannedPlainFile singleScanFile = new ScannedPlainFile(name, mime, data);

    ScannedDocument scannedDoc = new ScannedDocument();
    scannedDoc.setMetadatas(metadatas);
    scannedDoc.setScannedSignedFile(null);
    scannedDoc.setScanDate(date);
    scannedDoc.setScannedPlainFile(singleScanFile);


    fullInfo.getScannedFiles().add(scannedDoc);
  }


}