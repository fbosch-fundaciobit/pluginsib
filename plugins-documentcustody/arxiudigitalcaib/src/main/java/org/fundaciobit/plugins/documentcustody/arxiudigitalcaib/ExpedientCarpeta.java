package org.fundaciobit.plugins.documentcustody.arxiudigitalcaib;

/**
 * 
 * @author anadal
 *
 */
public class ExpedientCarpeta {

  public final String expedientID;

  public final String carpetaID;

  /**
   * @param expedientID
   * @param carpetaID
   */
  public ExpedientCarpeta(String expedientID, String carpetaID) {
    super();
    this.expedientID = expedientID;
    this.carpetaID = carpetaID;
  }
  
  
  @Override
  public String toString() {
    
    StringBuffer sb = new StringBuffer("E: " + expedientID);
    
    if (carpetaID != null) {
      sb.append(" | C: " + carpetaID);
    }
    
    return sb.toString();
  }
  

}
