package es.caib.plugins.arxiu.caib;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

import es.caib.plugins.arxiu.api.ArxiveException;
import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ConsultaResultat;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.IArxivePlugin;
import es.caib.plugins.arxiu.api.InformacioItem;

public class ArxiuCaibArxivePlugin extends AbstractPluginProperties implements IArxivePlugin {

	protected final Logger log = Logger.getLogger(getClass());

	public static final String ARXIUCAIB_PROPERTY_BASE = ARXIVE_BASE_PROPERTY + "arxiucaib.";
	
	public ArxiuCaibArxivePlugin() {
		super();
	}
	public ArxiuCaibArxivePlugin(String propertyKeyBase, Properties properties) {
		super(propertyKeyBase, properties);
	}
	public ArxiuCaibArxivePlugin(String propertyKeyBase) {
		super(propertyKeyBase);
	}
	
	// ----------------------------------------------------------------------------
	// ---------------------------- P R O P I E T A T S -------------------------
	// ----------------------------------------------------------------------------

	private String getPropertyBase() {
		return ARXIUCAIB_PROPERTY_BASE;
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
	 * =========================================================================
	 * ================ M E T O  D E S    E X P E D I E N T S ==================
	 * =========================================================================
	 */
	
	@Override
	public String expedientCrear(String nom, ExpedientMetadades metadades) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public InformacioItem expedientModificar(String identificador, String nom, ExpedientMetadades metadades)
			throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void expedientEsborrar(String identificador) throws ArxiveException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Expedient expedientDetalls(String identificador, String versio) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ConsultaResultat expedientConsulta(List<ConsultaFiltre> filtres, Integer pagina, Integer itemsPerPagina)
			throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<InformacioItem> expedientVersions(String identificador) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void expedientTancar(String identificador) throws ArxiveException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void expedientReobrir(String identificador) throws ArxiveException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String expedientExportarEni(String identificador) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * =========================================================================
	 * ================ M E T O  D E S    D O C U M E N T S ====================
	 * =========================================================================
	 */
	
	@Override
	public String documentCrear(Document document, String identificadorPare) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String documentEsborranyCrear(Document document, String identificadorPare) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public InformacioItem documentEstablirDefinitiu(String identificador) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public InformacioItem documentModificar(Document document) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void documentEsborrar(String identificador) throws ArxiveException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Document documentDetalls(String identificador, String versio, boolean ambContingut) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ConsultaResultat documentConsulta(List<ConsultaFiltre> filtres, Integer pagina, Integer itemsPerPagina)
			throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<InformacioItem> documentVersions(String identificador) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String documentGenerarCsv(String identificador) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void documentCopiar(String identificador, String identificadorDesti) throws ArxiveException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void documentMoure(String identificador, String identificadorDesti) throws ArxiveException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String documentExportarEni(String identificador) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * =========================================================================
	 * ================ M E T O  D E S    C A R P E T E S ======================
	 * =========================================================================
	 */
	
	@Override
	public String carpetaCrear(String nom, String identificadorPare) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public InformacioItem carpetaModificar(String identificador, String nom) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void carpetaEsborrar(String identificador) throws ArxiveException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Carpeta carpetaDetalls(String identificador) throws ArxiveException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void carpetaCopiar(String identificador, String identificadorDesti) throws ArxiveException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void carpetaMoure(String identificador, String identificadorDesti) throws ArxiveException {
		// TODO Auto-generated method stub
		
	}

}
