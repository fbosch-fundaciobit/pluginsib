/**
 * 
 */
package org.fundaciobit.plugins.documentcustody.filesystem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
public class FileSystemDocumentCustodyPlugin extends AbstractPluginProperties
  implements IDocumentCustodyPlugin {

  protected final Logger log = Logger.getLogger(getClass());

  /**
   * 
   */
  public FileSystemDocumentCustodyPlugin() {
    super();
  }

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public FileSystemDocumentCustodyPlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);
  }

  /**
   * @param propertyKeyBase
   */
  public FileSystemDocumentCustodyPlugin(String propertyKeyBase) {
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

  private static final String PROPERTY_BASE = DOCUMENTCUSTODY_BASE_PROPERTY + "filesystem.";

  private final String CUSTODY_PREFIX() {
    return getProperty(PROPERTY_BASE + "prefix", "CUST_");
  }

  private String getBaseDir() {
    return getProperty(PROPERTY_BASE + "basedir");
  }

  private String getURLBase() {
    return getProperty(PROPERTY_BASE + "baseurl");
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
      File f = new File(getBaseDir(), docPath);
      if (f.exists()) {
        f.delete();
      }
      FileOutputStream fos = new FileOutputStream(f);
      fos.write(document.getData());
      fos.close();

      File info = new File(getBaseDir(), infoPath);
      DocumentCustody clone = new DocumentCustody(document);
      clone.setData(null);
      write(clone, info);

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
      File f = new File(getBaseDir(), docPath);
      if (f.exists()) {
        f.delete();
      }
      FileOutputStream fos = new FileOutputStream(f);
      fos.write(document.getData());
      fos.close();

      File info = new File(getBaseDir(), infoPath);
      SignatureCustody clone = new SignatureCustody(document);
      clone.setData(null);
      write(clone, info);

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
      File f = new File(getBaseDir(), file);
      if (f.exists()) {
        if (!f.delete()) {
          f.deleteOnExit();
        }
      }
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

      File f = new File(getBaseDir(), docPath);
      if (!f.exists()) {
        return null;
      }
      FileInputStream fis = new FileInputStream(f);
      byte[] data = IOUtils.toByteArray(fis);
      fis.close();

      File info = new File(getBaseDir(), infoPath);
      AnnexCustody dc = (AnnexCustody) read(info);
      dc.setData(data);

      return dc;

    } catch (Exception ex) {
      final String msg = "Error intentant obtenir el document custodia amb ID = " + custodyID;
      log.error(msg, ex);
      throw new CustodyException(msg, ex);
    }
  }

  public static void write(Object f, File filename) throws Exception {
    XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
        new FileOutputStream(filename)));
    encoder.writeObject(f);
    encoder.close();
  }

  public static Object read(File filename) throws Exception {
    XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)));
    Object o = (Object) decoder.readObject();
    decoder.close();
    return o;
  }

}
