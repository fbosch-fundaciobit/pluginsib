package org.fundaciobit.plugins.barcode;

import java.awt.Image;
import java.net.URL;

import org.fundaciobit.plugins.IPlugin;
import org.fundaciobit.plugins.userinformation.UserInfo;

/**
 * 
 * @author anadal
 * 
 */
public interface IBarcodePlugin extends IPlugin {

  public static final int TYPE_TEXT = 0;

  public static final int TYPE_URL = 1;

  public static final int TYPE_USER_INFO = 2;

  public static final int TYPE_BINARY = 3;
  
  String getName();

  boolean isSupportedType(int type) throws Exception;

  Image generateTextBarCode(String text) throws Exception;

  Image generateUrlBarCode(URL url) throws Exception;

  Image generateUserInfoBarCode(UserInfo userInfo) throws Exception;

  Image generateBinaryBarCode(byte[] data) throws Exception;
  
  
  /**
   * Ha de retornar un tipus com.itextpdf.text.Image
   * @param text
   * @return
   * @throws Exception
   */
  Object generateTextBarCodeIText(String text) throws Exception;

  /**
   * Ha de retornar un tipus com.itextpdf.text.Image
   * @param url
   * @return
   * @throws Exception
   */
  Object generateUrlBarCodeIText(URL url) throws Exception;

  /**
   * Ha de retornar un tipus com.itextpdf.text.Image
   * @param userInfo
   * @return
   * @throws Exception
   */
  Object generateUserInfoBarCodeIText(UserInfo userInfo) throws Exception;

  /**
   * Ha de retornar un tipus com.itextpdf.text.Image
   * @param data
   * @return
   * @throws Exception
   */
  Object generateBinaryBarCodeIText(byte[] data) throws Exception;
  
  boolean requireRotation();

}
