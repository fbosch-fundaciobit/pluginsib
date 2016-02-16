package org.fundaciobit.scanweb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

import uk.co.mmscomputing.device.scanner.Scanner;
import uk.co.mmscomputing.device.scanner.ScannerDevice;
import uk.co.mmscomputing.device.scanner.ScannerIOException;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerListener;
import uk.co.mmscomputing.device.twain.TwainConstants;



/**
 * 
 * @author anadal
 *
 */
public class ScanPanelView extends JPanel implements ScannerListener {

	//Throwable error = null;

	private static final long serialVersionUID = 1L;
	
	
	

	private JPanel jPanelDocumentPrincipal = null;

	private JTextField jTextFieldDocumentPrincipalPath = null;
	//private JButton jButtonChooseDocumentPrincipalFile = null;
	private JButton jButtonOpenDocumentPrincipalFile = null;
	//private JButton jButtonVeureFitxerSeleccionat = null;
	private JButton jButtonVeurePlanaEscanejada = null;
	private JButton jButtonEliminarPlanaEscanejada = null;




	//private JFileChooser chooser;

	 private JComboBox jComboBoxPlanesEscanejades = null;


/*
	private JTextField jTextFieldDocumentPrincipalName = null;

	
	private ButtonGroup buttonGroup1 = null;
	private JRadioButton jRadioButtonEscanejar = null;
	private JRadioButton jRadioButtonCarregarArxiu = null;
	
	
	private JTextArea jTextAreaDocumentPrincipalDescripcio = null;
	

	private JLabel jLabelEscanejar = null;
	private JLabel jLabelCarregarArxiu = null;
*/
	/**
	 * Default constructor
	 */
	public ScanPanelView(/*CompulsaWorkflowData data*/) {
		super();
		//this.data = data;
		initialize();
		//getJTextFieldDocumentPrincipalName().requestFocusInWindow();
	}

	/**
	 * Initializes this
	 */
	protected void initialize() {

		this.setSize(560, 260);
		this.setLayout(new BorderLayout());


		
		this.add(getJPanelDocumentPrincipal(),BorderLayout.CENTER);

		scanner = Scanner.getDevice();
		scanner.addListener(this);
		
		
	}





