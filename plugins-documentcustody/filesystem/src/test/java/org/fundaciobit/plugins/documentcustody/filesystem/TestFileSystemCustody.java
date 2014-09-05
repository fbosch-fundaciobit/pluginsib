package org.fundaciobit.plugins.documentcustody.filesystem;

import java.io.File;

import org.fundaciobit.plugins.documentcustody.DocumentCustody;

/**
 * 
 * @author anadal
 *
 */
public class TestFileSystemCustody {

  
  public static void main(String[] args) {
    try {
      
      System.out.println(FileSystemDocumentCustodyPlugin.class.getCanonicalName());
      
      File info = new File("hola_info.txt");
      
      DocumentCustody clone = new DocumentCustody();
      clone.setDocumentType(DocumentCustody.DOCUMENT_ONLY);
      clone.setName("holacaracola.txt");
      clone.setData("holacaracola".getBytes());

      FileSystemDocumentCustodyPlugin.write(clone, info);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    
  }
}
