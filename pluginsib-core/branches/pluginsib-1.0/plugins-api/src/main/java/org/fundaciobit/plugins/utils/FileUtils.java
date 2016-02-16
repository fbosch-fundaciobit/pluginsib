package org.fundaciobit.plugins.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 
 * @author anadal
 *
 */
public class FileUtils {

  /**
   * By default File#delete fails for non-empty directories, it works like "rm". 
   * We need something a little more brutual - this does the equivalent of "rm -r"
   * @param path Root File Path
   * @return true iff the file and all sub files/directories have been removed
   */
  public static boolean deleteRecursive(File path) {
      if (!path.exists()) { return true; };
      boolean ret = true;
      if (path.isDirectory()){
          for (File f : path.listFiles()){
              ret = ret && FileUtils.deleteRecursive(f);
          }
      }
      return ret && path.delete();
  }
  
  public static byte[] readFromFile(File f) throws Exception {
    
    FileInputStream fis = new FileInputStream(f);
    try {
       return toByteArray(fis);
    } finally {
      try {
        fis.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    
  }
  
  
  
  public static InputStream readResource(Class<?> cls, String name) {
    //getClass().getr
    //ClassLoader classLoader = getClass().getClassLoader();
    //File file = new File(getClass().getResource(name).getFile());
    //return file;
    
    
    
    return cls.getClassLoader().getResourceAsStream(name);
  }
  
  
  public static URL getResourceAsURL(Class<?> cls, String name) {
    //getClass().getr
    //ClassLoader classLoader = getClass().getClassLoader();
    //File file = new File(getClass().getResource(name).getFile());
    //return file;
    
    
    
    return cls.getClassLoader().getResource(name);
  }
  
  
  
  public static byte[] toByteArray(InputStream input) throws IOException {

    ByteArrayOutputStream output = new ByteArrayOutputStream();

    copy(input, output);

    return output.toByteArray();

  }
  
  
  public static void copy(InputStream input, OutputStream output)
      throws IOException {
    byte[] buffer = new byte[4096];
    int n = 0;
    while (-1 != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
    }
  }
  
 
  
}
