package org.fundaciobit.plugins.documentcustody.filesystem;

import java.io.File;
import java.net.URLEncoder;
import java.util.Properties;

import org.fundaciobit.plugins.documentcustody.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.test.TestDocumentCustody;
import org.fundaciobit.plugins.utils.PluginsManager;

/**
 * 
 * @author anadal
 *
 */
public class TestFileSystemCustody extends TestDocumentCustody {

  
  //@org.junit.Test
  public void testHash() throws Exception {

    /*
    MD2
    MD5
    SHA
    SHA-256
    SHA-384
    SHA-512
    */
    
    String algorithm = "MD5"; // "SHA-512"); //"MD5");
    
    String dades="dades a \\à encriptar";
    String password = "pwd";
    for (int i = 1; i < 10; i++) {
      String result = FileSystemDocumentCustodyPlugin.generateHash(dades, "MD5", password + i);
      
      System.out.println(" MD5: (" + i + "): " + result); 
      if (!URLEncoder.encode(result, "utf-8").equals(result)) {
        break;
      }
    }
    System.out.println(" MD5: " + FileSystemDocumentCustodyPlugin.generateHash(dades, algorithm,  password));

  }
  
  
  @org.junit.Test
  public void testFull() throws Exception {

    
    
    final String packageBase = "es.caib.portafib.";
    
    final String propertyBase = packageBase + FileSystemDocumentCustodyPlugin.FILESYSTEM_PROPERTY_BASE;
    
    
    Properties fsProperties = new Properties();
    
    File f = new File("./testRepos");
    f.mkdirs();
    
    // Ficar propietats FILESYSTEM
    fsProperties.setProperty(propertyBase + "basedir", f.getAbsolutePath());
    fsProperties.setProperty(propertyBase + "prefix", "cust");
  
    
    IDocumentCustodyPlugin documentCustodyPlugin;
    documentCustodyPlugin = (IDocumentCustodyPlugin)PluginsManager.
        instancePluginByClass(FileSystemDocumentCustodyPlugin.class, packageBase, fsProperties);
    
    
    testDocumentCustody(documentCustodyPlugin, true);
    
  }


  
  
  public static void main(String[] args) {
    try {
     
      System.out.println(FileSystemDocumentCustodyPlugin.class.getCanonicalName());

      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    
  }
  
  
  
  
  
}
