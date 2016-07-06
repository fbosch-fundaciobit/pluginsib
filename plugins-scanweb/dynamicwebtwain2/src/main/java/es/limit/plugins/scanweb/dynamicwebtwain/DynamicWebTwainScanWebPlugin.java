package es.limit.plugins.scanweb.dynamicwebtwain;

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
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.scanweb.api.AbstractScanWebPlugin;
import org.fundaciobit.plugins.scanweb.api.IScanWebPlugin;
import org.fundaciobit.plugins.scanweb.api.ScanWebConfig;
import org.fundaciobit.plugins.scanweb.api.ScanWebMode;
import org.fundaciobit.plugins.scanweb.api.ScanWebStatus;
import org.fundaciobit.plugins.scanweb.api.ScannedDocument;
import org.fundaciobit.plugins.scanweb.api.ScannedPlainFile;
import org.fundaciobit.plugins.utils.Metadata;

/**
 * 
 * @author LIMIT 
 * @author anadal (Adaptar a API 2.0.0)
 * 
 */
public class DynamicWebTwainScanWebPlugin extends AbstractScanWebPlugin implements IScanWebPlugin {

	protected final Logger log = Logger.getLogger(getClass());

	private static final String PROPERTY_BASE = SCANWEB_BASE_PROPERTY + "dynamicwebtwain.";
	//private static Map<String, Properties> missatges = new HashMap<String, Properties>();

	/**
	 * 
	 */
	public DynamicWebTwainScanWebPlugin() {
		super();
	}

  public boolean isDebug() {
    return "true".equals(getProperty(PROPERTY_BASE + "debug"));
  }
	


	private String getDynamicWebTwainProperty(String name) {
		return getProperty(PROPERTY_BASE + name);
	}
	


	
	// XYZ Substituir per getProperty()
	protected String getDynamicWebTwainProperty(String name, String defaultValue) {
		return getProperty(PROPERTY_BASE + name, defaultValue);
	}


	/**
	 * @param propertyKeyBase
	 * @param properties
	 */
	public DynamicWebTwainScanWebPlugin(String propertyKeyBase, Properties properties) {
		super(propertyKeyBase, properties);
	}

	/**
	 * @param propertyKeyBase
	 */
	public DynamicWebTwainScanWebPlugin(String propertyKeyBase) {
		super(propertyKeyBase);
	}

	@Override
	public String getName(Locale locale)  {
		return "DynamicWebTwain";
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
    return "dynamicwebtwain";
  }
  
