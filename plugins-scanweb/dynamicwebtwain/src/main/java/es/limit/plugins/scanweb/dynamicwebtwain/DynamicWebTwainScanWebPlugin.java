package es.limit.plugins.scanweb.dynamicwebtwain;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.scanweb.IScanWebPlugin;
import org.fundaciobit.plugins.scanweb.ScanWebResource;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

/**
 * 
 * @author LIMIT 
 * 
 */
public class DynamicWebTwainScanWebPlugin extends AbstractPluginProperties implements IScanWebPlugin {

	protected final Logger log = Logger.getLogger(getClass());

	private static final String PROPERTY_BASE = SCANWEB_BASE_PROPERTY + "dynamicwebtwain.";
	private static Map<String, Properties> missatges = new HashMap<String, Properties>();

	/**
	 * 
	 */
	public DynamicWebTwainScanWebPlugin() {
		super();
	}


	@SuppressWarnings("unused")
	private String getDynamicWebTwainProperty(String name) {
		return getProperty(PROPERTY_BASE + name);
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
	public String getName(Locale locale) throws Exception {
		return "DynamicWebTwain";
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
		String applicationPath = getProperty("applicationPath", "regweb");
		return 	"<script type=\"text/javascript\" src=\"/" + applicationPath + "/anexo/scanwebresource/dynamsoft.webtwain.initiate.js\"> </script> \n" +
		"<script type=\"text/javascript\" src=\"/" + applicationPath + "/anexo/scanwebresource/dynamsoft.webtwain.config.js\"> </script>";
	}

	@Override
	public String getCoreJSP(HttpServletRequest request, long docID) throws Exception {
		
		// Carregam els texts en català per si hi ha algun problema al 
		// carregar els fitxers de missatges multiidioma
		String disp = "Dispositiu";
		String color = "Color";
		String res = "Resolució";
		String duplex = "Duplex";
		String clean = "Borra actual";
		String cleanAll = "Borra tot";
		String upError = "S\\'ha produït un error, i no s\\'ha pogut pujar el document escanejat.";

//		String idAnex = request.getParameter("anexo.id");

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
				disp = prop.getProperty("dwt.dispositiu");
				color = prop.getProperty("dwt.color");
				res = prop.getProperty("dwt.resolucio");
				duplex = prop.getProperty("dwt.duplex");
				clean = prop.getProperty("dwt.borra.actual");
				cleanAll = prop.getProperty("dwt.borra.tot");
				upError = prop.getProperty("dwt.error.upload");
			}
		}

