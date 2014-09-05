package org.fundaciobit.plugins.documentcustody;

import org.fundaciobit.plugins.IPlugin;
import org.fundaciobit.plugins.utils.Metadata;

/**
 * 
 * @author anadal
 * 
 */
public interface IDocumentCustodyPlugin extends IPlugin {
  
  public static final String DOCUMENTCUSTODY_BASE_PROPERTY = IPLUGIN_BASE_PROPERTIES + "documentcustody.";


  
  /**
   * 
   * @param proposedID Identificador proposat per la reserva
   * @param custodyParameters Parametres addicionsl requerits per a la realització de la Custodia. 
   * @return 
   * @throws Exception
   */
  String reserveCustodyID(String proposedID, String custodyParameters) throws CustodyException;

  
  /**
   * Custodia un document
   * @param custodyID
   * @param document
   * @throws Exception
   */
  void saveDocument(String custodyID, String custodyParameters,
      DocumentCustody document) throws CustodyException, NotSupportedCustodyException;
  
  /**
   * @param custodyID
   * @return Return content of document for plain document (without attached 
   * or detached signatures) or document with attached signatures
   * @throws Exception
   */
  byte[] getDocument(String custodyID) throws CustodyException;
  
  /**
   * @param custodyID
   * @return Return content of document for plain document (without attached 
   * or detached signatures) or document with attached signatures
   * @throws Exception
   */
  DocumentCustody getDocumentInfo(String custodyID) throws CustodyException;
  
  /** 
   * @return A list of suported document types defined in DocumentCustody class 
   */
  String[] getSupportedDocumentTypes();
  



  /**
   * Return detached sign if the document has it. For plain document (without attached 
   * or detached signatures) or document with attached signatures then return null 
   * @param custodyID
   * @return null if does not exist signature.
   * @throws Exception
   */
  void saveSignature(String custodyID, String custodyParameters,
      SignatureCustody signatureCustody) throws CustodyException, NotSupportedCustodyException;;



  /**
   * Return detached sign if the document has it. For plain document (without attached 
   * or detached signatures) or document with attached signatures then return null 
   * @param custodyID
   * @return
   * @throws Exception
   */
  byte[] getSignature(String custodyID) throws CustodyException;
  
  /**
   * Return detached sign if the document has it. For plain document (without attached 
   * or detached signatures) or document with attached signatures then return null 
   * @param custodyID
   * @return null if does not exist signature.
   * @throws Exception
   */
  SignatureCustody getSignatureInfo(String custodyID) throws CustodyException;

  
  /** 
   * @return A list of suported signature types defined in SignatureCustody class 
   */
  String[] getSupportedSignatureTypes();


  /** 
   * @return true if system automaically refresh signature o document with signature 
   *  to not loss validate of signature.
   */
  boolean refreshSignature();
  
  /**
   * 
   * @param custodyID
   * @param annex
   * @return AnnexID
   * @throws CustodyException
   */
  String addAnnex(String custodyID, AnnexCustody annex) throws CustodyException,  NotSupportedCustodyException;;
  
  /**
   * 
   * @param custodyID
   * @param annexID
   * @return null if annex not found
   */
  byte[] getAnnex(String custodyID, String annexID);
  
  /**
   * 
   * @param custodyID
   * @param annexID
   * @return null if annex not found
   */
  AnnexCustody getAnnexInfo(String custodyID, String annexID);
  
  boolean supportAnnexs();
  
  
  

  
  void addMetadata(Metadata metadata) throws CustodyException,  NotSupportedCustodyException;
  
  
  Metadata[] getMetadatas();
  
  
  boolean supportMetadata();
  
  /**
   * 
   * @param custodyID
   * @throws Exception
   */
  void deleteCustody(String custodyID) throws CustodyException,  NotSupportedCustodyException;


  boolean supportDelete();
  
  /**
   *
   * @param custodyID
   * @return
   * @throws Exception
   */
  String getValidationUrl(String custodyID) throws CustodyException;
  
  /**
   * Retorna un valor a partir de l'identificador de reserva que es pot
   * substituir quan calgui. Per defecte retornar custodyID 
   * @param custodyID
   * @return
   * @throws Exception
   */
  String getSpecialValue(String custodyID) throws CustodyException;

}
