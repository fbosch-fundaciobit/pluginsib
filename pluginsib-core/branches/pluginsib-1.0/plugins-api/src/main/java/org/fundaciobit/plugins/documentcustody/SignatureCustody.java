package org.fundaciobit.plugins.documentcustody;

/**
 * 
 * @author anadal
 *
 */
public class SignatureCustody extends AnnexCustody {

  public static final String CADES_SIGNATURE = "cades";

  public static final String XADES_SIGNATURE = "xades";

  public static final String SMIME_SIGNATURE = "smime";
  
  public static final String OTHER_SIGNATURE = "other";


  /**
   * true if data contains original document (attached).
   * false if data does not contain original document.
   * null can not obtain this information
   */
  protected Boolean attachedDocument = null;

  protected  String signatureType;

  public SignatureCustody() {
  }

  /**
   * @param name
   * @param mimeType
   * @param data
   * @param signatureType
   *          Available values are PADES_SIGNATURE, CADES_SIGNATURE,
   *          XADES_SIGNATURE, SMIME_SIGNATURE, OOXML_SIGNATURE, ODF_SIGNATURE,
   *          DETACHED_SIGNATURE (unknown type of detached signature),
   *          ATTACHED_SIGNATURE (unknown type with attached signature) or
   *          NONE_SIGNATURE if is a plain document without attached or detached
   *          sign
   * @param attachedDocument
   *          When signature type has ambiguous attached or detached value, then
   *          use this boolean to know it. true if data contains original
   *          document (attached). false if data does not contain original
   *          document. null when this value is not necessary
   */
  public SignatureCustody(String name, byte[] data, String signatureType,
      Boolean attachedDocument) {
    super(name, data);
    this.signatureType = signatureType;
    this.attachedDocument = attachedDocument;
  }
  
  
  public SignatureCustody(String name, String mime, byte[] data, String signatureType,
      Boolean attachedDocument) {
    super(name, mime, data);
    this.signatureType = signatureType;
    this.attachedDocument = attachedDocument;
  }
  

  /**
   * @param name
   * @param mimeType
   * @param data
   * @param attachedSignature
   */
  public SignatureCustody(SignatureCustody sc) {
    super(sc);
    this.attachedDocument = sc.attachedDocument;
    this.signatureType = sc.signatureType;
  }

  public String getSignatureType() {
    return signatureType;
  }

  public void setSignatureType(String signatureType) {
    this.signatureType = signatureType;
  }

  public Boolean getAttachedDocument() {
    return attachedDocument;
  }

  public void setAttachedDocument(Boolean attachedDocument) {
    this.attachedDocument = attachedDocument;
  }

}
