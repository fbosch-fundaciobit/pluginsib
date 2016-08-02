package org.fundaciobit.plugins.documentcustody.alfresco.base;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.fundaciobit.plugins.documentcustody.api.AbstractDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.CustodyException;
import org.fundaciobit.plugins.documentcustody.api.NotSupportedCustodyException;
import org.fundaciobit.plugins.documentcustody.alfresco.base.cmis.OpenCmisAlfrescoHelper;


/**
 * Implementació del plugin de custodia documental que guarda dins Alfresco.
 *  Si es defineix una URL base d'un servidor web, llavors es pot fer
 * que retorni la URL de validació.
 * 
 * @author anadal
 */
public class AlfrescoBaseDocumentCustodyPlugin extends AbstractDocumentCustodyPlugin {
  
  
  protected OpenCmisAlfrescoHelper openCmisAlfrescoHelper = new OpenCmisAlfrescoHelper(this);
  

  /**
   * 
   */
  public AlfrescoBaseDocumentCustodyPlugin() {
    super();
  }

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public AlfrescoBaseDocumentCustodyPlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);
  }


  public static final String ALFRESCO_PROPERTY_BASE = DOCUMENTCUSTODY_BASE_PROPERTY + "alfresco.";

  
  public String getAlfrescoUrl() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "url");
  }
  
  public String getRepositoryID() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "repository");
  }
  
  public String getSite() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "site");
  }
  
  public String getFullSitePath() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "fullsitepath");
  }
  
  public String getBasePath() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "basepath", "");
  }

  public String getUsername() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "access.user"); 
  }

  public String getPassword() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "access.pass");     
  }
  
  public String getAccessMethod() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "access.method", "WS");
  }
  

  
  @Override
  protected void writeFile(String custodyID, String relativePath, byte[] data) throws Exception {
    
    try {
      
      if (existsFile(custodyID, relativePath)) {
        deleteFile(custodyID, relativePath);
      }
      
      
      int pos = relativePath.replace('\\',  '/').lastIndexOf('/');
      
      String fileFinalName;
      String path;
      if (pos == -1) {
        fileFinalName = relativePath;
        path = "";
      } else {
        fileFinalName = relativePath.substring(0,pos + 1);
        path = relativePath.substring(0,pos);
      }
      
      System.out.println("FinalName = ]" + fileFinalName + "[");
      System.out.println("PATH = ]" + path + "[");
      
      Map<String, Object> properties = new HashMap<String, Object>();
      properties.put(PropertyIds.OBJECT_TYPE_ID, OpenCmisAlfrescoHelper.CMIS_DOCUMENT_TYPE);
      properties.put(PropertyIds.DESCRIPTION, custodyID);
      properties.put(PropertyIds.NAME, fileFinalName);
      
      //TODO: Treurer els parametres que es vulguin guardar dins properties del document i passarlos
        
       openCmisAlfrescoHelper.crearDocument(data, fileFinalName, path, properties, custodyID);

        log.debug("Pujat Document a Alfresco: "+ relativePath);

      } catch (Exception ex) {
      final String msg = "No s'ha pogut guardar el document amb id="+custodyID;
      log.error(msg, ex);
      }
    

  }


  @Override
  protected byte[] readFile(String custodyID, String relativePath) throws Exception {
    
    Document document = openCmisAlfrescoHelper.getDocument(relativePath, custodyID);
    
    return OpenCmisAlfrescoHelper.getCmisObjectContent(document);
    
  }

  @Override
  protected String getPropertyBase() {
    return ALFRESCO_PROPERTY_BASE;
  }
  


  @Override
  protected void deleteFile(String custodyID, String... relativePaths) {

    for (String filePath : relativePaths) {
      try {
        openCmisAlfrescoHelper.borrarDocument(filePath, custodyID);
      } catch(Throwable th) {
        log.warn("No ha pogut borrar el fitxer = " + filePath, th);
      } 
    }
    
    
  }

  @Override
  protected boolean existsFile(String custodyID, String relativePath) {
    try {
      Document document = openCmisAlfrescoHelper.getDocument(relativePath, custodyID);
      if (document == null) {
        return false;
      } else {
        return true;
      }
    } catch(Throwable th) {
      log.debug("No pot determinar si existeix fitxer en el següent path = " + relativePath, th);
      return false;  
    }
    
    
    
  }
  
  
  @Override
  public void deleteCustody(String custodyID) throws CustodyException, NotSupportedCustodyException {
    super.deleteCustody(custodyID);

    openCmisAlfrescoHelper.borrarCarpeta("", custodyID);
  }
  
  
  @Override
  public String reserveCustodyID(String parameters) throws CustodyException {
    String custodyId = super.reserveCustodyID(parameters);
    try {
      openCmisAlfrescoHelper.crearCarpeta(custodyId, null, ""); // Està bé que passem custodyID com a Stirng buit
    }catch(Exception e) {
      throw new CustodyException(e.getMessage(), e);
    }
    return custodyId;
  }
  
  

  /*
  @Override
  public void saveDocument(String custodyID, String custodyParameters, DocumentCustody document) throws CustodyException, NotSupportedCustodyException {

	  try {
		
			String fileFinalame = AlfrescoUtils.getFileNameWithCustodyId(document.getName(), custodyID, false);

	    	Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.OBJECT_TYPE_ID, OpenCmisAlfrescoHelper.CMIS_DOCUMENT_TYPE);
			properties.put(PropertyIds.DESCRIPTION, custodyID+"D");
			properties.put(PropertyIds.NAME, fileFinalame);
		  
			//TODO: Treurer els parametres que es vulguin guardar dins properties del document i passarlos
			
			String docPath = getPathFromCustodyParameters(custodyParameters);
	    	
	    	OpenCmisAlfrescoHelper.crearDocument(getCmisSession(), getSite(), document, fileFinalame, docPath, properties);

	    	log.debug("Pujat Document a Alfresco: "+docPath+"/"+fileFinalame);

	    } catch (Exception ex) {
			final String msg = "No s'ha pogut guardar el document amb id="+custodyID;
			log.error(msg, ex);
	    }
  }


  @Override
  public DocumentCustody getDocumentInfo(String custodyID) throws CustodyException {
	  
	  List<Document> docs = OpenCmisAlfrescoHelper.getDocumentById(getCmisSession(), custodyID+"D");
	  
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
	  
	  List<Document> docs = OpenCmisAlfrescoHelper.getDocumentById(getCmisSession(), custodyID+"D");
	  
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

			String docPath = getPathFromCustodyParameters(custodyParameters);
			
	    	OpenCmisAlfrescoHelper.crearDocument(getCmisSession(), getSite(), document, fileFinalame, docPath, properties);
	    	
	    	log.debug("Pujada firma a Alfresco: "+docPath+"/"+fileFinalame);
	    	
	    } catch (Exception ex) {
			final String msg = "No s'ha pogut guardar el document amb id="+custodyID;
			log.error(msg, ex);
	    }
  }
  
  @Override
  public SignatureCustody getSignatureInfo(String custodyID) throws CustodyException {

	  List<Document> docs = OpenCmisAlfrescoHelper.getDocumentById(getCmisSession(), custodyID+"S");

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

	  List<Document> docs = OpenCmisAlfrescoHelper.getDocumentById(getCmisSession(), custodyID+"S");

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
  */
  

  
  
  
  // =================================================
  
  public String getPathFromCustodyParameters(String custodyParameters) {
    return "/";
  }
  


}
