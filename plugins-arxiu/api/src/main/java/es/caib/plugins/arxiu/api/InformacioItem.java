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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identificador == null) ? 0 : identificador.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result + ((tipus == null) ? 0 : tipus.hashCode());
		result = prime * result + ((versio == null) ? 0 : versio.hashCode());
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
		InformacioItem other = (InformacioItem) obj;
		if (identificador == null) {
			if (other.identificador != null)
				return false;
		} else if (!identificador.equals(other.identificador))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		if (tipus != other.tipus)
			return false;
		if (versio == null) {
			if (other.versio != null)
				return false;
		} else if (!versio.equals(other.versio))
			return false;
		return true;
	}
	
	
	
}
