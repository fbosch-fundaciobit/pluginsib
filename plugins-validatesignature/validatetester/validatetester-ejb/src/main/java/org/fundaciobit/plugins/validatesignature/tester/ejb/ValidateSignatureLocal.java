package org.fundaciobit.plugins.validatesignature.tester.ejb;

import java.util.List;

import javax.ejb.Local;

import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureRequest;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureResponse;
import org.fundaciobit.plugins.validatesignature.tester.utils.Plugin;

/**
 * 
 * @author anadal
 *
 */
@Local
public interface ValidateSignatureLocal {
  
  public static final String JNDI_NAME = "validatesignature/ValidateSignatureEJB/local";
  
  
  public ValidateSignatureResponse validate(ValidateSignatureRequest validationRequest,
      long pluginID) throws Exception;
  
  public List<Plugin> getPlugins() throws Exception;

  /* XYZ ZZZ
  public List<Plugin> getAllPluginsFiltered(String signaturesSetID) throws Exception;
  
  public SignaturesSet signDocuments(Long pluginID, String signaturesSetID) throws Exception;
  
  public SignaturesSet getSignaturesSet(String signaturesSetID);

  public void clearSignaturesSet(String signaturesSetID);
  */

}
