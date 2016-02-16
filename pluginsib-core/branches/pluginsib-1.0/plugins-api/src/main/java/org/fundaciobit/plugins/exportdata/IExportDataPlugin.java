package org.fundaciobit.plugins.exportdata;

import org.fundaciobit.plugins.IPlugin;

/**
 * 
 * @author anadal
 * 
 */
public interface IExportDataPlugin extends IPlugin {

  public String getName();

  public ExportFile getIcon();

  public ExportFile getExportFile(ExportData exportData) throws Exception;

}
