package org.fundaciobit.plugins.documentcustody;

import java.util.ArrayList;
import java.util.HashMap;

import org.fundaciobit.plugins.IPlugin;
import org.fundaciobit.plugins.utils.Metadata;
import org.fundaciobit.plugins.utils.MetadataFormatException;

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
   * @param custodyParameters Parametres addicionals requerits per a la realització de la Custodia. 
   * @return 
   * @throws Exception
   */
  String reserveCustodyID(String custodyParameters) throws CustodyException;

  
  /**
   * Custodia un document
   * @param custodyID
   * @param document
   * @throws Exception
   */
  void saveDocument(String custodyID, String custodyParameters,
      DocumentCustody document) throws CustodyException, NotSupportedCustodyException;
  
  /**
   * 
   * @param custodyID
   * @param custodyParameters
   * @param document
   * @throws CustodyException
   * @throws NotSupportedCustodyException
   */
  void deleteDocument(String custodyID) throws CustodyException, NotSupportedCustodyException;
  
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
  
  
  boolean supportsDeleteDocument();
  



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
  
  
  void deleteSignature(String custodyID) throws CustodyException, NotSupportedCustodyException;
  
  boolean supportsDeleteSignature();

  
  /** 
   * @return A list of suported signature types defined in SignatureCustody class 
   */
  String[] getSupportedSignatureTypes();


  /** 
   * @return true if system automaically refresh signature or document with signature 
   *  to not loss validate of signature. false Otherwise. Null if unknown.
   */
  Boolean supportsAutomaticRefreshSignature();

  /**
   * 
   * @param custodyID
   * @param annex
   * @return AnnexID
   * @throws CustodyException
   */
  String addAnnex(String custodyID, AnnexCustody annex) throws CustodyException,  NotSupportedCustodyException;
  
  /**
   * 
   * @param custodyID
   * @param annexID
   * @throws CustodyException
   * @throws NotSupportedCustodyException
   */
  void deleteAnnex(String custodyID, String annexID) throws CustodyException, NotSupportedCustodyException;
  
  
  /**
   * 
   * @param custodyID
   * @param annexID
   * @throws CustodyException
   * @throws NotSupportedCustodyException
   */
  void deleteAllAnnexes(String custodyID) throws CustodyException, NotSupportedCustodyException;
  
  
  
  /**
   * 
   * @param custodyID
   * @param annexID
   * @return null if annex not found
   */
  byte[] getAnnex(String custodyID, String annexID) throws CustodyException ;
  
  /**
   * 
   * @param custodyID
   * @param annexID
   * @return null if annex not found
   */
  AnnexCustody getAnnexInfo(String custodyID, String annexID) throws CustodyException ;
  
  /**
   * 
   * @param custodyID
   * @return
   * @throws CustodyException
   */
  ArrayList<String> getAllAnnexes(String custodyID) throws CustodyException;

  
  boolean supportsAnnexes();
  
  boolean supportsDeleteAnnex();
  

  /**
   * 
   * @param custodyID
   * @param metadata
   * @throws CustodyException
   * @throws NotSupportedCustodyException
   */ 
  void addMetadata(String custodyID, Metadata metadata) throws CustodyException,  NotSupportedCustodyException, MetadataFormatException;

  /**
   * 
   * @param custodyID
   * @param metadata
   * @throws CustodyException
   * @throws NotSupportedCustodyException
   */
  void addMetadata(String custodyID, Metadata[] metadata) throws CustodyException,  NotSupportedCustodyException, MetadataFormatException;
  
  /**
   * 
   * @param custodyID
   * @return
   * @throws NotSupportedCustodyException
   */
  HashMap<String, ArrayList<Metadata>> getAllMetadata(String custodyID) throws  CustodyException,NotSupportedCustodyException;
  
  /**
   * 
   * @param custodyID
   * @return
   * @throws NotSupportedCustodyException
   */
  ArrayList<Metadata> getMetadata(String custodyID, String key) throws CustodyException, NotSupportedCustodyException;
  
  /**
   * 
   * @param custodyID
   * @param key
   * @return
   * @throws CustodyException
   * @throws NotSupportedCustodyException
   */
  Metadata getOnlyOneMetadata(String custodyID, String key) throws CustodyException, NotSupportedCustodyException;

  /**
   * 
   * @return
   */
  boolean supportsMetadata();

  /**
   * 
   * @param custodyID
   * @throws CustodyException
   */
  void deleteAllMetadata(String custodyID) throws CustodyException;

  /**
   * 
   * @param custodyID
   * @param key
   * @return
   * @throws CustodyException
   */
  ArrayList<Metadata> deleteMetadata(String custodyID, String key) throws CustodyException;
  
  
  boolean supportsDeleteMetadata();
  
  /**
   * 
   * @param custodyID
   * @throws Exception
   */
  void deleteCustody(String custodyID) throws CustodyException,  NotSupportedCustodyException;


  boolean supportsDeleteCustody();
  
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
