/**
 * 
 */
package es.caib.plugins.arxiu.api;

import java.util.List;

/**
 * Informació d'un contingut de l'arxiu de tipus document.
 *
 * @author Limit Tecnologies <limit@limit.es>
 */
public class Document {

	private String identificador;
	private String nom;
	private DocumentMetadades metadades;
	private List<Firma> firmes;
	private DocumentContingut contingut;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identificador == null) ? 0 : identificador.hashCode());
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
		if (identificador == null) {
			if (other.identificador != null)
				return false;
		} else if (!identificador.equals(other.identificador))
			return false;
		return true;
	}

}
