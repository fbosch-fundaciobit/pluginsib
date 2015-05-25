package org.fundaciobit.plugins.documentcustody.alfresco;

import java.util.Properties;

import org.fundaciobit.plugins.documentcustody.AbstractDocumentCustodyPlugin;

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
  protected void writeFile(String custodyID, String relativePath, byte[] data)
      throws Exception {
    // TODO Auto-generated method stub
    
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
