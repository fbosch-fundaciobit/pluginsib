package es.caib.plugins.arxiu.api;

import java.util.List;

import org.fundaciobit.plugins.IPlugin;

public interface IArxivePlugin extends IPlugin {

	public static final String ARXIVE_BASE_PROPERTY = IPLUGIN_BASE_PROPERTIES + "arxive.";

	/**
	 * =========================================================================
	 * ================ M E T O  D E S    E X P E D I E N T S ==================
	 * =========================================================================
	 */

	public String expedientCrear(
			String nom, 
			ExpedientMetadades metadades) throws ArxiveException;
	
	public InformacioItem expedientModificar(
			String identificador, 
			String nom, 
			ExpedientMetadades metadades) throws ArxiveException;
	
	public void expedientEsborrar(String identificador) throws ArxiveException;
	
	public Expedient expedientDetalls(
			String identificador,
			String versio) throws ArxiveException;
	
	public ConsultaResultat expedientConsulta(
			List<ConsultaFiltre> filtres,
			Integer pagina,
			Integer itemsPerPagina) throws ArxiveException;
	
	public List<InformacioItem> expedientVersions(String identificador) throws ArxiveException;
	
	public void expedientTancar(String identificador) throws ArxiveException;
	
	public void expedientReobrir(String identificador) throws ArxiveException;
	
	public String expedientExportarEni(String identificador) throws ArxiveException;
	
	/**
	 * =========================================================================
	 * ================ M E T O  D E S    D O C U M E N T S ====================
	 * =========================================================================
	 */

	public String documentCrear(
			Document document, 
			String identificadorPare) throws ArxiveException;
	
	public String documentEsborranyCrear(
			Document document, 
			String identificadorPare) throws ArxiveException;
	
	public InformacioItem documentEstablirDefinitiu(String identificador) throws ArxiveException;
	
	public InformacioItem documentModificar(Document document) throws ArxiveException;
	
	public void documentEsborrar(String identificador) throws ArxiveException;
	
	public Document documentDetalls(
			String identificador,
			String versio,
			boolean ambContingut) throws ArxiveException;
	
	public ConsultaResultat documentConsulta(
			List<ConsultaFiltre> filtres,
			Integer pagina,
			Integer itemsPerPagina) throws ArxiveException;
	
	public List<InformacioItem> documentVersions(String identificador) throws ArxiveException;
	
	public String documentGenerarCsv(String identificador) throws ArxiveException;
	
	public void documentCopiar(
			String identificador,
			String identificadorDesti) throws ArxiveException;
	
	public void documentMoure(
			String identificador,
			String identificadorDesti) throws ArxiveException;
	
	public String documentExportarEni(String identificador) throws ArxiveException;
	
	/**
	 * =========================================================================
	 * ================ M E T O  D E S    C A R P E T E S ======================
	 * =========================================================================
	 */

	public String carpetaCrear(
			String nom,
			String identificadorPare) throws ArxiveException;
	
	public InformacioItem carpetaModificar(
			String identificador,
			String nom) throws ArxiveException;
	
	public void carpetaEsborrar(String identificador) throws ArxiveException;
	
	public Carpeta carpetaDetalls(String identificador) throws ArxiveException;
	
	public void carpetaCopiar(
			String identificador,
			String identificadorDesti) throws ArxiveException;
	
	public void carpetaMoure(
			String identificador,
			String identificadorDesti) throws ArxiveException;
	
}
