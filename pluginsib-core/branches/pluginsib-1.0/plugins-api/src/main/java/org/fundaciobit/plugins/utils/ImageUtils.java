package org.fundaciobit.plugins.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
/**
 * 
 * @author anadal
 *
 */
public class ImageUtils {

  /**
   * Converts a given Image into a BufferedImage
   *
   * @param img The Image to be converted
   * @return The converted BufferedImage
   */
  public static BufferedImage toBufferedImage(Image img)
  {
      if (img instanceof BufferedImage)
      {
          return (BufferedImage) img;
      }

      // Create a buffered image with transparency
      BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

      // Draw the image on to the buffered image
      Graphics2D bGr = bimage.createGraphics();
      bGr.drawImage(img, 0, 0, null);
      bGr.dispose();

      // Return the buffered image
      return bimage;
  }
  
  

  public static BufferedImage getScaledImageHeight(BufferedImage buff, int height) {
    
    int width = (int)( (1.0 * (height + 1) * buff.getWidth()) / (1.0 * buff.getHeight()) );
    
    BufferedImage scaled = getScaledImage(buff, width, height);
    
    return scaled;
  }
  
  
  /**
  * Resizes an image using a Graphics2D object backed by a BufferedImage.
  * @param srcImg - source image to scale
  * @param w - desired width
  * @param h - desired height
  * @return - the new resized image
  */
  public static BufferedImage getScaledImage(BufferedImage src, int w, int h){
      int finalw = w;
      int finalh = h;
      double factor = 1.0d;
      if(src.getWidth() > src.getHeight()){
          factor = ((double)src.getHeight()/(double)src.getWidth());
          finalh = (int)(finalw * factor);                
      }else{
          factor = ((double)src.getWidth()/(double)src.getHeight());
          finalw = (int)(finalh * factor);
      }   

      BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
      Graphics2D g2 = resizedImg.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2.drawImage(src, 0, 0, finalw, finalh, null);
      g2.dispose();
      return resizedImg;
  }
  
  
}
