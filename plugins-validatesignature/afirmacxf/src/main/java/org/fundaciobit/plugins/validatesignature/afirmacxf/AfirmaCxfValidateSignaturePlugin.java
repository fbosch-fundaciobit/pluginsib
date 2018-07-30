package org.fundaciobit.plugins.validatesignature.afirmacxf;

import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.net.ssl.X509TrustManager;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.BindingProvider;

import net.java.xades.security.xml.XMLSignatureElement;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.util.encoders.Base64;
import org.fundaciobit.plugins.utils.XTrustProvider;
import org.fundaciobit.plugins.utils.cxf.CXFUtils;
import org.fundaciobit.plugins.utils.cxf.ClientHandler;
import org.fundaciobit.plugins.utils.cxf.ClientHandlerCertificate;
import org.fundaciobit.plugins.utils.cxf.ClientHandlerUsernamePassword;
import org.fundaciobit.plugins.validatesignature.afirmacxf.validarfirmaapi.DSSSignature;
import org.fundaciobit.plugins.validatesignature.afirmacxf.validarfirmaapi.DSSSignatureService;
import org.fundaciobit.plugins.validatesignature.api.AbstractValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.CertificateInfo;
import org.fundaciobit.plugins.validatesignature.api.SignatureCheck;
import org.fundaciobit.plugins.validatesignature.api.SignatureDetailInfo;
import org.fundaciobit.plugins.validatesignature.api.IValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.SignatureRequestedInformation;
import org.fundaciobit.plugins.validatesignature.api.TimeStampInfo;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureRequest;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureResponse;
import org.fundaciobit.plugins.validatesignature.api.ValidationStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.gob.afirma.i18n.Language;
import es.gob.afirma.integraFacade.GenerateMessageResponse;
import es.gob.afirma.integraFacade.pojo.DataInfo;
import es.gob.afirma.integraFacade.pojo.Detail;
import es.gob.afirma.integraFacade.pojo.IndividualSignatureReport;
import es.gob.afirma.integraFacade.pojo.ProcessingDetail;
import es.gob.afirma.integraFacade.pojo.VerifySignatureResponse;
import es.gob.afirma.signature.SigningException;
import es.gob.afirma.transformers.TransformersConstants;
import es.gob.afirma.transformers.TransformersFacade;
import es.gob.afirma.utils.Base64Coder;
import es.gob.afirma.utils.DSSConstants;
import es.gob.afirma.utils.GeneralConstants;
import es.gob.afirma.utils.DSSConstants.DSSTagsRequest;
import es.gob.afirma.utils.DSSConstants.ReportDetailLevel;


/**
 * 
 * @author anadal
 * 
 */
