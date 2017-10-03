package es.caib.plugins.arxiu.caib;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ConsultaResultat;
import es.caib.plugins.arxiu.api.ContingutTipus;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.IArxiuPlugin;
import es.caib.plugins.arxiu.api.InformacioItem;

public class CaibArxiuPlugin extends AbstractPluginProperties implements IArxiuPlugin {

	protected final Logger log = Logger.getLogger(getClass());

	public static final String ARXIUCAIB_PROPERTY_BASE = ARXIVE_BASE_PROPERTY + "caib.";
	
	private CaibArxiuClient arxiuClient;
	
	public CaibArxiuPlugin() {
		super();
	}
	public CaibArxiuPlugin(String propertyKeyBase, Properties properties) {
		super(propertyKeyBase, properties);
	}
	public CaibArxiuPlugin(String propertyKeyBase) {
		super(propertyKeyBase);
	}
	
	// ---------------------------------------------------------------------------
	// ---------------------------- P R O P I E T A T S --------------------------
	// ---------------------------------------------------------------------------

	private String getPropertyBase() {
		return ARXIUCAIB_PROPERTY_BASE;
	}
	
	private String getBaseUrl() {
		return getProperty(getPropertyBase() + "base.url");
	}
	private String getAplicacioCodi() {
		return getProperty(getPropertyBase() + "aplicacio.codi");
	}
	private String getUsuari() {
		return getProperty(getPropertyBase() + "usuari");
	}
	private String getContrasenya() {
		return getProperty(getPropertyBase() + "contrasenya");
	}
	
	private Boolean isDebugValue = null;

	public boolean isDebug() {
		if (isDebugValue == null) {
			isDebugValue = log.isDebugEnabled()
					|| "true".equals(getProperty(getPropertyBase() + "debug"));
		}
		return isDebugValue;
	}

	
	/**
	 * ===========================================================================
	 * =================== M E T O D E S   E X P E D I E N T S ===================
	 * ===========================================================================
	 */
	
	@Override
	public String expedientCrear(
			String nom,
			ExpedientMetadades metadades) throws ArxiuException {
		
		Expedient expedient = getArxiuClient().fileCreate(
				nom,
				metadades,
				getAplicacioCodi(),
				CaibArxiuConversioHelper.toCapsalera());
		
		return expedient.getIdentificador();
	}
	
	@Override
	public InformacioItem expedientModificar(
			String identificador, 
			String nom, 
			ExpedientMetadades metadades) throws ArxiuException {
		
		getArxiuClient().fileUpdate(
				identificador, 
				nom,
				metadades,
				getAplicacioCodi(),
				CaibArxiuConversioHelper.toCapsalera());
		
		return new InformacioItem(
				identificador, 
				nom,
				ContingutTipus.EXPEDIENT);
	}
	
