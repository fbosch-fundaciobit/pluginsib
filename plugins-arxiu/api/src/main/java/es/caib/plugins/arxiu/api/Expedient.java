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
public class Expedient extends ContingutArxiu {

	private ExpedientMetadades metadades;
	private List<ContingutArxiu> continguts;

	public Expedient() {
		super(ArxiuConstants.CONTINGUT_TIPUS_EXPEDIENT);
	}

	public ExpedientMetadades getMetadades() {
		return metadades;
	}
	public void setMetadades(ExpedientMetadades metadades) {
		this.metadades = metadades;
	}
	public List<ContingutArxiu> getContinguts() {
		return continguts;
	}
	public void setContinguts(List<ContingutArxiu> continguts) {
		this.continguts = continguts;
	}

}
