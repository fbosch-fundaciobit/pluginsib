package org.fundaciobit.plugins.documentcustody.alfresco.util;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.commons.io.IOUtils;

public class AlfrescoUtils {

	public static File bytesTofile(byte[] bFile, String fileName) {

        try {

        	File file = new File(fileName);
        	
        	FileOutputStream fileOuputStream = new FileOutputStream(file); 
		    fileOuputStream.write(bFile);
		    fileOuputStream.close();
		    
		    return file;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
	}
	
	public static byte[] getCmisObjectContent(Document cmisDoc) throws IOException {
		if (cmisDoc!=null) {
			ContentStream cs = cmisDoc.getContentStream();
			return IOUtils.toByteArray(cs.getStream());
		}else{
			return null;
		}
	}
	
	public static String getFileExtension(String name) {
		String extension = "";
		try {
			if (name!=null) {
				int indexLastDot = name.lastIndexOf(".");
				if (indexLastDot>0) {
					return name.substring(indexLastDot, name.length());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return extension;
	}
	
	public static String getActualYear() {
		Calendar cal = Calendar.getInstance();
		return Integer.toString(cal.get(Calendar.YEAR));
	}
	
	public static String getFileNameWithCustodyId(String name, String custodyId, boolean signature) {
		String out = "";
		String sign = "";
		try {
			if (name!=null) {
				
				if (signature) { sign = "_SIGN"; }

				int indexLastDot = name.lastIndexOf(".");
				if (indexLastDot>0) {
					return name.substring(0, indexLastDot) + "_" + custodyId + sign + name.substring(indexLastDot, name.length());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return out;
	}
	
	public static String removeCustodyIdFromFilename(String name, boolean signature) {
		
		String out = "";
		try {
			
			if (name!=null) {
				
				if (signature) {
					name.replaceAll("_SIGN", "");
				}

				int indexLastDot = name.lastIndexOf(".");
				int indexLastGb  = name.lastIndexOf("_");
				if (indexLastDot>0) {
					return name.substring(0, indexLastGb-1) + name.substring(indexLastDot, name.length());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return out;
	}
	
	public static String getPathFromRegistreObject(String registreXML) throws UnsupportedEncodingException, IllegalAccessException, ParseException {
		
    	ByteArrayInputStream stream=new ByteArrayInputStream(registreXML.getBytes("UTF-8"));
    	XMLDecoder xmlDec=new XMLDecoder(stream);
    	Object object=xmlDec.readObject();
    	xmlDec.close();
		
    	//Variables necessaries per crear la ruta per el document
    	String regES  = "";
    	String regANY = "";
    	String regNUM = "";
    	
    	for (Field field : object.getClass().getDeclaredFields()) {
    		
    	    field.setAccessible(true);
    	    Object value = field.get(object);
    	    
    	    if (value != null) {
    	    	
    	    	if ("destino".equals(field.getName())) { regES="Entrada"; }
    	    	if ("origen".equals(field.getName()))  { regES="Salida";  }
    	    	if ("numeroRegistro".equals(field.getName()))  { regNUM=value.toString();  }
    	    	if ("fecha".equals(field.getName())) {
    	    		SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
    	    		regANY = sdf.format(sdf.parse(value.toString()));
    	    	}
    	    	
    	        System.out.println(" - "+field.getName() + "=" + value);
    	        
//    	        if ("registroDetalle".equals(field.getName())) {
//    	        	
//    	        	for (Field subField : value.getClass().getDeclaredFields()) {
//    	        		subField.setAccessible(true);
//    		    	    Object subValue = subField.get(value);
//    	        		System.out.println("  > "+subField.getName() + "=" + subValue);
//    	        	}
//    	        }
    	    }
    	}
    	
    	return "/" + regANY + "/" + regES + "/" + regNUM;
	}
}