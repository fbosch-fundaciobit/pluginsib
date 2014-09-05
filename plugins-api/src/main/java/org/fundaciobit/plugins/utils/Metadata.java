package org.fundaciobit.plugins.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 
 * @author anadal
 * 
 */
public class Metadata {

  protected MetadataType metadataType;

  protected String key;

  protected String value;

  /**
   * @param key
   * @param value
   * @param type
   */
  public Metadata(String key, String value, MetadataType metadataType) {
    super();
    this.key = key;
    this.value = value;
    this.metadataType = metadataType;
  }

  /**
   * 
   */
  public Metadata() {
    super();
  }

  public MetadataType getMetadataType() {
    return metadataType;
  }

  public void setMetadataType(MetadataType metadataType) {
    this.metadataType = metadataType;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public static void checkMetadata(Metadata metadata) throws MetadataFormatException {

    if (metadata == null) {
      throw new MetadataFormatException("Metadata instance is null");
    }
    MetadataType type = metadata.getMetadataType();
    if (type == null) {
      throw new MetadataFormatException("Metadata Type is null");
    }

    if (metadata.getKey() == null) {
      throw new MetadataFormatException("Metadata Key is null");
    }
    String value = metadata.getValue();
    if (value == null) {
      throw new MetadataFormatException("Metadata Value is null");
    }

    switch (type) {

    case INTEGER: // java.lang.BigInteger
      try {
        new BigInteger(value);
      } catch (NumberFormatException nfe) {
        throw new MetadataFormatException("Metadata Value is not integer (" + value + ")", nfe);
      }
      break;
    case DECIMAL: // java.lang.BigDecimal
      try {
        new BigDecimal(value);
      } catch (NumberFormatException nfe) {
        throw new MetadataFormatException("Metadata Value is not decimal (" + value + ")", nfe);
      }
      break;
    case BOOLEAN: // java.lang.Boolean
      metadata.setValue(String.valueOf(Boolean.valueOf(value)));
      break;
    case BASE64:
      try {
        Base64.decode(value);
      } catch (Throwable e) {
        throw new MetadataFormatException("Metadata Value is not in Base64 (" + value + ")", e);
      }
      break;
    case DATE: // ISO8601
      try {
        ISO8601.ISO8601ToDate(value);
      } catch (Throwable e) {
        throw new MetadataFormatException("Metadata Value is not in ISO-8601 date format ("
            + value + ")", e);
      }

      break;

    default:
    case STRING:
      // OK

    }

  }

}
