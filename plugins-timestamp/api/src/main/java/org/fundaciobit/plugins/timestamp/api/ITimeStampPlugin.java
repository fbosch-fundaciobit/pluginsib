package org.fundaciobit.plugins.timestamp.api;

import org.fundaciobit.plugins.IPlugin;
import org.bouncycastle.tsp.TimeStampToken;

/**
 * Interficie per Segellat de Temps 
 *
 * @author anadal
 *
 */
public interface ITimeStampPlugin extends IPlugin {

  public static final String TIMESTAMP_BASE_PROPERTY = IPLUGIN_BASE_PROPERTIES  + "timestamp.";
  
  public TimeStampToken getTimeStamp(byte[] fichero) throws Exception;
  
  public byte[] getTimeStampDirect(byte[] fichero) throws Exception;
  
  
} 