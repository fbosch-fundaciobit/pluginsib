package org.fundaciobit.plugins.documentcustody.api.test;

import java.util.ArrayList;

import org.fundaciobit.plugins.documentcustody.api.AnnexCustody;
import org.fundaciobit.plugins.documentcustody.api.CustodyException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.NotSupportedCustodyException;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.utils.Metadata;
import org.fundaciobit.plugins.utils.MetadataFormatException;
import org.fundaciobit.plugins.utils.MetadataType;
import org.junit.Assert;

/**
 * 
 * @author anadal
 *
 */
public abstract class TestDocumentCustody {
  
  /**
   * 
   * @param documentCustodyPlugin
   * @param deleteOnFinish
   * @return CustodyID
   * @throws CustodyException
   * @throws NotSupportedCustodyException
   * @throws MetadataFormatException
   */
  protected String testDocumentCustody(IDocumentCustodyPlugin documentCustodyPlugin, boolean deleteOnFinish)
      throws CustodyException, NotSupportedCustodyException, MetadataFormatException {
    // Reserve ID
    final String custodyParameters = null;
    final String custodyID = documentCustodyPlugin.reserveCustodyID(custodyParameters);
          
    
    // SAVE DOCUMENT
    DocumentCustody doc = new DocumentCustody();
    doc.setName("holacaracola.txt");
    doc.setData("holacaracola".getBytes());
    
    documentCustodyPlugin.saveDocument(custodyID, custodyParameters, doc);
    
    
    if (documentCustodyPlugin.getDocumentInfo(custodyID) == null) {
      Assert.fail("No pot llegir document info");
    }
    
    if (documentCustodyPlugin.getDocument(custodyID) == null) {
      Assert.fail("No pot llegir document byte []");
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
       new Metadata("key", "kkkkzññ", MetadataType.BASE64),
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
   
   
   if (deleteOnFinish) {
     documentCustodyPlugin.deleteCustody(custodyID);
   }
   
   return custodyID;
  }

}
