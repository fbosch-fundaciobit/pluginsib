package es.caib.plugins.arxiu.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Limit
 *
 */
public class DocumentMetadades {

	private String identificador;						// Identificador de la metadada
	private String versioNti;							// http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e
	private List<String> organs;						// Codis Dir3 de les unitats orgàniques
	private Date data;									// Data de captura del document
	private String origen;								// Administració o ciutadà
	private String estatElaboracio;						// Estat elaboració:
														//		- EE01: Original
														//		- EE02: Còpia electrònica autèntica amb canvi de format
														//		- EE03: Còpia electrònica autèntica de document paper
														//		- EE04: Còpia electrònica parcial autèntica
	private String tipusDocumental;						// Tipus documental:
														//		- TD01 - Resolución.
														//		- TD02 - Acuerdo.
														//		- TD03 - Contrato.
														//		- TD04 - Convenio.
														//		- TD05 - Declaración.
														//		- TD06 - Comunicación.
														//		- TD07 - Notificación.
														//		- TD08 - Publicación.
														//		- TD09 - Acuse de recibo.
														//		- TD10 - Acta.
														//		- TD11 - Certificado.
														//		- TD12 - Diligencia.
														//		- TD13 - Informe.
														//		- TD14 - Solicitud.
														//		- TD15 - Denuncia.
														//		- TD16 - Alegación.
														//		- TD17 - Recursos.
														//		- TD18 - Comunicación ciudadano.
														//		- TD19 - Factura.
														//		- TD20 - Otros incautados.
														//		- TD99 - Otros
	private String serieDocumental;						// Identificador de la sèrie documental de l’arxiu
	private Map<String, String> metadadesAddicionals;	// Altres metadades que es vulguin desar (Map: <nomMetadada, valorMetadada>)
	
	public DocumentMetadades() {
		super();
	}

	public DocumentMetadades(
			String identificador, 
			String versioNti, 
			List<String> organs, 
			Date data, 
			String origen,
			String estatElaboracio, 
			String tipusDocumental, 
			String serieDocumental) {
		super();
		this.identificador = identificador;
		this.versioNti = versioNti;
		this.organs = organs;
		this.data = data;
		this.origen = origen;
		this.estatElaboracio = estatElaboracio;
		this.tipusDocumental = tipusDocumental;
		this.serieDocumental = serieDocumental;
	}
	
	public DocumentMetadades(
			String identificador, 
			String versioNti, 
			List<String> organs, 
			Date data, 
			String origen,
			String estatElaboracio, 
			String tipusDocumental, 
			String serieDocumental,
			Map<String, String> metadadesAddicionals) {
		super();
		this.identificador = identificador;
		this.versioNti = versioNti;
		this.organs = organs;
		this.data = data;
		this.origen = origen;
		this.estatElaboracio = estatElaboracio;
		this.tipusDocumental = tipusDocumental;
		this.serieDocumental = serieDocumental;
		this.metadadesAddicionals = metadadesAddicionals;
	}
	
	public DocumentMetadades(DocumentMetadades dm) {
		super();
		this.identificador = dm.identificador;
		this.versioNti = dm.versioNti;
		this.organs = dm.organs;
		this.data = dm.data;
		this.origen = dm.origen;
		this.estatElaboracio = dm.estatElaboracio;
		this.tipusDocumental = dm.tipusDocumental;
		this.serieDocumental = dm.serieDocumental;
		this.metadadesAddicionals = dm.metadadesAddicionals;
	}

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
