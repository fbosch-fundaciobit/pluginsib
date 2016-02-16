package org.fundaciobit.plugins.exportdata;

/**
 * 
 * @author anadal
 * 
 */
public class ExportData {

  protected String[] titles;

  protected ExportItem[][] tableItems;

  /**
   * 
   */
  public ExportData() {
    super();
  }

  /**
   * @param titles
   * @param fields
   * @param tableItems
   */
  public ExportData(String[] titles, ExportItem[][] tableItems) {
    super();
    this.titles = titles;
    this.tableItems = tableItems;
  }

  public String[] getTitles() {
    return titles;
  }

  public void setTitles(String[] titles) {
    this.titles = titles;
  }

  public ExportItem[][] getTableItems() {
    return tableItems;
  }

  public void setTableItems(ExportItem[][] tableItems) {
    this.tableItems = tableItems;
  }

}