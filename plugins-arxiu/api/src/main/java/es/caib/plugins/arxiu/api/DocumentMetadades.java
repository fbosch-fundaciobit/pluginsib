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
	private String tipusFirma;
	private String perfilFirma;
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
			String serieDocumental,
			String tipusFirma,
			String perfilFirma) {
		super();
		this.identificador = identificador;
		this.versioNti = versioNti;
		this.organs = organs;
		this.data = data;
		this.origen = origen;
		this.estatElaboracio = estatElaboracio;
		this.tipusDocumental = tipusDocumental;
		this.serieDocumental = serieDocumental;
		this.tipusFirma = tipusFirma;
		this.perfilFirma = perfilFirma;
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
			String tipusFirma,
			String perfilFirma,
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
		this.tipusFirma = tipusFirma;
		this.perfilFirma = perfilFirma;
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

	public String getTipoFirma() {
		return tipusFirma;
	}

	public void setTipoFirma(String tipusFirma) {
		this.tipusFirma = tipusFirma;
	}

	public String getPerfilFirma() {
		return perfilFirma;
	}

	public void setPerfilFirma(String perfilFirma) {
		this.perfilFirma = perfilFirma;
	}

	public Map<String, String> getMetadadesAddicionals() {
		return metadadesAddicionals;
	}

	public void setMetadadesAddicionals(Map<String, String> metadadesAddicionals) {
		this.metadadesAddicionals = metadadesAddicionals;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((estatElaboracio == null) ? 0 : estatElaboracio.hashCode());
		result = prime * result + ((identificador == null) ? 0 : identificador.hashCode());
		result = prime * result + ((metadadesAddicionals == null) ? 0 : metadadesAddicionals.hashCode());
		result = prime * result + ((organs == null) ? 0 : organs.hashCode());
		result = prime * result + ((origen == null) ? 0 : origen.hashCode());
		result = prime * result + ((serieDocumental == null) ? 0 : serieDocumental.hashCode());
		result = prime * result + ((tipusDocumental == null) ? 0 : tipusDocumental.hashCode());
		result = prime * result + ((versioNti == null) ? 0 : versioNti.hashCode());
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
		DocumentMetadades other = (DocumentMetadades) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (estatElaboracio == null) {
			if (other.estatElaboracio != null)
				return false;
		} else if (!estatElaboracio.equals(other.estatElaboracio))
			return false;
		if (identificador == null) {
			if (other.identificador != null)
				return false;
		} else if (!identificador.equals(other.identificador))
			return false;
		if (metadadesAddicionals == null) {
			if (other.metadadesAddicionals != null)
				return false;
		} else if (!metadadesAddicionals.equals(other.metadadesAddicionals))
			return false;
		if (organs == null) {
			if (other.organs != null)
				return false;
		} else if (!organs.equals(other.organs))
			return false;
		if (origen == null) {
			if (other.origen != null)
				return false;
		} else if (!origen.equals(other.origen))
			return false;
		if (serieDocumental == null) {
			if (other.serieDocumental != null)
				return false;
		} else if (!serieDocumental.equals(other.serieDocumental))
			return false;
		if (tipusDocumental == null) {
			if (other.tipusDocumental != null)
				return false;
		} else if (!tipusDocumental.equals(other.tipusDocumental))
			return false;
		if (versioNti == null) {
			if (other.versioNti != null)
				return false;
		} else if (!versioNti.equals(other.versioNti))
			return false;
		return true;
	}
	
	
	
}
