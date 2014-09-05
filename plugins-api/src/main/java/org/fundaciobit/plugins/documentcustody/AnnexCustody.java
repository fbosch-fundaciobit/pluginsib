package org.fundaciobit.plugins.documentcustody;

/**
 * 
 * @author anadal
 *
 */
public class AnnexCustody {

  protected String name;

  protected byte[] data;

  /**
   * 
   */
  public AnnexCustody() {
    super();
  }

  /**
   * @param name
   * @param data
   */
  public AnnexCustody(String name, byte[] data) {
    super();
    this.name = name;
    this.data = data;
  }
  
  /**
   * @param name
   * @param data
   */
  public AnnexCustody(AnnexCustody annexCustody) {
    super();
    this.name = annexCustody.name;
    this.data = annexCustody.data;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }
  
}
