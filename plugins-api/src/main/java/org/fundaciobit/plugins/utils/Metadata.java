package org.fundaciobit.plugins.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * 
 * @author anadal
 * 
 */
public class Metadata implements Serializable {

  protected MetadataType metadataType;

  protected String key;

  protected String value;

  
  public Metadata(String key, boolean value) {
    this(key, String.valueOf(value), MetadataType.BOOLEAN);
  }
  
  public Metadata(String key, String value) {
    this(key, value, MetadataType.STRING);
  }
  
  public Metadata(String key, long value) {
    this(key, String.valueOf(value), MetadataType.INTEGER);
  }
  
  public Metadata(String key, BigInteger value) {
    this(key, value == null? null : value.toString(), MetadataType.INTEGER);
  }
  
  
  public Metadata(String key, double value) {
    this(key, String.valueOf(value), MetadataType.DECIMAL);
  }
  
  public Metadata(String key, BigDecimal value) {
    this(key, value == null? null : value.toString(), MetadataType.DECIMAL);
  }
  
  
  public Metadata(String key, byte[] value) {
    this(key, value == null? null : Base64.encode(value) , MetadataType.BASE64);
  }
  
  
  public Metadata(String key, Date value) {
    this(key, value == null? null : ISO8601.dateToISO8601(value) , MetadataType.DATE);
  }
  
  
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
      for (int i = 0; i < value.length(); i++) {
        if (!Character.isDigit(value.charAt(i)) && '-' != value.charAt(i)) {
          throw new MetadataFormatException("Character at position " + i + " is not valid");
        }
      }
      try {
        new BigInteger(value);
      } catch (NumberFormatException nfe) {
        throw new MetadataFormatException("Metadata Value is not integer (" + value + ")", nfe);
      }
      break;
    case DECIMAL: // java.lang.BigDecimal
      for (int i = 0; i < value.length(); i++) {
        if (!Character.isDigit(value.charAt(i)) && '-' != value.charAt(i) && '.' != value.charAt(i)) {
          throw new MetadataFormatException("Character at position " + i + " is not valid");
        }
      }
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
  
  @Override
  public int hashCode() {
    return (this.key + "_" + this.value).hashCode();
  }
  
  
  
  public static void main(String[] args) {
    
    BigInteger bi = new BigInteger("-3245435.56");
    
    System.out.println(" HOLA " + bi.toString());
    
    
    
  }
  

}
