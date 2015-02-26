package org.fundaciobit.plugins.documentcustody.alfresco;

import java.io.File;
import java.util.Properties;

import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.alfresco.AlfrescoDocumentCustodyPlugin;
import org.fundaciobit.plugins.utils.PluginsManager;

/**
 * 
 * @author anadal
 *
 */
public class TestAlfrescoCustody {

  
  public static void main(String[] args) {
    try {
      
      System.out.println(AlfrescoDocumentCustodyPlugin.class.getCanonicalName());
      
     

      
      final String packageBase = "es.caib.portafib.";
      
      final String propertyBase = packageBase + AlfrescoDocumentCustodyPlugin.ALFRESCO_PROPERTY_BASE;
      
      
      Properties alfrescoProperties = new Properties();
      
      File f = new File("./testRepos");
      f.mkdirs();
      
      // Ficar propietats ALFRESCO
      alfrescoProperties.setProperty(propertyBase + "basedir", f.getAbsolutePath());
      
      // TODO FALTEN PROPERTIES ALFRESCO
      //    URL alfresco
      //    username alfresco
      //    password
      
      
      IDocumentCustodyPlugin documentCustodyPlugin;
      documentCustodyPlugin = (IDocumentCustodyPlugin)PluginsManager.instancePluginByClass(AlfrescoDocumentCustodyPlugin.class, packageBase, alfrescoProperties);
      
      
      DocumentCustody doc = new DocumentCustody();
      doc.setDocumentType(DocumentCustody.DOCUMENT_ONLY);
      doc.setName("holacaracola.txt");
      doc.setData("holacaracola".getBytes());
      
      String custodyID = "12341234";
      String custodyParameters = null;
      
      documentCustodyPlugin.saveDocument(custodyID, custodyParameters, doc);

    } catch (Exception e) {
      e.printStackTrace();
    }
    
    
  }
}
