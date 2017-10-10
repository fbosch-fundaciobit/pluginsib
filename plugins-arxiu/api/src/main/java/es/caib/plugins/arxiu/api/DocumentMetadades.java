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
	private String origen;
	private List<String> organs;
	private Date dataCaptura;
	private String estatElaboracio;
	private String tipusDocumental;
	private String format;
	private String extensio;
	private String serieDocumental;
	private String identificadorOrigen;
	private Map<String, Object> metadadesAddicionals;

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
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public List<String> getOrgans() {
		return organs;
	}
	public void setOrgans(List<String> organs) {
		this.organs = organs;
	}
	public Date getDataCaptura() {
		return dataCaptura;
	}
	public void setDataCaptura(Date dataCaptura) {
		this.dataCaptura = dataCaptura;
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
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getExtensio() {
		return extensio;
	}
	public void setExtensio(String extensio) {
		this.extensio = extensio;
	}
	public String getSerieDocumental() {
		return serieDocumental;
	}
	public void setSerieDocumental(String serieDocumental) {
		this.serieDocumental = serieDocumental;
	}
	public String getIdentificadorOrigen() {
		return identificadorOrigen;
	}
	public void setIdentificadorOrigen(String identificadorOrigen) {
		this.identificadorOrigen = identificadorOrigen;
	}
	public Map<String, Object> getMetadadesAddicionals() {
		return metadadesAddicionals;
	}
	public void setMetadadesAddicionals(Map<String, Object> metadadesAddicionals) {
		this.metadadesAddicionals = metadadesAddicionals;
	}

}
