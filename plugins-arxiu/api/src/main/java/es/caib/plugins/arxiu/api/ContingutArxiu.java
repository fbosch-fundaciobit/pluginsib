/**
 * 
 */
package es.caib.plugins.arxiu.api;

/**
 * Informació sobre un contingut genèric de l’arxiu.
 * Aquest contingut pot ser de tipus document, expedient
 * o carpeta.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ContingutArxiu {

	protected String identificador;
	protected String nom;
	protected String tipus;
	protected String versio;

	public ContingutArxiu(String tipus) {
		super();
		this.tipus = tipus;
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
	public String getTipus() {
		return tipus;
	}
	public String getVersio() {
		return versio;
	}
	public void setVersio(String versio) {
		this.versio = versio;
	}

}
