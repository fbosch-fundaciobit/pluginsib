package org.fundaciobit.plugins.barcode.qrcode;


import java.awt.Image;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import org.fundaciobit.plugins.userinformation.UserInfo;
import org.fundaciobit.plugins.utils.ImageUtils;

/**
 * 
 * @author anadal
 * 
 */
public class TestQrCodePlugin {

  public static void main(String[] args) {

    QrCodePlugin plugin = new QrCodePlugin();

    try {

      Image img = plugin.generateUrlBarCode(new URL("http://www.uib.es"));

      // jpg, gif, png
      ImageIO.write(ImageUtils.toBufferedImage(img), "png", new File("url.png"));

      UserInfo ui = new UserInfo();
      ui.setName("Xmas");
      ui.setEmail("anadal@hola.com");
      ui.setCompany("FundacioBIT");
      
      img = plugin.generateUserInfoBarCode(ui);

      // jpg, gif, png
      ImageIO.write(ImageUtils.toBufferedImage(img), "png", new File("userinfo.png"));

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
