package org.fundaciobit.plugins.validatesignature.integra;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.parsers.DocumentBuilderFactory;

import net.java.xades.security.xml.XMLSignatureElement;

import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.cms.CMSSignedData;
import org.fundaciobit.plugins.validatesignature.api.AbstractValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.IValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.SignatureRequestedInformation;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureRequest;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureResponse;
import org.fundaciobit.plugins.validatesignature.api.ValidationStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.gob.afirma.i18n.Language;
import es.gob.afirma.signature.SignatureFormatDetector;
import es.gob.afirma.signature.SigningException;

/**
 *
 * @author anadal
 *
 */
public class IntegraValidateSignaturePlugin extends AbstractValidateSignaturePlugin implements
    IValidateSignaturePlugin {

  public static final String FORMAT_UNRECOGNIZED = "UNRECOGNIZED";
  public static final String FORMAT_CADES_A = "CAdES-A";
  public static final String FORMAT_CADES_XL1 = "CAdES-XL1";
  public static final String FORMAT_CADES_XL2 = "CAdES-XL2";
  public static final String FORMAT_CADES_X1 = "CAdES-X1";
  public static final String FORMAT_CADES_X2 = "CAdES-X2";
  public static final String FORMAT_CADES_C = "CAdES-C";
  public static final String FORMAT_CADES_T = "CAdES-T";
  public static final String FORMAT_CADES_EPES = "CAdES-EPES";
  public static final String FORMAT_CADES_BES = "CAdES-BES";
  public static final String FORMAT_XADES_A = "XAdES-A";
  public static final String FORMAT_XADES_XL1 = "XAdES-XL1";
  public static final String FORMAT_XADES_XL2 = "XAdES-XL2";
  public static final String FORMAT_XADES_X1 = "XAdES-X1";
  public static final String FORMAT_XADES_X2 = "XAdES-X2";
  public static final String FORMAT_XADES_C = "XAdES-C";
  public static final String FORMAT_XADES_T = "XAdES-T";
  public static final String FORMAT_XADES_EPES = "XAdES-EPES";
  public static final String FORMAT_XADES_BES = "XAdES-BES";
  public static final String FORMAT_PDF = "PDF";
  public static final String FORMAT_PADES_BASIC = "PAdES-Basic";
  public static final String FORMAT_PADES_BES = "PAdES-BES";
  public static final String FORMAT_PADES_EPES = "PAdES-EPES";
  public static final String FORMAT_PADES_LTV = "PAdES-LTV";

  public static final Map<String, String> localSignProfile2PluginSignProfile = new HashMap<String, String>();

  public static final Map<String, String> localSignType2PluginSignType = new HashMap<String, String>();

  static {

    localSignType2PluginSignType.put(FORMAT_CADES_A, ValidateSignatureResponse.SIGNTYPE_CAdES);
    localSignType2PluginSignType.put(FORMAT_CADES_XL1,
        ValidateSignatureResponse.SIGNTYPE_CAdES);
    localSignType2PluginSignType.put(FORMAT_CADES_XL2,
        ValidateSignatureResponse.SIGNTYPE_CAdES);
    localSignType2PluginSignType
        .put(FORMAT_CADES_X1, ValidateSignatureResponse.SIGNTYPE_CAdES);
    localSignType2PluginSignType
        .put(FORMAT_CADES_X2, ValidateSignatureResponse.SIGNTYPE_CAdES);
    localSignType2PluginSignType.put(FORMAT_CADES_C, ValidateSignatureResponse.SIGNTYPE_CAdES);
    localSignType2PluginSignType.put(FORMAT_CADES_T, ValidateSignatureResponse.SIGNTYPE_CAdES);
    localSignType2PluginSignType.put(FORMAT_CADES_EPES,
        ValidateSignatureResponse.SIGNTYPE_CAdES);
    localSignType2PluginSignType.put(FORMAT_CADES_BES,
        ValidateSignatureResponse.SIGNTYPE_CAdES);
    localSignType2PluginSignType.put(FORMAT_XADES_A, ValidateSignatureResponse.SIGNTYPE_XAdES);
    localSignType2PluginSignType.put(FORMAT_XADES_XL1,
        ValidateSignatureResponse.SIGNTYPE_XAdES);
    localSignType2PluginSignType.put(FORMAT_XADES_XL2,
        ValidateSignatureResponse.SIGNTYPE_XAdES);
    localSignType2PluginSignType
        .put(FORMAT_XADES_X1, ValidateSignatureResponse.SIGNTYPE_XAdES);
    localSignType2PluginSignType
        .put(FORMAT_XADES_X2, ValidateSignatureResponse.SIGNTYPE_XAdES);
    localSignType2PluginSignType.put(FORMAT_XADES_C, ValidateSignatureResponse.SIGNTYPE_XAdES);
    localSignType2PluginSignType.put(FORMAT_XADES_T, ValidateSignatureResponse.SIGNTYPE_XAdES);
    localSignType2PluginSignType.put(FORMAT_XADES_EPES,
        ValidateSignatureResponse.SIGNTYPE_XAdES);
    localSignType2PluginSignType.put(FORMAT_XADES_BES,
        ValidateSignatureResponse.SIGNTYPE_XAdES);
    localSignType2PluginSignType.put(FORMAT_PDF, ValidateSignatureResponse.SIGNTYPE_PDF);
    localSignType2PluginSignType.put(FORMAT_PADES_BASIC,
        ValidateSignatureResponse.SIGNTYPE_PAdES);
    localSignType2PluginSignType.put(FORMAT_PADES_BES,
        ValidateSignatureResponse.SIGNTYPE_PAdES);
    localSignType2PluginSignType.put(FORMAT_PADES_EPES,
        ValidateSignatureResponse.SIGNTYPE_PAdES);
    localSignType2PluginSignType.put(FORMAT_PADES_LTV,
        ValidateSignatureResponse.SIGNTYPE_PAdES);

    localSignProfile2PluginSignProfile.put(FORMAT_CADES_A,
        ValidateSignatureResponse.SIGNPROFILE_A);
    localSignProfile2PluginSignProfile.put(FORMAT_CADES_XL1,
        ValidateSignatureResponse.SIGNPROFILE_XL1);
    localSignProfile2PluginSignProfile.put(FORMAT_CADES_XL2,
        ValidateSignatureResponse.SIGNPROFILE_XL2);
    localSignProfile2PluginSignProfile.put(FORMAT_CADES_X1,
        ValidateSignatureResponse.SIGNPROFILE_X1);
    localSignProfile2PluginSignProfile.put(FORMAT_CADES_X2,
        ValidateSignatureResponse.SIGNPROFILE_X2);
    localSignProfile2PluginSignProfile.put(FORMAT_CADES_C,
        ValidateSignatureResponse.SIGNPROFILE_C);
    localSignProfile2PluginSignProfile.put(FORMAT_CADES_T,
        ValidateSignatureResponse.SIGNPROFILE_T);
    localSignProfile2PluginSignProfile.put(FORMAT_CADES_EPES,
        ValidateSignatureResponse.SIGNPROFILE_EPES);
    localSignProfile2PluginSignProfile.put(FORMAT_CADES_BES,
        ValidateSignatureResponse.SIGNPROFILE_BES);
    localSignProfile2PluginSignProfile.put(FORMAT_XADES_A,
        ValidateSignatureResponse.SIGNPROFILE_A);
    localSignProfile2PluginSignProfile.put(FORMAT_XADES_XL1,
        ValidateSignatureResponse.SIGNPROFILE_XL1);
    localSignProfile2PluginSignProfile.put(FORMAT_XADES_XL2,
        ValidateSignatureResponse.SIGNPROFILE_XL2);
    localSignProfile2PluginSignProfile.put(FORMAT_XADES_X1,
        ValidateSignatureResponse.SIGNPROFILE_X1);
    localSignProfile2PluginSignProfile.put(FORMAT_XADES_X2,
        ValidateSignatureResponse.SIGNPROFILE_X2);
    localSignProfile2PluginSignProfile.put(FORMAT_XADES_C,
        ValidateSignatureResponse.SIGNPROFILE_C);
    localSignProfile2PluginSignProfile.put(FORMAT_XADES_T,
        ValidateSignatureResponse.SIGNPROFILE_T);
    localSignProfile2PluginSignProfile.put(FORMAT_XADES_EPES,
        ValidateSignatureResponse.SIGNPROFILE_EPES);
    localSignProfile2PluginSignProfile.put(FORMAT_XADES_BES,
        ValidateSignatureResponse.SIGNPROFILE_BES);
    // TODO Posam BES
    localSignProfile2PluginSignProfile.put(FORMAT_PDF,
        ValidateSignatureResponse.SIGNPROFILE_BES);
    localSignProfile2PluginSignProfile.put(FORMAT_PADES_BASIC,
        ValidateSignatureResponse.SIGNPROFILE_PADES_BASIC);
    localSignProfile2PluginSignProfile.put(FORMAT_PADES_BES,
        ValidateSignatureResponse.SIGNPROFILE_BES);
    localSignProfile2PluginSignProfile.put(FORMAT_PADES_EPES,
        ValidateSignatureResponse.SIGNPROFILE_EPES);
    localSignProfile2PluginSignProfile.put(FORMAT_PADES_LTV,
        ValidateSignatureResponse.SIGNPROFILE_PADES_LTV);

  }

  /**
   * 
   */
  public IntegraValidateSignaturePlugin() {
    super();
  }

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public IntegraValidateSignaturePlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);
  }

  /**
   * @param propertyKeyBase
   */
  public IntegraValidateSignaturePlugin(String propertyKeyBase) {
    super(propertyKeyBase);
  }

  @Override
  public ValidateSignatureResponse validateSignature(ValidateSignatureRequest validationRequest)
      throws Exception {

    byte[] signData = validationRequest.getSignatureData();
    String format = SignatureFormatDetector.getSignatureFormat(validationRequest
        .getSignatureData());

    ValidateSignatureResponse si = new ValidateSignatureResponse();
    ValidationStatus validationStatus = new ValidationStatus();
    
    final boolean debug =log.isDebugEnabled();
    
    if(debug) {
      log.debug("FormatCompost = ]" + format + "[");
    }

    if (format == null || "UNRECOGNIZED".equals(format)) {

      // No fer res: deixar ho com a null
      validationStatus.setStatus(ValidationStatus.SIGNATURE_ERROR);
      validationStatus.setErrorMsg("No s'ha pogut esbrinar el tipus de firma");
      
    } else {
      String signType = localSignType2PluginSignType.get(format);
      si.setSignType(signType);
      
      // System.out.println(" SignType = ]" + signType + "[");
      
      si.setSignProfile(localSignProfile2PluginSignProfile.get(format));

      if (signType != null) {
        si.setSignFormat(getSignFormat(signType, signData));
      }
      
      // TODO Això esta bé ja que només indica que el proces ha anat bé, no es refereix
      // a la validesa de la firma.
      validationStatus.setStatus(ValidationStatus.SIGNATURE_VALID);

    }

    si.setValidationStatus(validationStatus);

    return si;

  }

  @Override
  public SignatureRequestedInformation getSupportedSignatureRequestedInformation() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SignatureRequestedInformation getSupportedSignatureRequestedInformationBySignatureType(
      String signType) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * AQUEST MÈTODE ESTA DUPLICAT AL PLUGIN-AFIRMACXF
   * 
   * @param signType
   * @param signData
   * @return
   * @throws Exception
   */
  protected String getSignFormat(String signType, final byte[] signData) throws Exception {
    String signFormat;
    if (ValidateSignatureResponse.SIGNTYPE_CMS.equals(signType)) { // "CMS";
      // TODO Això no se si es correcte !!!!!!!
      try {
        signFormat = getCAdESFormat(signData);
      } catch (Throwable th) {
        log.error(
            "Error intentant obtenir el format d'una firma CMS emprant el mètode getCAdESFormat(): "
                + th.getMessage(), th);
        signFormat = null;
      }
    } else if (ValidateSignatureResponse.SIGNTYPE_CAdES.equals(signType)) { // "CAdES";
      signFormat = getCAdESFormat(signData);
    } else if (ValidateSignatureResponse.SIGNTYPE_XAdES.equals(signType)) { // "XAdES";
      signFormat = getXAdESFormat(signData);
    } else if (ValidateSignatureResponse.SIGNTYPE_ODF.equals(signType)) { // "ODF";
      signFormat = ValidateSignatureResponse.SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED;
    } else if (ValidateSignatureResponse.SIGNTYPE_PDF.equals(signType)) { // "PDF";
                                                                          // //
                                                                          // ?????
      signFormat = ValidateSignatureResponse.SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED;
    } else if (ValidateSignatureResponse.SIGNTYPE_PAdES.equals(signType)) { // "PAdES";
      signFormat = ValidateSignatureResponse.SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED;
    } else if (ValidateSignatureResponse.SIGNTYPE_OOXML.equals(signType)) { // "OOXML";
      signFormat = ValidateSignatureResponse.SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED;
    } else if (ValidateSignatureResponse.SIGNTYPE_XML_DSIG.equals(signType)) { // "XML_DSIG";
      // TODO Això no se si es correcte !!!!!!!
      try {
        signFormat = getXAdESFormat(signData);
      } catch (Throwable th) {
        log.error("Error intentant obtenir el format d'una firma XML_DSIG emprant el"
            + " mètode getXAdESFormat(): " + th.getMessage(), th);
        signFormat = null;
      }
    } else {
      log.warn("Error intentant trobar el format de una firma amb tipus desconegut: "
          + signType, new Exception());
      signFormat = null;
    }
    return signFormat;
  }

  /**
   * AQUEST MÈTODE ESTA DUPLICAT AL PLUGIN-AFIRMACXF
   * 
   * @param eSignature
   * @return
   * @throws SigningException
   */
  public static String getXAdESFormat(byte[] signature) throws Exception {

    DocumentBuilderFactory dBFactory = DocumentBuilderFactory.newInstance();
    dBFactory.setNamespaceAware(true);

    Document eSignature = dBFactory.newDocumentBuilder().parse(
        new ByteArrayInputStream(signature));

    XMLSignature xmlSignature;
    String rootName = eSignature.getDocumentElement().getNodeName();
    if (rootName.equalsIgnoreCase("ds:Signature") || rootName.equals("ROOT_COSIGNATURES")) {
      // "XAdES Enveloping"
      return ValidateSignatureResponse.SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED;
    }
    NodeList signatureNodeLs = eSignature.getElementsByTagName("ds:Manifest");
    if (signatureNodeLs.getLength() > 0) {
      // "XAdES Externally Detached
      return ValidateSignatureResponse.SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED;
    }
    NodeList signsList = eSignature.getElementsByTagNameNS(
        "http://www.w3.org/2000/09/xmldsig#", "Signature");
    if (signsList.getLength() == 0) {
      throw new SigningException(Language.getResIntegra("XS003"));
    }
    Node signatureNode = signsList.item(0);
    try {
      xmlSignature = new XMLSignatureElement((Element) signatureNode).getXMLSignature();
    } catch (MarshalException e) {
      throw new SigningException(Language.getResIntegra("XS005"), e);
    }
    List<?> references = xmlSignature.getSignedInfo().getReferences();
    for (int i = 0; i < references.size(); ++i) {
      if (!"".equals(((Reference) references.get(i)).getURI()))
        continue;
      // "XAdES Enveloped"
      return ValidateSignatureResponse.SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED;
    }
    // "XAdES Detached"
    return ValidateSignatureResponse.SIGNFORMAT_EXPLICIT_DETACHED;
  }

  /**
   * AQUEST MÈTODE ESTA DUPLICAT AL PLUGIN-AFIRMACXF
   * 
   * @param signature
   * @return
   * @throws Exception
   */
  public static String getCAdESFormat(byte[] signature) throws Exception {

    CMSSignedData cmsSignedData = new CMSSignedData(signature);

    // ContentInfo contentInfo = cmsSignedData.toASN1Structure();
    // //getContentInfo();
    ContentInfo contentInfo;
    try {
      java.lang.reflect.Method method;
      method = cmsSignedData.getClass().getMethod("toASN1Structure");

      contentInfo = (ContentInfo) method.invoke(cmsSignedData);

    } catch (Exception e) {
      java.lang.reflect.Method method;
      method = cmsSignedData.getClass().getMethod("getContentInfo");

      contentInfo = (ContentInfo) method.invoke(cmsSignedData);

    }

    // (Object)contentInfo.getContent()
    java.lang.reflect.Method method;
    method = contentInfo.getClass().getMethod("getContent");

    Object encodable = method.invoke(contentInfo);

    SignedData signedData = SignedData.getInstance(encodable);
    ContentInfo contentInfo2 = signedData.getEncapContentInfo();
    boolean isImplicit = false;
    if (contentInfo2 != null) {

      // signedData.getEncapContentInfo().getContent() != null) {
      java.lang.reflect.Method method2;
      method2 = contentInfo2.getClass().getMethod("getContent");

      Object obj = method2.invoke(contentInfo2);

      if (obj != null) {
        isImplicit = true;
      }
    }

    if (isImplicit) {
      // "CAdES attached/implicit signature";
      return ValidateSignatureResponse.SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED;
    } else {
      // "CAdES detached/explicit signature"
      return ValidateSignatureResponse.SIGNFORMAT_EXPLICIT_DETACHED;
    }
  }

}