	protected JPanel getJPanelDocumentPrincipal() {
		if (jPanelDocumentPrincipal == null) {
			jPanelDocumentPrincipal = new JPanel(new SpringLayout());
			jPanelDocumentPrincipal.setBorder(BorderFactory.createTitledBorder(null, "Dades del document", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			
			
			jPanelDocumentPrincipal.add(getJPanelLabelOrigenDocument());
			jPanelDocumentPrincipal.add(getJPanelFieldOrigenDocument());
						
			jPanelDocumentPrincipal.add(getJPanelLabelPlanesEscanejades());
			jPanelDocumentPrincipal.add(getJPanelFieldPlanesEscanejades());
			
			/*
			jPanelDocumentPrincipal.add(getJPanelLabelFitxer());
			jPanelDocumentPrincipal.add(getJPanelFieldFitxer());
			
			jPanelDocumentPrincipal.add(getJPanelLabelNom());
			jPanelDocumentPrincipal.add(getJPanelFieldNom());
			
			jPanelDocumentPrincipal.add(getJPanelLabelDescripcio());
			jPanelDocumentPrincipal.add(getJPanelFieldDescripcio());
			*/
			
			SwingUtilities.makeCompactGrid(jPanelDocumentPrincipal,
                    2, 2, //rows, cols
                    6, 6,        //initX, initY
                    6, 6);       //xPad, yPad
		}
		return jPanelDocumentPrincipal;
	}
	
	
	
	
	
	
	
	// ORIGEN DOCUMENT.
	JPanel jPanelOrigenDocument = null;
	protected JPanel getJPanelOrigenDocument() {
		if (jPanelOrigenDocument == null){
			jPanelOrigenDocument = new JPanel();
			jPanelOrigenDocument.setSize(new Dimension(400, 25));
			
			
			
			jPanelOrigenDocument.add(getJPanelLabelOrigenDocument());
			jPanelOrigenDocument.add(getJPanelFieldOrigenDocument());
		}
		return jPanelOrigenDocument;
	}
	
	JPanel jPanelLabelOrigenDocument = null;
	protected JPanel getJPanelLabelOrigenDocument(){
		if (jPanelLabelOrigenDocument == null){
			jPanelLabelOrigenDocument = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			jPanelLabelOrigenDocument.add(new JLabel("Accions"));
		}
		return jPanelLabelOrigenDocument;
	}
	JPanel jPanelFieldOrigenDocument = null;
	protected JPanel getJPanelFieldOrigenDocument() {
		if (jPanelFieldOrigenDocument == null) {
			jPanelFieldOrigenDocument = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			jPanelFieldOrigenDocument.add(getJButtonOpenDocumentPrincipalFile(), null);
			
			JButton selectButton = new JButton("Seleccionar origen");
			
	    selectButton.addActionListener(new ActionListener() {
        
        @Override
        public void actionPerformed(ActionEvent e) {
          try {
            ScanPanelView.this.scanner.select();
          } catch (ScannerIOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        }
      });
	    
	    jPanelFieldOrigenDocument.add(selectButton, null);
			
			
			/*
			jPanelFieldOrigenDocument.add(getJRadioButtonEscanejar(), null);
			jLabelEscanejar = new JLabel();
			jLabelEscanejar.setText("Escanejar");
			jPanelFieldOrigenDocument.add(jLabelEscanejar, null);
			jPanelFieldOrigenDocument.add(getJRadioButtonCarregarArxiu(), null);
			jLabelCarregarArxiu = new JLabel();
			jLabelCarregarArxiu.setText("CarregarArxiu");
			jPanelFieldOrigenDocument.add(jLabelCarregarArxiu, null);
			*/
		}
		return jPanelFieldOrigenDocument;
	}
	
	
	
	
	
	
	
	
	
	// PLANES ESCANEJADES
	JPanel jPanelPlanesEscanejades = null;
	protected JPanel getJPanelPlanesEscanejades(){
		if (jPanelPlanesEscanejades == null){
			jPanelPlanesEscanejades = new JPanel();
			jPanelPlanesEscanejades.setSize(new Dimension(450, 25));
			
			
			jPanelPlanesEscanejades.add(getJPanelLabelPlanesEscanejades());
			jPanelPlanesEscanejades.add(getJPanelFieldPlanesEscanejades());
		}
		return jPanelPlanesEscanejades;
	}
	
	JPanel jPanelLabelPlanesEscanejades = null;
	protected JPanel getJPanelLabelPlanesEscanejades(){
		if (jPanelLabelPlanesEscanejades == null){
			jPanelLabelPlanesEscanejades = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			
			jPanelLabelPlanesEscanejades.add(new JLabel("Planes escanejades"));
		}
		return jPanelLabelPlanesEscanejades;
	}
	
	JPanel jPanelFieldPlanesEscanejades = null;
	protected JPanel getJPanelFieldPlanesEscanejades(){
		if (jPanelFieldPlanesEscanejades == null) {
			jPanelFieldPlanesEscanejades = new JPanel(new FlowLayout(FlowLayout.LEFT));
			jPanelFieldPlanesEscanejades.add(getJComboBoxPlanesEscanejades(), null);
			jPanelFieldPlanesEscanejades.add(getJButtonVeurePlanaEscanejada(), null);
			jPanelFieldPlanesEscanejades.add(getJButtonEliminarPlanaEscanejada(), null);

		}
		return jPanelFieldPlanesEscanejades;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// FITXER DE DISC
	/*
	JPanel jPanelFitxer = null;
	protected JPanel getJPanelFitxer(){
		if (jPanelFitxer == null){
			jPanelFitxer = new JPanel();
			jPanelFitxer.setSize(new Dimension(400, 25));
			jPanelFitxer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			jPanelFitxer.setLayout(new BoxLayout(getJPanelFitxer(), BoxLayout.X_AXIS));
			jPanelFitxer.add(getJPanelLabelFitxer());
			jPanelFitxer.add(getJPanelFieldFitxer());
		}
		return jPanelFitxer;
	}
	
	JPanel jPanelLabelFitxer = null;
	protected JPanel getJPanelLabelFitxer(){
		if (jPanelLabelFitxer == null){
			jPanelLabelFitxer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			jPanelLabelFitxer.setSize(new Dimension(150, 25));
			jPanelLabelFitxer.add(new JLabel("Fitxer"));
		}
		return jPanelLabelFitxer;
	}
	
	JPanel jPanelFieldFitxer = null;
	protected JPanel getJPanelFieldFitxer(){
		if (jPanelFieldFitxer == null) {
			jPanelFieldFitxer = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			jPanelFieldFitxer.add(getJTextFieldDocumentPrincipalPath(), null);
			jPanelFieldFitxer.add(getJButtonChooseDocumentPrincipalFile(), null);
			jPanelFieldFitxer.add(getJButtonViewDocumentPrincipalFile(), null);
		}
		return jPanelFieldFitxer;
	}
		
		*/
		
		
		
		
		
	// NOM DEL DOCUMENT
	/*
	JPanel jPanelNom = null;
	protected JPanel getJPanelNom(){
		if (jPanelNom == null){
			jPanelNom = new JPanel();
			jPanelNom.setSize(new Dimension(400, 25));
			jPanelNom.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			jPanelNom.setLayout(new BoxLayout(getJPanelNom(), BoxLayout.X_AXIS));
			jPanelNom.add(getJPanelLabelNom());
			jPanelNom.add(getJPanelFieldNom());
		}
		return jPanelNom;
	}
	
	JPanel jPanelLabelNom = null;
	protected JPanel getJPanelLabelNom(){
		if (jPanelLabelNom == null){
			jPanelLabelNom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			jPanelLabelNom.setSize(new Dimension(150, 25));
			jPanelLabelNom.setAlignmentX(RIGHT_ALIGNMENT);
			jPanelLabelNom.add(new JLabel("Nom"));
		}
		return jPanelLabelNom;
	}
	
	JPanel jPanelFieldNom = null;
	protected JPanel getJPanelFieldNom(){
		if (jPanelFieldNom == null) {
			jPanelFieldNom = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			jPanelFieldNom.add(getJTextFieldDocumentPrincipalName(), null);
		}
		return jPanelFieldNom;
	}
	
	*/
	
	
	
	
	/*
	// DESCRIPCI� DEL DOCUMENT
	JPanel jPanelDescripcio = null;
	protected JPanel getJPanelDescripcio(){
		if (jPanelDescripcio == null){
			jPanelDescripcio = new JPanel();
			jPanelDescripcio.setSize(new Dimension(400, 25));
			jPanelDescripcio.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			jPanelDescripcio.setLayout(new BoxLayout(getJPanelDescripcio(), BoxLayout.X_AXIS));
			jPanelDescripcio.add(getJPanelLabelDescripcio());
			jPanelDescripcio.add(getJPanelFieldDescripcio());
		}
		return jPanelDescripcio;
	}
	
	JPanel jPanelLabelDescripcio = null;
	protected JPanel getJPanelLabelDescripcio(){
		if (jPanelLabelDescripcio == null){
			jPanelLabelDescripcio = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			jPanelLabelDescripcio.setSize(new Dimension(150, 25));
			jPanelLabelDescripcio.setAlignmentX(RIGHT_ALIGNMENT);
			jPanelLabelDescripcio.add(new JLabel("Descripci\u00F3"));
		}
		return jPanelLabelDescripcio;
	}
	
	JPanel jPanelFieldDescripcio = null;
	protected JPanel getJPanelFieldDescripcio(){
		if (jPanelFieldDescripcio == null) {
			jPanelFieldDescripcio = new JPanel(new FlowLayout(FlowLayout.LEFT));
			jPanelFieldDescripcio.add(getJTextAreaDocumentPrincipalDescripcio(), null);
		}
		return jPanelFieldDescripcio;
	}
	
	*/
		
		
		
		

	protected JTextField getJTextFieldDocumentPrincipalPath() {
		if (jTextFieldDocumentPrincipalPath == null) {
			jTextFieldDocumentPrincipalPath = new JTextField();
			jTextFieldDocumentPrincipalPath.setPreferredSize(new Dimension(282, 25));
		}
		return jTextFieldDocumentPrincipalPath;
	}

	/*
	protected JButton getJButtonChooseDocumentPrincipalFile() {
		if (jButtonChooseDocumentPrincipalFile == null) {
			jButtonChooseDocumentPrincipalFile = new JButton();
			jButtonChooseDocumentPrincipalFile.setPreferredSize(new Dimension(41, 21));
			jButtonChooseDocumentPrincipalFile.setIcon(new ImageIcon(getClass().getResource("/icons/openfile.gif")));
			jButtonChooseDocumentPrincipalFile.setMnemonic(KeyEvent.VK_UNDEFINED);
			jButtonChooseDocumentPrincipalFile.setToolTipText("Obrir");
			jButtonChooseDocumentPrincipalFile.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (!jButtonChooseDocumentPrincipalFile.isEnabled()) return;
					JFileChooser chooser = getChooser();
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						jTextFieldDocumentPrincipalPath.setText(chooser.getSelectedFile().getAbsolutePath());
					}
				}
			});
		}
		return jButtonChooseDocumentPrincipalFile;
	}
	*/
	private JPanel panel = this;
	protected JButton getJButtonOpenDocumentPrincipalFile() {
		if (jButtonOpenDocumentPrincipalFile == null) {
			jButtonOpenDocumentPrincipalFile = new JButton();
			//jButtonOpenDocumentPrincipalFile.setPreferredSize(new Dimension(122, 21));
			jButtonOpenDocumentPrincipalFile.setIcon(new ImageIcon(getClass().getResource("/icons/scanner.png")));
			jButtonOpenDocumentPrincipalFile.setMnemonic(KeyEvent.VK_UNDEFINED);
			jButtonOpenDocumentPrincipalFile.setText("Escannejar");
			jButtonOpenDocumentPrincipalFile.setToolTipText("Escannejar");
			jButtonOpenDocumentPrincipalFile.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (!jButtonOpenDocumentPrincipalFile.isEnabled()) return;
					try {
						scanner.acquire();
						panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						//getJTextFieldDocumentPrincipalName().setEnabled(false);
						//getJTextAreaDocumentPrincipalDescripcio().setEnabled(false);
						getJButtonOpenDocumentPrincipalFile().setEnabled(false);
						getJButtonVeurePlanaEscanejada().setEnabled(false);
						getJButtonEliminarPlanaEscanejada().setEnabled(false);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Error al escannejar el document: "+e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}

				}
			});
		}
		return jButtonOpenDocumentPrincipalFile;
	}

	/*
	protected JButton getJButtonViewDocumentPrincipalFile() {
		if (jButtonVeureFitxerSeleccionat == null) {
			jButtonVeureFitxerSeleccionat = new JButton();
			jButtonVeureFitxerSeleccionat.setPreferredSize(new Dimension(41, 21));
			jButtonVeureFitxerSeleccionat.setIcon(new ImageIcon(getClass().getResource("/icons/lupa.png")));
			jButtonVeureFitxerSeleccionat.setMnemonic(KeyEvent.VK_UNDEFINED);
			jButtonVeureFitxerSeleccionat.setToolTipText("Veure arxiu seleccionat");
			jButtonVeureFitxerSeleccionat.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (!jButtonVeureFitxerSeleccionat.isEnabled()) return;
					if ("".equals(jTextFieldDocumentPrincipalPath.getText())) {
						JOptionPane.showMessageDialog(null, "Heu d'escanejar un document o obrir-lo per poder visualitzar-lo", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						try {
							Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + jTextFieldDocumentPrincipalPath.getText());
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "No es pot obrir el PDF", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
		}
		return jButtonVeureFitxerSeleccionat;
	}
	*/
	
	protected JButton getJButtonVeurePlanaEscanejada() {
		if (jButtonVeurePlanaEscanejada == null) {
			jButtonVeurePlanaEscanejada = new JButton();
			jButtonVeurePlanaEscanejada.setPreferredSize(new Dimension(41, 21));
			jButtonVeurePlanaEscanejada.setIcon(new ImageIcon(getClass().getResource("/icons/lupa.png")));
			jButtonVeurePlanaEscanejada.setMnemonic(KeyEvent.VK_UNDEFINED);
			jButtonVeurePlanaEscanejada.setToolTipText("Veure plana seleccionada");
			jButtonVeurePlanaEscanejada.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (!jButtonVeurePlanaEscanejada.isEnabled()) return;
					if (jComboBoxPlanesEscanejades.getItemCount() == 1 && jComboBoxPlanesEscanejades.getSelectedItem().equals("No hi ha cap document")){
						JOptionPane.showMessageDialog(null, "Heu d'haver escanejar al menys una plana per poder veure-la", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					File f = jpegs.get(jComboBoxPlanesEscanejades.getSelectedIndex());
					try{
							Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + f.getAbsolutePath());
					} catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "No es pot obrir la imatge escanejada", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
				}
			});
		}
		return jButtonVeurePlanaEscanejada;
	}
	
