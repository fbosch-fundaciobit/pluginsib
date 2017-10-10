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
public class Document extends ContingutArxiu {

	private String estat;
	private DocumentMetadades metadades;
	private List<Firma> firmes;
	private DocumentContingut contingut;

	public Document() {
		super(ArxiuConstants.CONTINGUT_TIPUS_DOCUMENT);
	}

	public String getEstat() {
		return estat;
	}
	public void setEstat(String estat) {
		this.estat = estat;
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

}
