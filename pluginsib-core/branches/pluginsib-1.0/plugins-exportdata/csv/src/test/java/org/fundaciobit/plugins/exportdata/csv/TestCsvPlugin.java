package org.fundaciobit.plugins.exportdata.csv;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import org.fundaciobit.plugins.exportdata.ExportData;
import org.fundaciobit.plugins.exportdata.ExportFile;
import org.fundaciobit.plugins.exportdata.ExportItem;
import org.fundaciobit.plugins.exportdata.cvs.CSVPlugin;


/**
 * 
 * @author anadal
 * 
 */
public class TestCsvPlugin {

  public static void main(String[] args) {

    try {
      

      CSVPlugin ep = new CSVPlugin();
      
      ExportFile ef = ep.getIcon(); 
      byte[] image = ef.getData();
      
      System.out.println(image.length);
      
      String [] titles = new String[]{ "hola" , "cara", "cola"};
      
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      
      ExportItem[][] tableItems = { 
          { new ExportItem("a11", "a1"), new ExportItem("1/2/1934", sdf.parse("01/02/1934")), new ExportItem("Si", true) },
          { new ExportItem("b1", "b1"), new ExportItem("9/8/1976", sdf.parse("09/08/1976")), new ExportItem("No", false) },
      };
      
      ExportData exportData = new ExportData(titles, tableItems);
      
      ExportFile ef2 = ep.getExportFile(exportData);
      
      FileOutputStream fos = new FileOutputStream(ef2.getFilename());
      
      fos.write(ef2.getData());
      fos.flush();
      fos.close();
      

     
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
