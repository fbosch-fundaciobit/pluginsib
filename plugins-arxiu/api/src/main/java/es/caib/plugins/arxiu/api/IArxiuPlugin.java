package es.caib.plugins.arxiu.api;

import java.util.List;

import org.fundaciobit.plugins.IPlugin;

public interface IArxiuPlugin extends IPlugin {

	public static final String ARXIVE_BASE_PROPERTY = IPLUGIN_BASE_PROPERTIES + "arxiu.";

	/**
	 * =========================================================================
	 * ================ M E T O  D E S    E X P E D I E N T S ==================
	 * =========================================================================
	 */
	//tested
	public String expedientCrear(
			String nom, 
			ExpedientMetadades metadades) throws ArxiuException;
	//tested
	public InformacioItem expedientModificar(
			String identificador, 
			String nom, 
			ExpedientMetadades metadades) throws ArxiuException;
	//tested
	public void expedientEsborrar(
			String identificador) throws ArxiuException;
	//tested
	public Expedient expedientDetalls(
			String identificador,
			String versio) throws ArxiuException;
	//tested
	public ConsultaResultat expedientConsulta(
			List<ConsultaFiltre> filtres,
			Integer pagina,
			Integer itemsPerPagina) throws ArxiuException;
	//tested
	public List<InformacioItem> expedientVersions(
			String identificador) throws ArxiuException;
	//tested
	public void expedientTancar(
			String identificador) throws ArxiuException;
	//tested
	public void expedientReobrir(
			String identificador) throws ArxiuException;
	//tested
	public String expedientExportarEni(
			String identificador) throws ArxiuException;
	
	/**
	 * =========================================================================
	 * ================ M E T O  D E S    D O C U M E N T S ====================
	 * =========================================================================
	 */
	//tested
	public String documentCrear(
			Document document, 
			String identificadorPare) throws ArxiuException;
	//tested
	public String documentEsborranyCrear(
			Document document, 
			String identificadorPare) throws ArxiuException;
	//tested
	public InformacioItem documentEstablirDefinitiu(
			String identificador) throws ArxiuException;
	//tested
	public InformacioItem documentModificar(
			Document document) throws ArxiuException;
	//tested
	public void documentEsborrar(
			String identificador) throws ArxiuException;
	//tested
	public Document documentDetalls(
			String identificador,
			String versio,
			boolean ambContingut) throws ArxiuException;
	//tested
	public ConsultaResultat documentConsulta(
			List<ConsultaFiltre> filtres,
			Integer pagina,
			Integer itemsPerPagina) throws ArxiuException;
	//tested
	public List<InformacioItem> documentVersions(
			String identificador) throws ArxiuException;
	//tested
	public String documentGenerarCsv() throws ArxiuException;
	//tested
	public String documentCopiar(
			String identificador,
			String identificadorDesti) throws ArxiuException;
	//tested
	public void documentMoure(
			String identificador,
			String identificadorDesti) throws ArxiuException;
	//tested
	public String documentExportarEni(
			String identificador) throws ArxiuException;
	
	/**
	 * =========================================================================
	 * ================ M E T O  D E S    C A R P E T E S ======================
	 * =========================================================================
	 */
	//tested
	public String carpetaCrear(
			String nom,
			String identificadorPare) throws ArxiuException;
	//tested
	public InformacioItem carpetaModificar(
			String identificador,
			String nom) throws ArxiuException;
	//tested
	public void carpetaEsborrar(
			String identificador) throws ArxiuException;
	//tested
	public Carpeta carpetaDetalls(
			String identificador) throws ArxiuException;
	
	public String carpetaCopiar(
			String identificador,
			String identificadorDesti) throws ArxiuException;
	
	public void carpetaMoure(
			String identificador,
			String identificadorDesti) throws ArxiuException;
	
}
