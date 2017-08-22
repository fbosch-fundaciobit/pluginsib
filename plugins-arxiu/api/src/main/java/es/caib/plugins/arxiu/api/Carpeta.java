package es.caib.plugins.arxiu.api;

import java.util.List;

/**
 * 
 * @author Limit
 *
 */
public class Carpeta {

	private String identificador;					// Identificador de la carpeta
	private String nom;								// Nom de la carpeta
	private List<InformacioItem> informacioItems;	// Llista d’elements que pengen de la carpeta
	
	public Carpeta() {
		super();
	}

	public Carpeta(
			String identificador, 
			String nom, 
			List<InformacioItem> informacioItems) {
		super();
		this.identificador = identificador;
		this.nom = nom;
		this.informacioItems = informacioItems;
	}
	
	public Carpeta(
			String identificador, 
			String nom) {
		super();
		this.identificador = identificador;
		this.nom = nom;
	}
	
	public Carpeta(Carpeta c) {
		super();
		this.identificador = c.identificador;
		this.nom = c.nom;
		this.informacioItems = c.informacioItems;
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

	public List<InformacioItem> getInformacioItems() {
		return informacioItems;
	}
	
	public void setInformacioItems(List<InformacioItem> informacioItems) {
		this.informacioItems = informacioItems;
	}
	
}
