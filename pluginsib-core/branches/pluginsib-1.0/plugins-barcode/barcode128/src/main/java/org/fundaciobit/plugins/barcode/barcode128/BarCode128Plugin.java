package org.fundaciobit.plugins.barcode.barcode128;

import java.awt.Color;
import java.awt.Image;
import java.net.URL;

import org.fundaciobit.plugins.barcode.IBarcodePlugin;
import org.fundaciobit.plugins.userinformation.UserInfo;

import com.itextpdf.text.pdf.Barcode128;


/**
 * 
 * 
 * @author anadal
 */
public class BarCode128Plugin implements IBarcodePlugin {
  
  @Override
  public String getName() {
    return "BarCode128";
  }

  @Override
  public boolean isSupportedType(int type) throws Exception {
    switch (type) {
      case TYPE_TEXT:
      case TYPE_URL:
        return true;
  
      default:
        return false;
    }
  }

  @Override
  public Image generateTextBarCode(String text) throws Exception {

    if (text == null) {
      return null;
    }

/*

    BarcodePDF417 pdf417 = new BarcodePDF417();

    final int dpi = 100;

    // Configure the barcode generator

    // UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar
    //bean.setHeight(3.18);

    // BarcodeDimension dim = bean.calcDimensions(text);

    // Set up the canvas provider for monochrome PNG output

    BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi,
        BufferedImage.TYPE_BYTE_BINARY, false, 0);

    // Generate the barcode

    bean.generateBarcode(canvas, text);

    BufferedImage buff = canvas.getBufferedImage();

    if (height2 != null) {
      BufferedImage scaled = ImageUtils.getScaledImageHeight(buff, height2);
      return scaled;
    } else {
      return buff;
    }
*/
     // ITEXT
      Barcode128 code128 = new Barcode128();
      
      code128.setCode(text);

     
      
      java.awt.Image img = code128.createAwtImage(Color.BLACK, Color.WHITE);
     

    return img;

  }

  @Override
  public Image generateUrlBarCode(URL url) throws Exception {
    if (url == null) {
      return null;
    }
    return generateTextBarCode(url.toExternalForm());
  }



  @Override
  public Image generateUserInfoBarCode(UserInfo userInfo) throws Exception {
    return null;
  }

  @Override
  public Image generateBinaryBarCode(byte[] data) throws Exception {
    return null;
  }

  @Override
  public com.itextpdf.text.Image generateTextBarCodeIText(String text) throws Exception {
    Barcode128 code128 = new Barcode128();
    
    code128.setCode(text);
    
    java.awt.Image img = code128.createAwtImage(Color.BLACK, Color.WHITE);
    return com.itextpdf.text.Image.getInstance(img, null);
  }

  @Override
  public com.itextpdf.text.Image generateUrlBarCodeIText(URL url) throws Exception {
    if (url == null) {
      return null;
    }
    return generateTextBarCodeIText(url.toExternalForm());
  }

  @Override
  public com.itextpdf.text.Image generateUserInfoBarCodeIText(UserInfo userInfo)
      throws Exception {
    return null;
  }

  @Override
  public com.itextpdf.text.Image generateBinaryBarCodeIText(byte[] data) throws Exception {
    return null;
  }

  @Override
  public boolean requireRotation() {
    return true;
  }


}
