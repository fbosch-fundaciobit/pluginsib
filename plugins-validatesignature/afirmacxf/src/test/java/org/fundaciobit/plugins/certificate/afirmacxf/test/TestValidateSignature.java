package org.fundaciobit.plugins.certificate.afirmacxf.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.fundaciobit.plugins.utils.FileUtils;
import org.fundaciobit.plugins.validatesignature.afirmacxf.AfirmaCxfValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.SignatureInfo;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureRequest;

public class TestValidateSignature {


  public static void main(String[] args) {
    
    try {
      
      /*
      String xml = new String(FileUtils.readFromFile(new File("esborrar.txt"))); // VALID
      
      {
        SignatureInfo vs = new SignatureInfo();
        
        vs.setDetailInfo(new DetailInfo[] {  new DetailInfo() } );
        
        //processTimeStampInfo(xml, vs);
        
        TimeStampInfo[] allTSI = parseTimeStamp(xml);
        
        if (allTSI == null) {
          System.out.println("  - NO HI HA TSI ");
        } else {
        
          for (int i = 0; i < allTSI.length; i++) {
            
         
            TimeStampInfo tsi = allTSI[i];
            if (tsi != null) {
              
              System.out.println("  - SEGELL DE TEMPS ");
              System.out.println("      + Data: " + tsi.getCreationTime());
              System.out.println("      + Algo: " + tsi.getAlgorithm());
              System.out.println("      + CertificateIssuer: " + tsi.getCertificateIssuer());
              System.out.println("      + CertificateSubject: " + tsi.getCertificateSubject());
              System.out.println("      + Certificate: " + tsi.getCertificate());
              
              
            }
          }
        }
        
      
      }
      */
      /*
      String[][] entriesStr = parseAlgorithDigest(xml);
      
      
      for (int i = 0; i < entriesStr.length; i++) {
         System.out.println(i + " - " + Arrays.toString(entriesStr[i]));
       }
       */

      //if (true) return;
      

//     final String preA = "<vr:CertificateValue><![CDATA[";
//     final String postA = "]]></vr:CertificateValue>";
//     
//     System.out.println(" INDEX PRE= " + xml.indexOf(preA));
//     System.out.println(" INDEX POST= " + xml.indexOf(postA));
//     
//     
//
//      String[] entriesStr = parseXmlByIndexOf(xml, preA, postA);
//
//      //String[] entriesStr = parseXmlTag(xml, preA, postA);
//
//      for (int i = 0; i < entriesStr.length; i++) {
//        System.out.println(i + " - " + entriesStr[i]);
//      }
//    if (true) return;
      
   
      
//      
//      System.out.println(" AVUI = " + dateFormat.format(new Date()));
//      
//      String dateStr = "2021-02-09 mar 09:06:53 +0100";
//      
//      dateFormat.parse(dateStr);
//      

      
    Properties pluginProperties = new Properties();
    pluginProperties.load(new FileInputStream(new File("./config/plugin.properties")));

    String propertyKeyBase = "org.fundaciobit.exemple.signatureserverplugins.3.";
    

    
    //byte[] signature = FileUtils.readFromFile(new File("autofirma.pdf")); // VALID
    
    //byte[] signature = FileUtils.readFromFile(new File("peticioOK.pdf")); // INVALID => PAdESInvalidCertificateChain
    
    //byte[] signature = FileUtils.readFromFile(new File("pades_ltv.pdf")); // ResponderError
    
    //byte[] signature = FileUtils.readFromFile(new File("hola_firmat.pdf")); // INVALID => SignerCertificate:NotSupported
 
    //byte[] signature = FileUtils.readFromFile(new File("duesfirmes.pdf")); // VALID
    
    // byte[] signature = FileUtils.readFromFile(new File("miniapplet_epes_segelltemps_catcert.pdf")); // NO SUPORTAT SEGELL DE TEMPS
    
    byte[] signature = FileUtils.readFromFile(new File("miniapplet_epes_segelltemps_afirma.pdf")); // VALID
    
    //byte[] signature = FileUtils.readFromFile(new File("miniapplet_empleat_public.pdf")); // VALID
    
    
    

    AfirmaCxfValidateSignaturePlugin plugin;
    plugin = new AfirmaCxfValidateSignaturePlugin(propertyKeyBase, pluginProperties);
    
    {

      //long start = System.currentTimeMillis();
      
      ValidateSignatureRequest validationRequest = new ValidateSignatureRequest();
      
      validationRequest.setSignatureData(signature);
      validationRequest.setSignedDocumentData(null);
      
      validationRequest.setReturnCertificateInfo(false);
      validationRequest.setReturnValidationChecks(false);
      validationRequest.setValidateCertificateRevocation(false);
      validationRequest.setReturnCertificates(false);
      validationRequest.setReturnTimeStampInfo(true);
    
    SignatureInfo vs = plugin.validateSignature(validationRequest);

    
    AfirmaCxfValidateSignaturePlugin.printSignatureInfo(vs);
    
    // Elapsed Time = 4022 ms
    // Elapsed Time = 1501 ms
    // Elapsed Time = 1365 ms
    // Elapsed Time = 1351 ms
    //System.err.println( " Elapsed Time = " + (System.currentTimeMillis()- start) +" ms");
    
    }
    
    

    } catch (Exception e) {
       e.printStackTrace();
    }
    
  }
  
  
}