  @Override
  protected void getJavascriptCSS(HttpServletRequest request,
      String absolutePluginRequestPath, String relativePluginRequestPath, PrintWriter out,
      Locale languageUI) {
    
    // XYZ
    out.println("<script type=\"text/javascript\" src=\"" + relativePluginRequestPath + "scanner/jquery.js\"></script>");
    //out.println("<script src=\"" + relativePluginRequestPath + "js/registroCompulsaExt.js\" type=\"text/javascript\"></script>");
    
    //String applicationPath = getDynamicWebTwainProperty("applicationPath", "regweb");
    out.println("<script type=\"text/javascript\" src=\"" + relativePluginRequestPath + "scanner/dynamsoft.webtwain.initiate.js\"> </script>");
    out.println("<script type=\"text/javascript\" src=\"" + relativePluginRequestPath + "scanner/dynamsoft.webtwain.config.js\"> </script>");
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
  // ------------------- REQUEST GET-POST ---------------------------------------
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

      } else if (query.startsWith("scanner")) {

        // RECURSOS
        retornarRecursLocal(absolutePluginRequestPath, relativePluginRequestPath, scanWebID,
            query, request, response, languageUI);
        
      } else if (query.startsWith(UPLOAD_PAGE)) {

        uploadPage(absolutePluginRequestPath, relativePluginRequestPath, scanWebID,
            query, request, response, fullInfo, languageUI);
        
      } else if (query.startsWith(FINALPAGE)) {

        finalPage(absolutePluginRequestPath, relativePluginRequestPath, scanWebID,
            query, request, response, fullInfo, languageUI);
      
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

    PrintWriter out;
    out = generateHeader(request, response, absolutePluginRequestPath,
        relativePluginRequestPath, languageUI);
    
   
 // Carregam els texts en català per si hi ha algun problema al 
    // carregar els fitxers de missatges multiidioma
    String disp = "Dispositiu";
    String color = "Color";
    String res = "Resolució";
    String duplex = "Duplex";
    String clean = "Borra actual";
    String cleanAll = "Borra tot";
    String msgErrorValidacio = "Hi ha errors en el camp del formulari.";
    String upError = "S\\'ha produït un error, i no s\\'ha pogut pujar el document escanejat.";

//    String idAnex = request.getParameter("anexo.id");
/*  XYZ
    String lang = "ca";
    if (request != null) {
      lang = request.getParameter("lang");
      if (lang == null)
        lang = (String)request.getAttribute("lang");
    }
    if (lang != null) {
      // Carregam el fitxer de missatges, si encara no estava carregat
      if (!missatges.containsKey(lang)) {
        Properties prop = loadMissatgesProperties(lang);
        if (prop != null)
          missatges.put(lang, prop);
      }
      if ( missatges.containsKey(lang)) {
        Properties prop = missatges.get(lang);
        */
        disp = getTraduccio("dwt.dispositiu", languageUI);
        color = getTraduccio("dwt.color", languageUI);
        res = getTraduccio("dwt.resolucio", languageUI);
        duplex = getTraduccio("dwt.duplex", languageUI);
        clean = getTraduccio("dwt.borra.actual", languageUI);
        cleanAll = getTraduccio("dwt.borra.tot", languageUI);
        upError = getTraduccio("dwt.error.upload", languageUI);
        msgErrorValidacio = getTraduccio("dwt.error.validacio", languageUI);
        String pujarServidor = getTraduccio("pujarServidor", languageUI);
      
        // Taula que ho engloba tot
        out.println("<table><tr><td valign=\"top\">\n");  
        
        out.println("  <table style=\"min-height:200px;width:100%;height:100%;\">");
        
        // ----------------  FILA DE INFORMACIO DE FITXERS ESCANEJATS
        
        out.println("  <tr >");
        out.println("    <td align=\"center\">");
        //out.println("      <h3 style=\"padding:5px\">" + getTraduccio("llistatescanejats", languageUI) + "</h3>");
        
        out.println("    <table style=\"border: 2px solid black;\">");
        out.println("     <tr><td align=\"center\">");
        out.println("      <div id=\"escanejats\" style=\"width:350px;\">");
        
        out.println("        <img alt=\"Esperi\" style=\"vertical-align:middle;z-index:200\" src=\"" + absolutePluginRequestPath + "scanner/ajax-loader2.gif" + "\"><br/>");
          
        out.println("        <i>" +  getTraduccio("esperantservidor", languageUI) + "</i>");
        out.println("      </div>");
        out.println("     </td>");
        if (fullInfo.getMode() == ScanWebMode.SYNCHRONOUS) {
          out.println("</tr><tr><td align=\"center\">");
          //out.println("<br/><input type=\"button\" class=\"btn btn-success\" value=\"" + getTraduccio("final", languageUI) + "\" onclick=\"finalScanProcess()\" />");
          out.println("<br/><button class=\"btn btn-success\" onclick=\"finalScanProcess()\">" + getTraduccio("final", languageUI) + "</button>");
          out.println("</td>");
        }
        
        out.println("     </tr></table>");
        
        
        out.println("      <br/>");
        //out.println("  <input type=\"button\" class=\"btn btn-primary\" onclick=\"gotoCancel()\" value=\"" + getTraduccio("cancel", locale) + "\">");
        out.println("    </td>");
        out.println("  </tr>");
        out.println("  </table>");
       

        
        out.println("<script type=\"text/javascript\">");
        
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
        out.println("      document.getElementById(\"escanejats\").innerHTML = '" + getTraduccio("docspujats", languageUI) + ":' + request.responseText;");
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
        
        

    StringBuffer bufferOutput = new StringBuffer();
    bufferOutput.append(  "<script>");
    bufferOutput.append(  " Dynamsoft.WebTwainEnv.RegisterEvent('OnWebTwainReady', Dynamsoft_OnReady);\n");
    bufferOutput.append(  " var DWObject;\n");
    bufferOutput.append(  " function Dynamsoft_OnReady() {\n"); 
    bufferOutput.append(  "   DWObject = Dynamsoft.WebTwainEnv.GetWebTwain('dwtcontrolContainer'); // Get the Dynamic Web TWAIN object that is embeded in the div with id 'dwtcontrolContainer'\n"); 
    bufferOutput.append(  "   if (DWObject) {\n"); 
    bufferOutput.append(  "     var count = DWObject.SourceCount\n;"); 
    bufferOutput.append(  "     for (var i = 0; i < count; i++)\n"); 
    bufferOutput.append(  "       document.getElementById('scanSource').options.add(new Option(DWObject.GetSourceNameItems(i), i));\n");
    bufferOutput.append(  "     $(\"#scanSource\").trigger(\"chosen:updated\");\n");
    bufferOutput.append(  "   }\n" ); 
    bufferOutput.append(  " }\n");
    bufferOutput.append(  "\n"); 
    bufferOutput.append(  " function OnSuccess() {\n"); 
    bufferOutput.append(  "   console.log('successful');\n"); 
    bufferOutput.append(  " }\n");
    bufferOutput.append(  "\n");
    bufferOutput.append(  " function OnFailure(errorCode, errorString) {\n"); 
    bufferOutput.append(  "   console.log(errorString);\n");
    bufferOutput.append(  " }\n");
    bufferOutput.append(  "\n");
    bufferOutput.append(  " function AcquireImage() {\n");
//    bufferOutput.append(  "   debugger;\n");
    bufferOutput.append(  "   if (DWObject) {\n"); 
    bufferOutput.append(  "     DWObject.SelectSourceByIndex(document.getElementById('scanSource').selectedIndex);\n"); 
    bufferOutput.append(  "     DWObject.OpenSource();\n"); 
    bufferOutput.append(  "     DWObject.IfDisableSourceAfterAcquire = true;\n");
    bufferOutput.append(  "     if (document.getElementById('scanColor').value == 'N'){\n" ); 
    bufferOutput.append(  "       DWObject.PixelType = EnumDWT_PixelType.TWPT_BW;\n"); 
    bufferOutput.append(  "     } else if (document.getElementById('scanColor').value == 'G'){\n"); 
    bufferOutput.append(  "       DWObject.PixelType = EnumDWT_PixelType.TWPT_GRAY;\n" );
    bufferOutput.append(  "     } else { //if (document.getElementById('scanColor').value == 'C'){\n"); 
    bufferOutput.append(  "       DWObject.PixelType = EnumDWT_PixelType.TWPT_RGB;\n" );
    bufferOutput.append(  "     }\n");
    bufferOutput.append(  "     if (DWObject.Duplex > 0 && document.getElementById('scanDuplex').value == '2'){\n" ); 
    bufferOutput.append(  "       DWObject.IfDuplexEnabled = true;\n"); 
    bufferOutput.append(  "     } else {\n"); 
    bufferOutput.append(  "       DWObject.IfDuplexEnabled = false;\n"); 
    bufferOutput.append(  "     }\n");
    bufferOutput.append(  "     DWObject.IfFeederEnabled = false;\n"); 
    bufferOutput.append(  "     DWObject.IfShowUI = false;\n");
    bufferOutput.append(  "     DWObject.IfAutoDiscardBlankpages = true;\n");
    bufferOutput.append(  "     DWObject.Resolution = parseInt(document.getElementById('scanResolution').value);\n" ); 
    bufferOutput.append(  "     DWObject.AcquireImage();\n"); 
//    bufferOutput.append(  "     Dynamsoft_OnReady();\n");
//    bufferOutput.append(  "     alert('Ha sortit de AcquireImage interna.');\n");
    bufferOutput.append(  "   }\n");
    bufferOutput.append(  " }\n");
    bufferOutput.append(  "\n");
    bufferOutput.append(  " function btnRemoveSelectedImage_onclick() {\n");
    bufferOutput.append(  "   if (DWObject) {\n");
    bufferOutput.append(  "     DWObject.RemoveAllSelectedImages();\n");
    bufferOutput.append(  "   }\n");
    bufferOutput.append(  " }\n");
    bufferOutput.append(  "\n");
    bufferOutput.append(  " function btnRemoveAllImages_onclick() {\n");
    bufferOutput.append(  "   if (DWObject) {\n");
    bufferOutput.append(  "     DWObject.RemoveAllImages();\n");
    bufferOutput.append(  "   }\n");
    bufferOutput.append(  " }\n");
    bufferOutput.append(  "\n");
    bufferOutput.append(  " function ResetScan() {\n" );
    bufferOutput.append(  "   if (DWObject) {\n");
    bufferOutput.append(  "     DWObject.RemoveAllImages();\n");
    bufferOutput.append(  "   }\n");
    bufferOutput.append(  "   $('#pestanyes a:first').tab('show')\n");
    bufferOutput.append(  " }\n");
    bufferOutput.append(  "\n");
    bufferOutput.append(  " function UploadScan() {\n");
    bufferOutput.append(  "   if (DWObject) {\n" );
    bufferOutput.append(  "     if (DWObject.HowManyImagesInBuffer == 0) {\n");
    //    bufferOutput.append(  "       if ($('#archivo').val() == \"\"){\n");
    //    bufferOutput.append(  "         alert('No ha adjuntat cap fitxer ni escanejat cap document.')\n");
    //    bufferOutput.append(  "         return false;\n");
    //    bufferOutput.append(  "       } else {\n");
    bufferOutput.append(  "       return true;\n");
    //    bufferOutput.append(  "       }\n");
    bufferOutput.append(  "     }\n");
    bufferOutput.append(  "     var strHTTPServer = location.hostname;\n" ); 
    bufferOutput.append(  "     var CurrentPathName = unescape(location.pathname);\n" );
    bufferOutput.append(  "     var path = CurrentPathName.substring(0, CurrentPathName.lastIndexOf('/'));\n" );
//    bufferOutput.append(  "     var idAnex = path.substring(path.lastIndexOf('/') + 1);\n" );
    //bufferOutput.append(  "     var CurrentPath = '/" + getDynamicWebTwainProperty("applicationPath", "regweb") + "';\n" );
    //bufferOutput.append(  "     var strActionPage = CurrentPath + '/" + getDynamicWebTwainProperty("guardarScanPath", "anexo/guardarScan") + "/" + scanWebID + "';\n" ); 
    
    bufferOutput.append(  "     var strActionPage = '/" + relativePluginRequestPath + UPLOAD_PAGE + "';\n" );
    
    bufferOutput.append(  "     DWObject.IfSSL = false; // Set whether SSL is used\n" );
    
    // TODO XYZ Extreure host i port de la URL ABSOLUTA !!!! 
    
    bufferOutput.append(  "     DWObject.HTTPPort = location.port == '' ? 80 : location.port;\n" );
    bufferOutput.append(  "     var Digital = new Date();\n");
    bufferOutput.append(  "     var uploadfilename = Math.floor(new Date().getTime() / 1000) // Uses milliseconds according to local time as the file name\n" ); 
    bufferOutput.append(  "     var result = DWObject.HTTPUploadAllThroughPostAsPDF(strHTTPServer, strActionPage, uploadfilename + '.pdf');\n" );
    bufferOutput.append(  "     if (!result) {\n");
    bufferOutput.append(  "       alert('" + upError + "');\n");
    bufferOutput.append(  "       return false;\n");
    bufferOutput.append(  "     }\n");
    bufferOutput.append(  "   }\n");
    bufferOutput.append(  "   return true;\n");
    bufferOutput.append(  " }\n");
    
    
    // TODO XYZ S'HA DE CANVIAR O BORRAR (TE SENTIT ???)
    String boto =  getDynamicWebTwainProperty("idBotoDesaAnnex"); //, "desaAnnex");
    if (boto != null) {
      bufferOutput.append(  " $( document ).ready(function() {\n");
      bufferOutput.append(  "   $('#"+ boto +"')[0].onclick = null;\n");
      bufferOutput.append(  "   $('#"+ boto +"').click(function() {  \n");
    
      
      bufferOutput.append(  "         pujarServidor();\n");
      
  //    bufferOutput.append(  "       procesarAnexo('" + request.getLocale() + "');\n");
  //    bufferOutput.append(  "     }\n");
      bufferOutput.append(  "   });\n");
      bufferOutput.append(  " });\n");
    }
    
    
    bufferOutput.append(  " function pujarServidor() {\n");
    
    if (getDynamicWebTwainProperty("scriptValidacioJS") != null) {
      bufferOutput.append(  "   if (DWObject) {\n" );
      bufferOutput.append(  "     if (DWObject.HowManyImagesInBuffer > 0) {\n");      
      bufferOutput.append(  "       if("+getDynamicWebTwainProperty("scriptValidacioJS")+") {\n");
      bufferOutput.append(  "         UploadScan();\n");
      bufferOutput.append(  "       }else{\n");
      bufferOutput.append(  "         alert('"+msgErrorValidacio+"');\n");
      bufferOutput.append(  "         return false;\n");
      bufferOutput.append(  "       }\n");
      bufferOutput.append(  "     }\n");
      bufferOutput.append(  "   }\n");
    }else{
      bufferOutput.append(  "     UploadScan();\n");
    }
    
    bufferOutput.append(  " };\n");
    
    bufferOutput.append(  "</script>");
    bufferOutput.append(  "\n");
    
    
    
    
    
    
    bufferOutput.append(  "<div id=\"scanParams\" class=\"col-xs-6\">\n");
    bufferOutput.append(  " <div id=\"scanSourceGroup\" class=\"form-group col-xs-12\">\n");
    bufferOutput.append(  "   <div class=\"col-xs-4 pull-left etiqueta_regweb control-label\">\n");
    bufferOutput.append(  "     <label for=\"scanSource\"><span class=\"text-danger\">*</span> " + disp + "</label>\n");
    bufferOutput.append(  "     </div>\n");
    bufferOutput.append(  "     <div class=\"col-xs-8\">\n");
    bufferOutput.append(  "       <select size=\"1\" id=\"scanSource\" class=\"chosen-select\">\n");
    bufferOutput.append(  "       </select>\n");
    bufferOutput.append(  "   </div>\n");
    bufferOutput.append(  " </div>\n");
    bufferOutput.append(  "\n");
    bufferOutput.append(  " <div id=\"scanColorGroup\" class=\"form-group col-xs-12\">\n");
    bufferOutput.append(  "   <div class=\"col-xs-4 pull-left etiqueta_regweb control-label\">\n");
    bufferOutput.append(  "     <label for=\"scanColor\"><span class=\"text-danger\">*</span> " + color + "</label>\n");
    bufferOutput.append(  "     </div>\n");
    bufferOutput.append(  "     <div class=\"col-xs-8\">\n");
    bufferOutput.append(  "       <select size=\"1\" id=\"scanColor\" class=\"chosen-select\">\n");
    bufferOutput.append(  "       <option value='N'>B/N</option>");
    bufferOutput.append(  "       <option value='G' selected='selected'>Gris</option>");
    bufferOutput.append(  "       <option value='C'>Color</option>");
    bufferOutput.append(  "       </select>\n");
    bufferOutput.append(  "   </div>\n");
    bufferOutput.append(  " </div>\n");
    bufferOutput.append(  "\n");
    bufferOutput.append(  " <div id=\"scanResolutionGroup\" class=\"form-group col-xs-12\">\n");
    bufferOutput.append(  "   <div class=\"col-xs-4 pull-left etiqueta_regweb control-label\">\n");
    bufferOutput.append(  "     <label for=\"scanResolution\"><span class=\"text-danger\">*</span> " + res + "</label>\n");
    bufferOutput.append(  "     </div>\n");
    bufferOutput.append(  "     <div class=\"col-xs-8\">\n");
    bufferOutput.append(  "       <select size=\"1\" id=\"scanResolution\" class=\"chosen-select\">\n");
    bufferOutput.append(  "       <option value='200' selected='selected'>200</option>");
    bufferOutput.append(  "       <option value='300'>300</option>");
    bufferOutput.append(  "       <option value='400'>400</option>");
    bufferOutput.append(  "       <option value='600'>600</option>");
    bufferOutput.append(  "       </select>\n");
    bufferOutput.append(  "   </div>\n");
    bufferOutput.append(  " </div>\n");
    bufferOutput.append(  "\n");
    bufferOutput.append(  " <div id=\"scanDuplexGroup\" class=\"form-group col-xs-12\">\n");
    bufferOutput.append(  "   <div class=\"col-xs-4 pull-left etiqueta_regweb control-label\">\n");
    bufferOutput.append(  "     <label for=\"scanDuplex\"><span class=\"text-danger\">*</span> " + duplex + "</label>\n");
    bufferOutput.append(  "     </div>\n");
    bufferOutput.append(  "     <div class=\"col-xs-8\">\n");
    bufferOutput.append(  "       <select size=\"1\" id=\"scanDuplex\" class=\"chosen-select\">\n");
    bufferOutput.append(  "       <option value='1' selected='selected'>Una cara</option>");
    bufferOutput.append(  "       <option value='2'>Doble cara</option>");
    bufferOutput.append(  "       </select>\n");
    bufferOutput.append(  "   </div>\n");
    bufferOutput.append(  " </div>\n");
    bufferOutput.append(  "\n");

    bufferOutput.append(  " <div id=\"scanButtonsGroup\" class=\"form-group col-xs-12\">\n");
    bufferOutput.append(  "   <div class=\"col-xs-4 pull-left etiqueta_regweb control-label\"></div>\n");
    bufferOutput.append(  "     <div class=\"col-xs-8\">\n");
    bufferOutput.append(  "<br/>");
    bufferOutput.append(  "     <button class=\"btn btn-sm\" type=\"button\" value='Scan' onclick='AcquireImage();return false;' >Scan</button>");
    bufferOutput.append(  "     <button class=\"btn btn-sm\" type=\"button\" value='" + clean + "' onclick='pujarServidor();' >" + pujarServidor +"</button>");
    bufferOutput.append(  "<br/>");    
    bufferOutput.append(  "     <button class=\"btn btn-sm\" type=\"button\" value='" + cleanAll + "' onclick='btnRemoveAllImages_onclick();' >" + cleanAll + "</button>");
    bufferOutput.append(  "     <button class=\"btn btn-sm\" type=\"button\" value='" + clean + "' onclick='btnRemoveSelectedImage_onclick();' >" + clean +"</button>");
   
    bufferOutput.append(  "   </div>\n");
    bufferOutput.append(  " </div>\n");
    bufferOutput.append(  "\n");
    bufferOutput.append(  "</div>");
    bufferOutput.append(  "\n");
    
    
    bufferOutput.append(" </td><td>\n");
    
    
    bufferOutput.append(  "<div id=\"scanContainerGroup\" class=\"col-xs-6\" style=\"margin-bottom: 5px;\">\n");
    bufferOutput.append(  " <div id='dwtcontrolContainer'></div>");
    bufferOutput.append(  "</div>");
    
    bufferOutput.append(" </td></tr><table>\n");
    
    out.println(bufferOutput.toString());

    generateFooter(out);
    
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

  public static final String UPLOAD_PAGE = "upload";


  protected void uploadPage(String absolutePluginRequestPath,
      String relativePluginRequestPath, long scanWebID, String query,
      HttpServletRequest request, HttpServletResponse response,
      ScanWebConfig fullInfo, Locale languageUI) {

    log.debug("Entra dins uploadPage(...");

    Map<String, FileItem> map = super.readFilesFromRequest(request, response);

    if (map == null || map.size() == 0) {
      log.error(" S'ha cridat a " + UPLOAD_PAGE + " però no s'ha enviat cap arxiu !!!!");
      return;
    }

    // Recollim la primera entrada
    Entry<String,FileItem> entry = new TreeMap<String, FileItem>(map).firstEntry(); 
    FileItem fileItem = entry.getValue();
    
    final String nomFitxer = entry.getKey();
    log.info("UPLOAD:: Processant fitxer amb nom " + nomFitxer);
    

    byte[] data;
    try {
      data = IOUtils.toByteArray(fileItem.getInputStream());
    } catch (IOException e) {
      log.error(" No s'ha pogut llegir del request el fitxer amb paràmetre "
          + nomFitxer);
      return;
    }

    String name = fileItem.getName();
    if (name != null) {
      name = FilenameUtils.getName(name);
    }
    /*
    String mime = fileItem.getContentType();
    if (mime == null) {
      mime = "application/pdf";
    }
    */
    String mime = "application/pdf";
  
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
    
    log.info("UPLOAD:: FINAL ");
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
  
  

	
	/* XYZ
	
	@Override
	public String getCoreJSP(HttpServletRequest request, long docID) throws Exception {
		
		

		return 	bufferOutput.toString();

	}

	@Override
	public ScanWebResource getResource(HttpServletRequest request, String resourcename, long docID) throws Exception {
		ScanWebResource resource = null;
		byte[] contingut = null;
		String mime = getMimeType(resourcename);
		String name = getName(resourcename);

		InputStream input = getClass().getResourceAsStream("/es/limit/plugins/scanweb/dynamicwebtwain/scanner/" + resourcename);

		//	  if ("initiate".equalsIgnoreCase(resourcename)) {
		//		  input = getClass().getResourceAsStream("/es/limit/plugins/scanweb/dynamicwebtwain/scanner/dynamsoft.webtwain.initiate.js");
		//		  mime = "text/javascript";
		//		  name = "initiate.js";
		//	  } else if ("dynamsoft.webtwain.config.js".equalsIgnoreCase(resourcename)) {
		//		  input = getClass().getResourceAsStream("/es/limit/plugins/scanweb/dynamicwebtwain/scanner/dynamsoft.webtwain.config.js");
		//		  mime = "text/javascript";
		//		  name = "config.js";
		//	  }
		if (input != null)
			try {
				contingut = IOUtils.toByteArray(input);
				if( "dynamsoft.webtwain.config.js".equalsIgnoreCase(resourcename)){
					String outputResource = new String(contingut, "UTF-8");
					outputResource = outputResource.replaceAll("regweb", getDynamicWebTwainProperty("applicationPath", "regweb"));
					contingut = outputResource.getBytes("UTF-8");
				}
				resource = new ScanWebResource(name, mime, contingut);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return resource;
	}

	private String getMimeType(String resourcename) {
		String mime = "text/plain";
		if (resourcename != null && !"".equals(resourcename)) {
			String type = resourcename.substring(resourcename.lastIndexOf(".") + 1);
			if ("cab".equalsIgnoreCase(type)) {
				mime="application/octet-stream";
			} else if ("exe".equalsIgnoreCase(type)) {
				mime = "application/octet-stream";
			} else if ("pkg".equalsIgnoreCase(type)) {
				mime="application/octet-stream";
			} else if ("msi".equalsIgnoreCase(type)) {
				mime="application/octet-stream";
			} else if ("js".equalsIgnoreCase(type)) {
				mime="text/javascript";
			} else if ("zip".equalsIgnoreCase(type)) {
				mime="application/zip";
			} else if ("css".equalsIgnoreCase(type)) {
				mime="text/css";
			} else if ("gif".equalsIgnoreCase(type)) {
				mime="image/gif";
			} else if ("png".equalsIgnoreCase(type)) {
				mime="image/png";
			}
		}
		return mime;
	}

	private String getName(String resourcename) {
		String name = "";
		if (resourcename != null && !"".equals(resourcename)) {
			name = resourcename.substring(resourcename.lastIndexOf("/") + 1);
		}
		return name;
	}

	private Properties loadMissatgesProperties(String lang) {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = getClass().getResourceAsStream("/es/limit/plugins/scanweb/dynamicwebtwain/missatges_" + lang +".properties");

			// carrega el fitxer de properties
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}
*/


}