	protected JButton getJButtonEliminarPlanaEscanejada() {
		if (jButtonEliminarPlanaEscanejada == null) {
			jButtonEliminarPlanaEscanejada = new JButton();
			jButtonEliminarPlanaEscanejada.setPreferredSize(new Dimension(41, 21));
			jButtonEliminarPlanaEscanejada.setIcon(new ImageIcon(getClass().getResource("/icons/papelera.png")));
			jButtonEliminarPlanaEscanejada.setMnemonic(KeyEvent.VK_UNDEFINED);
			jButtonEliminarPlanaEscanejada.setToolTipText("Eliminar plana seleccionada");
			jButtonEliminarPlanaEscanejada.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (!jButtonEliminarPlanaEscanejada.isEnabled()) return;
					if (jComboBoxPlanesEscanejades.getItemCount() == 1 && jComboBoxPlanesEscanejades.getSelectedItem().equals("No hi ha cap document")){
						JOptionPane.showMessageDialog(null, "Heu d'haver escanejar al menys una plana per poder eliminar-la", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					if (JOptionPane.showConfirmDialog(null, "Estau segur/a de que voleu esborrar aquesta plana?", "Confirmaci\u00F3", JOptionPane.YES_NO_OPTION) > 0) return;
					
					File f = jpegs.get(jComboBoxPlanesEscanejades.getSelectedIndex());
					try{
						f.delete();
						int selected = jComboBoxPlanesEscanejades.getSelectedIndex();
				        jpegs.remove(selected);

				        if (jpegs.size() == 0){
				        	jComboBoxPlanesEscanejades.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No hi ha cap document" }));
				        }
				        else{
				            if (selected > 0) selected = selected - 1;
				            String[] options = new String[jpegs.size()];

				            for (int i=0; i<jpegs.size(); i++) options[i] = "Plana " + (i+1);
				            jComboBoxPlanesEscanejades.setModel(new javax.swing.DefaultComboBoxModel(options));
				            jComboBoxPlanesEscanejades.setSelectedIndex(selected);
				        }
					} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, "No es pot obrir el PDF", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
				}
			});
		}
		return jButtonEliminarPlanaEscanejada;
	}

	


