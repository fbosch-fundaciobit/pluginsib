package es.caib.plugins.arxiu.api;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.ListUtils;

import es.caib.arxiudigital.apirest.constantes.Aspectos;

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
	private List<Aspectos> aspectes;			// Aspectes del document
	
//	public boolean igual(
//			Document d) {
//		
//		if(d.getFirmes().size() != firmes.size()) return false;
//		for(int i = 0; i < firmes.size(); i++) {
//			if(!d.getFirmes().get(i).igual(firmes.get(i))) return false;
//		}
//		
//		String origenId = d.getContingut().getIdentificadorOrigen() == null ?
//				"" : d.getContingut().getIdentificadorOrigen();
//		
//		return d.getIdentificador().equals(identificador) &&
//				d.getNom().equals(nom) &&
//				d.getMetadades().getIdentificador().equals(metadades.getIdentificador()) &&
//				d.getMetadades().getVersioNti().equals(metadades.getVersioNti()) &&
//				d.getMetadades().getVersioNti().equals(metadades.getVersioNti()) &&
//				ListUtils.isEqualList(d.getMetadades().getOrgans(), metadades.getOrgans()) &&
//				d.getMetadades().getData().getTime() == metadades.getData().getTime() &&
//				d.getMetadades().getOrigen().equals(metadades.getOrigen()) &&
//				d.getMetadades().getEstatElaboracio().equals(metadades.getEstatElaboracio()) &&
//				d.getMetadades().getTipusDocumental().equals(metadades.getTipusDocumental()) &&
//				d.getMetadades().getSerieDocumental().equals(metadades.getSerieDocumental()) &&
//				Arrays.equals(d.getContingut().getContingut(), contingut.getContingut()) &&
//				d.getContingut().getTipusMime().equals(contingut.getTipusMime()) &&
//				origenId.equals(contingut.getIdentificadorOrigen());
//				
//	}
	
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

	public List<Aspectos> getAspectes() {
		return aspectes;
	}

	public void setAspectes(List<Aspectos> aspectes) {
		this.aspectes = aspectes;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contingut == null) ? 0 : contingut.hashCode());
		result = prime * result + ((firmes == null) ? 0 : firmes.hashCode());
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
		Document other = (Document) obj;
		if (contingut == null) {
			if (other.contingut != null)
				return false;
		} else if (!contingut.equals(other.contingut))
			return false;
		if (firmes == null) {
			if (other.firmes != null)
				return false;
		} else if (!firmes.equals(other.firmes))
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
