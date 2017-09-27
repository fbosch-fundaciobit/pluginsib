/**
 * 
 */
package es.caib.plugins.arxiu.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Meta-dades associades a un document.
 *
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DocumentMetadades {

	private String identificador;
	private String versioNti;
	private List<String> organs;
	private Date data;
	private String origen;
	private String estatElaboracio;
	private String tipusDocumental;
	private String serieDocumental;
	private Map<String, String> metadadesAddicionals;

	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getVersioNti() {
		return versioNti;
	}
	public void setVersioNti(String versioNti) {
		this.versioNti = versioNti;
	}
	public List<String> getOrgans() {
		return organs;
	}
	public void setOrgans(List<String> organs) {
		this.organs = organs;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public String getEstatElaboracio() {
		return estatElaboracio;
	}
	public void setEstatElaboracio(String estatElaboracio) {
		this.estatElaboracio = estatElaboracio;
	}
	public String getTipusDocumental() {
		return tipusDocumental;
	}
	public void setTipusDocumental(String tipusDocumental) {
		this.tipusDocumental = tipusDocumental;
	}
	public String getSerieDocumental() {
		return serieDocumental;
	}
	public void setSerieDocumental(String serieDocumental) {
		this.serieDocumental = serieDocumental;
	}
	public Map<String, String> getMetadadesAddicionals() {
		return metadadesAddicionals;
	}
	public void setMetadadesAddicionals(Map<String, String> metadadesAddicionals) {
		this.metadadesAddicionals = metadadesAddicionals;
	}

}
