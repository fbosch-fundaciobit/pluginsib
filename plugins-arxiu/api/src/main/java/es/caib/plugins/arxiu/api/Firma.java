package es.caib.plugins.arxiu.api;

import java.util.Arrays;

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
	
	
//	public boolean igual(
//			Firma f) {
//		
//		return  Arrays.equals(f.getContingut(), contingut) &&
//				f.getTipusMime().equals(tipusMime) &&
//				f.getTipus().equals(tipus) &&
//				f.getNom().equals(nom) &&
//				f.getCsvRegulacio().equals(csvRegulacio);
//	}
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(contingut);
		result = prime * result + ((csvRegulacio == null) ? 0 : csvRegulacio.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result + ((tipus == null) ? 0 : tipus.hashCode());
		result = prime * result + ((tipusMime == null) ? 0 : tipusMime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Firma other = (Firma) obj;
		if (!Arrays.equals(contingut, other.contingut))
			return false;
		if (csvRegulacio == null) {
			if (other.csvRegulacio != null)
				return false;
		} else if (!csvRegulacio.equals(other.csvRegulacio))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		if (tipus == null) {
			if (other.tipus != null)
				return false;
		} else if (!tipus.equals(other.tipus))
			return false;
		if (tipusMime == null) {
			if (other.tipusMime != null)
				return false;
		} else if (!tipusMime.equals(other.tipusMime))
			return false;
		return true;
	}
	
	
	
}
