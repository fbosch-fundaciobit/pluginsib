package es.caib.plugins.arxiu.filesystem;

import java.util.Date;
import java.util.List;

import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.InformacioItem;

/**
 * 
 * @author Limit
 *
 */
public class ExpedientDao {

	private String identificador;
	private String nom;
	
	private String versio;
	private String gestorVersions;
	private boolean obert;
	
	private String idMetadades;
	private String versioNti;
	private String origen;
	private List<String> organs;
	private Date dataObertura;
	private String classificacio;
	private String estat;
	private List<String> interessats;
	private String serieDocumental;	
	
	private List<InformacioItem> continguts;
	
	
	public Expedient getExpedient() {
		
		return new Expedient(
				identificador,
				nom,
				new ExpedientMetadades(
						idMetadades,
						versioNti,
						origen,
						organs,
						dataObertura,
						classificacio,
						estat,
						serieDocumental,
						null),
				continguts);
	}
	
	public ExpedientDao(
			String identificador,
			String nom,
			String versio,
			String gestorVersions,
			boolean obert,
			String idMetadades,
			String versioNti,
			String origen,
			List<String> organs,
			Date dataObertura,
			String classificacio,
			String estat,
			List<String> interessats,
			String serieDocumental,
			List<InformacioItem> continguts) {
		
		super();
		
		this.identificador = identificador;
		this.nom = nom;
		this.versio = versio;
		this.gestorVersions = gestorVersions;
		this.obert = obert;
		this.idMetadades = idMetadades;
		this.versioNti = versioNti;
		this.origen = origen;
		this.organs = organs;
		this.dataObertura = dataObertura;
		this.classificacio = classificacio;
		this.estat = estat;
		this.interessats = interessats;
		this.serieDocumental = serieDocumental;
		this.continguts = continguts;
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
	
	public String getVersio() {
		return versio;
	}
	public void setVersio(String versio) {
		this.versio = versio;
	}
	
	public String getGestorVersions() {
		return gestorVersions;
	}
	public void setGestorVersions(String gestorVersions) {
		this.gestorVersions = gestorVersions;
	}
	
	public boolean isObert() {
		return obert;
	}
	public void setObert(boolean obert) {
		this.obert = obert;
	}
	
	public String getIdMetadades() {
		return idMetadades;
	}
	public void setIdMetadades(String idMetadades) {
		this.idMetadades = idMetadades;
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
	
	public List<InformacioItem> getContinguts() {
		return continguts;
	}
	public void setContinguts(List<InformacioItem> continguts) {
		this.continguts = continguts;
	}
	
	
}