	@Override
	public void expedientEsborrar(
			String identificador) throws ArxiuException {
		
		getArxiuClient().fileDelete(
				identificador,
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	@Override
	public Expedient expedientDetalls(
			String identificador,
			String versio) throws ArxiuException {
		
		List<InformacioItem> informacioItems = expedientVersions(identificador);
		
		String identificadorExpedient = null;
		if(versio == null) {
			if(informacioItems.size() > 0)
				identificadorExpedient = informacioItems.get(informacioItems.size()-1).getIdentificador();
			else
				throw new ArxiuException(
						"No s'ha trobat cap versió de l'expedient (id=" + identificador + ")");
		} else {
			int posicio = Integer.parseInt(versio) - 1;
			if(posicio >= 0 && posicio < informacioItems.size())
				identificadorExpedient = informacioItems.get(posicio).getIdentificador();
			else
				throw new ArxiuException(
						"No s'ha trobat la versió del expedient (id=" + identificador +
						", versio=" + versio + ")");
		}
		
		return getArxiuClient().fileGet(
				identificadorExpedient,
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	@Override
	public ConsultaResultat expedientConsulta(
			List<ConsultaFiltre> filtres,
			Integer pagina,
			Integer itemsPerPagina) throws ArxiuException {
		
		List<InformacioItem> result = new ArrayList<InformacioItem>();
		List<InformacioItem> informacioItemList = null;
		
		int numPagina = 1;
		while(informacioItemList == null ||
			  informacioItemList.size() == CaibArxiuClient.NUMERO_PAGINES_RESULTAT_CERCA) {
			
			informacioItemList = getArxiuClient().fileSearch(
					filtres, 
					numPagina, 
					CaibArxiuConversioHelper.toCapsalera());
			result.addAll(informacioItemList);
			numPagina++;
		}
		
		int fromIndex = pagina.intValue() * itemsPerPagina.intValue();
		int toIndex = (pagina.intValue() + 1) * itemsPerPagina.intValue();
		if(toIndex > result.size()) toIndex = result.size();
				
		return new ConsultaResultat(
				itemsPerPagina,
				new Integer(result.size() / itemsPerPagina.intValue()),
				new Integer(result.size()),
				pagina,
				result.size() == 0 ? new ArrayList<InformacioItem>() : result.subList(fromIndex, toIndex));
	}
	
	@Override
	public List<InformacioItem> expedientVersions(
			String identificador) throws ArxiuException {
		
		return getArxiuClient().fileVersionList(
				identificador,
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	@Override
	public void expedientTancar(
			String identificador) throws ArxiuException {
		
		getArxiuClient().fileClose(
				identificador,
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	@Override
	public void expedientReobrir(
			String identificador) throws ArxiuException {
		
		getArxiuClient().fileReopen(
				identificador,
				CaibArxiuConversioHelper.toCapsalera());
	}
	@Override
	public String expedientExportarEni(
			String identificador) throws ArxiuException {
		
		return getArxiuClient().fileExport(
				identificador,
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	
	/**
	 * ===================================================================
	 * ================ M E T O D E S   D O C U M E N T S ================
	 * ===================================================================
	 */
	
	@Override
	public String documentCrear(
			Document document,
			String identificadorPare) throws ArxiuException {
		
		Document documentCreat = getArxiuClient().documentFinalCreate(
				identificadorPare,
				document,
				getAplicacioCodi(),
				CaibArxiuConversioHelper.toCapsalera());
		
		return documentCreat.getIdentificador();
	}
	
	@Override
	public String documentEsborranyCrear(
			Document document,
			String identificadorPare) throws ArxiuException {
		
		Document documentCreat = getArxiuClient().documentDraftCreate(
				identificadorPare,
				document,
				getAplicacioCodi(),
				CaibArxiuConversioHelper.toCapsalera());
		
		return documentCreat.getIdentificador();
	}
	
	@Override
	public InformacioItem documentEstablirDefinitiu(
			String identificador) throws ArxiuException {
		
		Document document = documentDetalls(
				identificador,
				null,
				false);
		
		getArxiuClient().documentFinalSet(
				document,
				getAplicacioCodi(),
				CaibArxiuConversioHelper.toCapsalera());
		
		return new InformacioItem(
				document.getIdentificador(), 
				document.getNom(),
				ContingutTipus.DOCUMENT);
	}
	
	@Override
	public InformacioItem documentModificar(
			Document document) throws ArxiuException {
		
		getArxiuClient().documentUpdate(
				document,
				getAplicacioCodi(),
				CaibArxiuConversioHelper.toCapsalera());
		
		return new InformacioItem(
				document.getIdentificador(), 
				document.getNom(),
				ContingutTipus.DOCUMENT);
	}
	
	@Override
	public void documentEsborrar(
			String identificador) throws ArxiuException {
		
		getArxiuClient().documentDelete(
				identificador,
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	@Override
	public Document documentDetalls(
			String identificador,
			String versio,
			boolean ambContingut) throws ArxiuException {
		
		List<InformacioItem> informacioItems = documentVersions(identificador);
		
		String identificadorDocument = null;
		if(versio == null) {
			if(informacioItems.size() > 0)
				identificadorDocument = informacioItems.get(informacioItems.size()-1).getIdentificador();
			else
				throw new ArxiuException(
						"No s'ha trobat cap versió del document (id=" + identificador + ")");
		} else {
			int posicio = Integer.parseInt(versio) - 1;
			if(posicio >= 0 && posicio < informacioItems.size())
				identificadorDocument =informacioItems.get(posicio).getIdentificador();
			else
				throw new ArxiuException(
						"No s'ha trobat la versió del document (id=" + identificador +
						", versio=" + versio + ")");
		}
		
		String nodeIdAmbVersio = (versio != null) ?
				versio + "@" + identificadorDocument : identificadorDocument;
		return getArxiuClient().documentGet(
				identificador,
				null,
				ambContingut,
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	@Override
	public ConsultaResultat documentConsulta(
			List<ConsultaFiltre> filtres,
			Integer pagina,
			Integer itemsPerPagina) throws ArxiuException {
		
		List<InformacioItem> result = new ArrayList<InformacioItem>();
		List<InformacioItem> informacioItemList = null;
		
		int numPagina = 1;
		while(informacioItemList == null ||
			  informacioItemList.size() == CaibArxiuClient.NUMERO_PAGINES_RESULTAT_CERCA) {
			
			informacioItemList = getArxiuClient().documentSearch(
					filtres, 
					numPagina, 
					CaibArxiuConversioHelper.toCapsalera());
			result.addAll(informacioItemList);
			numPagina++;
		}
		
		int fromIndex = pagina.intValue() * itemsPerPagina.intValue();
		int toIndex = (pagina.intValue() + 1) * itemsPerPagina.intValue();
		if(toIndex > result.size()) toIndex = result.size();
				
		return new ConsultaResultat(
				itemsPerPagina,
				new Integer(result.size() / itemsPerPagina.intValue()),
				new Integer(result.size()),
				pagina,
				result.size() == 0 ? new ArrayList<InformacioItem>() : result.subList(fromIndex, toIndex));
	}
	
	@Override
	public List<InformacioItem> documentVersions(
			String identificador) throws ArxiuException {
		
		return getArxiuClient().documentVersionList(
				identificador,
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	@Override
	public String documentGenerarCsv() throws ArxiuException {
		
		return getArxiuClient().documentCsvGenerate(
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	@Override
	public String documentCopiar(
			String identificador,
			String identificadorDesti) throws ArxiuException {
		
		return getArxiuClient().documentCopy(
				identificador,
				identificadorDesti,
				CaibArxiuConversioHelper.toCapsalera());
		
	}
	
	@Override
	public void documentMoure(
			String identificador,
			String identificadorDesti) throws ArxiuException {
		
		getArxiuClient().documentMove(
				identificador,
				identificadorDesti,
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	@Override
	public String documentExportarEni(
			String identificador) throws ArxiuException {
		
		return getArxiuClient().documentEniGet(
				identificador,
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	
	/**
	 * =================================================================
	 * ================ M E T O D E S   C A R P E T E S ================
	 * =================================================================
	 */
	
	@Override
	public String carpetaCrear(
			String nom,
			String identificadorPare) throws ArxiuException {
		
		return getArxiuClient().folderCreate(
				identificadorPare,
				nom,
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	@Override
	public InformacioItem carpetaModificar(
			String identificador,
			String nom) throws ArxiuException {
		
		getArxiuClient().folderUpdate(
				identificador,
				nom,
				CaibArxiuConversioHelper.toCapsalera());
		
		return new InformacioItem(
				identificador, 
				nom,
				ContingutTipus.CARPETA);
	}
	
	@Override
	public void carpetaEsborrar(
			String identificador) throws ArxiuException {
		
		getArxiuClient().folderDelete(
				identificador,
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	@Override
	public Carpeta carpetaDetalls(
			String identificador) throws ArxiuException {
		
		return getArxiuClient().folderGet(
				identificador,
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	@Override
	public String carpetaCopiar(
			String identificador,
			String identificadorDesti) throws ArxiuException {
		
		return getArxiuClient().folderCopy(
				identificador,
				identificadorDesti,
				CaibArxiuConversioHelper.toCapsalera());
		
	}
	
	@Override
	public void carpetaMoure(
			String identificador,
			String identificadorDesti) throws ArxiuException {
		
		getArxiuClient().folderMove(
				identificador,
				identificadorDesti,
				CaibArxiuConversioHelper.toCapsalera());
	}
	
	/**
	 * ===============================================================
	 * ================ M E T O D E S   P R I V A T S ================
	 * ===============================================================
	 */
	
	private CaibArxiuClient getArxiuClient() {
		if (arxiuClient == null) {
			arxiuClient = new CaibArxiuClientImpl(
					getBaseUrl(),
					getAplicacioCodi(),
					getUsuari(),
					getContrasenya());
		}
		return arxiuClient;
	}
	

}
