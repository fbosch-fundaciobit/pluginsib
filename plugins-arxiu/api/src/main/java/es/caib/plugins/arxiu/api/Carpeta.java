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
	
	
//	public boolean igual(
//			Carpeta c) {
//		
//		return c.getIdentificador().equals(identificador) &&
//				c.getNom().equals(nom);
//	}
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identificador == null) ? 0 : identificador.hashCode());
		result = prime * result + ((informacioItems == null) ? 0 : informacioItems.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
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
		Carpeta other = (Carpeta) obj;
		if (identificador == null) {
			if (other.identificador != null)
				return false;
		} else if (!identificador.equals(other.identificador))
			return false;
		if (informacioItems == null) {
			if (other.informacioItems != null)
				return false;
		} else if (!informacioItems.equals(other.informacioItems))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}
	
	
	
}
