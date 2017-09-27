package es.caib.plugins.arxiu.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Limit
 *
 */
public class ExpedientMetadades {

	private String identificador;						// Identificador de la metadada
	private String versioNti;							// http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e
	private String origen;								// Administració o ciutadà
	private List<String> organs;						// Codis Dir3 de les unitats orgàniques
	private Date dataObertura;							// Data d’obertura de l’expedient
	private String classificacio;						// Codi SIA del procediment
	private String estat;								// Estat de l’expedient:
														//		- E01: Obert
														//		- E02: Tancat
														//		- E03: Índex per a remissió tancat
	private List<String> interessats;					// NIFs dels interessats associats a l’expedient
	private String serieDocumental;						// Identificador de la sèrie documental de l’arxiu
	private Map<String, String> metadadesAddicionals;	// Altres metadades que es vulguin desar (Map: <nomMetadada, valorMetadada>)
	
	public ExpedientMetadades() {
		super();
	}

	public ExpedientMetadades(
			String identificador, 
			String versioNti,
			String origen,
			List<String> organs,
			Date dataObertura,
			String classificacio, 
			String estat, 
			String serieDocumental) {
		super();
		this.identificador = identificador;
		this.versioNti = versioNti;
		this.origen = origen;
		this.organs = organs;
		this.dataObertura = dataObertura;
		this.classificacio = classificacio;
		this.estat = estat;
		this.serieDocumental = serieDocumental;
	}
	
	public ExpedientMetadades(
			String identificador, 
			String versioNti,
			String origen,
			List<String> organs, 
			Date dataObertura,
			String classificacio, 
			String estat, 
			List<String> interessats, 
			String serieDocumental) {
		super();
		this.identificador = identificador;
		this.versioNti = versioNti;
		this.origen = origen;
		this.organs = organs;
		this.dataObertura = dataObertura;
		this.classificacio = classificacio;
		this.estat = estat;
		this.interessats = interessats;
		this.serieDocumental = serieDocumental;
	}
	
	public ExpedientMetadades(
			String identificador, 
			String versioNti,
			String origen,
			List<String> organs, 
			Date dataObertura,
			String classificacio, 
			String estat, 
			String serieDocumental,
			Map<String, String> metadadesAddicionals) {
		super();
		this.identificador = identificador;
		this.versioNti = versioNti;
		this.origen = origen;
		this.organs = organs;
		this.dataObertura = dataObertura;
		this.classificacio = classificacio;
		this.estat = estat;
		this.serieDocumental = serieDocumental;
		this.metadadesAddicionals = metadadesAddicionals;
	}
	
	public ExpedientMetadades(
			String identificador,
			String versioNti,
			String origen,
			List<String> organs, 
			Date dataObertura,
			String classificacio, 
			String estat, 
			List<String> interessats, 
			String serieDocumental,
			Map<String, String> metadadesAddicionals) {
		super();
		this.identificador = identificador;
		this.versioNti = versioNti;
		this.origen = origen;
		this.organs = organs;
		this.dataObertura = dataObertura;
		this.classificacio = classificacio;
		this.estat = estat;
		this.interessats = interessats;
		this.serieDocumental = serieDocumental;
		this.metadadesAddicionals = metadadesAddicionals;
	}
	
	public ExpedientMetadades(ExpedientMetadades em) {
		super();
		this.identificador = em.identificador;
		this.versioNti = em.versioNti;
		this.origen = em.origen;
		this.organs = em.organs;
		this.dataObertura = em.dataObertura;
		this.classificacio = em.classificacio;
		this.estat = em.estat;
		this.interessats = em.interessats;
		this.serieDocumental = em.serieDocumental;
		this.metadadesAddicionals = em.metadadesAddicionals;
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

	public Date getDataObertura() {
		return dataObertura;
	}

	public void setDataObertura(Date dataObertura) {
		this.dataObertura = dataObertura;
	}

	public String getClassificacio() {
		return classificacio;
	}

	public void setClassificacio(String classificacio) {
		this.classificacio = classificacio;
	}

	public String getEstat() {
		return estat;
	}

	public void setEstat(String estat) {
		this.estat = estat;
	}

	public List<String> getInteressats() {
		return interessats;
	}

	public void setInteressats(List<String> interessats) {
		this.interessats = interessats;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classificacio == null) ? 0 : classificacio.hashCode());
		result = prime * result + ((dataObertura == null) ? 0 : dataObertura.hashCode());
		result = prime * result + ((estat == null) ? 0 : estat.hashCode());
		result = prime * result + ((identificador == null) ? 0 : identificador.hashCode());
		result = prime * result + ((interessats == null) ? 0 : interessats.hashCode());
		result = prime * result + ((metadadesAddicionals == null) ? 0 : metadadesAddicionals.hashCode());
		result = prime * result + ((organs == null) ? 0 : organs.hashCode());
		result = prime * result + ((origen == null) ? 0 : origen.hashCode());
		result = prime * result + ((serieDocumental == null) ? 0 : serieDocumental.hashCode());
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
		ExpedientMetadades other = (ExpedientMetadades) obj;
		if (classificacio == null) {
			if (other.classificacio != null)
				return false;
		} else if (!classificacio.equals(other.classificacio))
			return false;
		if (dataObertura == null) {
			if (other.dataObertura != null)
				return false;
		} else if (!dataObertura.equals(other.dataObertura))
			return false;
		if (estat == null) {
			if (other.estat != null)
				return false;
		} else if (!estat.equals(other.estat))
			return false;
		if (identificador == null) {
			if (other.identificador != null)
				return false;
		} else if (!identificador.equals(other.identificador))
			return false;
		if (interessats == null) {
			if (other.interessats != null)
				return false;
		} else if (!interessats.equals(other.interessats))
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
		if (versioNti == null) {
			if (other.versioNti != null)
				return false;
		} else if (!versioNti.equals(other.versioNti))
			return false;
		return true;
	}
	
	
	
}
