package es.fundaciobit.plugins.scanweb.iecisa;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.fundaciobit.plugins.scanweb.IScanWebPlugin;
import org.fundaciobit.plugins.scanweb.ScanWebResource;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

/**
 * 
 * @author anadal
 * 
 */
public class IECISAScanWebPlugin extends AbstractPluginProperties implements IScanWebPlugin {

	protected final Logger log = Logger.getLogger(getClass());

	private static final String PROPERTY_BASE = SCANWEB_BASE_PROPERTY + "iecisa.";


	 public static final int HEIGHT =  350;
	

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


  
  public String getResourcePath() {
    return getProperty(PROPERTY_BASE + "resourcePath");
  }

  public String getUploadPath() {
    return getProperty(PROPERTY_BASE + "uploadPath");
  }

  public String getFileFieldName() {
    return getProperty(PROPERTY_BASE + "fileFieldName");
  }

  public String getSubmitButton() {
    return getProperty(PROPERTY_BASE + "submitButton");
  }
	


	/**
	 * @param propertyKeyBase
	 */
	public IECISAScanWebPlugin(String propertyKeyBase) {
		super(propertyKeyBase);
	}

	@Override
	public String getName(Locale locale) throws Exception {
		return "IECISA ScanWeb";
	}

	@Override
	public void controllerGET(HttpServletRequest request, long docID) throws Exception {
		// TODO
	}

	@Override
	public void controllerPOST(HttpServletRequest request, long docID) throws Exception {
		// TODO
	}


	@Override
	public String getHeaderJSP(HttpServletRequest request, long docID) throws Exception {
    return "";
	}
	


	@Override
	public String getCoreJSP(HttpServletRequest request, long docID) throws Exception {

    String uploadPath = getUploadPath();
    uploadPath = uploadPath.replace("{0}", String.valueOf(docID));
    
    String resourcePath = getResourcePath();
    resourcePath = resourcePath.replace("{0}", String.valueOf(docID));
    
    String context = request.getContextPath();
    
    String home = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +  context;
    
    String fileFieldName = getFileFieldName();
    return  "<script src=\"https://www.java.com/js/deployJava.js\"></script>\n"
          + "<div style=\"border-style:double double double double;\">"
          + "<center>"
          + "<script>\n"
          + "\n"
          + "    var isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;\n"
          + "    // Firefox 1.0+\n"
          + "    var isFirefox = typeof InstallTrigger !== 'undefined';\n"
          + "    // At least Safari 3+: \"[object HTMLElementConstructor]\"\n"
          + "    var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;\n"
          + "    // Internet Explorer 6-11\n"
          + "    var isIE = /*@cc_on!@*/false || !!document.documentMode;\n"
          + "    // Edge 20+\n"
          + "    var isEdge = !isIE && !!window.StyleMedia;\n"
          + "    // Chrome 1+\n"
          + "    var isChrome = !!window.chrome && !!window.chrome.webstore;\n"
          + "    // Blink engine detection\n"
          + "    var isBlink = (isChrome || isOpera) && !!window.CSS;\n"
          + "\n"
//          + "    var home;\n"
//          + "    home = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '" + context + "';\n"
          + "\n"
          + "   function escanejarAmbFinestra() {\n"
          + "    var scan;       \n"
          + "    scan = document.getElementById('iecisa_scan');\n"
          + "    var result;\n"
          + "    result = scan.showInWindow();\n"
          + "    if (result) {\n"
          + "      alert(\"ERROR: NO es pot mostrar l'utilitat d'escaneig en una finestra:\" + result);\n"
          + "    } else {\n"
          + "      // OK\n"
          + "    }\n"
          + "  }\n"
          + "\n"
          + "   function downloadJNLP() {\n"
          + "     location.href=\"" + home + resourcePath + "jnlp\";\n"
          + "  }\n"
          
          + "\n"
//          + "  function enviarFitxer() {\n"
//          + "    var scan;       \n"
//          + "    scan = document.getElementById('iecisa_scan');\n"
//          + "    var result;\n"
//          + "    result = scan.upload('" + context + uploadPath + "','" + fileFieldName + "');\n\n"
//          + "    if (result) {\n"
//          + "      if (result == '') {\n"
//          + "        alert(\"WARNING: No s'ha escanejat cap pagina\");\n"
//          + "      } else {\n"
//          + "        alert(\"ERROR: \" +result);\n"
//          + "      }\n"
//          + "    } else {\n"
//          + "      alert('OK');\n"
//          + "    }\n"
//          + "       \n"
//          + "  }\n"
          + "\n\n"
          //+ "   alert('IS CROME? ' + isChrome);"
          + "  if (isFirefox) {\n"
          //+ "    var scan;\n"
          //+ "    scan = document.getElementById('iecisa_scan');\n"
          //+ "    if (scan) { scan.style.display = 'none' }\n"
          + "    document.write('<input type=\"button\" value=\"Pitja per Escanejar\" onclick=\"escanejarAmbFinestra();\" />');\n"
          + "  }\n"
          + "\n\n"
          + "  if (!isChrome) {\n"
          + "  var attributes = {\n"
          + "    id:'iecisa_scan',\n"
          + "    code:'es.ieci.tecdoc.fwktd.applets.scan.applet.IdocApplet',\n"
          + "    archive:'" + home + resourcePath + "applet/plugin-scanweb-iecisascanweb-applet.jar',\n"
          + "    width: " + getWidth() + ",\n" // isFirefox? 1 :
          + "    height: " + HEIGHT + "\n" // isFirefox? 1 : 
          + "  };\n"
          + "  var parameters = {\n"
          + "    servlet:'" + home + uploadPath + "',\n"
          + "    fileFormName:'" + fileFieldName + "'\n"
          + "  } ;\n"
          + "  deployJava.runApplet(attributes, parameters, '1.6');\n"
          + "  }; // Final Chrome\n"
          + "\n\n"
          
          + "  if (isChrome) {\n"
          + "    document.write('<input type=\"button\" value=\"Pitja per Escanejar\" onclick=\"downloadJNLP()\" />');\n"
          + "  }\n"
          + "\n"
//          + "  $( document ).ready(function() {\n"
//          + "    $('#" + getSubmitButton() + "')[0].onclick = null;\n"
//          + "    $('#" + getSubmitButton() + "').click(function() {  \n"
//          // TODO COM HO FEIM !!!!!!
//          + "          enviarFitxer();\n"
//          + "    });\n"

          + "</script></center></div>\n";


	}

