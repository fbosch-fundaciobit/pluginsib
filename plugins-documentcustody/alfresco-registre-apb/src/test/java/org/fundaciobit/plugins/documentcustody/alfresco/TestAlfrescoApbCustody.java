package org.fundaciobit.plugins.documentcustody.alfresco;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.utils.PluginsManager;

/**
 * 
 * @author anadal
 *
 */
public class TestAlfrescoApbCustody {

  
  public static void main(String[] args) {
    try {
      
      System.out.println(AlfrescoDocumentCustodyPlugin.class.getCanonicalName());

      final String packageBase = "es.caib.example.";

      Properties alfrescoProperties = new Properties();

      alfrescoProperties.load(new FileInputStream("test.properties"));
      

      //File f = new File("./testRepos");
      //f.mkdirs();

      IDocumentCustodyPlugin documentCustodyPlugin;
      documentCustodyPlugin = (IDocumentCustodyPlugin)PluginsManager.instancePluginByClass(AlfrescoDocumentCustodyPlugin.class, packageBase, alfrescoProperties);
      
      
      DocumentCustody doc = new DocumentCustody();
      doc.setDocumentType(DocumentCustody.DOCUMENT_ONLY);
      doc.setName("holacaracola.txt");
      doc.setData("holacaracola".getBytes());
      
      
      ClassLoader classLoader = new TestAlfrescoApbCustody().getClass().getClassLoader();
      InputStream is = classLoader.getResource("registre.xml").openStream();
      if (is == null) {
        System.out.println(" No trob fitxer registre.xml");
        return;
      }

      
      String custodyParameters = IOUtils.toString(is, "utf-8");
      
      String custodyID = documentCustodyPlugin.reserveCustodyID(custodyParameters);
      
      documentCustodyPlugin.saveDocument(custodyID, custodyParameters, doc);

    } catch (Exception e) {
      e.printStackTrace();
    }
    
    
  }
}