		StringBuffer bufferOutput = new StringBuffer();
		bufferOutput.append(	"<script>");
		bufferOutput.append(	"	Dynamsoft.WebTwainEnv.RegisterEvent('OnWebTwainReady', Dynamsoft_OnReady);\n");
		bufferOutput.append(	"	var DWObject;\n");
		bufferOutput.append(	"	function Dynamsoft_OnReady() {\n"); 
		bufferOutput.append(	"		DWObject = Dynamsoft.WebTwainEnv.GetWebTwain('dwtcontrolContainer'); // Get the Dynamic Web TWAIN object that is embeded in the div with id 'dwtcontrolContainer'\n"); 
		bufferOutput.append(	"		if (DWObject) {\n"); 
		bufferOutput.append(	"			var count = DWObject.SourceCount\n;"); 
		bufferOutput.append(	"			for (var i = 0; i < count; i++)\n"); 
		bufferOutput.append(	"				document.getElementById('scanSource').options.add(new Option(DWObject.GetSourceNameItems(i), i));\n");
		bufferOutput.append(	"			$(\"#scanSource\").trigger(\"chosen:updated\");\n");
		bufferOutput.append(	"		}\n" ); 
		bufferOutput.append(	"	}\n");
		bufferOutput.append(	"\n"); 
		bufferOutput.append(	"	function OnSuccess() {\n"); 
		bufferOutput.append(	"		console.log('successful');\n"); 
		bufferOutput.append(	"	}\n");
		bufferOutput.append(	"\n");
		bufferOutput.append(	"	function OnFailure(errorCode, errorString) {\n"); 
		bufferOutput.append(	"		console.log(errorString);\n");
		bufferOutput.append(	"	}\n");
		bufferOutput.append(	"\n");
		bufferOutput.append(	"	function AcquireImage() {\n"); 
		bufferOutput.append(	"		if (DWObject) {\n"); 
		bufferOutput.append(	"			DWObject.SelectSourceByIndex(document.getElementById('scanSource').selectedIndex);\n"); 
		bufferOutput.append(	"			DWObject.OpenSource();\n"); 
		bufferOutput.append(	"			DWObject.IfDisableSourceAfterAcquire = true;\n");
		bufferOutput.append(	"			if (document.getElementById('scanColor').value == 'N'){\n" ); 
		bufferOutput.append(	"				DWObject.PixelType = EnumDWT_PixelType.TWPT_BW;\n"); 
		bufferOutput.append(	"			} else if (document.getElementById('scanColor').value == 'G'){\n"); 
		bufferOutput.append(	"				DWObject.PixelType = EnumDWT_PixelType.TWPT_GRAY;\n" );
		bufferOutput.append(	"			} else { //if (document.getElementById('scanColor').value == 'C'){\n"); 
		bufferOutput.append(	"				DWObject.PixelType = EnumDWT_PixelType.TWPT_RGB;\n" );
		bufferOutput.append(	"			}\n");
		bufferOutput.append(	"			if (DWObject.Duplex > 0 && document.getElementById('scanDuplex').value == '2'){\n" ); 
		bufferOutput.append(	"				DWObject.IfDuplexEnabled = true;\n"); 
		bufferOutput.append(	"			} else {\n"); 
		bufferOutput.append(	"				DWObject.IfDuplexEnabled = false;\n"); 
		bufferOutput.append(	"			}\n");
		bufferOutput.append(	"			DWObject.IfFeederEnabled = false;\n"); 
		bufferOutput.append(	"			DWObject.IfShowUI = false;\n");
		bufferOutput.append(	"			DWObject.IfAutoDiscardBlankpages = true;\n");
		bufferOutput.append(	"			DWObject.Resolution = parseInt(document.getElementById('scanResolution').value);\n" ); 
		bufferOutput.append(	"			DWObject.AcquireImage();\n"); 
//		bufferOutput.append(	"			Dynamsoft_OnReady();\n");
		bufferOutput.append(	"		}\n"); 
		bufferOutput.append(	"	}\n");
		bufferOutput.append(	"\n");
		bufferOutput.append(	"	function btnRemoveSelectedImage_onclick() {\n");
		bufferOutput.append(	"		if (DWObject) {\n");
		bufferOutput.append(	"			DWObject.RemoveAllSelectedImages();\n");
		bufferOutput.append(	"		}\n");
		bufferOutput.append(	"	}\n");
		bufferOutput.append(	"\n");
		bufferOutput.append(	"	function btnRemoveAllImages_onclick() {\n");
		bufferOutput.append(	"		if (DWObject) {\n");
		bufferOutput.append(	"			DWObject.RemoveAllImages();\n");
		bufferOutput.append(	"		}\n");
		bufferOutput.append(	"	}\n");
		bufferOutput.append(	"\n");
		bufferOutput.append(	"	function ResetScan() {\n" );
		bufferOutput.append(	"		if (DWObject) {\n");
		bufferOutput.append(	"			DWObject.RemoveAllImages();\n");
		bufferOutput.append(	"		}\n");
		bufferOutput.append(	"		$('#pestanyes a:first').tab('show')\n");
		bufferOutput.append(	"	}\n");
		bufferOutput.append(	"\n");
		bufferOutput.append(	"	function UploadScan() {\n");
		bufferOutput.append(	"		if (DWObject) {\n" );
		bufferOutput.append(	"			if (DWObject.HowManyImagesInBuffer == 0) {\n");
		//	  bufferOutput.append(	"				if ($('#archivo').val() == \"\"){\n");
		//	  bufferOutput.append(	"					alert('No ha adjuntat cap fitxer ni escanejat cap document.')\n");
		//	  bufferOutput.append(	"					return false;\n");
		//	  bufferOutput.append(	"				} else {\n");
		bufferOutput.append(	"				return true;\n");
		//	  bufferOutput.append(	"				}\n");
		bufferOutput.append(	"			}\n");
		bufferOutput.append(	"			var strHTTPServer = location.hostname;\n" ); 
		bufferOutput.append(	"			var CurrentPathName = unescape(location.pathname);\n" );
		bufferOutput.append(	"			var path = CurrentPathName.substring(0, CurrentPathName.lastIndexOf('/'));\n" );
		bufferOutput.append(	"			var idAnex = path.substring(path.lastIndexOf('/') + 1);\n" );
		bufferOutput.append(	"			var CurrentPath = '/" + getProperty("applicationPath", "regweb") + "';\n" ); //CurrentPathName.substring(0, CurrentPathName.lastIndexOf('/') + 1);\n");
		bufferOutput.append(	"			var strActionPage = CurrentPath + '/anexo/guardarScan/' + idAnex;\n" ); 
		bufferOutput.append(	"			DWObject.IfSSL = false; // Set whether SSL is used\n" );
		bufferOutput.append(	"			DWObject.HTTPPort = location.port == '' ? 80 : location.port;\n" );
		bufferOutput.append(	"			var Digital = new Date();\n");
		bufferOutput.append(	"			var uploadfilename = Math.floor(new Date().getTime() / 1000) // Uses milliseconds according to local time as the file name\n" ); 
		bufferOutput.append(	"			var result = DWObject.HTTPUploadAllThroughPostAsPDF(strHTTPServer, strActionPage, uploadfilename + '.pdf');\n" );
		bufferOutput.append(	"			if (!result) {\n");
		bufferOutput.append(	"				alert('" + upError + "');\n");
		bufferOutput.append(	"				return false;\n");
		bufferOutput.append(	"			}\n");
		bufferOutput.append(	"		}\n");
		bufferOutput.append(	"		return true;\n");
		bufferOutput.append(	"	}\n");
		bufferOutput.append(	"	$( document ).ready(function() {\n");
		bufferOutput.append(	"		$('#desaAnnex')[0].onclick = null;\n");
		bufferOutput.append(	"		$('#desaAnnex').click(function() {  \n");
		bufferOutput.append(	"			if (UploadScan()) {\n");
		bufferOutput.append(	"				procesarAnexo('" + request.getLocale() + "');\n");
		bufferOutput.append(	"			}\n");
		bufferOutput.append(	"		});\n");
		bufferOutput.append(	"		$('#modalNuevoAnexo').on('hidden.bs.modal', function () {\n");
		bufferOutput.append(	"			ResetScan();\n");
		bufferOutput.append(	" 		})\n");
		bufferOutput.append(	"	});\n");
		bufferOutput.append(	"</script>");
		bufferOutput.append(	"\n");
		bufferOutput.append(	"<div class=\"col-xs-6\">\n");
		bufferOutput.append(	"	<div class=\"form-group col-xs-12\">\n");
		bufferOutput.append(	"		<div class=\"col-xs-4 pull-left etiqueta_regweb control-label\">\n");
		bufferOutput.append(	"			<label for=\"scanSource\"><span class=\"text-danger\">*</span> " + disp + "</label>\n");
		bufferOutput.append(	"	  	</div>\n");
		bufferOutput.append(	"	  	<div class=\"col-xs-8\">\n");
		bufferOutput.append(	"	   		<select size=\"1\" id=\"scanSource\" class=\"chosen-select\">\n");
		bufferOutput.append(	"	    	</select>\n");
		bufferOutput.append(	"		</div>\n");
		bufferOutput.append(	"	</div>\n");
		bufferOutput.append(	"\n");
		bufferOutput.append(	"	<div class=\"form-group col-xs-12\">\n");
		bufferOutput.append(	"		<div class=\"col-xs-4 pull-left etiqueta_regweb control-label\">\n");
		bufferOutput.append(	"			<label for=\"scanColor\"><span class=\"text-danger\">*</span> " + color + "</label>\n");
		bufferOutput.append(	"	  	</div>\n");
		bufferOutput.append(	"	  	<div class=\"col-xs-8\">\n");
		bufferOutput.append(	"	   		<select size=\"1\" id=\"scanColor\" class=\"chosen-select\">\n");
		bufferOutput.append(	"				<option value='N'>B/N</option>");
		bufferOutput.append(	"				<option value='G' selected='selected'>Gris</option>");
		bufferOutput.append(	"				<option value='C'>Color</option>");
		bufferOutput.append(	"	    	</select>\n");
		bufferOutput.append(	"		</div>\n");
		bufferOutput.append(	"	</div>\n");
		bufferOutput.append(	"\n");
		bufferOutput.append(	"	<div class=\"form-group col-xs-12\">\n");
		bufferOutput.append(	"		<div class=\"col-xs-4 pull-left etiqueta_regweb control-label\">\n");
		bufferOutput.append(	"			<label for=\"scanResolution\"><span class=\"text-danger\">*</span> " + res + "</label>\n");
		bufferOutput.append(	"	  	</div>\n");
		bufferOutput.append(	"	  	<div class=\"col-xs-8\">\n");
		bufferOutput.append(	"	   		<select size=\"1\" id=\"scanResolution\" class=\"chosen-select\">\n");
		bufferOutput.append(	"				<option value='200' selected='selected'>200</option>");
		bufferOutput.append(	"				<option value='300'>300</option>");
		bufferOutput.append(	"				<option value='400'>400</option>");
		bufferOutput.append(	"				<option value='600'>600</option>");
		bufferOutput.append(	"	    	</select>\n");
		bufferOutput.append(	"		</div>\n");
		bufferOutput.append(	"	</div>\n");
		bufferOutput.append(	"\n");
		bufferOutput.append(	"	<div class=\"form-group col-xs-12\">\n");
		bufferOutput.append(	"		<div class=\"col-xs-4 pull-left etiqueta_regweb control-label\">\n");
		bufferOutput.append(	"			<label for=\"scanDuplex\"><span class=\"text-danger\">*</span> " + duplex + "</label>\n");
		bufferOutput.append(	"	  	</div>\n");
		bufferOutput.append(	"	  	<div class=\"col-xs-8\">\n");
		bufferOutput.append(	"	   		<select size=\"1\" id=\"scanDuplex\" class=\"chosen-select\">\n");
		bufferOutput.append(	"				<option value='1' selected='selected'>Una cara</option>");
		bufferOutput.append(	"				<option value='2'>Doble cara</option>");
		bufferOutput.append(	"	    	</select>\n");
		bufferOutput.append(	"		</div>\n");
		bufferOutput.append(	"	</div>\n");
		bufferOutput.append(	"\n");
		bufferOutput.append(	"	<div class=\"form-group col-xs-12\">\n");
		bufferOutput.append(	"		<div class=\"col-xs-4 pull-left etiqueta_regweb control-label\"></div>\n");
		bufferOutput.append(	"	  	<div class=\"col-xs-8\">\n");
		bufferOutput.append(	"			<button class=\"btn btn-sm\" value='Scan' onclick='AcquireImage();' >Scan</button>");
		bufferOutput.append(	"			<button class=\"btn btn-sm\" value='Scan' onclick='btnRemoveAllImages_onclick();' >" + cleanAll + "</button>");
		bufferOutput.append(	"			<button class=\"btn btn-sm\" value='Scan' onclick='btnRemoveSelectedImage_onclick();' >" + clean +"</button>");
		bufferOutput.append(	"		</div>\n");
		bufferOutput.append(	"	</div>\n");
		bufferOutput.append(	"\n");
		bufferOutput.append(	"</div>");
		bufferOutput.append(	"\n");
		bufferOutput.append(	"<div class=\"col-xs-6\">\n");
		bufferOutput.append(	"	<div id='dwtcontrolContainer'></div>");
		bufferOutput.append(	"</div>");

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
					outputResource = outputResource.replaceAll("regweb", getProperty("applicationPath", "regweb"));
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


  @Override
  public int getMinHeight(HttpServletRequest request, long docID) throws Exception {
    return 485;
  }
}