package org.fundaciobit.plugins.exportdata.cvs;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.exportdata.ExportData;
import org.fundaciobit.plugins.exportdata.ExportFile;
import org.fundaciobit.plugins.exportdata.ExportItem;
import org.fundaciobit.plugins.exportdata.IExportDataPlugin;


/**
 * 
 * 
 * @author anadal
 */
public class CSVPlugin implements IExportDataPlugin {

  protected final Logger log = Logger.getLogger(getClass());

  
  @Override
  public String getName() {
    return "CSV";
  }

  @Override
  public ExportFile getIcon() {
    try {
      final String name = "csv.png";
      final byte[] data = IOUtils.toByteArray(getClass().getResourceAsStream("/" + name));
      
      return new ExportFile("image/png", name, data);
    } catch (IOException e) {
      log.error("Error accedint al recurs csv.png", e);
      return null;
    }
  }

  @Override
  public ExportFile getExportFile(ExportData exportData) throws Exception {

    StringBuffer str = new StringBuffer();
    // Titols
    String[] titles = exportData.getTitles();
    for (int i = 0; i < titles.length; i++) {
      if (i != 0) {
        str.append('\t');
      }
      str.append(titles[i]);
    }
    str.append('\n');

    // Contingut
    ExportItem[][] items = exportData.getTableItems();
    for (int i = 0; i < items.length; i++) {
      ExportItem[] cols = items[i];
      for (int j = 0; j < cols.length; j++) {
        if (j != 0) {
          str.append('\t');
        }
        str.append(cols[j].getStringValue());
      }
      str.append('\n');
    }

    ExportFile exportFile;
    exportFile = new ExportFile("text/csv", "export.csv", str.toString().getBytes());

    return exportFile;

  }

}
