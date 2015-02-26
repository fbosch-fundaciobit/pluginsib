/**
 * 
 */
package org.fundaciobit.plugins.documentcustody.alfresco;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.documentcustody.AnnexCustody;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.CustodyException;
import org.fundaciobit.plugins.documentcustody.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.NotSupportedCustodyException;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;
import org.fundaciobit.plugins.utils.Metadata;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;

/**
 * Implementació del plugin de custodia documental que guarda les signatures en
 * un fitxer. Si es defineix una URL base d'un servidor web, llavors es pot fer
 * que retorni la URL de validació.
 * 
 * @author anadal
 */
public class AlfrescoDocumentCustodyPlugin extends AbstractPluginProperties
  implements IDocumentCustodyPlugin {

  protected final Logger log = Logger.getLogger(getClass());

  /**
   * 
   */
  public AlfrescoDocumentCustodyPlugin() {
    super();
  }

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public AlfrescoDocumentCustodyPlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);
  }

  /**
   * @param propertyKeyBase
   */
  public AlfrescoDocumentCustodyPlugin(String propertyKeyBase) {
    super(propertyKeyBase);
  }

  private final String CUSTODY_DOCUMENT_PREFIX(String custodyID) {
    return CUSTODY_PREFIX() + "DOC_" + custodyID;
  }

  private final String CUSTODY_SIGNATURE_PREFIX(String custodyID) {
    return CUSTODY_PREFIX() + "SIGN_" + custodyID;
  }

  private final String CUSTODY_DOCUMENT_INFO_PREFIX(String custodyID) {
    return CUSTODY_PREFIX() + "INFODOC_" + custodyID;
  }

  private final String CUSTODY_SIGNATURE_INFO_PREFIX(String custodyID) {
    return CUSTODY_PREFIX() + "INFOSIGN_" + custodyID;
  }

  public static final String ALFRESCO_PROPERTY_BASE = DOCUMENTCUSTODY_BASE_PROPERTY + "alfresco.";

  private final String CUSTODY_PREFIX() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "prefix", "CUST_");
  }


  @Override
  public String reserveCustodyID(String proposedID, String parameters) throws CustodyException {
    return proposedID;
  }

  @Override
  public void saveDocument(String custodyID, String custodyParameters, DocumentCustody document)
      throws CustodyException, NotSupportedCustodyException {

    String infoPath;
    String docPath;

    infoPath = CUSTODY_DOCUMENT_INFO_PREFIX(custodyID);
    docPath = CUSTODY_DOCUMENT_PREFIX(custodyID);

    try {
      
      if (existsFile(docPath)) {
        deleteFile(docPath);
      }
      
      byte[] data = document.getData();
      writeData(docPath, data);

      
      DocumentCustody clone = new DocumentCustody(document);
      clone.setData(null);
      write(clone, infoPath);

    } catch (Exception ex) {
      final String msg = "No s'ha pogut custodiar el document";
      log.error(msg, ex);
      throw new CustodyException(msg, ex);
    }

  }



  @Override
  public DocumentCustody getDocumentInfo(String custodyID) throws CustodyException {

    if (custodyID == null) {
      return null;
    }

    String docPath = CUSTODY_DOCUMENT_PREFIX(custodyID);
    String infoPath = CUSTODY_DOCUMENT_INFO_PREFIX(custodyID);
    return (DocumentCustody) getDocOrSign(custodyID, docPath, infoPath);
  }

  @Override
  public byte[] getDocument(String custodyID) throws CustodyException {
    DocumentCustody info = getDocumentInfo(custodyID);
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

  @Override
  public void saveSignature(String custodyID, String custodyParameters,
      SignatureCustody document) throws CustodyException {

    String infoPath;
    String docPath;

    infoPath = CUSTODY_SIGNATURE_INFO_PREFIX(custodyID);
    docPath = CUSTODY_SIGNATURE_PREFIX(custodyID);

    try {
      
      if (existsFile(docPath)) {
        deleteFile(docPath);
      }
      writeData(docPath, document.getData());
      
      SignatureCustody clone = new SignatureCustody(document);
      clone.setData(null);
      write(clone, infoPath);

    } catch (Exception ex) {
      final String msg = "No s'ha pogut custodiar el document";
      log.error(msg, ex);
      throw new CustodyException(msg, ex);
    }

  }

  @Override
  public SignatureCustody getSignatureInfo(String custodyID) throws CustodyException {

    if (custodyID == null) {
      return null;
    }

    String docPath = CUSTODY_SIGNATURE_PREFIX(custodyID);
    String infoPath = CUSTODY_SIGNATURE_INFO_PREFIX(custodyID);
    return (SignatureCustody) getDocOrSign(custodyID, docPath, infoPath);
  }

  @Override
  public byte[] getSignature(String custodyID) throws CustodyException {
    SignatureCustody info = getSignatureInfo(custodyID);
    return info.getData();
  }

  @Override
  public String[] getSupportedSignatureTypes() {
    return new String[] { SignatureCustody.CADES_SIGNATURE, SignatureCustody.XADES_SIGNATURE,
        SignatureCustody.SMIME_SIGNATURE, SignatureCustody.OTHER_SIGNATURE };
  }

  /**
   * @return true if system automaically refresh signature o document with
   *         signature to not loss validate of signature.
   */
  @Override
  public boolean refreshSignature() {
    return false;
  }

  /**
   * 
   * @param custodyID
   * @param annex
   * @return AnnexID
   * @throws CustodyException
   */
  @Override
  public String addAnnex(String custodyID, AnnexCustody annex) throws CustodyException,
      NotSupportedCustodyException {
    throw new NotSupportedCustodyException();
  }

  /**
   * 
   * @param custodyID
   * @param annexID
   * @return null if annex not found
   */
  @Override
  public byte[] getAnnex(String custodyID, String annexID) {
    return null;
  }

  /**
   * 
   * @param custodyID
   * @param annexID
   * @return null if annex not found
   */
  @Override
  public AnnexCustody getAnnexInfo(String custodyID, String annexID) {
    return null;
  }

  @Override
  public boolean supportAnnexs() {
    return false;
  }

  @Override
  public void addMetadata(Metadata metadata) throws CustodyException,
      NotSupportedCustodyException {
    throw new NotSupportedCustodyException();
  }

  @Override
  public Metadata[] getMetadatas() {
    return null;
  }

  @Override
  public boolean supportMetadata() {
    return false;
  }
  
  @Override
  public boolean supportDelete() {
    return true;
  }

  @Override
  public void deleteCustody(String custodyID) throws CustodyException {

    String[] prefixes = new String[] { CUSTODY_DOCUMENT_PREFIX(custodyID),
        CUSTODY_SIGNATURE_PREFIX(custodyID), CUSTODY_DOCUMENT_INFO_PREFIX(custodyID),
        CUSTODY_SIGNATURE_INFO_PREFIX(custodyID) };

    for (String file : prefixes) {
      deleteFile(file);
    }

  }



  /**
   * Si existeix el document de Signatura llavors el retornam, ja que és el que
   * es necessita per validar el document. En cas contrari o no té firmes o el
   * propi document ja duu adjunta la firma, per lo que retornam el document.
   */
  @SuppressWarnings("deprecation")
  @Override
  public String getValidationUrl(String custodyID) throws CustodyException {

    String baseUrl = getURLBase();
    if (baseUrl == null) {
      return null;
    }

    return MessageFormat.format(baseUrl, custodyID, URLEncoder.encode(custodyID));

  }

  @Override
  public String getSpecialValue(String custodyID) throws CustodyException {
    return custodyID;
  }

  protected AnnexCustody getDocOrSign(String custodyID, String docPath, String infoPath)
      throws CustodyException {
    try {

      //File f = new File(getBaseDir(), docPath);
      if (!existsFile(docPath)) {
        return null;
      }
      byte[] data = readData(docPath);

      
      AnnexCustody dc = (AnnexCustody) read(infoPath);
      dc.setData(data);

      return dc;

    } catch (Exception ex) {
      final String msg = "Error intentant obtenir el document custodia amb ID = " + custodyID;
      log.error(msg, ex);
      throw new CustodyException(msg, ex);
    }
  }

 

  
  
  // Directori base de alfresco 
  private String getBaseDir() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "basedir");
  }


  private String getURLBase() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "baseurl");
  }
  
  
  // TODO Implementar per Alfresco
  private void write(Object f, String relativePath) throws Exception {
    File info = new File(getBaseDir(), relativePath);
    XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
        new FileOutputStream(info)));
    encoder.writeObject(f);
    encoder.close();
  }

  // TODO Implementar per Alfresco
  private Object read(String  relativePath) throws Exception {
    File info = new File(getBaseDir(), relativePath);
    XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(info)));
    Object o = (Object) decoder.readObject();
    decoder.close();
    return o;
  }
  
  // TODO Implementar per Alfresco
  private void deleteFile(String relativePath) {
    File f = new File(getBaseDir(), relativePath);
    if (f.exists()) {
      if (!f.delete()) {
        f.deleteOnExit();
      }
    }
  }
  
  
  //TODO Implementar per Alfresco
  private boolean existsFile(String relativePath) {
    File f = new File(getBaseDir(), relativePath);
    return f.exists();
  }
  
  //TODO Implementar per Alfresco
  private void writeData(String relativePath, byte[] data) throws FileNotFoundException, IOException {
    FileOutputStream fos = new FileOutputStream(new File(getBaseDir(), relativePath));
    fos.write(data);
    fos.close();
  }
  
  //TODO Implementar per Alfresco
  private byte[] readData(String docPath) throws FileNotFoundException, IOException {
    byte[] data;
    {
    FileInputStream fis = new FileInputStream(new File(getBaseDir(), docPath));
    data = IOUtils.toByteArray(fis);
    fis.close();
    }
    return data;
  }

}
