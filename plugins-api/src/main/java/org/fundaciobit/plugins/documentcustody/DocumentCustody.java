package org.fundaciobit.plugins.documentcustody;

/**
 * 
 * @author anadal
 * 
 */
public class DocumentCustody extends AnnexCustody {

  public static final String PDF_WITH_SIGNATURE = "pdf";

  public static final String OOXML_WITH_SIGNATURE = "ooxml";

  public static final String ODT_WITH_SIGNATURE = "odt";

  public static final String DOCUMENT_ONLY = "none";
  
  public static final String OTHER_DOCUMENT = "other";

  String documentType;

  public DocumentCustody() {
  }

  /**
   * @param name
   * @param mimeType
   * @param data
   * @param signatureType
   *          Available values are PADES_SIGNATURE, OOXML_SIGNATURE, ODF_SIGNATURE,
   *          
   *          NONE_SIGNATURE if is a plain document without attached signature
   *          sign
   */
  public DocumentCustody(String name, byte[] data, String documentType) {
    super();
    this.name = name;
    this.data = data;
    this.documentType = documentType;
  }

  /**
   * @param name
   * @param mimeType
   * @param data
   * @param attachedSignature
   */
  public DocumentCustody(DocumentCustody dc) {
    super(dc);
    this.documentType = dc.documentType;
  }

  public String getDocumentType() {
    return documentType;
  }

  public void setDocumentType(String documentType) {
    this.documentType = documentType;
  }

}
