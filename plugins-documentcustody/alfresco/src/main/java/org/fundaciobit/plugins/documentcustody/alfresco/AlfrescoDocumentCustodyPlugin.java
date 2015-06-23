package org.fundaciobit.plugins.documentcustody.alfresco;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.fundaciobit.plugins.documentcustody.AbstractDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.CustodyException;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.NotSupportedCustodyException;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;
import org.fundaciobit.plugins.documentcustody.alfresco.cmis.OpenCmisAlfrescoHelper;
import org.fundaciobit.plugins.documentcustody.alfresco.util.AlfrescoUtils;

/**
 * Implementació del plugin de custodia documental que guarda dins Alfresco.
 *  Si es defineix una URL base d'un servidor web, llavors es pot fer
 * que retorni la URL de validació.
 * 
 * @author anadal
 */
public class AlfrescoDocumentCustodyPlugin extends AbstractDocumentCustodyPlugin {



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


  public static final String ALFRESCO_PROPERTY_BASE = DOCUMENTCUSTODY_BASE_PROPERTY + "alfresco.";
  
  
  private String getUrlAlfresco() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "url");
  }
  
  
  private String getUsernameAlfresco() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "username");
  }
  
  
  private String getPasswordAlfresco() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "password");
  }
  


  @Override
  protected void deleteFile(String custodyID, String... relativePaths) {
    // TODO Auto-generated method stub
    
  }

  @Override
  protected boolean existsFile(String custodyID, String relativePath) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void saveDocument(String custodyID, String custodyParameters, DocumentCustody document) throws CustodyException, NotSupportedCustodyException {

	  try {
		
			String fileFinalame = AlfrescoUtils.getFileNameWithCustodyId(document.getName(), custodyID, false);

	    	Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.OBJECT_TYPE_ID, OpenCmisAlfrescoHelper.CMIS_DOCUMENT_TYPE);
			properties.put(PropertyIds.DESCRIPTION, custodyID+"D");
			properties.put(PropertyIds.NAME, fileFinalame);
		  
			//TODO: Treurer els parametres que es vulguin guardar dins properties del document i passarlos
			
			String docPath = AlfrescoUtils.getPathFromRegistreObject(custodyParameters);
	    	
	    	OpenCmisAlfrescoHelper.crearDocument(document, fileFinalame, docPath, properties);

	    	log.debug("Pujat Document a Alfresco: "+docPath+"/"+fileFinalame);

	    } catch (Exception ex) {
			final String msg = "No s'ha pogut guardar el document amb id="+custodyID;
			log.error(msg, ex);
	    }
  }

  @Override
  public DocumentCustody getDocumentInfo(String custodyID) throws CustodyException {
	  
	  List<Document> docs = OpenCmisAlfrescoHelper.getDocumentById(custodyID+"D");
	  
	  if (docs!=null) {
		  
		  if (docs.size()==1) {
			  
			  try {

				  Document alfDoc = docs.get(0);
				  
				  DocumentCustody cusDoc = new DocumentCustody();
				  cusDoc.setData(AlfrescoUtils.getCmisObjectContent(alfDoc));
				  String nomArxiu = AlfrescoUtils.removeCustodyIdFromFilename(alfDoc.getName(), false);
				  cusDoc.setName(nomArxiu);
				  cusDoc.setMime(alfDoc.getContentStreamMimeType());
				  
				  return cusDoc;
				  
			  }catch (Exception ex) {
			      final String msg = "Error al recuperar el contingut del document amb custodyID: "+custodyID;
			      log.error(msg, ex);
			      throw new CustodyException(msg, ex);
			  }

		  }else{
		      final String msg = "S´ha trobat mes de un document amb el mateix custodyID! ("+custodyID+")";
		      log.error(msg);
		      throw new CustodyException(msg);
		  }
	  }else{
	      final String msg = "No s´ha trobat cap document amb custodyID: "+custodyID;
	      log.error(msg);
	      throw new CustodyException(msg);
	  }
  }
  
  @Override
  public void deleteDocument(String custodyID) throws CustodyException, NotSupportedCustodyException {
	  
	  List<Document> docs = OpenCmisAlfrescoHelper.getDocumentById(custodyID+"D");
	  
	  if (docs!=null) {
		  
		  if (docs.size()==1) {
			  
			  try {

				  docs.get(0).delete();
				  
			  }catch (Exception ex) {
			      final String msg = "Error al borrar el document amb custodyID: "+custodyID;
			      log.error(msg, ex);
			      throw new CustodyException(msg, ex);
			  }

		  }else{
		      final String msg = "S´ha trobat mes de un document amb el mateix custodyID! ("+custodyID+")";
		      log.error(msg);
		      throw new CustodyException(msg);
		  }
	  }else{
	      final String msg = "No s´ha trobat cap document amb custodyID: "+custodyID;
	      log.error(msg);
	      throw new CustodyException(msg);
	  }
  }
  
  @Override
  public void saveSignature(String custodyID, String custodyParameters, SignatureCustody document) throws CustodyException {
	  
	  try {
			
			String fileFinalame = AlfrescoUtils.getFileNameWithCustodyId(document.getName(), custodyID, true);

	    	Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.OBJECT_TYPE_ID, OpenCmisAlfrescoHelper.CMIS_DOCUMENT_TYPE);
			properties.put(PropertyIds.DESCRIPTION, custodyID+"S");
			properties.put(PropertyIds.NAME, fileFinalame);

			String docPath = AlfrescoUtils.getPathFromRegistreObject(custodyParameters);
			
	    	OpenCmisAlfrescoHelper.crearDocument(document, fileFinalame, docPath, properties);
	    	
	    	log.debug("Pujada firma a Alfresco: "+docPath+"/"+fileFinalame);
	    	
	    } catch (Exception ex) {
			final String msg = "No s'ha pogut guardar el document amb id="+custodyID;
			log.error(msg, ex);
	    }
  }
  
  @Override
  public SignatureCustody getSignatureInfo(String custodyID) throws CustodyException {

	  List<Document> docs = OpenCmisAlfrescoHelper.getDocumentById(custodyID+"S");

	  if (docs!=null) {

		  if (docs.size()==1) {

			  try {

				  Document alfDoc = docs.get(0);
				  
				  SignatureCustody regSig = new SignatureCustody();
				  regSig.setData(AlfrescoUtils.getCmisObjectContent(alfDoc));
				  String nomArxiu = AlfrescoUtils.removeCustodyIdFromFilename(alfDoc.getName(), true);
				  regSig.setName(nomArxiu);
				  regSig.setMime(alfDoc.getContentStreamMimeType());
				  
				  return regSig;
				  
			  }catch (Exception ex) {
			      final String msg = "Error al recuperar el contingut de la firma amb custodyID: "+custodyID;
			      log.error(msg, ex);
			      throw new CustodyException(msg, ex);
			  }
		  }else{
		      final String msg = "S´ha trobat mes de una firma amb el mateix custodyID! ("+custodyID+")";
		      log.error(msg);
		      throw new CustodyException(msg);
		  }
	  }else{
	      final String msg = "No s´ha trobat cap firma amb custodyID: "+custodyID;
	      log.error(msg);
	      throw new CustodyException(msg);
	  }
  }
  
  @Override
  public void deleteSignature(String custodyID) throws CustodyException, NotSupportedCustodyException {

	  List<Document> docs = OpenCmisAlfrescoHelper.getDocumentById(custodyID+"S");

	  if (docs!=null) {

		  if (docs.size()==1) {

			  try {

				  docs.get(0).delete();
				  
			  }catch (Exception ex) {
			      final String msg = "Error al borrar la firma amb custodyID: "+custodyID;
			      log.error(msg, ex);
			      throw new CustodyException(msg, ex);
			  }
		  }else{
		      final String msg = "S´ha trobat mes de una firma amb el mateix custodyID! ("+custodyID+")";
		      log.error(msg);
		      throw new CustodyException(msg);
		  }
	  }else{
	      final String msg = "No s´ha trobat cap firma amb custodyID: "+custodyID;
	      log.error(msg);
	      throw new CustodyException(msg);
	  }
  }
  
  @Override
  public void deleteCustody(String custodyID) throws CustodyException, NotSupportedCustodyException {

    if (!supportsDeleteCustody()) {
      throw new NotSupportedCustodyException();
    }
    
    deleteAllMetadata(custodyID);
    
    deleteAllAnnexes(custodyID);
    
    this.deleteSignature(custodyID);

    this.deleteDocument(custodyID);
  }
  
  @Override
  protected void writeFile(String custodyID, String relativePath, byte[] data) throws Exception {
	    try {

	    	OpenCmisAlfrescoHelper.getDocumentById(custodyID);

	    } catch (Exception ex) {
			final String msg = "No s'ha pogut guardar el document amb id="+custodyID;
			log.error(msg, ex);
	    }
  }

  @Override
  protected byte[] readFile(String custodyID, String relativePath) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String getPropertyBase() {
    return ALFRESCO_PROPERTY_BASE;
  }

}
