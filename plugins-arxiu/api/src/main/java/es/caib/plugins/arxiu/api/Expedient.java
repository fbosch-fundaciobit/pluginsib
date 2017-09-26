package es.caib.plugins.arxiu.api;

import java.util.List;

import org.apache.commons.collections4.ListUtils;

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
	
	
//	public boolean igual(
//			Expedient e) {
//		
//		return e.getIdentificador().equals(identificador) &&
//				e.getNom().equals(nom) &&
//				e.getMetadades().getIdentificador().equals(metadades.getIdentificador()) &&
//				e.getMetadades().getVersioNti().equals(metadades.getVersioNti()) &&
//				e.getMetadades().getOrigen().equals(metadades.getOrigen()) &&
//				e.getMetadades().getOrigen().equals(metadades.getOrigen()) &&
//				ListUtils.isEqualList(e.getMetadades().getOrgans(), metadades.getOrgans()) &&
//				e.getMetadades().getDataObertura().getTime() == metadades.getDataObertura().getTime() &&
//				e.getMetadades().getClassificacio().equals(metadades.getClassificacio()) &&
//				e.getMetadades().getEstat().equals(metadades.getEstat()) &&
//				e.getMetadades().getSerieDocumental().equals(metadades.getSerieDocumental());
//	}
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((continguts == null) ? 0 : continguts.hashCode());
		result = prime * result + ((identificador == null) ? 0 : identificador.hashCode());
		result = prime * result + ((metadades == null) ? 0 : metadades.hashCode());
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
		Expedient other = (Expedient) obj;
		if (continguts == null) {
			if (other.continguts != null)
				return false;
		} else if (!continguts.equals(other.continguts))
			return false;
		if (identificador == null) {
			if (other.identificador != null)
				return false;
		} else if (!identificador.equals(other.identificador))
			return false;
		if (metadades == null) {
			if (other.metadades != null)
				return false;
		} else if (!metadades.equals(other.metadades))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}
	
	
	
}
