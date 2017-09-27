/**
 * 
 */
package es.caib.plugins.arxiu.api;

import java.util.List;

/**
 * Informació d'un contingut de l'arxiu de tipus expedient.
 *
 * @author Limit Tecnologies <limit@limit.es>
 */
public class Expedient {

	private String identificador;
	private String nom;
	private ExpedientMetadades metadades;
	private List<InformacioItem> continguts;

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
