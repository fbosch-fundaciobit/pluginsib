package org.fundaciobit.scanweb;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import com.lowagie.text.DocumentException;

/**
 * Hello world!
 *
 */
public class ScanApp 
{
  
  /**
   * Create the GUI and show it.  For thread safety,
   * this method should be invoked from the
   * event-dispatching thread.
   */
  private static void createAndShowGUI() {
    
      //Create and set up the window.
      JFrame frame = new JFrame("Assistent de Compulsa");
      frame.setSize(600, 400);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      frame.getContentPane().setLayout(new BorderLayout());
    
      
     // CompulsaWorkflowData data =  new CompulsaWorkflowData(new IbkeyData());
      
      ScanPanelView scanview = new ScanPanelView();
      
      ScanSampleView fullView = new ScanSampleView(scanview);
      
      frame.getContentPane().add(fullView, BorderLayout.CENTER);
      
      //Display the window.
      frame.pack();
      frame.setVisible(true);
      
      try {
        System.out.println("XMAS FITXER PDF = " + scanview.jpegsToPdf());
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (DocumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
  }
  
  
  /*
  public static void main2(String[] args) {
    
    
    try {
      File f = new File("c:\\hola.pdf");
      
      GestorDocumentalConfig config = new GestorDocumentalConfig();
      URL url = new AlfrescoGestorDocumental(config).uploadFile(f);
      
      System.out.println("URL: " + url.toExternalForm());
      
      
      //AlfrescoGestorDocumental.prova();
      //AlfrescoGestorDocumental.uploadFileStatic(f);
      
      
      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    
  }
  
  */
  

  public static void main(String[] args) {
    
    System.out.println( "Hello World!" );
    
    
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
          createAndShowGUI();
      }
     });
    
    
    /*
    
    
    
    //File file = new File("D:\\fitxers\\SCAN.png");
    File file = new File("c:\\hola.pdf");
    
    try {
      Properties prop = new Properties();
      
      //  ---- GESTOR DOCUMENTAL HTTP ---------- 
      
//      String cls = HttpGestorDocumental.class.getName();
//      prop.put(GestorDocumentalConfig.CLASS_KEY, cls);
//      prop.put(GestorDocumentalConfig.PATH_KEY, "http://ibit151/gestordocumental/UploadFile.jsp");
     
      //  ---- GESTOR DOCUMENTAL ALFRESCO ---------- 
//      String cls = AlfrescoGestorDocumental.class.getName();
//      prop.put(GestorDocumentalConfig.CLASS_KEY, cls);
//      prop.put(GestorDocumentalConfig.PATH_KEY, "http://alfresco.ibit.org/alfresco");
//      
//      prop.put(GestorDocumentalConfig.BASE_KEY + AlfrescoGestorDocumental.USR_KEY, "anadal");
//      prop.put(GestorDocumentalConfig.BASE_KEY + AlfrescoGestorDocumental.PWD_KEY , "12RKBE09");
//      prop.put(GestorDocumentalConfig.BASE_KEY + AlfrescoGestorDocumental.DIRECTORY_KEY , "borrar/"); // "USER_HOMES/anadal/prova/";
//      prop.put(GestorDocumentalConfig.BASE_KEY + AlfrescoGestorDocumental.SITEID_KEY , "ODES");
//      prop.put(GestorDocumentalConfig.BASE_KEY + AlfrescoGestorDocumental.CONTAINERID_KEY , "documentLibrary");
      
      
//      GestorDocumentalConfig config = GestorDocumentalConfig.getFromProperties(prop);
//      
//      GestorDocumentalManager.setInstance(GestorDocumentalManager.create(config));
      
      
      
//       URL url = GestorDocumentalManager.getInstance().uploadFile(file);
//       System.out.println("URL XXXX: " + url);
       
      
      

      
    
    System.out.println();

    } catch(Exception e) {
      e.printStackTrace();
    }
    
    
    
    
    
//      String base = "http://registrevm.ibit.org/regweb/pedirdatos.jsp";
//      
//     
//      String params = "localitzadorsDocs=" + URLEncoder.encode("http://vd.caib.es/1305104722095-1851648-2648886061039214335,http://vd.caib.es/1305104722095-1851648-2648886061039214335")
//                      + "&emailRemitent=" + URLEncoder.encode("anadal@ibit.org")
//                      + "&nomRemitent=" +  URLEncoder.encode("Ciutad� de prova")
//                      + "&pextracte=" + URLEncoder.encode("Prova OTAE");
//      
//      
//      String URL = base + "?" + params;
//    
//      System.out.println(URL);
//      
//      
//      try {
//        obreNavegador(URL);
//      } catch (IOException e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//      }
    
    
    
    
    

    
    
      //Schedule a job for the event-dispatching thread:
      //creating and showing this application's GUI.
*/
      
