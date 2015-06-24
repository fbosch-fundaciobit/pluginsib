package org.fundaciobit.plugins.documentcustody.alfresco;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.bindings.spi.webservices.CXFPortProvider;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.fundaciobit.plugins.documentcustody.AbstractDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.CustodyException;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.NotSupportedCustodyException;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;
import org.fundaciobit.plugins.documentcustody.alfresco.cmis.OpenCmisAlfrescoHelper;
import org.fundaciobit.plugins.documentcustody.alfresco.util.AlfrescoUtils;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;

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

  
  public String getAlfrescoUrl() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "url");
  }
  
  public String getRepositoryID() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "repository");
  }
  
  public String getSite() {
    return getProperty(ALFRESCO_PROPERTY_BASE + "site");
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

			String docPath = AlfrescoUtils.getPathFromRegistreObject(custodyParameters);
			
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
  
  @Override
  protected void writeFile(String custodyID, String relativePath, byte[] data) throws Exception {
	    try {

	    	OpenCmisAlfrescoHelper.getDocumentById(getCmisSession(), custodyID);

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
  
  
  
  // =================================================
  
  private Session cmisSession;
  

  public Session getCmisSession() {
    
    if (cmisSession == null) {

      SessionFactory factory = SessionFactoryImpl.newInstance();
      Map<String, String> parameter = new HashMap<String, String>();
      String methodAccess = getAccessMethod();

      if ("ATOM".equals(methodAccess)) {
        
        parameter.put(SessionParameter.ATOMPUB_URL, getAlfrescoUrl());
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameter.put(SessionParameter.AUTH_HTTP_BASIC, "true");
        parameter.put(SessionParameter.USER, getUsername());
        parameter.put(SessionParameter.PASSWORD, getPassword());
        parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

        List<Repository> repositories = factory.getRepositories(parameter);
        cmisSession = repositories.get(0).createSession();
        
      }else if ("WS".equals(methodAccess)) {
        
        parameter.put(SessionParameter.USER, getUsername());
        parameter.put(SessionParameter.PASSWORD, getPassword());
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.WEBSERVICES.value());
        parameter.put(SessionParameter.REPOSITORY_ID, getRepositoryID());
        parameter.put(SessionParameter.WEBSERVICES_PORT_PROVIDER_CLASS, CXFPortProvider.class.getName());
        parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");
        //parameter.put(SessionParameter.WEBSERVICES_JAXWS_IMPL, "sunjre");
        parameter.put(SessionParameter.WEBSERVICES_ACL_SERVICE, getAlfrescoUrl()+"/ACLService?WSDL");
        parameter.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, getAlfrescoUrl()+"/DiscoveryService?WSDL");
        parameter.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, getAlfrescoUrl()+"/MultiFilingService?WSDL");
        parameter.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, getAlfrescoUrl()+"/NavigationService?WSDL");
        parameter.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, getAlfrescoUrl()+"/ObjectService?WSDL");
        parameter.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, getAlfrescoUrl()+"/PolicyService?WSDL");
        parameter.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, getAlfrescoUrl()+"/RelationshipService?WSDL");
        parameter.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, getAlfrescoUrl()+"/RepositoryService?WSDL");
        parameter.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, getAlfrescoUrl()+"/VersioningService?WSDL");
        parameter.put(SessionParameter.LOCALE_ISO3166_COUNTRY, "ES");
        parameter.put(SessionParameter.LOCALE_ISO639_LANGUAGE, "es");
        parameter.put(SessionParameter.LOCALE_VARIANT, "");
        
        /*List<Repository> repositories = factory.getRepositories(parameter);
        for (Repository r : repositories) {
            System.out.println("Found repository: " + r.getName());
        }*/
        
        cmisSession = factory.createSession(parameter);
      }

    }

    return cmisSession;
  }
  
  
  public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  
  private HttpRequestFactory requestFactory;

  public HttpRequestFactory getRequestFactory() {
    if (requestFactory == null) {
        requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
          public void initialize(HttpRequest request) throws IOException {
            request.setParser(new JsonObjectParser(new JacksonFactory()));
            request.getHeaders().setBasicAuthentication(getUsername(), getPassword());
          }
        });
    }
    return requestFactory;
  }

}
