/**
 * 
 */
package es.caib.plugins.arxiu.api;

/**
 * Informació sobre un contingut d’un expedient o carpeta.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class InformacioItem {

	private String identificador;
	private String nom;
	private String tipus;
	private String versio;

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
	public String getTipus() {
		return tipus;
	}
	public void setTipus(String tipus) {
		this.tipus = tipus;
	}
	public String getVersio() {
		return versio;
	}
	public void setVersio(String versio) {
		this.versio = versio;
	}

}
