package org.fundaciobit.plugins.exportdata;

/**
 * 
 * @author anadal
 * 
 */
public class ExportItem {

  protected String stringValue;

  protected Object value;

  /**
   * 
   */
  public ExportItem() {
    super();
  }

  /**
   * @param stringValue
   * @param value
   * @param field
   */
  public ExportItem(String stringValue, Object value) {
    super();
    this.stringValue = stringValue;
    this.value = value;
  }

  public String getStringValue() {
    return stringValue;
  }

  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

}
