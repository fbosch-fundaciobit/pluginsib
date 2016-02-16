package es.fundaciobit.plugins.scanweb.scan_mmscomputing;

import java.io.IOException;
import java.io.InputStream;
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
public class MmsComputingScanWebPlugin extends AbstractPluginProperties implements IScanWebPlugin {

	protected final Logger log = Logger.getLogger(getClass());

	private static final String PROPERTY_BASE = SCANWEB_BASE_PROPERTY + "mmscomputing.";


	 public static final int HEIGHT =  250;
	

	/**
	 * 
	 */
	public MmsComputingScanWebPlugin() {
		super();
	}

	 /**
   * @param propertyKeyBase
   * @param properties
   */
  public MmsComputingScanWebPlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);
  }


  /*
	private String getMmsComputingProperty22(String name) {
		return getProperty(PROPERTY_BASE + name);
	}
	

	private String getMmsComputingProperty(String name, String defaultValue) {
		return getProperty(PROPERTY_BASE + name, defaultValue);
	}
*/
  
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
	public MmsComputingScanWebPlugin(String propertyKeyBase) {
		super(propertyKeyBase);
	}

	@Override
	public String getName(Locale locale) throws Exception {
		return "MmsComputing Scan";
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
    
    String fileFieldName = getFileFieldName();
    return  "<script src=\"https://www.java.com/js/deployJava.js\"></script>\n"
          + "<div style=\"border-style:double double double double;\">"
          + "<center>"
          + "<script>\n"
          + "    var home;\n"
          + "    home = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '" + context + "';\n"
          + "\n"
          + "  function enviarFitxer() {\n"
          + "    var scan;       \n"
          + "    scan = document.getElementById('scan_mms');\n"
          + "    var result;\n"
          + "    result = scan.upload('" + context + uploadPath + "','" + fileFieldName + "');\n\n"
          + "    if (result) {\n"
          + "      if (result == '') {\n"
          + "        alert(\"WARNING: No s'ha escanejat cap pagina\");\n"
          + "      } else {\n"
          + "        alert(\"ERROR: \" +result);\n"
          + "      }\n"
          + "    } else {\n"
          + "      alert('OK');\n"
          + "    }\n"
          + "       \n"
          + "  }\n"
          + "\n\n"
          + "  var attributes = {\n"
          + "    id:'scan_mms', code:'org.fundaciobit.scanweb.ScanApplet',\n"
          + "    archive:home + '" + resourcePath + "applet/scanmmscomputing-panel-signed-1.0.0.jar',\n"
          + "    width:550,"
          + "    height:" + HEIGHT + "};\n"
          + "  var parameters = {} ;\n"
          + "  deployJava.runApplet(attributes, parameters, '1.6');\n"
          + "\n"
          + "  $( document ).ready(function() {\n"
          + "    $('#" + getSubmitButton() + "')[0].onclick = null;\n"
          + "    $('#" + getSubmitButton() + "').click(function() {  \n"
          + "          enviarFitxer();\n"
          + "    });\n"
          + "  });\n"
          + "</script></center></div>\n";


	}

	@Override
	public ScanWebResource getResource(HttpServletRequest request, String resourcePath, long docID) throws Exception {
		ScanWebResource resource = null;
		byte[] contingut = null;
		String mime = getMimeType(resourcePath);
		resourcePath = resourcePath.replace('\\', '/');
		
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
    
    MmsComputingScanWebPlugin mms = new MmsComputingScanWebPlugin();
    long docID = 123;
    ScanWebResource swr;
    try {
      swr = mms.getResource(null, "/applet/scanmmscomputing-panel-signed-1.0.0.jar", docID);
    
    
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
  
}