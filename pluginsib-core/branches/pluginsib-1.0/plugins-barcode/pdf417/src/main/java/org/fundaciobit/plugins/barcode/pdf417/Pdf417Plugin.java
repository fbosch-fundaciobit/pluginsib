package org.fundaciobit.plugins.barcode.pdf417;

import java.awt.Color;
import java.awt.Image;
import java.net.URL;

import org.fundaciobit.plugins.barcode.IBarcodePlugin;
import org.fundaciobit.plugins.userinformation.UserInfo;

import com.itextpdf.text.pdf.BarcodePDF417;


/**
 * 
 * 
 * @author anadal
 */
public class Pdf417Plugin implements IBarcodePlugin {
  
  @Override
  public String getName() {
    return "Pdf417";
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
      BarcodePDF417 pdf417 = new BarcodePDF417();
     
     pdf417.setText(text);
    // pdf417.setYHeight(3.18f); // 0.50f *
    // pdf417.getYHeight());
     
      
      java.awt.Image img = pdf417.createAwtImage(Color.BLACK, Color.WHITE);
     

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
    BarcodePDF417 pdf417 = new BarcodePDF417();
    pdf417.setText(text);
    return pdf417.getImage();
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
