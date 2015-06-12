package org.fundaciobit.plugins.documentcustody;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.documentcustody.AnnexCustody;
import org.fundaciobit.plugins.documentcustody.CustodyException;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.NotSupportedCustodyException;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;
import org.fundaciobit.plugins.utils.Metadata;
import org.fundaciobit.plugins.utils.MetadataFormatException;

/**
 * Implementacio genèrica de DocumentCustody. Només s'han d'implementar 5 mètodes:
 *    - deleteFile(String custodyID, String ... relativePaths);
 *    - existsFile(String custodyID, String relativePath);
 *    - writeFile(String custodyID, String relativePath, byte[] data) throws Exception;
 *    - byte[] readFile(String custodyID, String relativePath) throws Exception;
 *    - String getPropertyBase();
 *
 * @author anadal
 *
 */
public abstract class AbstractDocumentCustodyPlugin extends AbstractPluginProperties
  implements IDocumentCustodyPlugin {

  protected final Logger log = Logger.getLogger(getClass());
  
  private String prefix = null;

  /**
   * 
   */
  public AbstractDocumentCustodyPlugin() {
    super();
  }

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public AbstractDocumentCustodyPlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);
  }

  /**
   * @param propertyKeyBase
   */
  public AbstractDocumentCustodyPlugin(String propertyKeyBase) {
    super(propertyKeyBase);
  }

  private final String getCustodyDocumentName(String custodyID) {
    return CUSTODY_PREFIX() + custodyID + ".DOC";
  }
  
  private final String getCustodyDocumentInfoName(String custodyID) {
    return CUSTODY_PREFIX() + custodyID + ".DOCINFO";
  }

  private final String getCustodySignatureName(String custodyID) {
    return CUSTODY_PREFIX() + custodyID + ".SIGN";
  }

  private final String getCustodySignatureInfoName(String custodyID) {
    return CUSTODY_PREFIX() +  custodyID + ".SIGNINFO";
  }
  
  
  private final String getCustodyAnnexName(String annexID) {
    return CUSTODY_PREFIX() + annexID + ".ANNEX";
  }


  private final String getCustodyAnnexInfoName(String annexID) {
    return CUSTODY_PREFIX() + annexID + ".ANNEXINFO";
  }
  
  private final String getCustodyAnnexListName(String custodyID) {
    return CUSTODY_PREFIX() + custodyID + ".ANNEXLIST";
  }
  
  private final String getCustodyMetadataInfoName(String custodyID) {
    return CUSTODY_PREFIX() + custodyID + ".METAINFO";
  }
  
  
  private final String getCustodyHashesFile() {
    return CUSTODY_PREFIX() + "HASH__FILE.properties";
  }


  protected String CUSTODY_PREFIX() {
    if (prefix == null) {
      String pfix = getProperty(getPropertyBase() + "prefix");
      if (pfix == null || pfix.trim().length() == 0) {
        pfix = "";
      } else {
        pfix = pfix.trim();
        if (!pfix.endsWith("_")) {
          pfix = pfix + "_";
        }
      }
      prefix = pfix;  
    }
    return prefix; 
  }


  protected String getURLBase() {
    return getProperty(getPropertyBase() + "baseurl");
  }
  
  protected String getHashPassword() {
    return getProperty(getPropertyBase() + "hash.password","");
  }
  
  /**
   * Valid values       MD2, MD5, SHA,SHA-256,SHA-384,SHA-512
   * @return
   */
  protected String getHashAlgorithm() {
    return getProperty(getPropertyBase() + "hash.algorithm","MD5");
  }
  
  
  // ---------------------------------------------------------------------
  // ---------------------------------------------------------------------
  // ---------------------- C O M M O N    C O D E  ----------------------
  // ---------------------------------------------------------------------
  // ---------------------------------------------------------------------


  @Override
  public synchronized String reserveCustodyID(String parameters) throws CustodyException {
    
    final String custodyID = String.valueOf(System.nanoTime());
    try {
      Thread.sleep(50);
    } catch (InterruptedException e1) {
    }
    
    

    // Inicialitza Fitxers de HASH si la URL conté el sistema de HASH
    String baseUrl = getURLBase();
    if (baseUrl != null && baseUrl.indexOf("{2}") != -1) {
       String hash = generateHash(custodyID, getHashAlgorithm(), getHashPassword());
       
       try {
         Properties props = new Properties();
         final String path = getCustodyHashesFile();
         
         if (existsFile(custodyID, path)) {
           byte [] data = readFile(custodyID, path);
           props.load(new ByteArrayInputStream(data));
         }
         props.setProperty(hash, custodyID);
         //System.out.println(" Guardant fitxer(" + hashes.getAbsolutePath() + ");");
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         props.store(baos, "Hash Properties");
         
         writeFile(custodyID, path, baos.toByteArray());
         
       } catch(Exception e) {
         throw new CustodyException("Error adding hash value to properties file.", e);
       }

    }
    
    return custodyID;
  }
  
  
  @Override
  public boolean supportsDeleteCustody() {    
    return this.supportsDeleteDocument() 
        && this.supportsDeleteSignature()
        && this.supportsDeleteAnnex()
        && this.supportsDeleteMetadata();
  }


  @Override
  public void deleteCustody(String custodyID) throws CustodyException, NotSupportedCustodyException {

    if (!supportsDeleteCustody()) {
      throw new NotSupportedCustodyException();
    }
    
    this.deleteAllMetadata(custodyID);
    
    this.deleteAllAnnexes(custodyID);
    
    this.deleteSignature(custodyID);

    this.deleteDocument(custodyID);
    
  }

  /**
   * Si existeix el document de Signatura llavors el retornam, ja que és el que
   * es necessita per validar el document. En cas contrari o no té firmes o el
   * propi document ja duu adjunta la firma, per lo que retornam el document.
   * 
   * Valors de substitució:
   *     // {0} => custodyID
   *     // {1} => URLEncode(custodyID)
   *     // {2} => Hash(custodyID)
   */
  @SuppressWarnings("deprecation")
  @Override
  public String getValidationUrl(String custodyID) throws CustodyException {

    String baseUrl = getURLBase();
    if (baseUrl == null) {
      return null;
    }
    // {0} => custodyID
    // {1} => URLEncode(custodyID)
    final String urlEncoded = URLEncoder.encode(custodyID);
    
    // {2} => Hash(custodyID)
    final String hash;
    if (baseUrl.indexOf("{2}") == -1) {
      hash = "";
    } else {
      hash = generateHash(custodyID, getHashAlgorithm(), getHashPassword());
    }

    return MessageFormat.format(baseUrl, custodyID,urlEncoded, hash);

  }
  
  
  public static String generateHash(String data, String algorithm, String salt) {
    try {
      
      java.security.MessageDigest md = java.security.MessageDigest.getInstance(algorithm);
      byte[] array = md.digest((data + salt).getBytes());
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < array.length; ++i) {
        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
     }
      return sb.toString();
  } catch (java.security.NoSuchAlgorithmException e) {
  }
  return null;
}

  

  @Override
  public String getSpecialValue(String custodyID) throws CustodyException {
    return custodyID;
  }
  


  
  // ---------------------------------------------------------------------
  // ---------------------------------------------------------------------
  // -------------------------- D O C U  M E N T -------------------------
  // ---------------------------------------------------------------------
  // ---------------------------------------------------------------------


  @Override
  public void saveDocument(String custodyID, String custodyParameters, DocumentCustody document)
      throws CustodyException, NotSupportedCustodyException {

    String infoPath;
    String docPath;

    infoPath = getCustodyDocumentInfoName(custodyID);
    docPath = getCustodyDocumentName(custodyID);

    try {
      
      if (existsFile(custodyID, docPath)) {
        deleteFile(custodyID, docPath);
      }
      writeFile(custodyID, docPath, document.getData());
     

      
      DocumentCustody clone = new DocumentCustody(document);
      clone.setData(null);
      writeObject(custodyID, infoPath, clone);

    } catch (Exception ex) {
      final String msg = "No s'ha pogut custodiar el document";
      log.error(msg, ex);
      throw new CustodyException(msg, ex);
    }

  }
  
  
  @Override
  public boolean supportsDeleteDocument() {
    return true;
  }
  
  
  /**
   * 
   * @param custodyID
   * @param custodyParameters
   * @param document
   * @throws CustodyException
   * @throws NotSupportedCustodyException
   */
  public void deleteDocument(String custodyID) throws CustodyException, NotSupportedCustodyException {
    deleteFile(custodyID, 
        getCustodyDocumentName(custodyID), getCustodyDocumentInfoName(custodyID));
    
  }
  
  

  @Override
  public DocumentCustody getDocumentInfo(String custodyID) throws CustodyException {

    if (custodyID == null) {
      return null;
    }

    String docPath = getCustodyDocumentName(custodyID);
    String infoPath = getCustodyDocumentInfoName(custodyID);
    return (DocumentCustody) getDocOrSign(custodyID, docPath, infoPath);
  }
  
  

  @Override
  public DocumentCustody getDocumentInfoOnly(String custodyID) throws CustodyException {
    if (custodyID == null) {
      return null;
    }

    String infoPath = getCustodyDocumentInfoName(custodyID);
    return (DocumentCustody) getDocOrSignOnlyInfo(custodyID, infoPath);
  }


  @Override
  public byte[] getDocument(String custodyID) throws CustodyException {
    DocumentCustody info = getDocumentInfo(custodyID);
    if (info == null) {
      return null;
    }
    return info.getData();
  }

  /**
   * @return A list of suported document types defined in DocumentCustody class
   */
  @Override
  public String[] getSupportedDocumentTypes() {
    return new String[] { DocumentCustody.PDF_WITH_SIGNATURE,
        DocumentCustody.OOXML_WITH_SIGNATURE, DocumentCustody.ODT_WITH_SIGNATURE,
        DocumentCustody.DOCUMENT_ONLY, DocumentCustody.OTHER_DOCUMENT_WITH_SIGNATURE };
  }

  
  // ---------------------------------------------------------------------
  // ---------------------------------------------------------------------
  // -------------------------- S I G N A T U R E -----------------------
  // ---------------------------------------------------------------------
  // ---------------------------------------------------------------------

  
  
  @Override
  public void saveSignature(String custodyID, String custodyParameters,
      SignatureCustody document) throws CustodyException {

    String infoPath;
    String docPath;

    infoPath = getCustodySignatureInfoName(custodyID);
    docPath = getCustodySignatureName(custodyID);

    try {
      
      if (existsFile(custodyID, docPath)) {
        deleteFile(custodyID, docPath);
      }
      writeFile(custodyID, docPath, document.getData());

      SignatureCustody clone = new SignatureCustody(document);
      clone.setData(null);
      writeObject(custodyID, infoPath, clone);

    } catch (Exception ex) {
      final String msg = "No s'ha pogut custodiar la firma del document";
      log.error(msg, ex);
      throw new CustodyException(msg, ex);
    }

  }
  
  
  @Override
  public boolean supportsDeleteSignature() {
    return true;
  }
  
  
  @Override
  public void deleteSignature(String custodyID) throws CustodyException, NotSupportedCustodyException {
    deleteFile(custodyID, getCustodySignatureName(custodyID), getCustodySignatureInfoName(custodyID));
  }
  

  @Override
  public SignatureCustody getSignatureInfo(String custodyID) throws CustodyException {

    if (custodyID == null) {
      return null;
    }

    String docPath = getCustodySignatureName(custodyID);
    String infoPath = getCustodySignatureInfoName(custodyID);
    return (SignatureCustody) getDocOrSign(custodyID, docPath, infoPath);
  }
  
  
  @Override
  public SignatureCustody getSignatureInfoOnly(String custodyID) throws CustodyException {
    if (custodyID == null) {
      return null;
    }
    String infoPath = getCustodySignatureInfoName(custodyID);
    return (SignatureCustody) getDocOrSignOnlyInfo(custodyID, infoPath);
  }


  @Override
  public byte[] getSignature(String custodyID) throws CustodyException {
    SignatureCustody info = getSignatureInfo(custodyID);
    if (info == null) {
      return null;
    }
    return info.getData();
  }

  @Override
  public String[] getSupportedSignatureTypes() {
    return new String[] { SignatureCustody.CADES_SIGNATURE, SignatureCustody.XADES_SIGNATURE,
        SignatureCustody.SMIME_SIGNATURE, SignatureCustody.OTHER_SIGNATURE };
  }

  /**
   * @return true if system automaically refresh signature o document with
   *         signature to not loss validate of signature. false otherwise. Null unknown
   */
  @Override
  public Boolean supportsAutomaticRefreshSignature() {
    return false;
  }
  
  
  
  // ---------------------------------------------------------------------
  // ---------------------------------------------------------------------
  // ----------------------------- A N N E X  ----------------------------
  // ---------------------------------------------------------------------
  // ---------------------------------------------------------------------

  /**
   * 
   * @param custodyID
   * @param annex
   * @return AnnexID
   * @throws CustodyException
   */
  @Override
  public synchronized String addAnnex(String custodyID, AnnexCustody annex) throws CustodyException,
      NotSupportedCustodyException {
    
    final String annexID = custodyID + "_" + System.nanoTime();
    try {
       Thread.sleep(500);      
    } catch (Exception e) {
    }
    
    String infoPath;
    String docPath;

    infoPath = getCustodyAnnexInfoName(annexID);
    docPath = getCustodyAnnexName(annexID);

    try {
      
      if (existsFile(custodyID, docPath)) {
        deleteFile(custodyID, docPath);
      }
      writeFile(custodyID, docPath, annex.getData());

      AnnexCustody clone = new AnnexCustody(annex);
      clone.setData(null);
      writeObject(custodyID, infoPath, clone);
      

      ArrayList<String> annexIDs = getAllAnnexes(custodyID);
      annexIDs.add(annexID);
      writeObject(custodyID, getCustodyAnnexListName(custodyID), annexIDs );
      

    } catch (Exception ex) {
      final String msg = "No s'ha pogut guardar un annexe del document";
      log.error(msg, ex);
      throw new CustodyException(msg, ex);
    }

    return annexID;
  }
  
  
  @Override
  public boolean supportsDeleteAnnex() {
    return true;
  }
  
  
  @Override
  public void deleteAllAnnexes(String custodyID) throws CustodyException,
      NotSupportedCustodyException {
    
    ArrayList<String> annexIDs = getAllAnnexes(custodyID);
    
    ArrayList<String> paths = new ArrayList<String>();
    
    for (String annexID : annexIDs) {
      paths.add(getCustodyAnnexName(annexID));
      paths.add(getCustodyAnnexInfoName(annexID));
    }

    paths.add(getCustodyAnnexListName(custodyID));
    
    deleteFile(custodyID, paths.toArray(new String[paths.size()]));

  }
  
  
  @Override
  public void deleteAnnex(String custodyID, String annexID) 
      throws CustodyException, NotSupportedCustodyException {
    deleteFile(custodyID, getCustodyAnnexName(annexID), getCustodyAnnexInfoName(annexID));

    ArrayList<String> annexIDs = getAllAnnexes(custodyID);
    annexIDs.remove(annexID);
    String listPath = getCustodyAnnexListName(custodyID);
    writeObject(custodyID, listPath, annexIDs);

  }
  
  

  @Override
  public ArrayList<String> getAllAnnexes(String custodyID) throws CustodyException {
    
    String listPath = getCustodyAnnexListName(custodyID);
  
    if (existsFile(custodyID, listPath)) {
      ArrayList<String> annexIDs = (ArrayList<String>)readObject(custodyID, listPath);
      return annexIDs; 
    } else {
      return new ArrayList<String>();
    }
   
  }

  

  

  /**
   * 
   * @param custodyID
   * @param annexID
   * @return null if annex not found
   */
  @Override
  public byte[] getAnnex(String custodyID, String annexID) throws CustodyException {
    AnnexCustody ac = getAnnexInfo(custodyID, annexID);
    if (ac == null) {
      return null;
    }
    return ac.getData();
  }


  /**
   * 
   * @param custodyID
   * @param annexID
   * @return null if annex not found
   */
  @Override
  public AnnexCustody getAnnexInfo(String custodyID, String annexID) throws CustodyException {

      if (custodyID == null || annexID == null) {
        return null;
      }

      String docPath = getCustodyAnnexName(annexID);
      String infoPath = getCustodyAnnexInfoName(annexID);
      return (AnnexCustody) getDocOrSign(custodyID, docPath, infoPath);

  }
  
  
  @Override
  public AnnexCustody getAnnexInfoOnly(String custodyID, String annexID)
      throws CustodyException {
    if (custodyID == null || annexID == null) {
      return null;
    }

    String infoPath = getCustodyAnnexInfoName(annexID);
    return (AnnexCustody) getDocOrSignOnlyInfo(custodyID, infoPath);
  }
  
  

  
  
  

  @Override
  public boolean supportsAnnexes() {
    return true;
  }
  

  
  // ---------------------------------------------------------------------
  // ---------------------------------------------------------------------
  // -------------------------- M E T A D A T A --------------------------
  // ---------------------------------------------------------------------
  // ---------------------------------------------------------------------


  @Override
  public boolean supportsMetadata() {
    return true;
  }
  
  @Override
  public boolean supportsDeleteMetadata() {
    return true;
  }
  
  
  @Override
  public void addMetadata(String custodyID, Metadata metadata) throws CustodyException,  NotSupportedCustodyException, MetadataFormatException {
    
    if (metadata != null) {
      Metadata.checkMetadata(metadata);
      HashMap<String, ArrayList<Metadata>> metas = readMetadataInfo(custodyID);
      ArrayList<Metadata> list = metas.get(metadata.getKey());
      if (list == null) {
        list = new ArrayList<Metadata>();
        metas.put(metadata.getKey(), list);
      }
      list.add(metadata);
      writeMetadataInfo(custodyID, metas);
    }
            
  }
  
  @Override
  public void addMetadata(String custodyID, Metadata[] metadata) throws CustodyException,  NotSupportedCustodyException, MetadataFormatException {
    if (metadata != null) {
      // TODO Optimitzar
      for (int i = 0; i < metadata.length; i++) {
        if(metadata[i] != null) {
          Metadata.checkMetadata(metadata[i]);
        };
      }
      for (int i = 0; i < metadata.length; i++) {
        addMetadata(custodyID, metadata[i]);
      }
    }
  }
  
  

  @Override
  public void updateMetadata(String custodyID, Metadata metadata) throws CustodyException,
      NotSupportedCustodyException, MetadataFormatException {
    
    if (metadata != null && metadata.getKey() != null) {
      deleteMetadata(custodyID, metadata.getKey() );
    }
    addMetadata(custodyID, metadata);
  }

  @Override
  public void updateMetadata(String custodyID, Metadata[] metadata) throws CustodyException,
      NotSupportedCustodyException, MetadataFormatException {
    if (metadata != null) {
      for (Metadata m : metadata) {
        updateMetadata(custodyID, m);
      } 
    }
  }

  @Override
  public ArrayList<Metadata> deleteMetadata(String custodyID, String[] keys)
      throws CustodyException {
    ArrayList<Metadata> borrades = new ArrayList<Metadata>();
    if (keys == null) {
      
    }
    return borrades;
  }

  
  @Override  
  public HashMap<String, ArrayList<Metadata>> getAllMetadata(String custodyID) throws NotSupportedCustodyException, CustodyException {

    HashMap<String, ArrayList<Metadata>> all = readMetadataInfo(custodyID);

    return all;
  }
  
  @Override  
  public ArrayList<Metadata> getMetadata(String custodyID, String key) throws CustodyException, NotSupportedCustodyException {
    return readMetadataInfo(custodyID).get(key);
  }
  
  @Override
  public Metadata getOnlyOneMetadata(String custodyID, String key) throws CustodyException,
      NotSupportedCustodyException {
    ArrayList<Metadata> list = getMetadata(custodyID, key);
    if (list == null || list.size() == 0) {
      return null;
    }
    return list.get(0);
  }
  
 
  @Override
  public void deleteAllMetadata(String custodyID) throws CustodyException {

    deleteFile(custodyID, getCustodyMetadataInfoName(custodyID));

  }
  
  @Override
  public ArrayList<Metadata> deleteMetadata(String custodyID, String key) throws CustodyException {

    HashMap<String, ArrayList<Metadata>> metas = readMetadataInfo(custodyID);
    
    ArrayList<Metadata> borrades = metas.remove(key);
    
    writeMetadataInfo(custodyID, metas);
    
    return borrades;
    
  }
  
  /**
   * 
   * @return
   */
  protected HashMap<String, ArrayList<Metadata>> readMetadataInfo(String custodyID) throws CustodyException {
    final String path = getCustodyMetadataInfoName(custodyID);

    if (existsFile(custodyID, path)) {
      return (HashMap<String, ArrayList<Metadata>>)readObject(custodyID, path);
    } else {
      return new HashMap<String, ArrayList<Metadata>>();
    }
  }
  
  /**
   * 
   * @param custodyID
   * @param metas
   * @throws CustodyException
   */
  protected void writeMetadataInfo(String custodyID, HashMap<String, ArrayList<Metadata>> metas) throws CustodyException {
    final String path = getCustodyMetadataInfoName(custodyID);
    writeObject(custodyID, path, metas);
  }
  
  
  
  
  // ---------------------------------------------------------------------
  // ---------------------------------------------------------------------
  // ------------------------------- UTILS -------------------------------
  // ---------------------------------------------------------------------
  // ---------------------------------------------------------------------

  protected AnnexCustody getDocOrSign(String custodyID, String docPath, String infoPath)
      throws CustodyException {
    try {

      
      if (!existsFile(custodyID, docPath)) {
        return null;
      }
      
      AnnexCustody dc = getDocOrSignOnlyInfo(custodyID, infoPath);
      
      if (dc != null ) {
        byte[] data = readFile(custodyID, docPath);
        dc.setData(data);
      }

      return dc;

    } catch (Exception ex) {
      final String msg = "Error intentant obtenir el document custodia amb ID = "
        + custodyID + " en el path " + docPath + " (" + infoPath + ")";
      log.error(msg, ex);
      throw new CustodyException(msg, ex);
    }
  }

  
  protected AnnexCustody getDocOrSignOnlyInfo(String custodyID, String infoPath)
      throws CustodyException {
    
    AnnexCustody dc = (AnnexCustody) readObject(custodyID, infoPath);
    
    return dc;
    
  }
  
  
  
  protected void writeObject(String custodyID, String relativePath, Object object) throws CustodyException {
    try {
      java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
      XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
          baos));
      encoder.writeObject(object);
      encoder.close();
      writeFile(custodyID, relativePath, baos.toByteArray());
    } catch (Exception e) {
      throw new CustodyException(e);
    } 
  }

  
  protected Object readObject(String custodyID, String relativePath) throws CustodyException {
    try {
      byte[] data = readFile(custodyID, relativePath);
      if (data == null) {
        return null;
      }
      XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(data));
      Object o = (Object) decoder.readObject();
      decoder.close();
      return o;
    } catch (Exception e) {
      throw new CustodyException(e);
    }
  }
  
  // Implementar 
  protected abstract void deleteFile(String custodyID, String ... relativePaths);
  
  
  // Implementar
  protected abstract boolean existsFile(String custodyID, String relativePath);
  
  // Implementar
  protected abstract void writeFile(String custodyID, String relativePath, byte[] data) throws Exception;
  
  // Implementar
  protected abstract byte[] readFile(String custodyID, String relativePath) throws Exception;
  
  // Implementar
  protected abstract String getPropertyBase();

 
  
}