/*
	protected JFileChooser getChooser() {
		if (chooser == null) {
			FileFilter ff = new FileFilter() {
				public boolean accept(File f) {
					return f.isDirectory() || f.getName().toLowerCase().endsWith("pdf");
				}

				public String getDescription() {
					return "Fitxers PDF";
				}
			};
			chooser = new JFileChooser();
			chooser.setFileFilter(ff);
		}
		return chooser;
	}
*/
	







	/**
	 * Executa la lógica principal de l'enviament
	 */
	/*
	class UploadAndInitWorkflowBusinessThread extends Thread {

		ActionListener actionListener = null;
		IbkeyActionController actionController = null;
		IbkeyData data = null;
		String action = null;

		UploadAndInitWorkflowBusinessThread(String action, IbkeyActionController actionController, IbkeyData data, ActionListener actionListener) {
			this.actionController = actionController;
			this.data = data;
			this.action = action;
			this.actionListener = actionListener;
		}

		public void run() {
			try {
				CompulsaWorkflowData cwd = ((CompulsaWorkflowData) data);
				actionController.execute(action, cwd);
			} catch (Throwable e1) {
				setError(e1);
				Logger.getLogger(IbkeyView.class).error(e1.getMessage(), e1);
			}
		}
	}
	*/

	/**
	 * Crida al controlador
	 */
	/*
	class ControllerExecution extends Thread {

		IbkeyActionController controller = null;
		String command = null;

		ControllerExecution(IbkeyActionController controller, String command) {
			this.controller = controller;
			this.command = command;
		}

		public void run() {
			try {
				controller.execute(command, getData());
			} catch (Throwable e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				Logger.getLogger(IbkeyView.class).error(e1.getMessage(), e1);
			}
		}
	}
	*/

	/**
	 * Crida a la vista
	 */
	/*
	class ViewEndExecution extends Thread {

		ActionListener actionListener = null;

		ViewEndExecution(ActionListener actionListener) {
			this.actionListener = actionListener;
		}

		public void run() {
			actionListener.actionPerformed(new ActionEvent(this, -1, IbkeyActionController.UPLOAD_AND_INIT_TASK_END));
		}
	}
	*/

	

	/*
	protected JRadioButton getJRadioButtonCarregarArxiu() {
		if (jRadioButtonCarregarArxiu == null){
			jRadioButtonCarregarArxiu = new JRadioButton();
			buttonGroup1.add(jRadioButtonCarregarArxiu);
			
			jRadioButtonCarregarArxiu.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					activarDesactivarEscaneoSeleccionFichero();
				}
			});
		}
		return jRadioButtonCarregarArxiu;
	}
	
	
	
	protected JRadioButton getJRadioButtonEscanejar() {
		if (jRadioButtonEscanejar == null){
			jRadioButtonEscanejar = new JRadioButton();
			if (buttonGroup1 == null) buttonGroup1 = new ButtonGroup();
			buttonGroup1.add(jRadioButtonEscanejar);
			
			jRadioButtonEscanejar.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					activarDesactivarEscaneoSeleccionFichero();
				}
			});
		}
		jRadioButtonEscanejar.setSelected(true);
		return jRadioButtonEscanejar;
	}
	*/
	
	protected JComboBox getJComboBoxPlanesEscanejades() {
		if (jComboBoxPlanesEscanejades == null){
			jComboBoxPlanesEscanejades = new JComboBox();
			jComboBoxPlanesEscanejades.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No hi ha cap document" }));
		}
		return jComboBoxPlanesEscanejades;
	}

	/*
	protected JTextField getJTextFieldDocumentPrincipalName() {
		if (jTextFieldDocumentPrincipalName == null) {
			jTextFieldDocumentPrincipalName = new JTextField();
			jTextFieldDocumentPrincipalName.setPreferredSize(new Dimension(380, 25));
		}
		return jTextFieldDocumentPrincipalName;
	}



	protected JTextArea getJTextAreaDocumentPrincipalDescripcio() {
		if (jTextAreaDocumentPrincipalDescripcio == null) {
			jTextAreaDocumentPrincipalDescripcio = new JTextArea();
			jTextAreaDocumentPrincipalDescripcio.setPreferredSize(new Dimension(380, 50));
			jTextAreaDocumentPrincipalDescripcio.setBorder(new LineBorder(Color.gray));
		}
		return jTextAreaDocumentPrincipalDescripcio;
	}

	*/

	Scanner scanner;
	ArrayList<File> jpegs = new ArrayList<File>();

	/**
	 * Método que se dispara ante los cambios de estado del scanner.
	 * Se encarga de guardar los JPGs escaneados a medida que el scanner notifica que est�n listos.
	 */
	private int srcManagerOpenCount = 0;
	
	@Override
	public void update(ScannerIOMetadata.Type type, ScannerIOMetadata metadata) {
		if (type.equals(ScannerIOMetadata.ACQUIRED)) {
			BufferedImage image = metadata.getImage();
			
			File ff = metadata.getFile();
			
			
			System.out.println("FILE SCAN: " + ff);
			
			System.out.println("Have an image now!");
			try {
				File f = File.createTempFile("scanned", ".jpg");
				ImageIO.write(image, "jpg", f);
				System.out.println("Escrita: " + f.getAbsolutePath());
				addJPEG(f);
			} catch (Exception e) {
				Logger.getLogger(this.getClass()).error(e.getMessage(),e);
			}
		} else if (type.equals(ScannerIOMetadata.NEGOTIATE)) {
			ScannerDevice device = metadata.getDevice();
			if (device == null) {
				JOptionPane.showMessageDialog(null, "No s'ha trobat cap escanner", "Error", JOptionPane.ERROR_MESSAGE);
				System.out.println("Null device negotiating scanner");
			} else {
			  System.out.println(" PASSA PER NEGOTIATE");
	      
	      try{
	        device.setResolution(100);
//	        device.setRegionOfInterest(0.0,0.0,40.0,50.0);       // top-left corner 40x50 mm
//	        device.setRegionOfInterest(0,0,400,500);               // top-left corner 400x500 pixels
//	        device.setShowUserInterface(true);
//	        device.setShowProgressBar(true);
	      }catch(Exception e){
	        e.printStackTrace();
	      }
			}
		} else if (type.equals(ScannerIOMetadata.STATECHANGE)) {
			System.err.println(metadata.getStateStr());
			if (metadata.isState(TwainConstants.STATE_SRCENABLED)) {
			  
				panel.setCursor(Cursor.getDefaultCursor());
				getJButtonOpenDocumentPrincipalFile().setEnabled(true);
				/*
				getJTextFieldDocumentPrincipalName().setEnabled(true);
				getJTextAreaDocumentPrincipalDescripcio().setEnabled(true);
				*/
				getJButtonVeurePlanaEscanejada().setEnabled(true);
				getJButtonEliminarPlanaEscanejada().setEnabled(true);
			}
			if (metadata.isState(TwainConstants.STATE_SRCMNGOPEN)){ // Este se dispara al abrir y al cerrar.
				srcManagerOpenCount++;
				if (srcManagerOpenCount % 2 == 0){ // Cada dos veces que se dispara es porque se cierra la ventana. 
					//getJButtonEndavant().setEnabled(true);
				}
			}
			
			
		} else if (type.equals(ScannerIOMetadata.EXCEPTION)) {
			Logger.getLogger(this.getClass()).error( metadata.getException().getMessage(), metadata.getException());
			JOptionPane.showMessageDialog(null, metadata.getException().getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			
			panel.setCursor(Cursor.getDefaultCursor());
			getJButtonOpenDocumentPrincipalFile().setEnabled(true);
			/*
			getJTextFieldDocumentPrincipalName().setEnabled(true);
			getJTextAreaDocumentPrincipalDescripcio().setEnabled(true);
			
			*/
			getJButtonVeurePlanaEscanejada().setEnabled(true);
			getJButtonEliminarPlanaEscanejada().setEnabled(true);
		}
	}

	private void addJPEG(File f) {
		jpegs.add(f);
		f.deleteOnExit();

        if (jComboBoxPlanesEscanejades.getItemCount() == 1 && jComboBoxPlanesEscanejades.getItemAt(0).equals("No hi ha cap document")) jComboBoxPlanesEscanejades.removeItemAt(0);

        jComboBoxPlanesEscanejades.addItem("Plana " + jpegs.size());
        jComboBoxPlanesEscanejades.setSelectedIndex(jComboBoxPlanesEscanejades.getItemCount()-1);
	}
	
	
	public boolean isScannedWithFile() {
	   return jpegs.size() != 0;
	}
	
	
	
	public File jpegsToPdf() throws IOException, DocumentException {
	  if (jpegs.size() == 0) return null;
	  
	  File pdf = File.createTempFile("Documento", ".pdf");
    pdf.deleteOnExit(); // Son ficheros temporales que se borrar�n una vez acabado el proceso.
    
    jpegsToPdf(pdf);
    
    System.out.println("Cerrando PDF " + pdf.getAbsolutePath());
    
    return pdf;
    
	}
	
	
	public void jpegsToPdf(File f) throws IOException, DocumentException {
	  OutputStream salida = new FileOutputStream(f);
    jpegsToPdf(salida);
	}
	

	/**
	 * Toma el conjunto de im�genes JPG generadas y compone un PDF con ellas.
	 * 
	 * @return Ruta absoluta al fichero PDF
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public boolean jpegsToPdf(OutputStream salida) throws DocumentException, MalformedURLException, IOException {
		
		if (jpegs.size() == 0) return false;
		

			// Inicializamos y creamos el PDF
			com.lowagie.text.Rectangle a4 = PageSize.A4;
			float horizontalCorrectionMargin = 75; // Para que los A4 escaneados
			// no se salgan de la hoja
			// por la derecha
			float horizontalMargins = a4.getBorderWidthLeft() + a4.getBorderWidthRight() + horizontalCorrectionMargin;
			float verticalMargins = a4.getBorderWidthTop() + a4.getBorderWidthBottom();
			float horizontalImageSize = a4.getWidth() - horizontalMargins;
			float verticalImageSize = a4.getHeight() - verticalMargins;
			
			Document document = new Document(a4);
			
			PdfWriter.getInstance(document, salida);
			
			// Solicitud Pere Joseph: quitar el margen de arriba y darle m�s espacio abajo.
			float topMargin = document.topMargin();
			float bottomMargin = document.bottomMargin();

			// Atenci�n porque algunas impresoras no pueden imprimir sin margen; ser�a m�s recomendable hacer:
			// bottomMargin += 3*topMargin/4; (3 cuartos del margen superior m�s)
			// topMargin = topMargin/4; (el margen superior ocupa una cuarta parte)
			bottomMargin += topMargin;
			topMargin = 0f;
			
			// Aplicaci�n de los nuevos m�rgenes.
			document.setMargins(document.leftMargin(), document.rightMargin(), topMargin, bottomMargin);

			// Adici�n de las im�genes.
			document.open();
			for (File jpegFile : jpegs) {
				String ficheroJPG = jpegFile.getPath();
				System.out.println("Añadiendo JPG " + ficheroJPG + " al PDF.");
				
				/*
				FileInputStream fis = new FileInputStream(ficheroJPG);
        //System.out.println(file.exists() + "!!");
        //InputStream in = resource.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        byte[] bytes = bos.toByteArray();
        */
				
				
				com.lowagie.text.Image jpg = null;
				
				jpg = AccessController.doPrivileged(new ReadFilePrivilegedAction(ficheroJPG,
				    horizontalImageSize,
		         verticalImageSize, document));
				    
				if (jpg != null) {

  				
				}
			}

			// finalizaci�n del PDF
			
			document.close();
			
			return true;

			// Los JPG estan marcados para "deleteOnExit" y no hace falta borrarlos ahora.

	}
	

	public static class ReadFilePrivilegedAction implements PrivilegedAction<com.lowagie.text.Image> {
	  
	  final String ficheroJPG;
	  
	  final float horizontalImageSize;
    final float verticalImageSize;
    
    final Document document;
	  
	  
	  

    /**
     * @param ficheroJPG
     * @param horizontalImageSize
     * @param verticalImageSize
     * @param document
     */
    public ReadFilePrivilegedAction(String ficheroJPG, float horizontalImageSize,
        float verticalImageSize, Document document) {
      super();
      this.ficheroJPG = ficheroJPG;
      this.horizontalImageSize = horizontalImageSize;
      this.verticalImageSize = verticalImageSize;
      this.document = document;
    }




    public com.lowagie.text.Image run()  {
        try {
          com.lowagie.text.Image jpg = com.lowagie.text.Image.getInstance(ficheroJPG);
          
          
          //com.lowagie.text.Image jpg = com.lowagie.text.Image.getInstance(bytes);
          if (jpg.getWidth() > horizontalImageSize || jpg.getHeight() > verticalImageSize) {
            jpg.scaleToFit(horizontalImageSize, verticalImageSize);
          }
          document.add(jpg);
          
          
          
        }  catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        
        return null;
        
    }
  
  }
	
	
	
	public static void obreNavegador(String url) throws IOException {

    // Obrim el visor d'URLS per defecte (si no és un navegador,
    // no s'obrirà al navegador)

    if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) // si
        // es
        // windows
        Runtime.getRuntime().exec(
                new String[] { "cmd", "/C", "start", url });
    else if (System.getProperty("os.name").toLowerCase().indexOf("linux") != -1) // si
        // es
        // linux
        Runtime.getRuntime().exec(
                new String[] { "/usr/bin/firefox", url });
    else if (System.getProperty("os.name").toLowerCase().indexOf("mac os x") != -1) // si
        // es
        // mac
        Runtime.getRuntime().exec(new String[] { "open ", url });

  }
	
	

	/**
	 * Valida les dades
	 */
	/*
	protected boolean validateSubmitData() {
		if (jRadioButtonEscanejar.isSelected() && jpegs.size() == 0){
			JOptionPane.showMessageDialog(null, "\u00C9s necessari haver escanejat un document", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if (jRadioButtonCarregarArxiu.isSelected() && (data == null || data.getFilePath() == null || data.getFilePath().equals(""))){
			JOptionPane.showMessageDialog(null, "\u00C9s necessari seleccionar un document", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if (jRadioButtonCarregarArxiu.isSelected() && !data.getFilePath().toLowerCase().endsWith(".pdf")){
			JOptionPane.showMessageDialog(null, "\u00C9s necessari que el document seleccionat sigui un document en format PDF", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if (jRadioButtonCarregarArxiu.isSelected()){
			File f = new File(data.getFilePath());
			if (!f.exists()){
				JOptionPane.showMessageDialog(null, "No es troba el document especificat a la ruta indicada. Per favor, introdueixi una ruta vàlida a un PDF", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		
		if ( (data.getName() == null || "".equals(data.getName()))) {
			JOptionPane.showMessageDialog(null, "\u00C9s necessari introduir un nom pel document principal per a poder continuar el procediment", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( (data.getDescription() == null || "".equals(data.getDescription()))) {
			JOptionPane.showMessageDialog(null, "\u00C9s necessari introduir una descripci\u00F3 pel document principal per a poder continuar el procediment", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	*/

	/**
	 * Copia les dades del formulari al data
	 */
	/*
	public IbkeyData gatherData() {
		if (jRadioButtonEscanejar.isSelected()) {
		  System.out.println("DATA: " + data);
		  data.setFilePath(jpegsToPdf());
		} else {
		  data.setFilePath(jTextFieldDocumentPrincipalPath.getText());
		}
		data.setName(getJTextFieldDocumentPrincipalName().getText());
		data.setDescription(getJTextAreaDocumentPrincipalDescripcio().getText());

		return data;
	}
	

	public void execute(String action) throws IbkeyControllerException {
		// se n'encarrega el mètode actionPerformed
	}

	
	public IbkeyActionController getController() {
		return controller;
	}

	public IbkeyData getData() {
		return data;
	}

	public IbkeyView getParentView() {
		return null;
	}
	*/

	/**
	 * Copia les dades del data al formulari i les valida
	 */
	/*
	public void scatterData() {
		getJTextFieldDocumentPrincipalPath().setText(data.getFilePath());
		getJTextFieldDocumentPrincipalName().setText(data.getName());
		getJTextAreaDocumentPrincipalDescripcio().setText(data.getDescription());
		
		if (data == null || data.getFilePath() == null || "".equals(data.getFilePath())){
			jRadioButtonCarregarArxiu.setSelected(false);
			jRadioButtonEscanejar.setSelected(true);
		}
		else{
			jRadioButtonCarregarArxiu.setSelected(true);
			jRadioButtonEscanejar.setSelected(false);
		}
		
		this.validate();
		activarDesactivarEscaneoSeleccionFichero();
	}
	*/

	/*
	public void setController(IbkeyActionController controller) {
		this.controller = controller;
	}

	public void setData(IbkeyData data) {
		this.data = (CompulsaWorkflowData) data;
		scatterData();
	}

	public void setParentView(IbkeyView parent) {
		// res a fer
	}
	*/
	/*
	private void activarDesactivarEscaneoSeleccionFichero() {
		if (jRadioButtonEscanejar.isSelected()){
			jTextFieldDocumentPrincipalPath.setEnabled(false);
			jButtonChooseDocumentPrincipalFile.setEnabled(false);
			jButtonVeureFitxerSeleccionat.setEnabled(false);
			jComboBoxPlanesEscanejades.setEnabled(true);
			jButtonOpenDocumentPrincipalFile.setEnabled(true);
			jButtonVeurePlanaEscanejada.setEnabled(true);
			jButtonEliminarPlanaEscanejada.setEnabled(true);
		}
		else{
			jTextFieldDocumentPrincipalPath.setEnabled(true);
			jButtonChooseDocumentPrincipalFile.setEnabled(true);
			jButtonVeureFitxerSeleccionat.setEnabled(true);
			jComboBoxPlanesEscanejades.setEnabled(false);
			jButtonOpenDocumentPrincipalFile.setEnabled(false);
			jButtonVeurePlanaEscanejada.setEnabled(false);
			jButtonEliminarPlanaEscanejada.setEnabled(false);
		}
	}
	*/

}
