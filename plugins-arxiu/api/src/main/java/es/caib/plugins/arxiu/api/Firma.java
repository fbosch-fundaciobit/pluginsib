package es.caib.plugins.arxiu.api;

/**
 * 
 * @author Limit
 *
 */
public class Firma {

	private byte[] contingut;		// Contingut de la firma
	private String tipusMime;		// Format del contingut de la firma
	private String tipus;			// Tipus de firma
									//		- TF01 - CSV.
									//		- TF02 - XAdES internally detached signature.
									//		- TF03 - XAdES enveloped signature.
									//		- TF04 - CAdES detached/explicit signature.
									//		- TF05 - CAdES attached/implicit signature.
									//		- TF06 - PadES
									//		- TF07 - SMIME
									//		- TF08 - ODT
									//		- TF09 - OOXML
	private String nom;				// Nom del fitxer que conté la firma
	private String csvRegulacio;	// Referència a la llei que regula l’aplicació del CSV
	
	public Firma() {
		super();
	}

	public Firma(
			byte[] contingut, 
			String tipusMime, 
			String tipus) {
		super();
		this.contingut = contingut;
		this.tipusMime = tipusMime;
		this.tipus = tipus;
	}
	
	public Firma(
			byte[] contingut, 
			String tipusMime, 
			String tipus, 
			String nom) {
		super();
		this.contingut = contingut;
		this.tipusMime = tipusMime;
		this.tipus = tipus;
		this.nom = nom;
	}
	
	public Firma(
			byte[] contingut, 
			String tipusMime, 
			String tipus, 
			String nom, 
			String csvRegulacio) {
		super();
		this.contingut = contingut;
		this.tipusMime = tipusMime;
		this.tipus = tipus;
		this.nom = nom;
		this.csvRegulacio = csvRegulacio;
	}
	
	public Firma(Firma f) {
		super();
		this.contingut = f.contingut;
		this.tipusMime = f.tipusMime;
		this.tipus = f.tipus;
		this.nom = f.nom;
		this.csvRegulacio = f.csvRegulacio;
	}

	public byte[] getContingut() {
		return contingut;
	}

	public void setContingut(byte[] contingut) {
		this.contingut = contingut;
	}

	public String getTipusMime() {
		return tipusMime;
	}

	public void setTipusMime(String tipusMime) {
		this.tipusMime = tipusMime;
	}

	public String getTipus() {
		return tipus;
	}

	public void setTipus(String tipus) {
		this.tipus = tipus;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getCsvRegulacio() {
		return csvRegulacio;
	}

	public void setCsvRegulacio(String csvRegulacio) {
		this.csvRegulacio = csvRegulacio;
	}
	
}