      System.out.println( "Good Bye!" );
      
      
  }
  
  
  
  
  public static  class ScanSampleView extends JPanel implements ActionListener {

    private JLabel jLabelText = null;
    private JScrollPane jScrollPaneCenter = null;
    
    private JPanel jPanelBottom = null;
    private JPanel jPanelBottom1 = null;
    private JProgressBar jProgressBar = null;
    
    private JButton jButtonEndavant = null;
    private JPanel jPanelBottom2 = null;

    private JPanel jPanelCenter = null;
    
    protected final ScanPanelView scanview;
    
    
    public ScanSampleView(ScanPanelView scanview) {
      this.scanview = scanview;
      initialize();
    }
    
    
    public void initialize() {

      jLabelText = new JLabel();
      jLabelText.setText("Pas 1: Selecci\u00F3 del document i descripci\u00F3 de les seves dades.");
      jLabelText.setHorizontalTextPosition(SwingConstants.CENTER);
      jLabelText.setHorizontalAlignment(SwingConstants.CENTER);
      
      this.setLayout(new BorderLayout());
      this.add(jLabelText, BorderLayout.NORTH);

      this.add(getJScrollPaneCenter(), BorderLayout.CENTER);
      
      this.add(getJPanelBottom(), BorderLayout.SOUTH);

    }
    
    
    
    protected JButton getJButtonEndavant() {
      if (jButtonEndavant == null) {
        jButtonEndavant = new JButton();
        jButtonEndavant.setText("Guardar PDF");
        jButtonEndavant.setActionCommand("setData");
        jButtonEndavant.addActionListener(this);
      }
      return jButtonEndavant;
    }
    
    
    protected JPanel getJPanelBottom() {
      if (jPanelBottom == null) {
        jPanelBottom = new JPanel();
        jPanelBottom.setLayout(new BorderLayout());
        jPanelBottom.setPreferredSize(new Dimension(467, 60));
        jPanelBottom.add(getJPanelBottom2(), BorderLayout.CENTER);
        jPanelBottom.add(getJPanelBottom1(), BorderLayout.SOUTH);
      }
      return jPanelBottom;
    }
    
    protected JPanel getJPanelBottom1() {
      if (jPanelBottom1 == null) {
        jPanelBottom1 = new JPanel();
        jPanelBottom1.setLayout(new BorderLayout());
        jPanelBottom1.setVisible(true);
        jPanelBottom1.setPreferredSize(new Dimension(0, 20));
        jPanelBottom1.add(getJProgressBar(), BorderLayout.CENTER);
      }
      return jPanelBottom1;
    }
    
    private JPanel getJPanelBottom2() {
      if (jPanelBottom2 == null) {
        jPanelBottom2 = new JPanel();
        jPanelBottom2.setLayout(new FlowLayout());
        jPanelBottom2.setPreferredSize(new Dimension(252, 40));     
        jPanelBottom2.add(getJButtonEndavant(), null);
      }
      return jPanelBottom2;
    }
    
    
    protected JScrollPane getJScrollPaneCenter() {
      if (jScrollPaneCenter == null) {
        jScrollPaneCenter = new JScrollPane();
        jScrollPaneCenter.setBorder(null);
        jScrollPaneCenter.setViewportView(getJPanelCenter());
      }
      return jScrollPaneCenter;
    }
    
    
    protected JPanel getJPanelCenter() {
      if (jPanelCenter == null) {
        jPanelCenter = new JPanel();
        jPanelCenter.setLayout(new BorderLayout());
        jPanelCenter.add(scanview, BorderLayout.CENTER);
      }
      return jPanelCenter;
    }
    
    
    protected JProgressBar getJProgressBar() {
      if (jProgressBar == null) {
        jProgressBar = new JProgressBar();
        jProgressBar.setPreferredSize(new Dimension(200, 14));
        jProgressBar.setValue(-1);
        jProgressBar.setVisible(false);
        jProgressBar.setIndeterminate(false);
      }
      return jProgressBar;
    }
    
    
    
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
      
        if (scanview.isScannedWithFile()) {
          
          JFileChooser chooser = new JFileChooser();
          chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          int returnVal = chooser.showSaveDialog(this);
          
          if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
              File salida = chooser.getSelectedFile();
              this.scanview.jpegsToPdf(salida);
            }  catch (DocumentException doe) {
              Logger.getLogger(this.getClass()).error(doe.getMessage(),doe);
              JOptionPane.showMessageDialog(null, doe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              
            } catch (MalformedURLException mue) {
              Logger.getLogger(this.getClass()).error(mue.getMessage(),mue);
              JOptionPane.showMessageDialog(null, mue.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              
            } catch (IOException ioe) {
              Logger.getLogger(this.getClass()).error(ioe.getMessage(),ioe);
              JOptionPane.showMessageDialog(null, ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              
            }
            
        } else {
            System.out.println("Open command cancelled by user.");
        }
          
        }
     
    }
    
    
  }
  
  
  
  
  
   
}
