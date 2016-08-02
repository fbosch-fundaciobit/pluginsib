package org.fundaciobit.plugins.documentcustody.alfresco;

import java.io.FileInputStream;
import java.util.Properties;

import org.fundaciobit.plugins.documentcustody.alfresco.base.AlfrescoBaseDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.test.TestDocumentCustody;
import org.fundaciobit.plugins.utils.PluginsManager;

/**
 *
 * @author anadal
 *
 */
public class TestAlfrescoBaseCustody extends TestDocumentCustody {

  public static void main(String[] args) {
    try {

      System.out.println(AlfrescoBaseDocumentCustodyPlugin.class.getCanonicalName());

      final String packageBase = "es.caib.example.";

      Properties alfrescoProperties = new Properties();

      alfrescoProperties.load(new FileInputStream("test.properties"));

      // Ficar propietats ALFRESCO
      /*
       * 
       * final String propertyBase = packageBase +
       * AlfrescoDocumentCustodyPlugin.ALFRESCO_PROPERTY_BASE;
       * 
       * 
       * 
       * alfrescoProperties.setProperty(propertyBase + "url",
       * "http://localhost:9080/alfresco/api/-default-/public/cmis/versions/1.0/atom"
       * );
       * 
       * 
       * //workspace://SpacesStore/b886bad2-998d-4674-a120-1fcc2f1f533c
       * 
       * alfrescoProperties.setProperty(propertyBase + "repository",
       * "b886bad2-998d-4674-a120-1fcc2f1f533c"); //"USER_HOMES/anadal/test/");
       * 
       * 
       * 
       * alfrescoProperties.setProperty(propertyBase + "basepath","/test");
       * 
       * alfrescoProperties.setProperty(propertyBase + "site","ODES");
       * alfrescoProperties.setProperty(propertyBase + "access.user", "anadal");
       * 
       * 
       * alfrescoProperties.setProperty(propertyBase + "access.pass", "anadal");
       * 
       * // WS ATOM
       * alfrescoProperties.setProperty(propertyBase + "access.method", "ATOM");
       * 
       */

      IDocumentCustodyPlugin documentCustodyPlugin;
      documentCustodyPlugin = (IDocumentCustodyPlugin) PluginsManager.instancePluginByClass(
          AlfrescoBaseDocumentCustodyPlugin.class, packageBase, alfrescoProperties);

      TestAlfrescoBaseCustody test = new TestAlfrescoBaseCustody();
      boolean deleteOnFinish = false;
      test.testDocumentCustody(documentCustodyPlugin, deleteOnFinish);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
