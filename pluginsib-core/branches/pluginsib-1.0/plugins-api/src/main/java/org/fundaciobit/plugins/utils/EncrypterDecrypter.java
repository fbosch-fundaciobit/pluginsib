package org.fundaciobit.plugins.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * 
 * @author anadal
 * 
 */
public class EncrypterDecrypter {

  public static final String ALGORITHM_AES = "AES";

  public static final String ALGORITHM_DES = "DES";

  public static final String ALGORITHM_RSA = "RSA";

  protected final String algorithm;

  protected final Key key;

  /**
   * @param keyValue
   * @param algorithm
   */
  public EncrypterDecrypter(byte[] keyValue, String algorithm) throws Exception {
    this.algorithm = algorithm;
    this.key = generateKey(keyValue, this.algorithm);
  }


  public static String encrypt(String algorithm, byte[] keyValue, String data) throws Exception {
    EncrypterDecrypter ae = new EncrypterDecrypter(keyValue, algorithm);
    return ae.encrypt(data);
  }

  
  public static String decrypt(String algorithm, byte[] keyValue, String encryptedData) throws Exception {
    EncrypterDecrypter ae = new EncrypterDecrypter(keyValue, algorithm);    
    return ae.decrypt(encryptedData);
  }

  public String getAlgorithm() {
    return algorithm;
  }


  public String encrypt(String data) throws Exception {
    byte[] encVal = encrypt(data.getBytes());
    String encryptedValue = Base64.encodeBytes(encVal, Base64.URL_SAFE);
    return encryptedValue;
  }

  public String decrypt(String encryptedData) throws Exception {
    byte[] decodedValue = Base64.decode(encryptedData, Base64.URL_SAFE);
    byte[] decValue = decrypt(decodedValue);
    String decryptedValue = new String(decValue);
    return decryptedValue;
  }
  
  
  public byte[] encrypt(byte[] data) throws Exception {
    Cipher c = Cipher.getInstance(this.algorithm);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = c.doFinal(data);    
    return encVal;
  }
  
  public byte[] decrypt(byte[] encryptedData)
      throws Exception {
    Cipher c = Cipher.getInstance(this.algorithm);
    c.init(Cipher.DECRYPT_MODE, key);    
    byte[] decValue = c.doFinal(encryptedData);
    return decValue;
  }
  
  

  private static Key generateKey(byte[] keyValue, String algorithm) throws Exception {
    Key key = new SecretKeySpec(keyValue, algorithm);
    return key;
  }

}
