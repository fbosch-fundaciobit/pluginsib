package org.fundaciobit.plugins.exportdata.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.fundaciobit.plugins.exportdata.ExportData;
import org.fundaciobit.plugins.exportdata.ExportFile;
import org.fundaciobit.plugins.exportdata.ExportItem;
import org.fundaciobit.plugins.exportdata.IExportDataPlugin;

/**
 * 
 * 
 * @author anadal
 */
public class ExcelPlugin implements IExportDataPlugin {

  protected final Logger log = Logger.getLogger(getClass());

  @Override
  public String getName() {
    return "Excel";
  }

  @Override
  public ExportFile getIcon() {
    try {
      final String name = "excel.png";
      final byte[] data = IOUtils.toByteArray(getClass().getResourceAsStream("/" + name));
      return new ExportFile("image/png", name, data);
    } catch (IOException e) {
      log.error("Error accedint al recurs excel.png", e);
      return null;
    }
  }

  @Override
  public ExportFile getExportFile(ExportData exportData) throws Exception {

    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("Export Excel");

    int rownum = 0;

    // Titols
    {
      String[] titles = exportData.getTitles();
      Row row = sheet.createRow(rownum++);
      int cellnum = 0;

      for (int i = 0; i < titles.length; i++) {
        Cell cell = row.createCell(cellnum++);
        cell.setCellValue(titles[i]);
      }
    }

    // Contingut
    ExportItem[][] items = exportData.getTableItems();
    for (int i = 0; i < items.length; i++) {
      Row row = sheet.createRow(rownum++);
      ExportItem[] cols = items[i];
      int cellnum = 0;
      for (int j = 0; j < cols.length; j++) {

        Object obj = cols[j].getValue();
        if (obj == null) {
          cellnum++;
        } else {
          Cell cell = row.createCell(cellnum++);
          if (obj instanceof Boolean) {
            cell.setCellValue((Boolean) obj);
          } else if (obj instanceof Double) {
            cell.setCellValue(((Double) obj).doubleValue());
          } else {
            cell.setCellValue(cols[j].getStringValue());
          }
        }
      }
    }

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    workbook.write(out);
    out.close();

    ExportFile exportFile;
    exportFile = new ExportFile("application/vnd.ms-excel", "export.xls", out.toByteArray());

    return exportFile;

  }

}