	public int getWidth() {
	 return 550;
	}
	
	
	@Override
	public ScanWebResource getResource(HttpServletRequest request, String resourcePath, long docID) throws Exception {
		ScanWebResource resource = null;
		byte[] contingut = null;
		String mime = getMimeType(resourcePath);
		resourcePath = resourcePath.replace('\\', '/');
		
		if(resourcePath.startsWith("jnlp") || resourcePath.contains("jnlp")) {
		  return jnlpGet(request, docID);
		} 

		resourcePath = resourcePath.startsWith("/")? resourcePath : ('/' + resourcePath);
		//String name = getName(resourcename);

		InputStream input = getClass().getResourceAsStream(resourcePath);

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
				
				int pos = resourcePath.lastIndexOf('/');
				String resourcename = pos == -1? resourcePath : resourcePath.substring(pos + 1); 

				resource = new ScanWebResource(resourcename, mime, contingut);
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
			}  else if ("jar".equalsIgnoreCase(type)) {
        mime="application/java-archive";
      }
		}
		return mime;
	}
/*
	private String getName(String resourcename) {
		String name = "";
		if (resourcename != null && !"".equals(resourcename)) {
			name = resourcename.substring(resourcename.lastIndexOf("/") + 1);
		}
		return name;
	}
*/
	

  @Override
  public int getMinHeight(HttpServletRequest request, long docID) throws Exception {
    return HEIGHT;
  }
  
  
  
  public static void main(String[] args) {
    
    IECISAScanWebPlugin iecisa = new IECISAScanWebPlugin();
    long docID = 123;
    ScanWebResource swr;
    try {
      swr = iecisa.getResource(null, "/applet/plugin-scanweb-iecisascanweb-applet.jar", docID);
    
    
      if (swr == null) {
        System.out.println(" IS NULL !!!!");      
      } else {
        System.out.println(" IS NOT NULL = " + swr.getName());
      }
      
      URL url = new URL("http://localhost:8080/regweb3/anexo/guardarScan/52839");
      System.out.println("URL PROTO = " + url.getProtocol());
      
      
      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  
  
  
  
  protected ScanWebResource jnlpGet(HttpServletRequest request, long docID)  {
    
    
    String uploadPath = getUploadPath();
    uploadPath = uploadPath.replace("{0}", String.valueOf(docID));
    
    String resourcePath = getResourcePath();
    resourcePath = resourcePath.replace("{0}", String.valueOf(docID));
    
    String context = request.getContextPath();
    

    
    String home = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +  context;


    StringWriter writer = new StringWriter();

    PrintWriter out = new PrintWriter(writer);
    
    String appletUrlBase = home + resourcePath + "applet/";

    log.info(" appletUrlBase = ]" + appletUrlBase + "[ ");
    
    String appletUrl =  appletUrlBase + "plugin-scanweb-iecisascanweb-applet.jar";

   
    log.info(" appletUrl = ]" + appletUrl + "[ ");

    out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    out.println("<jnlp spec=\"1.0+\" codebase=\"" + appletUrl  + "\" >");
    out.println("    <information>");
    out.println("        <title>ScanWeb Applet</title>");
    out.println("        <vendor>IECISA</vendor>");
    out.println("        <homepage href=\"http://www.fundaciobit.org/\" />");
      out.println("        <description>ScanWeb Applet de IECISA</description>");
    //out.println("         <icon href=\"" + absolutePluginRequestPath + "/img/portafib.ico" + "\" />");
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

      
    out.println("       <param name=\"servlet\" value=\"" + home + uploadPath + "\"/>");

    String fileFieldName = getFileFieldName();
    out.println("       <param name=\"fileFormName\" value=\"" + fileFieldName + "\"/>");


    out.println("   </applet-desc>");
    out.println("</jnlp>");
    
    
    ScanWebResource swr = new ScanWebResource("iecisascanweb.jnlp",
        "application/x-java-jnlp-file",   writer.toString().getBytes() );

    return swr;
    
    
  }

  
  
  
}