package org.fundaciobit.plugins.barcode.barcode128;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import org.fundaciobit.plugins.barcode.barcode128.BarCode128Plugin;
import org.fundaciobit.plugins.utils.ImageUtils;

/**
 * 
 * @author anadal
 * 
 */
public class TestBarCode128Plugin {

  public static void main(String[] args) {

    final String text = "http://vd.caib.es/1399274075742PINBAL00000000000000001179161128677562589045";

    BarCode128Plugin plugin = new BarCode128Plugin();

    Image img;
    try {
      img = plugin.generateUrlBarCode(new URL(text));

      // jpg, gif, png
      ImageIO.write(ImageUtils.toBufferedImage(img), "png", new File("BARCODE128_url.png"));

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
