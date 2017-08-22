package es.caib.plugins.arxiu.api;

import java.util.List;

/**
 * 
 * @author Limit
 *
 */
public class Document {

	private String identificador;				// Identificador del document
	private String nom;							// Nom del document
	private DocumentMetadades metadades;		// Metadades del document
	private List<Firma> firmes;					// Llista de firmes del document
	private DocumentContingut contingut;		// Contingut del document
	
	public Document() {
		super();
	}

	public Document(
			String identificador, 
			String nom, 
			DocumentContingut contingut) {
		super();
		this.identificador = identificador;
		this.nom = nom;
		this.contingut = contingut;
	}
	
	public Document(
			String identificador, 
			String nom,
			DocumentContingut contingut, 
			DocumentMetadades metadades) {
		super();
		this.identificador = identificador;
		this.nom = nom;
		this.contingut = contingut;
		this.metadades = metadades;
	}
	
	public Document(
			String identificador, 
			String nom,
			DocumentContingut contingut, 
			List<Firma> firmes) {
		super();
		this.identificador = identificador;
		this.nom = nom;
		this.contingut = contingut;
		this.firmes = firmes;
	}
	
	public Document(
			String identificador, 
			String nom,
			DocumentContingut contingut, 
			DocumentMetadades metadades, 
			List<Firma> firmes) {
		super();
		this.identificador = identificador;
		this.nom = nom;
		this.contingut = contingut;
		this.metadades = metadades;
		this.firmes = firmes;
	}
	
	public Document(Document d) {
		super();
		this.identificador = d.identificador;
		this.nom = d.nom;
		this.contingut = d.contingut;
		this.metadades = d.metadades;
		this.firmes = d.firmes;
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

	public DocumentMetadades getMetadades() {
		return metadades;
	}

	public void setMetadades(DocumentMetadades metadades) {
		this.metadades = metadades;
	}

	public List<Firma> getFirmes() {
		return firmes;
	}

	public void setFirmes(List<Firma> firmes) {
		this.firmes = firmes;
	}

	public DocumentContingut getContingut() {
		return contingut;
	}

	public void setContingut(DocumentContingut contingut) {
		this.contingut = contingut;
	}
	
}