public class AfirmaCxfValidateSignaturePlugin extends AbstractValidateSignaturePlugin
    implements IValidateSignaturePlugin {

  
  public static final Map<String, String> localSignProfile2PluginSignProfile = new HashMap<String, String>();

  public static final Map<String, String> localSignType2PluginSignType = new HashMap<String, String>();

  public static final Map<String, String> localAlgorithm2PluginAlgorithm = new HashMap<String, String>();

  public static final Map<String, String> localAlgorithmEnc2PluginAlgorithm = new HashMap<String, String>();
  
  
  public static final SignatureRequestedInformation supportedSignatureRequestedInformation = new SignatureRequestedInformation();
  

  static {
    
    supportedSignatureRequestedInformation.setReturnCertificateInfo(true);
    supportedSignatureRequestedInformation.setReturnCertificates(true);
    supportedSignatureRequestedInformation.setReturnSignatureTypeFormatProfile(true);
    supportedSignatureRequestedInformation.setReturnTimeStampInfo(true);
    supportedSignatureRequestedInformation.setReturnValidationChecks(true);
    supportedSignatureRequestedInformation.setValidateCertificateRevocation(true);
    
    
    
    
    localSignType2PluginSignType.put(DSSConstants.SignTypesURIs.XADES_V_1_3_2,
        SIGNTYPE_XAdES); // = "http://uri.etsi.org/01903/v1.3.2#";
    localSignType2PluginSignType.put(DSSConstants.SignTypesURIs.XADES_V_1_2_2,
        SIGNTYPE_XAdES); // = "http://uri.etsi.org/01903/v1.2.2#";
    localSignType2PluginSignType.put(DSSConstants.SignTypesURIs.XADES_V_1_1_1,
        SIGNTYPE_XAdES); // = "http://uri.etsi.org/01903/v1.1.1#";
    localSignType2PluginSignType.put(DSSConstants.SignTypesURIs.CADES,
        SIGNTYPE_CAdES); // = "http://uri.etsi.org/01733/v1.7.3#";
    localSignType2PluginSignType.put(DSSConstants.SignTypesURIs.CMS,
        SIGNTYPE_CMS); // = "urn:ietf:rfc:3369";
    localSignType2PluginSignType.put(DSSConstants.SignTypesURIs.CMS_TST,
        SIGNTYPE_CMS); // = "urn:afirma:dss:1.0:profile:XSS:forms:CMSWithTST";
    localSignType2PluginSignType.put(DSSConstants.SignTypesURIs.ODF,
        SIGNTYPE_ODF); // = "urn:afirma:dss:1.0:profile:XSS:forms:ODF";
    localSignType2PluginSignType.put(DSSConstants.SignTypesURIs.PDF,
        SIGNTYPE_PDF); // = "urn:afirma:dss:1.0:profile:XSS:forms:PDF";
    localSignType2PluginSignType.put(DSSConstants.SignTypesURIs.PADES,
        SIGNTYPE_PAdES); // = "urn:afirma:dss:1.0:profile:XSS:forms:PAdES";
    localSignType2PluginSignType.put(DSSConstants.SignTypesURIs.XML_DSIG,
        SIGNTYPE_XML_DSIG); // = "urn:ietf:rfc:3275";
    localSignType2PluginSignType.put(DSSConstants.SignTypesURIs.PKCS7,
        SIGNTYPE_PKCS7); // = "urn:ietf:rfc:2315";
    localSignType2PluginSignType.put(DSSConstants.SignTypesURIs.XML_TST,
        SIGNTYPE_XML_TST); // = "urn:oasis:names:tc:dss:1.0:core:schema:XMLTimeStampToken";

    //  Attribute that represents identifier for CAdES Baseline.
    // Definit a DSSConstants.SignTypesURIs.CADES_BASELINE_2_2_1,
    localSignType2PluginSignType.put("http://uri.etsi.org/103173/v2.2.1#",
        SIGNTYPE_CAdES);

    // Attribute that represents identifier for PAdES Baseline.
    // Definit a DSSConstants.SignTypesURIs.PADES_BASELINE_2_1_1,
    localSignType2PluginSignType.put("http://uri.etsi.org/103172/v2.1.1#",
        SIGNTYPE_PAdES);
 
    // Attribute that represents identifier for XAdES Baseline.
    // Definit a DSSConstants.SignTypesURIs.XADES_BASELINE_2_1_1,
    localSignType2PluginSignType.put("http://uri.etsi.org/103171/v2.1.1#",
        SIGNTYPE_XAdES);

    
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.BES,
          SIGNPROFILE_BES); //= "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:BES";
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.EPES,
          SIGNPROFILE_EPES); // = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:EPES";
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.T,
          SIGNPROFILE_T); // = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-T";
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.C,
          SIGNPROFILE_C); // = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-C";
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.X,
          SIGNPROFILE_X); // = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X";
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.X_1,
          SIGNPROFILE_X1); // = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-1";
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.X_2,
          SIGNPROFILE_X2); // = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-2";
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.X_L,
          SIGNPROFILE_XL); // = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-L";
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.X_L_1,
          SIGNPROFILE_XL1); // = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-L-1";
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.X_L_2,
          SIGNPROFILE_XL2); // = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-L-2";
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.A,
          SIGNPROFILE_A); // = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-A";
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.PADES_BASIC,
          SIGNPROFILE_PADES_BASIC); // = "urn:afirma:dss:1.0:profile:XSS:PAdES:1.2.1:forms:Basico";
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.PADES_BES,
          SIGNPROFILE_BES); // = "urn:afirma:dss:1.0:profile:XSS:PAdES:1.1.2:forms:BES";
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.PADES_EPES,
          SIGNPROFILE_EPES); // = "urn:afirma:dss:1.0:profile:XSS:PAdES:1.1.2:forms:EPES";
      localSignProfile2PluginSignProfile.put(DSSConstants.SignatureForm.PADES_LTV,
          SIGNPROFILE_PADES_LTV); // = "urn:afirma:dss:1.0:profile:XSS:PAdES:1.1.2:forms:LTV";


      /**
       * TODO Constant DSSConstants.SignatureForm.B_LEVEL
       *  Attribute that represents B_LEVEL identifier form.
       */
      localSignProfile2PluginSignProfile.put("urn:afirma:dss:1.0:profile:XSS:AdES:forms:B-Level",
          SIGNPROFILE_BES); 

      /**
       * TODO Constant DSSConstants.SignatureForm.T_LEVEL
       *  Attribute that represents T_LEVEL identifier form.
       */
      localSignProfile2PluginSignProfile.put("urn:afirma:dss:1.0:profile:XSS:AdES:forms:T-Level",
          SIGNPROFILE_T); 

      /**
       * TODO Constant DSSConstants.SignatureForm.LT_LEVEL
       *  Attribute that represents LT_LEVEL identifier form..
       */
      localSignProfile2PluginSignProfile.put("urn:afirma:dss:1.0:profile:XSS:AdES:forms:LT-Level",
          SIGNPROFILE_XL); 

      /**
       * TODO Constant DSSConstants.SignatureForm.LTA_LEVEL
       *  Attribute that represents LT_LEVEL identifier form.
       */
      localSignProfile2PluginSignProfile.put("urn:afirma:dss:1.0:profile:XSS:AdES:forms:LTA-Level",
          SIGNPROFILE_A); 
      
    
    localAlgorithm2PluginAlgorithm.put("http://www.w3.org/2000/09/xmldsig#sha1",
        SignatureDetailInfo.SIGN_ALGORITHM_SHA1);
    localAlgorithm2PluginAlgorithm.put("http://www.w3.org/2000/09/xmldsig#sha256",
        SignatureDetailInfo.SIGN_ALGORITHM_SHA256);
    localAlgorithm2PluginAlgorithm.put("http://www.w3.org/2000/09/xmldsig#sha384",
        SignatureDetailInfo.SIGN_ALGORITHM_SHA384);
    localAlgorithm2PluginAlgorithm.put("http://www.w3.org/2000/09/xmldsig#sha512",
        SignatureDetailInfo.SIGN_ALGORITHM_SHA512);

    localAlgorithmEnc2PluginAlgorithm.put("http://www.w3.org/2001/04/xmlenc#sha1",
        SignatureDetailInfo.SIGN_ALGORITHM_SHA1);
    localAlgorithmEnc2PluginAlgorithm.put("http://www.w3.org/2001/04/xmlenc#sha256",
        SignatureDetailInfo.SIGN_ALGORITHM_SHA256);
    localAlgorithmEnc2PluginAlgorithm.put("http://www.w3.org/2001/04/xmlenc#sha384",
        SignatureDetailInfo.SIGN_ALGORITHM_SHA384);
    localAlgorithmEnc2PluginAlgorithm.put("http://www.w3.org/2001/04/xmlenc#sha512",
        SignatureDetailInfo.SIGN_ALGORITHM_SHA512);
  }

  public static final String AFIRMACXF_BASE_PROPERTIES = VALIDATE_SIGNATURE_BASE_PROPERTY
      + "afirmacxf.";

  public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
      "yyyy-MM-dd EEE HH:mm:ss Z", new Locale("es"));

  public static final SimpleDateFormat dateFormatTimeStamp = new SimpleDateFormat(
      "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", new Locale("es"));

  // TRANSFORMES XML

  public static final String APPLICATIONID_PROPERTY = AFIRMACXF_BASE_PROPERTIES
      + "applicationID";

  public static final String TRANSFORMERSTEMPLATESPATH_PROPERTY = AFIRMACXF_BASE_PROPERTIES
      + "TransformersTemplatesPath";

  // CRIDADA CXF
  public static final String ENDPOINT = AFIRMACXF_BASE_PROPERTIES + "endpoint";
  public static final String APPLICATION_ID = AFIRMACXF_BASE_PROPERTIES + "applicationid";

  // UsernameToken
  public static final String AUTH_UP_USERNAME = AFIRMACXF_BASE_PROPERTIES
      + "authorization.username";
  public static final String AUTH_UP_PASSWORD = AFIRMACXF_BASE_PROPERTIES
      + "authorization.password";

  // CERTIFICATE Token
  public static final String AUTH_KS_PATH = AFIRMACXF_BASE_PROPERTIES
      + "authorization.ks.path";
  public static final String AUTH_KS_TYPE = AFIRMACXF_BASE_PROPERTIES
      + "authorization.ks.type";
  public static final String AUTH_KS_PASSWORD = AFIRMACXF_BASE_PROPERTIES
      + "authorization.ks.password";
  public static final String AUTH_KS_ALIAS = AFIRMACXF_BASE_PROPERTIES
      + "authorization.ks.cert.alias";
  public static final String AUTH_KS_CERT_PASSWORD = AFIRMACXF_BASE_PROPERTIES
      + "authorization.ks.cert.password";

  public static final String PRINT_XML = AFIRMACXF_BASE_PROPERTIES + "printxml";
  
  /**
   * 
   */
  public AfirmaCxfValidateSignaturePlugin() {
    super();
  }

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public AfirmaCxfValidateSignaturePlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);
  }

  /**
   * @param propertyKeyBase
   */
  public AfirmaCxfValidateSignaturePlugin(String propertyKeyBase) {
    super(propertyKeyBase);
  }

  private void checkNullProperty(String key, String value) throws Exception {
    if (value == null) {
      throw new Exception("Property " + key + " for " + this.getClass().getName()
          + " must be defined.");
    }
  }

  protected static TimeStampInfo[] parseTimeStamp(String xml) {

    // String str =
    // "<vr:DigestAlgAndValue><ds:DigestMethod Algorithm=\"(.+?)\"></ds:DigestMethod><ds:DigestValue><!\\[CDATA\\[(.+?)\\]\\]></ds:DigestValue></vr:DigestAlgAndValue>";

    String str = "<vr:Properties>.+<vr:CreationTime>(.+?)</vr:CreationTime>" // =
                                                                             // 2017-03-08T11:45:57.000Z
        + ".*</vr:TimeStampContent><vr:MessageHashAlg Type=\"urn:afirma:dss:1.0:profile:XSS:detail:MessageHashAlg\"><dss:Code>(.+?)</dss:Code></vr:MessageHashAlg>" // http://www.w3.org/2001/04/xmlenc#sha512
        // +
        // ".*<vr:CertificateIdentifier><ds:X509IssuerName>(.+?)</ds:X509IssuerName>"
        // // CN=MINISDEF-EC-WPG,OU=PKI,O=MDEF,C=ES
        + ".+<vr:CertificateValidity><vr:CertificateIdentifier><ds:X509IssuerName>(.+?)</ds:X509IssuerName>" // CN=MINISDEF-EC-WPG,OU=PKI,O=MDEF,C=ES
        + ".+<vr:Subject>(.+?)</vr:Subject>.+</vr:Properties>" // CN=Sello de tiempo TS@ - @firma
                                             // -
                                             // desarrollo,SERIALNUMBER=S2833002E,OU=PKI,O=MDEF,C=ES
    ; // +".+"; //<vr:ValidityPeriodOK
      // Type="urn:afirma:dss:1.0:profile:XSS:detail:Certificate"><dss:Code>urn:afirma:dss:1.0:profile:XSS:detail:Certificate:code:ValidPeriod</dss:Code></vr:ValidityPeriodOK><vr:ExtensionsOK
      // Type="urn:afirma:dss:1.0:profile:XSS:detail:Certificate"><dss:Code>urn:afirma:dss:1.0:profile:XSS:detail:Certificate:code:ValidExtension</dss:Code></vr:ExtensionsOK><vr:SignatureOK><vr:SigMathOK
      // Type="urn:afirma:dss:1.0:profile:XSS:detail:Certificate"><dss:Code>urn:afirma:dss:1.0:profile:XSS:detail:Certificate:code:ValidSignature</dss:Code></vr:SigMathOK></vr:SignatureOK><vr:CertificateStatus><vr:CertStatusOK
      // Type="urn:afirma:dss:1.0:profile:XSS:detail:Certificate"><dss:Code>urn:afirma:dss:1.0:profile:XSS:detail:Certificate:code:ValidStatus</dss:Code></vr:CertStatusOK><vr:RevocationEvidence><vr:CRLValidity><vr:CRLIdentifier><xades:Issuer>CN=MINISDEF-EC-WPG,OU=PKI,O=MDEF,C=ES</xades:Issuer><xades:IssueTime>2017-03-14T10:29:40.000Z</xades:IssueTime><xades:Number>45713</xades:Number></vr:CRLIdentifier><vr:SignatureOK><vr:SigMathOK
      // Type="urn:afirma:dss:1.0:profile:XSS:detail:RevocationStatusEvidence"><dss:Code>urn:afirma:dss:1.0:profile:XSS:detail:RevocationStatusEvidence:code:ValidSignature</dss:Code></vr:SigMathOK></vr:SignatureOK></vr:CRLValidity></vr:RevocationEvidence></vr:CertificateStatus></vr:CertificateValidity><vr:TrustOrigin
      // Type="urn:oasis:names:tc:dss:1.0:trustorigin:certDataBase"></vr:TrustOrigin></vr:PathValidityDetail></vr:CertificatePathValidity></vr:SignatureTimeStamp></vr:UnsignedSignatureProperties></vr:UnsignedProperties></vr:Properties>";

    Pattern p = Pattern.compile(str);
    Matcher m = p.matcher(xml);

    ArrayList<TimeStampInfo> list = new ArrayList<TimeStampInfo>();


    while (m.find()) {

      TimeStampInfo tsi = new TimeStampInfo();

      try {
        tsi.setCreationTime(dateFormatTimeStamp.parse(m.group(1)));
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      tsi.setAlgorithm(localAlgorithmEnc2PluginAlgorithm.get(m.group(2)));

      tsi.setCertificateIssuer(m.group(3));
      tsi.setCertificateSubject(m.group(4));

      list.add(tsi);

    }

    if (list.size() == 0) {
      return null;
    }

    return list.toArray(new TimeStampInfo[list.size()]);

  }

  protected static String[][] parseAlgorithDigest(String xml) {

    String str = "<vr:DigestAlgAndValue><ds:DigestMethod Algorithm=\"(.+?)\"></ds:DigestMethod><ds:DigestValue><!\\[CDATA\\[(.+?)\\]\\]></ds:DigestValue></vr:DigestAlgAndValue>";
    Pattern p = Pattern.compile(str);
    Matcher m = p.matcher(xml);

    ArrayList<String> entriesAlg = new ArrayList<String>();
    ArrayList<String> entriesDig = new ArrayList<String>();
    while (m.find()) {
      entriesAlg.add(m.group(1));
      entriesDig.add(m.group(2));
    }

    if (entriesAlg.size() == 0) {
      return null;
    }

    String[] entriesAlgStr = entriesAlg.toArray(new String[entriesAlg.size()]);

    String[] entriesDigStr = entriesDig.toArray(new String[entriesDig.size()]);

    return new String[][] { entriesAlgStr, entriesDigStr };
  }

  protected static String[][] parseXmlByIndexOfAndByGroup(String xml, String separator,
      String pre, String post) {

    String[] parts = xml.split(Pattern.quote(separator));

    ArrayList<String[]> certs = new ArrayList<String[]>();

    for (int i = 0; i < parts.length; i++) {

      String xmlPart = parts[i];
      String[] values = parseXmlByIndexOf(xmlPart, pre, post);

      if (values != null) {
        certs.add(values);
      }

    }

    String[][] entriesStr;
    if (certs.size() == 0) {
      entriesStr = null;
    } else {
      entriesStr = certs.toArray(new String[certs.size()][]);
    }
    return entriesStr;

  }

  protected static String[] parseXmlByIndexOf(String xml, String pre, String post) {

    int fromIndex = 0;

    ArrayList<String> entries = new ArrayList<String>();
    while (true) {

      int preIndex = xml.indexOf(pre, fromIndex);
      int postIndex = xml.indexOf(post, fromIndex);

      if (preIndex == -1 || postIndex == -1) {
        break;
      }

      String value = xml.substring(preIndex + pre.length(), postIndex);

      entries.add(value);

      fromIndex = postIndex + post.length();

    }

    String[] entriesStr;
    if (entries.size() == 0) {
      entriesStr = null;
    } else {
      entriesStr = entries.toArray(new String[entries.size()]);
    }
    return entriesStr;
  }

  protected static String[] parseXmlTag(String xml, String pre, String post) {
    Pattern p = Pattern.compile(pre + "(.+?)" + post);
    Matcher m = p.matcher(xml);

    ArrayList<String> entries = new ArrayList<String>();

    while (m.find()) {
      entries.add(m.group(1));
    }
    String[] entriesStr;
    if (entries.size() == 0) {
      entriesStr = null;
    } else {
      entriesStr = entries.toArray(new String[entries.size()]);
    }
    return entriesStr;
  }

  protected boolean isDebug() {
    return "true".equals(getProperty(AFIRMACXF_BASE_PROPERTIES + "debug"));
  }
  
  
  @Override
  public SignatureRequestedInformation getSupportedSignatureRequestedInformation() {

    return supportedSignatureRequestedInformation;

  }

  @Override
  public SignatureRequestedInformation getSupportedSignatureRequestedInformationBySignatureType(String signType) {
      return supportedSignatureRequestedInformation;
  }

  @Override
  public ValidateSignatureResponse validateSignature(ValidateSignatureRequest validationRequest)
      throws Exception {
    
    SignatureRequestedInformation sri = validationRequest.getSignatureRequestedInformation();
    
    if (sri == null) {
      sri = new SignatureRequestedInformation();
    }
    
    final boolean debug = log.isDebugEnabled() || isDebug();

    final String applicationID;
    applicationID = getPropertyRequired(APPLICATIONID_PROPERTY);

    if(debug) {
      log.info("applicationID = " + applicationID);
    }

    // Instanciamos un mapa con los paràmetros de entrada
    Map<String, Object> inParams = new HashMap<String, Object>();

    // OK
    // VerifySignatureRequest verSigReq = new VerifySignatureRequest();
    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY, applicationID);

    // OK VerificationReport verRep = new VerificationReport();
    // OK verSigReq.setVerificationReport(verRep);
    // OK verSigReq.getVerificationReport().getIncludeCertificateValues()
    // Indicamos que queremos incluir la cadena de certificaci�n en la
    // respuesta
    // == Retorna els certificats de cada firma (requereix REPORT_DETAIL_LEVEL =
    // ALL_DETAILS)
    if (Boolean.TRUE.equals(sri.getReturnCertificates())) {
      inParams.put(DSSTagsRequest.INCLUDE_CERTIFICATE, Boolean.TRUE.toString());
    }

    // OK verSigReq.getVerificationReport().getIncludeRevocationValues(
    // Indicamos que queremos incluir informaci�n del estado de
    // revocaci�n de la cadena de certificaci�n en la respuesta
    // == NO FA RES
    // inParams.put(DSSTagsRequest.INCLUDE_REVOCATION,
    // Boolean.FALSE.toString());

    // OK verSigReq.getVerificationReport().getReportDetailLevel()
    // Indicamos que el nivel de detalle de la respuesta debe ser el
    // máximo
    // == ALL_DETAILS mostra més coses en l'XML però no en el Facades Pojos
    if (Boolean.TRUE.equals(sri.getReturnCertificates())
        || Boolean.TRUE.equals(sri.getReturnTimeStampInfo())) {
      inParams.put(DSSTagsRequest.REPORT_DETAIL_LEVEL, ReportDetailLevel.ALL_DETAILS); // ALL_DETAILS);
    } else {
      inParams.put(DSSTagsRequest.REPORT_DETAIL_LEVEL, ReportDetailLevel.NO_DETAILS); // ALL_DETAILS);
    }

    // OK verSigReq.getVerificationReport().setCheckCertificateStatus(true);
    // Indicamos que queremos validar el estado de revocaci�n del
    // certificado
    // == Vull suposar que ho fa.
    if (Boolean.TRUE.equals(sri.getValidateCertificateRevocation())) {
      inParams.put(DSSTagsRequest.CHECK_CERTIFICATE_STATUS, "true");
    }

    /**
     * OK OptionalParameters optParam = new OptionalParameters();
     * verSigReq.setOptionalParameters(optParam);
     * optParam.setAdditionalReportOption(true);
     */
    // Indicamos que deseamos obtener en la respuesta informaci�n de los
    // sellos de tiempo contenidos
    // == retorna informacio del Segell de temp dins TimeStampInfo de DetailInfo
    if (Boolean.TRUE.equals(sri.getReturnTimeStampInfo())) {
      inParams.put(DSSTagsRequest.ADDICIONAL_REPORT_OPT_SIGNATURE_TIMESTAMP,
          "urn:afirma:dss:1.0:profile:XSS:SignatureProperty:SignatureTimeStamp");
    }

    // OK optParam.setReturnProcessingDetails(true);
    // Indicamos que queremos devolver informaci�n acerca de los
    // procesos de validaci�n llevados a cabo
    // == Retorna CODE/MESS/TYPE que es informacio especifica de les validacions
    // que s'han passat i quines són vàlides i quines invalides
    if (Boolean.TRUE.equals(sri.getReturnValidationChecks())) {
      inParams.put(DSSTagsRequest.RETURN_PROCESSING_DETAILS, "");
    }

    // OK optParam.setReturnReadableCertificateInfo(true);
    // Indicamos que queremos verificar el estado de cada uno de los
    // certificados de la cadena de certificación
    // == Controlat
    if (Boolean.TRUE.equals(sri.getReturnCertificateInfo())) {
      inParams.put(DSSTagsRequest.RETURN_READABLE_CERT_INFO, "");
    }

    // OK optParam.setReturnSignedDataInfo(true);
    // Indicamos que queremos obtener informaci�n sobre los datos firmados
    // == Això només retorna informació de DigestValue per cada firma
    // inParams.put(DSSTagsRequest.RETURN_SIGNED_DATA_INFO, "true");

    // OK verSigReq.setSignature(padesLtvSignature);
    // Indicamos la firma a validar
    // NOTA XYZ ZZZ Si es XML revisar mètode incorporateSignatureImplicit
    final byte[] signData = validationRequest.getSignatureData();
    String xadesFormat = null;
    if (CXFUtils.isXMLFormat(signData)) {
      
      
      //inParams.put("dss:SignatureObject",  new String(signData));
      xadesFormat = getXAdESFormat(signData);
      
      incorporateXMLSignature(inParams, signData, xadesFormat);
      /*
      inParams.put("dss:InputDocuments/dss:Document/dss:Base64XML",
          new String(Base64.encode(signData)));
      */
    } else {
      inParams.put(DSSTagsRequest.SIGNATURE_BASE64, new String(Base64.encode(signData)));
    }

    // Ok verSigReq.setDocument(txtSignedFile);
    // Indicamos los datos firmados, codificados en Base64
    // ES XML => inParams.put(DSSTagsRequest.BASE64XML, new
    // String(Base64.encode(docXML)));
    // NO ES XML => inParams.put(DSSTagsRequest.BASE64DATA, new
    // String(Base64.encode(docNoXML)));
    final byte[] docData = validationRequest.getSignedDocumentData();
    if (docData != null) {
      // GenerateMessageRequest.generateVerifySignRequest(verSigReq);

      String encodedDoc = new String(Base64Coder.encodeBase64(docData));

      if (CXFUtils.isXMLFormat(docData)) {
        inParams.put(DSSTagsRequest.BASE64XML, encodedDoc);
      } else {
        inParams.put(DSSTagsRequest.BASE64DATA, encodedDoc);
      }

    }
    
    TransformersFacade transformersFacade = getTransformersFacade();
    


    if (debug) {
      log.info(" ========== IN PARAMS =========== ");

      for (String b : inParams.keySet()) {
        log.info(b + " => " + inParams.get(b));
      }
      log.info(" ================================= ");
    }

    
    

    // Construimos el XML que constituye la petici�n
    String xmlInput = transformersFacade.generateXml(inParams,
        GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD,
        TransformersConstants.VERSION_10);
    
    if (debug || "true".equals(getProperty(PRINT_XML))) {
      log.info("IN_XML = \n" + xmlInput);
    }
    

    // Invocamos el servicio y obtenemos un XML de respuesta
    String xmlOutput = cridadaWs(xmlInput);


    // Parseamos la respuesta en un mapa
    Map<String, Object> propertiesResult = transformersFacade.parseResponse(xmlOutput,
        GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD,
        TransformersConstants.VERSION_10);


    if (debug || "true".equals(getProperty(PRINT_XML))) {
      log.info("OUT_XML = \n" + xmlOutput);
    }

    if (debug) {
      log.info(" ================================= ");

      for (String b : propertiesResult.keySet()) {
        log.info(b + " => " + propertiesResult.get(b));
      }
      log.info(" ================================= ");
    }

    VerifySignatureResponse verSigRes = new VerifySignatureResponse();

    if (propertiesResult != null) {
      GenerateMessageResponse.generateVerifySignatureResponse(propertiesResult, verSigRes);
    }

    if (debug) {
      internalPrint(verSigRes);
    }

    // ************* FINAL PRINT ********************

    // SignatureFormatEnum sfe = new SignatureFormatEnum(type, form);

    ValidateSignatureResponse signatureInfo = new ValidateSignatureResponse();

    if (verSigRes.getResult() == null) {
      signatureInfo.getValidationStatus().setErrorMsg(
          "No ha retornat informació de l'estat de la firma");
      return signatureInfo;
    }

    String major = verSigRes.getResult().getResultMajor();

    ValidationStatus status = signatureInfo.getValidationStatus();

    if (DSSConstants.ResultProcessIds.VALID_SIGNATURE.equals(major)) {
      status.setStatus(ValidationStatus.SIGNATURE_VALID);
    } else {

      String msg = verSigRes.getResult().getResultMessage();

      if (msg == null) {
        // XYZ ZZZ Traduir
        msg = "Informació de l'error no disponible";
      }
      
      // Error de Comunicacio o de Servidor
      if (DSSConstants.ResultProcessIds.REQUESTER_ERROR.equals(major)
          || DSSConstants.ResultProcessIds.RESPONDER_ERROR.equals(major)) {
        throw new Exception(msg + "(" + major + ")");
      }
      

      if (DSSConstants.ResultProcessIds.INVALID_SIGNATURE.equals(major)) {

        status.setStatus(ValidationStatus.SIGNATURE_INVALID);

        String minor = verSigRes.getResult().getResultMinor();

        if (minor != null) {
          msg = msg + " (" + minor + ")";
        }
      } else {
        status.setStatus(ValidationStatus.SIGNATURE_ERROR);

        String minor = verSigRes.getResult().getResultMinor();

        if (minor == null) {
          msg = msg + " (" + major + ")";
        } else {
          msg = msg + " (" + major + " | " + minor + ")";
        }

      }

      status.setErrorMsg(msg);

    }

    // XYZ ZZZ
//    FileWriter fw = new FileWriter("esborrar.txt");
//    fw.write(xmlOutput);
//    fw.flush();
//    fw.close();

    // ALGORTIHM & DIGEST
    {
      String[][] values = parseAlgorithDigest(xmlOutput);

      if (values != null) {
        initArray(signatureInfo, values[0].length);
        SignatureDetailInfo[] detailInfo = signatureInfo.getSignatureDetailInfo();

        for (int j = 0; j < detailInfo.length; j++) {
          detailInfo[j].setAlgorithm(localAlgorithm2PluginAlgorithm.get(values[0][j]));
          detailInfo[j].setDigestValue(values[1][j]);
        }

      }

    }
    // Certificate
    {
      // TODO Es pega amb la informació de Segell de Temps ???
      final String splitBy = "<vr:IndividualSignatureReport>"; // <vr:CertificatePathValidity>";

      final String preA = "<vr:CertificateValue><![CDATA[";
      final String postA = "]]></vr:CertificateValue>";

      String[][] certificatesB64 = parseXmlByIndexOfAndByGroup(xmlOutput, splitBy, preA, postA);

      if (certificatesB64 != null) {

        initArray(signatureInfo, certificatesB64.length);
        SignatureDetailInfo[] detailInfo = signatureInfo.getSignatureDetailInfo();

        for (int j = 0; j < detailInfo.length; j++) {
          String[] certificatesBySign = certificatesB64[j];

          byte[][] chain = new byte[certificatesBySign.length][];
          for (int i = 0; i < chain.length; i++) {
            chain[i] = Base64.decode(certificatesBySign[i]);
          }

          detailInfo[j].setCertificateChain(chain);
        }

      }

    }

    // Informació TimeStamp
    TimeStampInfo[] allTSI = parseTimeStamp(xmlOutput);
    if (allTSI != null) {
      SignatureDetailInfo[] detailInfo = signatureInfo.getSignatureDetailInfo();

      for (int j = 0; j < allTSI.length; j++) {
        // XYZ Pot ser no tengui informacio de Segell de temps alguna firma i
        // l'array ja no correspon
        detailInfo[j].setTimeStampInfo(allTSI[j]);
      }
    }


    // DSSConstants.ResultProcessIds.INVALID_SIGNATURE
    // DSSConstants.ResultProcessIds.REQUESTER_ERROR
    // DSSConstants.ResultProcessIds.RESPONDER_ERROR

    String type = (String) propertiesResult.get("dss:OptionalOutputs/dss:SignatureType"); // =>
                                                                                          // urn:afirma:dss:1.0:profile:XSS:forms:PAdES
    String profile = (String) propertiesResult.get("dss:OptionalOutputs/ades:SignatureForm"); // =>
                                                                                             // urn:afirma:dss:1.0:profile:XSS:PAdES:1.2.1:forms:Basico
    
    
    String pluginType = null;
    if (type != null) {
      pluginType = localSignType2PluginSignType.get(type); 
      signatureInfo.setSignType(pluginType);
      
      if (pluginType != null) {
        // Cercarem el format: implicit(attached), explicit (detached)
        String signFormat = getSignFormat(pluginType, signData);
        signatureInfo.setSignFormat(signFormat);
      }
    }
    
    if (profile != null) {
      String profilePlugin = localSignProfile2PluginSignProfile.get(profile);
      
      if (pluginType != null && (SIGNTYPE_PAdES.equals(pluginType) || SIGNTYPE_CAdES.equals(pluginType) )) {
        List<IndividualSignatureReport> reports = verSigRes.getVerificationReport();
        if (reports != null && reports.size() !=0) {
          // Get last Signature 
          IndividualSignatureReport report =reports.get(reports.size() - 1);
          if (SIGNPROFILE_BES.equals(profilePlugin) && report.getSignaturePolicyIdentifier() != null) {
            profilePlugin = SIGNPROFILE_EPES;
          }
        }
      }
      signatureInfo.setSignProfile(profilePlugin);
    }
    
    
    
    

    List<IndividualSignatureReport> reports = verSigRes.getVerificationReport();
    initArray(signatureInfo, reports.size());
    int c = 0;
    for (IndividualSignatureReport report : reports) {

      SignatureDetailInfo di = signatureInfo.getSignatureDetailInfo()[c++];

      // Politica EPES
      di.setPolicyIdentifier(report.getSignaturePolicyIdentifier());

      // Detalls de Checks de Validacio
      ProcessingDetail pd = report.getProcessingDetails();
      if (pd != null) {
        di.setValidChecks(convertDetail(pd.getListValidDetail()));
        di.setInvalidChecks(convertDetail(pd.getListInvalidDetail()));
        di.setIndeterminateChecks(convertDetail(pd.getListIndeterminateDetail()));
      }

      // Info de Certificat
      Map<String, Object> certificateInfo = report.getReadableCertificateInfo();
      if (certificateInfo != null && certificateInfo.size() != 0) {

        CertificateInfo ci = new CertificateInfo();
        ci.setApellidosResponsable((String) certificateInfo.get("ApellidosResponsable"));
        ci.setOrganizacionEmisora((String) certificateInfo.get("OrganizacionEmisora"));
        ci.setSegundoApellidoResponsable((String) certificateInfo
            .get("segundoApellidoResponsable"));
        ci.setVersionPolitica((String) certificateInfo.get("versionPolitica"));
        ci.setUsoCertificado((String) certificateInfo.get("usoCertificado"));
        ci.setPais((String) certificateInfo.get("pais"));
        ci.setSubject((String) certificateInfo.get("subject"));
        ci.setNumeroSerie((String) certificateInfo.get("numeroSerie"));
        ci.setPrimerApellidoResponsable((String) certificateInfo
            .get("primerApellidoResponsable"));
        ci.setNombreApellidosResponsable((String) certificateInfo
            .get("NombreApellidosResponsable"));
        String hasta = (String) certificateInfo.get("validoHasta");
        if (hasta != null) {
          ci.setValidoHasta(dateFormat.parse(hasta));
        }
        ci.setIdPolitica((String) certificateInfo.get("idPolitica"));
        ci.setIdEuropeo((String) certificateInfo.get("ID_europeo"));
        String desde = (String) certificateInfo.get("validoDesde");
        if (desde != null) {
          ci.setValidoDesde(dateFormat.parse(desde));
        }
        ci.setTipoCertificado((String) certificateInfo.get("tipoCertificado"));
        ci.setEmail((String) certificateInfo.get("email"));
        ci.setClasificacion((String) certificateInfo.get("clasificacion"));
        ci.setIdEmisor((String) certificateInfo.get("idEmisor"));
        ci.setNifResponsable((String) certificateInfo.get("NIFResponsable"));
        ci.setExtensionUsoCertificado((String) certificateInfo.get("extensionUsoCertificado"));
        ci.setNombreResponsable((String) certificateInfo.get("nombreResponsable"));
        ci.setPolitica((String) certificateInfo.get("politica"));
        // Empleat Public
        ci.setNifEntidadSuscriptora((String) certificateInfo.get("NIFEntidadSuscriptora"));
        ci.setEntidadSuscriptora((String) certificateInfo.get("entidadSuscriptora"));
        ci.setUnidadOrganizativa((String) certificateInfo.get("unidadOrganizativa"));
        ci.setNumeroIdentificacionPersonal((String) certificateInfo
            .get("numeroIdentificacionPersonal"));
        ci.setOrganizacion((String) certificateInfo.get("organizacion"));
        ci.setPuesto((String) certificateInfo.get("puesto"));

        /*
         * InfoCert[NIFEntidadSuscriptora] = S0711001H
         * InfoCert[entidadSuscriptora] = COMUNITAT AUTONOMA DE LES ILLES
         * BALEARS InfoCert[IDlogOn] = null InfoCert[unidadOrganizativa] =
         * FUNDACIÓ BALEAR D'INNOVACIÓ I TECNOLOGIA - ADMINISTRACÍO DIGITAL
         * InfoCert[nombreResponsable] = ANTONI
         * InfoCert[numeroIdentificacionPersonal] = U80067
         * InfoCert[organizacion] = COMUNITAT AUTONOMA DE LES ILLES BALEARS
         * InfoCert[puesto] = CONSULTOR SENIOR InfoCert[extensionUsoCertificado]
         * = KeyPurposeId 0: Any extended key usage
         */

        // TODO XYZ ZZZ Falta resta de propietats
        // ci.setOtherProperties(otherProperties);

        di.setCertificateInfo(ci);

      }
    }

    return signatureInfo;

  }



  protected void internalPrint(VerifySignatureResponse verSigRes) {
    {


      {
        log.info("RESULT MAJOR= " + verSigRes.getResult().getResultMajor());
        log.info("RESULT MINOR= " + verSigRes.getResult().getResultMinor());
        log.info("RESULT MESSAGE= " + verSigRes.getResult().getResultMessage());
      }

      log.info("FORMAT = " + verSigRes.getSignatureFormat());

      List<IndividualSignatureReport> reports = verSigRes.getVerificationReport();
      int r = 0;
      for (IndividualSignatureReport report : reports) {
        log.info(" ---- REPORT[" + r++ + "] ---- ");

        if (report.getDetailedReport() != null) {
          log.info("  report.getDetailedReport(): " + report.getDetailedReport());
        }

        ProcessingDetail pd = report.getProcessingDetails();
        if (pd != null) {

          log.info(printDetail(pd.getListInvalidDetail(), "INVALIT"));
          log.info(printDetail(pd.getListIndeterminateDetail(), "INDETERMINATE"));
          log.info(printDetail(pd.getListValidDetail(), "VALIT"));
        }
        //
        Map<String, Object> certificateInfo = report.getReadableCertificateInfo();
        if (certificateInfo != null && certificateInfo.size() != 0) {

          for (String k : certificateInfo.keySet()) {
            log.info("  InfoCert[" + k + "] = " + certificateInfo.get(k));
          }
        }

        // DSSConstants.ResultProcessIds.INVALID_SIGNATURE
        // DSSConstants.ResultProcessIds.VALID_SIGNATURE
        // DSSConstants.ResultProcessIds.REQUESTER_ERROR
        // DSSConstants.ResultProcessIds.RESPONDER_ERROR
        log.info("  report.getResult().getResultMajor(): "
            + report.getResult().getResultMajor());
        log.info("  report.getResult().getResultMinor(): "
            + report.getResult().getResultMinor());
        log.info("  report.getResult().getResultMessage(): "
            + report.getResult().getResultMessage());

        log.info("  report.getSignaturePolicyIdentifier(): "
            + report.getSignaturePolicyIdentifier());

        if (report.getSigPolicyDocument() != null) {
          log.info("  report.getSigPolicyDocument()" + report.getSigPolicyDocument());
        }

      }

      List<DataInfo> dataList = verSigRes.getSignedDataInfo();
      int n = 0;
      for (DataInfo dataInfo : dataList) {
        log.info(" ---- SIGN[" + n++ + "] ---- ");

        if (dataInfo.getSignedDataRefs() != null) {
          log.info("    dataInfo.getSignedDataRefs() = "
              + Arrays.toString(dataInfo.getSignedDataRefs().toArray()));
        }
        if (dataInfo.getContentData() != null) {
          log.info("    dataInfo.getContentData().length = "
              + dataInfo.getContentData().length);
        }

        log.info("    dataInfo.getDocumentHash().getDigestMethod() = "
            + dataInfo.getDocumentHash().getDigestMethod());

        log.info("    dataInfo.getDocumentHash().getDigestValue().length = "
            + dataInfo.getDocumentHash().getDigestValue().length);

      }

    }
  }

  protected List<SignatureCheck> convertDetail(List<Detail> details) {

    if (details == null || details.size() == 0) {
      return null;
    }

    List<SignatureCheck> checks = new ArrayList<SignatureCheck>();
    for (Detail detail : details) {
      checks.add(new SignatureCheck(detail.getCode(), detail.getType()));
    }

    return checks;
  }

  protected void initArray(ValidateSignatureResponse si, int size) {

    if (si.getSignatureDetailInfo() == null) {
      SignatureDetailInfo[] array = new SignatureDetailInfo[size];
      si.setSignatureDetailInfo(array);
      for (int i = 0; i < array.length; i++) {
        array[i] = new SignatureDetailInfo();
      }
    }

  }

  public String printDetail(List<Detail> details, String title) {

    if (details == null || details.size() == 0) {
      return "";
    }

    StringBuffer str = new StringBuffer(" +++++  DETAIL " + title + " +++++\n");
    int d = 0;
    for (Detail detail : details) {
      str.append(d + ".-CODE=" + detail.getCode() + "\n");
      str.append(d + ".-MESS=" + detail.getMessage() + "\n");
      str.append(d + ".-TYPE=" + detail.getType() + "\n");
    }
    str.append("\n");
    return str.toString();
  }

  // Cache
  protected DSSSignature api = null;

  protected long lastConnection = 0;

  public synchronized String cridadaWs(String inputXml) throws Exception {

    

    String endPoint = getPropertyRequired(ENDPOINT);
    checkNullProperty(ENDPOINT, endPoint);
    
    if (endPoint.toLowerCase().startsWith("https")) {
      if ("true".equalsIgnoreCase(getProperty(AFIRMACXF_BASE_PROPERTIES + "ignoreservercertificates"))) {
        XTrustProvider.install();
      }
    }
    

    final ClientHandler clientHandler;

    String username = getProperty(AUTH_UP_USERNAME);

    if (username != null && username.trim().length() != 0) {
      String password = getProperty(AUTH_UP_PASSWORD);

      clientHandler = new ClientHandlerUsernamePassword(username, password);

    } else {

      String keystoreLocation = getPropertyRequired(AUTH_KS_PATH);

      String keystoreType = getPropertyRequired(AUTH_KS_TYPE);
      String keystorePassword = getPropertyRequired(AUTH_KS_PASSWORD);
      String keystoreCertAlias = getPropertyRequired(AUTH_KS_ALIAS);
      String keystoreCertPassword = getPropertyRequired(AUTH_KS_CERT_PASSWORD);

      clientHandler = new ClientHandlerCertificate(keystoreLocation, keystoreType,
          keystorePassword, keystoreCertAlias, keystoreCertPassword);
    }

    // Cada 10 minuts refem la comunicació
    long now = System.currentTimeMillis();
    if (lastConnection + 10 * 60 * 1000L < now) {
      lastConnection = now;
      api = null;
    }

    if (api == null) {
      DSSSignatureService service = new DSSSignatureService(new java.net.URL(endPoint + "?wsdl"));
      api = service.getDSSAfirmaVerify();
    }
    
    // ---------------
    /*
    Client proxy = ClientProxy.getClient( api );
    HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
    TLSClientParameters tcp = new TLSClientParameters();
    tcp.setTrustManagers( new TrustManager[] { new TrustAllX509TrustManager() } );
    conduit.setTlsClientParameters( tcp );
    */
    // ----------------
    

    Map<String, Object> reqContext = ((BindingProvider) api).getRequestContext();
    reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPoint);

    clientHandler.addSecureHeader(api);


    String xmlResposta = api.verify(inputXml);
   
    return xmlResposta;

  }

  
  public class TrustAllX509TrustManager implements X509TrustManager {

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }

  }
  

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------
  // ------------- METODES COMUNICACIO AFIRMA FEDERAT
  // ---------------------------
  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------

  private static void addSoftwareLibrary(File file) throws Exception {
    Method method = URLClassLoader.class
        .getDeclaredMethod("addURL", new Class[] { URL.class });
    method.setAccessible(true);
    method.invoke(ClassLoader.getSystemClassLoader(), new Object[] { file.toURI().toURL() });
  }

  public static boolean compareStr(String str1, String str2) {
    return (str1 == null ? str2 == null : str1.equals(str2));
  }

  protected TransformersFacade getTransformersFacade() throws Exception {

    String newClassPath = getPropertyRequired(TRANSFORMERSTEMPLATESPATH_PROPERTY);


    addSoftwareLibrary(new File(newClassPath));

    TransformersFacade transformersFacade = TransformersFacade.getInstance();

    Properties transfProp = (Properties) FieldUtils.readField(transformersFacade,
        "transformersProperties", true);

    // S'obte d'una propietat
    transfProp.put("TransformersTemplatesPath",
        getPropertyRequired(TRANSFORMERSTEMPLATESPATH_PROPERTY));

    return transformersFacade;
  }

 
  public void incorporateXMLSignature(Map<String, Object> inputParameters, byte[] signature, String xadesFormat)
      throws Exception {

    if (SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED.equals(xadesFormat)) { // "XAdES Enveloping"
      inputParameters.put("dss:SignatureObject", new String(signature));
    } else if ((SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED.equals(xadesFormat)) // "XAdES Enveloped"
        || (SIGNFORMAT_EXPLICIT_DETACHED.equals(xadesFormat))) { // "XAdES Detached"

      String idSignaturePtr = String.valueOf(Math.random() * 9999.0D);
      inputParameters
          .put("dss:SignatureObject/dss:SignaturePtr@WhichDocument", idSignaturePtr);
      inputParameters.put("dss:InputDocuments/dss:Document@ID", idSignaturePtr);

      inputParameters.put("dss:InputDocuments/dss:Document/dss:Base64XML",
          new String(Base64.encode(signature)));
    }

  }

  
  /**
   * AQUEST MÈTODE ESTA DUPLICAT AL PLUGIN-INTEGR@
   * @param signType
   * @param signData
   * @return
   * @throws Exception
   */
  protected String getSignFormat(String signType, final byte[] signData) throws Exception {
    String signFormat;
    if (SIGNTYPE_CMS.equals(signType)) { // "CMS";
      // TODO Això no se si es correcte !!!!!!!
      try {
        signFormat = getCAdESFormat(signData);
      } catch(Throwable th) {
        log.error("Error intentant obtenir el format d'una firma CMS emprant el mètode getCAdESFormat(): " + th.getMessage(), th);
        signFormat = null;
      }
    } else if (SIGNTYPE_CAdES.equals(signType)) { // "CAdES";
      signFormat = getCAdESFormat(signData);
    } else if (SIGNTYPE_XAdES.equals(signType)) { // "XAdES";
      signFormat = getXAdESFormat(signData);
    } else if (SIGNTYPE_ODF.equals(signType)) { // "ODF";
      signFormat = SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED;
    } else if (SIGNTYPE_PDF.equals(signType)) { // "PDF"; // ?????
      signFormat = SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED;
    } else if (SIGNTYPE_PAdES.equals(signType)) { // "PAdES";
      signFormat = SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED;
    } else if (SIGNTYPE_OOXML.equals(signType)) { // "OOXML";
      signFormat = SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED;
    } else if (SIGNTYPE_XML_DSIG.equals(signType)) { // "XML_DSIG";
      // TODO Això no se si es correcte !!!!!!!
      try {
        signFormat = getXAdESFormat(signData);
      } catch(Throwable th) {
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
   * AQUEST MÈTODE ESTA DUPLICAT AL PLUGIN-INTEGR@ 
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
      //  "XAdES Enveloping"
      return SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED;
    }
    NodeList signatureNodeLs = eSignature.getElementsByTagName("ds:Manifest");
    if (signatureNodeLs.getLength() > 0) {
      //  "XAdES Externally Detached
      return SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED;
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
      //  "XAdES Enveloped"
      return SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED;
    }
    //  "XAdES Detached"
    return SIGNFORMAT_EXPLICIT_DETACHED;
  }
  
  
  /**
   * AQUEST MÈTODE ESTA DUPLICAT AL PLUGIN-INTEGR@  
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
      //  "CAdES attached/implicit signature";
      return SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED;
    } else {
      //  "CAdES detached/explicit signature"
      return SIGNFORMAT_EXPLICIT_DETACHED;
    }
  }


  
  

}
