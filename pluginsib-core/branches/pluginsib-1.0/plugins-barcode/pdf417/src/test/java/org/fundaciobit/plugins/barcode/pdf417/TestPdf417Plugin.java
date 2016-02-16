package org.fundaciobit.plugins.barcode.pdf417;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;

import org.fundaciobit.plugins.utils.ImageUtils;

/**
 * 
 * @author anadal
 * 
 */
public class TestPdf417Plugin {

  public static void main(String[] args) {

    final String text = "http://vd.caib.es/1399274075742PINBAL00000000000000001179161128677562589045";

    Pdf417Plugin plugin = new Pdf417Plugin();

    Image img;
    try {
      img = plugin.generateUrlBarCode(new URL(text));

      // jpg, gif, png
      ImageIO.write(ImageUtils.toBufferedImage(img), "png", new File("pdf417_url.png"));

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
