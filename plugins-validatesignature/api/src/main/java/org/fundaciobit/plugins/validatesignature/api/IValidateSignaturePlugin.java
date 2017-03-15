package org.fundaciobit.plugins.validatesignature.api;

import org.fundaciobit.plugins.IPlugin;


/**
 * 
 * @author anadal
 *
 */
public interface IValidateSignaturePlugin extends IPlugin {

  public static final String CMS = "CMS";
  public static final String CAdES = "CAdES";
  public static final String CAdES_BES = "CAdES_BES";
  public static final String CAdES_EPES = "CAdES_EPES";
  public static final String CAdES_T = "CAdES_T";
  public static final String CAdES_X = "CAdES_X";
  public static final String CAdES_X1 = "CAdES_X1";
  public static final String CAdES_X2 = "CAdES_X2";
  public static final String CAdes_XL = "CAdes_XL";
  public static final String CAdES_XL1 = "CAdES_XL1";
  public static final String CAdES_XL2 = "CAdES_XL2";
  public static final String CAdES_A = "CAdES_A";
  public static final String XAdES = "XAdES";
  public static final String XAdES_BES = "XAdES_BES";
  public static final String XAdES_EPES = "XAdES_EPES";
  public static final String XAdES_T = "XAdES_T";
  public static final String XAdES_X = "XAdES_X";
  public static final String XAdES_X1 = "XAdES_X1";
  public static final String XAdES_X2 = "XAdES_X2";
  public static final String XAdES_XL = "XAdES_XL";
  public static final String XAdES_XL1 = "XAdES_XL1";
  public static final String XAdES_XL2 = "XAdES_XL2";
  public static final String XAdES_A = "XAdES_A";
  public static final String ODF = "ODF";
  public static final String PDF = "PDF";
  public static final String PAdES = "PAdES";
  public static final String PAdES_BES = "PAdES_BES";
  public static final String PAdES_EPES = "PAdES_EPES";
  public static final String PAdES_LTV = "PAdES_LTV";

  public static final String UNKNOWN = "unknown";
  
  
  public static final String VALIDATE_SIGNATURE_BASE_PROPERTY = IPLUGIN_BASE_PROPERTIES + "validatesignature.";
  
  
  public boolean filter();
  
  
  public SignatureInfo validateSignature(ValidateSignatureRequest validationRequest) throws Exception;
  
  
}
