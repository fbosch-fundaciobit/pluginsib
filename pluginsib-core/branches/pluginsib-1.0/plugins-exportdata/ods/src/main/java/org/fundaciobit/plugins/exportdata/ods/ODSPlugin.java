package org.fundaciobit.plugins.exportdata.ods;

import java.io.File;
import java.io.IOException;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.exportdata.ExportData;
import org.fundaciobit.plugins.exportdata.ExportFile;
import org.fundaciobit.plugins.exportdata.ExportItem;
import org.fundaciobit.plugins.exportdata.IExportDataPlugin;
import org.jopendocument.dom.spreadsheet.SpreadSheet;


/**
 * 
 * 
 * @author anadal
 */
public class ODSPlugin implements IExportDataPlugin {

  protected final Logger log = Logger.getLogger(getClass());

  
  @Override
  public String getName() {
    return "ODS";
  }

  @Override
  public ExportFile getIcon() {
    try {
      final String name = "ods.png";
      final byte[] data = IOUtils.toByteArray(getClass().getResourceAsStream("/" + name));
      
      return new ExportFile("image/png", name, data);
    } catch (IOException e) {
      log.error("Error accedint al recurs ods.png", e);
      return null;
    }
  }

  @Override
  public ExportFile getExportFile(ExportData exportData) throws Exception {

    ExportItem[][] items = exportData.getTableItems();
    Object[][] objects = new Object[items.length][];
    for (int i = 0; i < objects.length; i++) {
      ExportItem[] row = items[i];
      objects[i] = new Object[row.length];
      for (int j = 0; j < row.length; j++) {
        objects[i][j] = row[j].getValue();
      }
    }

    

    TableModel model = new DefaultTableModel(objects, exportData.getTitles());  
      
    byte[] data; 
    File file = null;
    try {
      file = File.createTempFile("plugins.exportdata.ods-", ".ods");
      SpreadSheet.createEmpty(model).saveAs(file);
      data = FileUtils.readFileToByteArray(file);
    } finally {
      if (file != null && file.exists()) {
        if (!file.delete()) {
          file.deleteOnExit();
        }
      }
    }

    ExportFile exportFile;
    exportFile = new ExportFile("application/vnd.oasis.opendocument.spreadsheet", "export.ods", data);

    return exportFile;

  }

}
