package es.caib.plugins.arxiu.api;

import java.util.List;

/**
 * 
 * @author Limit
 *
 */
public class Expedient {

	private String identificador;					// Identificador de l’expedient
	private String nom;								// Nom de l’expedient
	private ExpedientMetadades metadades;			// Metadades de l’expedient
	private List<InformacioItem> continguts;		// Llista d’items que penjen de l’expedient
	
	public Expedient() {
		super();
	}

	public Expedient(
			String identificador, 
			String nom) {
		super();
		this.identificador = identificador;
		this.nom = nom;
	}
	
	public Expedient(
			String identificador, 
			String nom, 
			ExpedientMetadades metadades) {
		super();
		this.identificador = identificador;
		this.nom = nom;
		this.metadades = metadades;
	}
	
	public Expedient(
			String identificador, 
			String nom, 
			List<InformacioItem> continguts) {
		super();
		this.identificador = identificador;
		this.nom = nom;
		this.continguts = continguts;
	}
	
	public Expedient(
			String identificador, 
			String nom, 
			ExpedientMetadades metadades, 
			List<InformacioItem> continguts) {
		super();
		this.identificador = identificador;
		this.nom = nom;
		this.metadades = metadades;
		this.continguts = continguts;
	}
	
	public Expedient(Expedient e) {
		super();
		this.identificador = e.identificador;
		this.nom = e.nom;
		this.metadades = e.metadades;
		this.continguts = e.continguts;
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

	public ExpedientMetadades getMetadades() {
		return metadades;
	}

	public void setMetadades(ExpedientMetadades metadades) {
		this.metadades = metadades;
	}

	public List<InformacioItem> getContinguts() {
		return continguts;
	}

	public void setContinguts(List<InformacioItem> continguts) {
		this.continguts = continguts;
	}
	
}
