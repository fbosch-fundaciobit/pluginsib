package org.fundaciobit.plugins.documentcustody.filesystem;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Properties;

import org.fundaciobit.plugins.documentcustody.AnnexCustody;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;
import org.fundaciobit.plugins.utils.Metadata;
import org.fundaciobit.plugins.utils.MetadataFormatException;
import org.fundaciobit.plugins.utils.MetadataType;
import org.fundaciobit.plugins.utils.PluginsManager;
import org.junit.Assert;

/**
 * 
 * @author anadal
 *
 */
public class TestFileSystemCustody {

  
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
    
    
    // Reserve ID
    final String custodyParameters = null;
    final String custodyID = documentCustodyPlugin.reserveCustodyID(custodyParameters);
          
    
    // SAVE DOCUMENT
    DocumentCustody doc = new DocumentCustody();
    doc.setDocumentType(DocumentCustody.DOCUMENT_ONLY);
    doc.setName("holacaracola.txt");
    doc.setData("holacaracola".getBytes());
    
    documentCustodyPlugin.saveDocument(custodyID, custodyParameters, doc);
    
    
    if (documentCustodyPlugin.getDocumentInfo(custodyID) == null) {
      Assert.fail("Nopot llegir document info");
    }
    
    if (documentCustodyPlugin.getDocument(custodyID) == null) {
      Assert.fail("Nopot llegir document byte []");
    }
    
    
    // SAVE SIGNATURE
    SignatureCustody signatureCustody = new SignatureCustody();
    
    signatureCustody.setAttachedDocument(false);
    signatureCustody.setData("firma dades".getBytes());
    signatureCustody.setName("firma.xml");
    signatureCustody.setSignatureType(SignatureCustody.XADES_SIGNATURE);
    
    documentCustodyPlugin.saveSignature(custodyID, custodyParameters, signatureCustody);
    
    if (documentCustodyPlugin.getSignatureInfo(custodyID) == null) {
      Assert.fail("Nopot llegir Signature info");
    }
    
    if (documentCustodyPlugin.getSignature(custodyID) == null) {
      Assert.fail("Nopot llegir Signature byte []");
    }
    
    
    // ANNEXES
    
   AnnexCustody annexCustody = new AnnexCustody();
    
   
   annexCustody.setData("annex content file".getBytes());
   annexCustody.setName("firma.xml");
   annexCustody.setMime("application/octet-stream");
   
   documentCustodyPlugin.addAnnex(custodyID, annexCustody);
   
   documentCustodyPlugin.addAnnex(custodyID, annexCustody);
   
   ArrayList<String> annexes = documentCustodyPlugin.getAllAnnexes(custodyID);
   
   Assert.assertEquals(2, annexes.size());
   
   
   // Metadata
   
   String key = "k1";
   documentCustodyPlugin.addMetadata(custodyID, new Metadata(key, "value11", MetadataType.STRING));
   
   ArrayList<Metadata> list = documentCustodyPlugin.getMetadata(custodyID, key);
   Assert.assertEquals(1, list.size());
   
   documentCustodyPlugin.addMetadata(custodyID, new Metadata(key, "value22", MetadataType.STRING));
   
   list = documentCustodyPlugin.getMetadata(custodyID, key);
   Assert.assertEquals(2, list.size());
   
   documentCustodyPlugin.addMetadata(custodyID, new Metadata("k2", "12", MetadataType.INTEGER));
   
   list = documentCustodyPlugin.getMetadata(custodyID, key);
   Assert.assertEquals(2, list.size());
   
   list = documentCustodyPlugin.getMetadata(custodyID, "k2");
   Assert.assertEquals(1, list.size());
   
   
   documentCustodyPlugin.deleteMetadata(custodyID, key);
   list = documentCustodyPlugin.getMetadata(custodyID, key);
   Assert.assertNull(list);

   Assert.assertEquals(1, documentCustodyPlugin.getMetadata(custodyID, "k2").size());
   
   Metadata[] errors = new Metadata[] {
       new Metadata("key", "zzzzññ", MetadataType.BASE64),
       new Metadata("key", "12.8", MetadataType.INTEGER),
       new Metadata("key", "12,8", MetadataType.DECIMAL),
       //new Metadata("key", "truez", MetadataType.BOOLEAN),
       
   };
   for (Metadata error : errors) {
     try {
       documentCustodyPlugin.addMetadata(custodyID, error);
       Assert.fail("metadata erronia s'ha afegit com a bona");
     } catch(MetadataFormatException mfe) {
       // OK
     }
   }
   
   
    
   // documentCustodyPlugin.deleteCustody(custodyID);
    
  }
  
  
  public static void main(String[] args) {
    try {
     
      System.out.println(FileSystemDocumentCustodyPlugin.class.getCanonicalName());
      


      
      
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    
  }
  
  
  
  
  
}
