package org.fundaciobit.plugins.validatesignature.api;

/**
 * 
 * @author anadal
 *
 */
public class Check {

  protected String name;

  protected String type;

  /**
   * 
   */
  public Check() {
    super();
  }

  /**
   * @param name
   * @param type
   */
  public Check(String name, String type) {
    super();
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
