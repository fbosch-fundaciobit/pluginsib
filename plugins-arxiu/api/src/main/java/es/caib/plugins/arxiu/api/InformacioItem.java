package es.caib.plugins.arxiu.api;

import java.io.Serializable;

/**
 * 
 * @author Limit
 *
 */
public class InformacioItem implements Serializable {

	private String identificador;		// Identificador de l’item
	private String nom;					// Nom de l’item
	private ContingutTipus tipus;		// Tipus de contingut:
										//		- EXPEDIENT
										//		- DOCUMENT
										//		- CARPETA
	private String versio;				// Versió de l’item
	
	public InformacioItem() {
		super();
	}

	public InformacioItem(
			String identificador, 
			String nom, 
			ContingutTipus tipus) {
		super();
		this.identificador = identificador;
		this.nom = nom;
		this.tipus = tipus;
	}
	
	public InformacioItem(
			String identificador, 
			String nom, 
			ContingutTipus tipus, 
			String versio) {
		super();
		this.identificador = identificador;
		this.nom = nom;
		this.tipus = tipus;
		this.versio = versio;
	}
	
	public InformacioItem(InformacioItem ii) {
		super();
		this.identificador = ii.identificador;
		this.nom = ii.nom;
		this.tipus = ii.tipus;
		this.versio = ii.versio;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}

	public ContingutTipus getTipus() {
		return tipus;
	}
	
	public void setTipus(ContingutTipus tipus) {
		this.tipus = tipus;
	}

	public String getVersio() {
		return versio;
	}

	public void setVersio(String versio) {
		this.versio = versio;
	}
	
}
