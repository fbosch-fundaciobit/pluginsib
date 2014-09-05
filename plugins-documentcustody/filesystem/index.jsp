<%@page import="java.io.ByteArrayOutputStream"
%><%@page import="java.io.OutputStream"
%><%@page import="java.io.FileInputStream"
%><%@page import="java.io.File"
%><%@page language="java" 
%><%!

  public static final String PADES_SIGNATURE = "pdf";

  public static final String CADES_SIGNATURE = "cades";

  public static final String XADES_SIGNATURE = "xades";

  public static final String SMIME_SIGNATURE = "smime";

  public static final String OOXML_SIGNATURE = "ooxml";

  public static final String ODT_SIGNATURE = "odt";

  public static final String NONE_SIGNATURE = "none";

    private static final String CUSTODY_DOCUMENT_PREFIX(String custodyID) {
      return CUSTODY_PREFIX() + "DOC_" + custodyID;
    }

    private static final String CUSTODY_SIGNATURE_PREFIX(String custodyID) {
      return CUSTODY_PREFIX() + "SIGN_" + custodyID;
    }

    private static final String CUSTODY_DOCUMENT_INFO_PREFIX(String custodyID) {
      return CUSTODY_PREFIX() + "INFODOC_" + custodyID;
    }
	
    private static final String CUSTODY_SIGNATURE_INFO_PREFIX(String custodyID) {
      return CUSTODY_PREFIX() + "INFOSIGN_" + custodyID;
    }
    
    private static final String PROPERTY_BASE = "plugins.documentcustody.filesystem.";

    private static final String CUSTODY_PREFIX() {
      return System.getProperty(PROPERTY_BASE + "prefix", "CUST_");
    }

    
    public byte[] readFile(File info) throws Exception {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      FileInputStream input = new FileInputStream(info);
      
      byte[] buffer = new byte[4096];
      int n = 0;
      while (-1 != (n = input.read(buffer))) {
        output.write(buffer, 0, n);
      }
     
      input.close();
      
      return output.toByteArray();
    }
    
    
    public String get(String match, String text) {
      
      /*
      <void property="mimeType"> 
      <string>text/plain</string> 
     </void> 
      */
      
      String search = "<void property=\"" + match + "\">";
      int index = text.indexOf(search) + search.length();
      
      index = text.indexOf("<string>", index) + "<string>".length();
      
      int index2 = text.indexOf("</string>", index);
      
      return text.substring(index, index2);
      
    }
    
    
    
%><%


    String custodyID = request.getParameter("custodyID");
    
    //System.out.println("   ---------------------------------------- ");
    //System.out.println("CustodyID : " + custodyID);
    
    File getBaseDir = new java.io.File(application.getRealPath("/"));
    
    System.out.println("BASE DIR INDEX:JSP : " + getBaseDir);
    
    File toDownload;
    String fileInfo;
    
	String type;
    try {
      
        File doc = new File(getBaseDir,CUSTODY_DOCUMENT_PREFIX(custodyID));
		
        //System.out.println(" Cercant document: " + doc.getAbsolutePath());
        if (doc.exists()) {
			toDownload = doc;
			fileInfo = CUSTODY_DOCUMENT_INFO_PREFIX(custodyID);  
			type = "documentType";
        } else {
			File sign = new File(getBaseDir, CUSTODY_SIGNATURE_PREFIX(custodyID));
			//System.out.println(" Cercant signatura: " + sign.getAbsolutePath());
			if (sign.exists()) {
				toDownload = sign;
				fileInfo = CUSTODY_SIGNATURE_INFO_PREFIX(custodyID);
				type = "signatureType";
			} else {
				System.out.println("ERROR: No s'ha trobat ni signatura ni document per ID " + custodyID);
				response.sendError(response.SC_NOT_FOUND);
				return;
			}
      }
	  
	  
	  String info = new String(readFile(new File(getBaseDir,fileInfo)));
	  String filename = get("name", info);
	  String signatureType = get(type, info);
	  String contentType;
	  if (PADES_SIGNATURE.equals(signatureType)) {
		contentType = "application/pdf";
	  } else if (XADES_SIGNATURE.equals(signatureType)) {
		contentType = "text/xml";
	  } else if (SMIME_SIGNATURE.equals(signatureType)) {
		contentType = "application/pkcs7-mime";
	  } else if (ODT_SIGNATURE.equals(signatureType)) {
		contentType = "application/vnd.oasis.opendocument.text";
	  } else {
		contentType = "application/octet-stream";
	  }

      FileInputStream input;

      //response.setContentType(contentType);
      response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
      response.setContentLength((int) toDownload.length());

      OutputStream output = response.getOutputStream();
      input = new FileInputStream(toDownload);
      
      byte[] buffer = new byte[4096];
      int n = 0;
      while (-1 != (n = input.read(buffer))) {
        output.write(buffer, 0, n);
      }
     
      input.close();
 
      
    } catch (Exception e) {
      System.err.println("Error retornant document amd ID=" + custodyID);
      e.printStackTrace(System.err);
      response.sendError(response.SC_NOT_FOUND);
      return;
    }

%>