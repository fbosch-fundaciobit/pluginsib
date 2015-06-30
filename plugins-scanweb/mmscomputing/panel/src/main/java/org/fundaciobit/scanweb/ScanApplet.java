package org.fundaciobit.scanweb;

import java.awt.BorderLayout;
import java.applet.Applet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.JFrame;

/**
 * 
 * @author anadal
 *
 */
public class ScanApplet extends Applet {

  ScanPanelView scanPanelView = new ScanPanelView();

  public ScanApplet() {
    JFrame.setDefaultLookAndFeelDecorated(true);
    init();
    start();
  }

  @Override
  public void init() {
    setLayout(new BorderLayout());

    add(scanPanelView, BorderLayout.CENTER);

  }

  public String upload(String url, String fieldName) {

    String result =  AccessController.doPrivileged(new UploadFilePrivilegedAction(
       this.scanPanelView, checkURL(url), fieldName));
    
    return result;
  }
  
  
  
  public static class UploadFilePrivilegedAction implements PrivilegedAction<String> {

    ScanPanelView scanPanelView;

    String fieldName;

    String url2;

    public UploadFilePrivilegedAction(ScanPanelView scanPanelView, String url, String fieldName) {
      this.scanPanelView = scanPanelView;
      this.fieldName = fieldName;
      this.url2 = url;
    }

    @Override
    public String run() {
      if (scanPanelView.isScannedWithFile()) {
        try {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          scanPanelView.jpegsToPdf(baos);
          
          System.out.println("Calling Upload FIle = ]" + url2 + "[");
          
          try {
            URL url = new URL(url2);
            System.out.println("URL PROTO = " + url.getProtocol());
          } catch (Exception e) {
            e.printStackTrace();
          }
          

          UploadFile uf = new UploadFile(url2);
          uf.addFilePart(fieldName, new ByteArrayInputStream(baos.toByteArray()),
              "scannedFile.pdf");

          uf.finish();

          return null;
        } catch (Exception e) {
          e.printStackTrace();
          return e.getMessage();
        }

      } else {
        return "";
      }
    }

  }
    
  
  
  
  
  /**
   * 
   * @param url
   * @return
   */
  public String checkURL(String url) {

    if(url == null || url.trim().length() == 0 || url.startsWith("http")) {
      return url;
    }
    
    // És una ruta relativa     
    URL cbase = getCodeBase();
    
    String newUrl = cbase.getProtocol() + "://" + cbase.getHost()
        + ( cbase.getPort() == -1? "" : (":" + cbase.getPort())) + url;
    
    System.out.println(" Canviada " + url + " per ]" + newUrl + "[");
    
    return newUrl;
    
  }

}