package org.fundaciobit.plugins.utils.cxf;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * 
 * @author anadal
 *
 */
public class CXFUtils {

  public static boolean isXMLFormat(byte[] data) {

    if (!isBinaryFile(data)) {

      boolean isXML = isXMLLike(new String(data));

      // System.out.println("isXML = " + isXML);

      if (isXML) {
        return true;
      }

    }
    return false;
  }

  public static boolean isBinaryFile(byte[] data) {

    int size = data.length;
    if (size > 1024)
      size = 1024;

    int ascii = 0;
    int other = 0;

    for (int i = 0; i < size; i++) {
      byte b = data[i];
      if (b < 0x09)
        return true;

      if (b == 0x09 || b == 0x0A || b == 0x0C || b == 0x0D)
        ascii++;
      else if (b >= 0x20 && b <= 0x7E)
        ascii++;
      else
        other++;
    }

    if (other == 0)
      return false;
    return 100 * other / (ascii + other) > 95;
  }

  protected static boolean isXMLLike(String inXMLStr) {

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = null;
    try {
      db = dbf.newDocumentBuilder();
      InputSource is = new InputSource();
      is.setCharacterStream(new StringReader(inXMLStr));

      Document doc = db.parse(is);

      doc.getDocumentElement().getTextContent();
      
      return true;
    } catch (Throwable e1) {
      // handle ParserConfigurationException   
      return false;
    }

  }

}
