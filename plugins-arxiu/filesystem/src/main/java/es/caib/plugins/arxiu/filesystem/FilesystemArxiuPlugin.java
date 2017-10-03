package es.caib.plugins.arxiu.filesystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ConsultaResultat;
import es.caib.plugins.arxiu.api.ContingutTipus;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentContingut;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.IArxiuPlugin;
import es.caib.plugins.arxiu.api.InformacioItem;
import es.caib.plugins.arxiu.api.Tables;

public class FilesystemArxiuPlugin extends AbstractPluginProperties implements IArxiuPlugin {
	
	
	protected final Logger log = Logger.getLogger(getClass());

	public static final String FILESYSTEM_PROPERTY_BASE = ARXIVE_BASE_PROPERTY + "filesystem.";
	
	private static final int NUM_CARPETES_CODIFICADES = 256;
	
	private String base;
	private Boolean emmagatzemamentCodificat;
	private FilesystemArxiuDAO filesystemArxiuDAO;
	private FilesystemArxiuFilesystem filesystemArxiuFilesystem;
	
	public FilesystemArxiuPlugin() throws ArxiuException {
		super();
		
		inicialitzarSistema();
	}
	public FilesystemArxiuPlugin(
			String propertyKeyBase,
			Properties properties) throws ArxiuException {
		
		super(propertyKeyBase, properties);
		
		inicialitzarSistema();
	}
	public FilesystemArxiuPlugin(
			String propertyKeyBase) throws ArxiuException {
		
		super(propertyKeyBase);
		
		inicialitzarSistema();
	}
	
	
	/**
	 * =====================================================================
	 * ======================== P R O P I E T A T S ========================
	 * =====================================================================
	 */
	
	private String getPropertyBase() {
		return FILESYSTEM_PROPERTY_BASE;
	}
	
	private String getBasePath() throws ArxiuException {
		
		if(base != null) return base;
		
		String aux = getProperty(getPropertyBase() + "base.path");
		
		if(!FilesystemArxiuFilesystem.esCarpeta(aux))
			throw new ArxiuException(
					"La ruta base configurada pel sistema de fitxers no correspon a una carpeta");
		
		if(aux.charAt(aux.length()-1) == '/' || aux.charAt(aux.length()-1) == '\'')
			aux = aux.substring(0, aux.length()-1);
		
		base = aux;
		
		return base;
	}
	private boolean isEmmagatzemamentCodificat() throws ArxiuException {
		
		if(emmagatzemamentCodificat != null) return emmagatzemamentCodificat;
		
		Boolean result = getFilesystemArxiuDAO().isEmmagatzemamentCodificat();
		
		if(result == null) {
			emmagatzemamentCodificat = "true".equals(
				getProperty(getPropertyBase() + "emmagatzemament.codificat"));
			
			return emmagatzemamentCodificat;
		} else 
			return result.booleanValue();
	}
	
	
	/**
	 * ===============================================================
	 * ================ M E T O D E S   P R I V A T S ================
	 * ===============================================================
	 */
	
	private FilesystemArxiuDAO getFilesystemArxiuDAO() throws ArxiuException {
		
		if (filesystemArxiuDAO == null) {
			filesystemArxiuDAO = new FilesystemArxiuDAOImpl(
					getBasePath());
		}
		
		return filesystemArxiuDAO;
	}
	
	private FilesystemArxiuFilesystem getFilesystemArxiuFilesystem() throws ArxiuException {
		
		if (filesystemArxiuFilesystem == null) {
			filesystemArxiuFilesystem = new FilesystemArxiuFilesystem(
					getBasePath());
		}
		
		return filesystemArxiuFilesystem;
	}
	
	
	/**
	 * =====================================================================
	 * ================ M E T O D E S   E X P E D I E N T S ================
	 * =====================================================================
	 */
	
	@Override
	public String expedientCrear(
			String nom,
			ExpedientMetadades metadades) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor creant un expedient.", e);
		}
		
		try {
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			String expedientId = getFilesystemArxiuDAO().getIdentificador();
			
			ExpedientDao expedient = getFilesystemArxiuDAO().fileCreate(
					w,
					new ExpedientDao(
							expedientId,
							nom,
							null,
							null,
							true,
							metadades.getIdentificador(),
							metadades.getVersioNti(),
							metadades.getOrigen(),
							metadades.getOrgans(),
							metadades.getDataObertura(),
							metadades.getClassificacio(),
							metadades.getEstat(),
							metadades.getInteressats(),
							metadades.getSerieDocumental(),
							null));
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
			
			return expedient.getIdentificador();
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error creant un expedient.", e);
		}
	}

	@Override
	public InformacioItem expedientModificar(
			String identificador,
			String nom,
			ExpedientMetadades metadades) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor modificant un expedient.", e);
		}
		
		try {
			if(!getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificador))
				throw new ArxiuException(
						"L'identificador " + identificador + " no correspon a un expedient");
			
			ExpedientDao expedient = getFilesystemArxiuDAO().fileGet(identificador);
			
			if(!expedient.isObert())
				throw new ArxiuException(
						"L'expedient amb id=" + identificador + " no es troba obert");
				
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			String expedientId = getFilesystemArxiuDAO().getIdentificador();
			VersionResponse versionResponse = getFilesystemArxiuDAO().addVersion(
					w,
					expedient.getGestorVersions(),
					expedientId);
			ExpedientDao expedientUpdate = new ExpedientDao(
					expedientId,
					nom,
					versionResponse.getVersion(),
					expedient.getGestorVersions(),
					expedient.isObert(),
					metadades.getIdentificador(),
					metadades.getVersioNti(),
					metadades.getOrigen(),
					metadades.getOrgans(),
					metadades.getDataObertura(),
					metadades.getClassificacio(),
					metadades.getEstat(),
					metadades.getInteressats(),
					metadades.getSerieDocumental(),
					expedient.getContinguts());
			getFilesystemArxiuDAO().fileCreate(w, expedientUpdate);
			
			ExpedientDao expedientOriginal = getFilesystemArxiuDAO().fileGet(identificador);
			expedientOriginal.setContinguts(null);
			getFilesystemArxiuDAO().fileDelete(w, identificador);
			getFilesystemArxiuDAO().fileCreate(w, expedientOriginal);
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
			
			return new InformacioItem(
					expedientId,
					nom,
					ContingutTipus.EXPEDIENT,
					expedientUpdate.getVersio());
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error modificant un expedient.", e);
		}
	}

	@Override
	public void expedientEsborrar(
			String identificador) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor esborrant un expedient.", e);
		}
		
		try {
			if(!getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificador))
				throw new ArxiuException(
						"L'identificador " + identificador + " no correspon a un expedient");
			
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			ExpedientDao expedient = getFilesystemArxiuDAO().fileGet(identificador);
			for(InformacioItem informacioItem : expedient.getContinguts()) {
				if(informacioItem.getTipus() == ContingutTipus.CARPETA) {
					carpetaEsborrarRecursiu(w, informacioItem.getIdentificador());
				} else if(informacioItem.getTipus() == ContingutTipus.DOCUMENT) {
					documentEsborrarRecursiu(w, informacioItem.getIdentificador());
				}
			}
			getFilesystemArxiuDAO().fileDelete(w, identificador);
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error esborrant un expedient.", e);
		}
	}

	@Override
	public Expedient expedientDetalls(
			String identificador,
			String versio) throws ArxiuException {
		
		if(!getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificador))
			throw new ArxiuException(
					"L'identificador " + identificador + " no correspon a un expedient");
		
		if(versio == null) {
			ExpedientDao e = getFilesystemArxiuDAO().fileGet(identificador);
			return e.getExpedient();
		}
		
		ExpedientDao expedient = getFilesystemArxiuDAO().fileGet(identificador);
		List<String> expedientIds =
				getFilesystemArxiuDAO().versionManagerGetVersions(expedient.getGestorVersions());
		
		for(String id : expedientIds) {
			ExpedientDao e = getFilesystemArxiuDAO().fileGet(id);
			if(e.getVersio().equals(versio)) return e.getExpedient();
		}
		
		return null;
	}

	@Override
	public ConsultaResultat expedientConsulta(
			List<ConsultaFiltre> filtres,
			Integer pagina,
			Integer itemsPerPagina) throws ArxiuException {
		
		List<InformacioItem> informacioItems = getFilesystemArxiuDAO().fileSearch(filtres);
		informacioItems.sort(new Comparator<InformacioItem>(){
			@Override
		     public int compare(InformacioItem item1, InformacioItem item2){
				int id1 = Integer.parseInt(item1.getIdentificador());
				int id2 = Integer.parseInt(item2.getIdentificador());
		        if(id1 == id2) return 0;
		        return id1 < id2 ? -1 : 1;
		     }
		});
		
		Integer numRetornat = new Integer(informacioItems.size());
		Integer numPagines = new Integer((numRetornat.intValue()-1) / itemsPerPagina.intValue() + 1);
		
		if(pagina > numPagines)
			throw new ArxiuException(
					"La pagina que es vol consultar " + pagina + " es major que el número de pàgines " + numPagines);
		
		int fromIndex = (pagina.intValue()-1) * itemsPerPagina.intValue();
		int toIndex = pagina.intValue() * itemsPerPagina.intValue();
		if(toIndex > numRetornat) toIndex = numRetornat;
		return new ConsultaResultat(
				toIndex - fromIndex,
				numPagines,
				numRetornat,
				pagina,
				new ArrayList<>(informacioItems.subList(fromIndex, toIndex)));
	}

	@Override
	public List<InformacioItem> expedientVersions(
			String identificador) throws ArxiuException {
		
		if(!getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificador))
			throw new ArxiuException(
					"L'identificador " + identificador + " no correspon a un expedient");
		
		ExpedientDao expedient = getFilesystemArxiuDAO().fileGet(identificador);
		List<String> fileIds = getFilesystemArxiuDAO().versionManagerGetVersions(expedient.getGestorVersions());
		List<InformacioItem> informacioItems =  new ArrayList<InformacioItem>();
		for(String id : fileIds) {
			ExpedientDao e = getFilesystemArxiuDAO().fileGet(id);
			informacioItems.add(new InformacioItem(
					e.getIdentificador(),
					e.getNom(),
					ContingutTipus.EXPEDIENT,
					e.getVersio()));
		}
		
		return informacioItems;
	}

	@Override
	public void expedientTancar(
			String identificador) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor tancant un expedient.", e);
		}
		
		try {
			if(!getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificador))
				throw new ArxiuException(
						"L'identificador " + identificador + " no correspon a un expedient");
			
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			getFilesystemArxiuDAO().fileClose(
					w,
					identificador);
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error al entrar al semàfor tancant un expedient.", e);
		}
	}

	@Override
	public void expedientReobrir(
			String identificador) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor reobrint un expedient.", e);
		}
		
		try {
			if(!getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificador))
				throw new ArxiuException(
						"L'identificador " + identificador + " no correspon a un expedient");
			
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			getFilesystemArxiuDAO().fileReopen(w, identificador);
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error reobrint un expedient.", e);
		}
	}

	@Override
	public String expedientExportarEni(
			String identificador) throws ArxiuException {
		
		throw new ArxiuException(""
				+ "La funció exportar expedient no està disponible per a la implementació amb filesystem");
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
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor creant un document.", e);
		}
		
		try {
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			boolean isDraft = false;
			String documentId = getFilesystemArxiuDAO().getIdentificador();
			DocumentDao documentCreat = getFilesystemArxiuDAO().documentCreate(
					w, 
					new DocumentDao(
							documentId,
							document.getNom(),
							null,
							null,
							isDraft,
							identificadorPare,
							document.getMetadades().getIdentificador(),
							document.getMetadades().getVersioNti(),
							document.getMetadades().getOrgans(),
							document.getMetadades().getData(),
							document.getMetadades().getOrigen(),
							document.getMetadades().getEstatElaboracio(),
							document.getMetadades().getTipusDocumental(),
							document.getMetadades().getSerieDocumental(),
							document.getFirmes(),
							document.getContingut().getTipusMime(),
							document.getContingut().getIdentificadorOrigen()));
			
			if(getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificadorPare)) {
				getFilesystemArxiuDAO().fileAddSon(
						w,
						identificadorPare,
						new InformacioItem(
								documentId,
								document.getNom(),
								ContingutTipus.DOCUMENT,
								documentCreat.getVersio()));
			} else if(getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificadorPare)) {
				getFilesystemArxiuDAO().folderAddSon(
						w,
						identificadorPare,
						new InformacioItem(
								documentId,
								document.getNom(),
								ContingutTipus.DOCUMENT,
								documentCreat.getVersio()));
			}
			
			getFilesystemArxiuFilesystem().crearDocument(
					getDocumentPath(documentId),
					document.getContingut().getContingut());	
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
			
			return documentCreat.getIdentificador();
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error creant un document.", e);
		}
	}

	@Override
	public String documentEsborranyCrear(
			Document document,
			String identificadorPare) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor creant un document esborrany.", e);
		}
		
		try {
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			boolean isDraft = true;
			String documentId = getFilesystemArxiuDAO().getIdentificador();
			DocumentDao documentCreat = getFilesystemArxiuDAO().documentCreate(
					w, 
					new DocumentDao(
							documentId,
							document.getNom(),
							null,
							null,
							isDraft,
							identificadorPare,
							document.getMetadades().getIdentificador(),
							document.getMetadades().getVersioNti(),
							document.getMetadades().getOrgans(),
							document.getMetadades().getData(),
							document.getMetadades().getOrigen(),
							document.getMetadades().getEstatElaboracio(),
							document.getMetadades().getTipusDocumental(),
							document.getMetadades().getSerieDocumental(),
							document.getFirmes(),
							document.getContingut().getTipusMime(),
							document.getContingut().getIdentificadorOrigen()));
			
			if(getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificadorPare)) {
				getFilesystemArxiuDAO().fileAddSon(
						w,
						identificadorPare,
						new InformacioItem(
								documentId,
								document.getNom(),
								ContingutTipus.DOCUMENT,
								documentCreat.getVersio()));
			} else if(getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificadorPare)) {
				getFilesystemArxiuDAO().folderAddSon(
						w,
						identificadorPare,
						new InformacioItem(
								documentId,
								document.getNom(),
								ContingutTipus.DOCUMENT,
								documentCreat.getVersio()));
			}
			
			getFilesystemArxiuFilesystem().crearDocument(
					getDocumentPath(documentId),
					document.getContingut().getContingut());	
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
			
			return documentCreat.getIdentificador();
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error creant un document esborrany.", e);
		}
	}

	@Override
	public InformacioItem documentEstablirDefinitiu(
			String identificador) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor establint com a definitiu un document.", e);
		}
		
		try {
			if(!getFilesystemArxiuDAO().conte(Tables.TABLE_DOCUMENT, identificador))
				throw new ArxiuException(
						"L'identificador " + identificador + " no correspon a un document");
			
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			getFilesystemArxiuDAO().documentFinalSet(w, identificador);
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
			
			DocumentDao document = getFilesystemArxiuDAO().documentGet(identificador);
			return new InformacioItem(
					document.getIdentificador(),
					document.getNom(),
					ContingutTipus.DOCUMENT,
					document.getVersio());
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error establint com a definitiu un document.", e);
		}
	}

	@Override
	public InformacioItem documentModificar(
			Document document) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor modificant un document.", e);
		}
		
		try {
			if(!getFilesystemArxiuDAO().conte(Tables.TABLE_DOCUMENT, document.getIdentificador()))
				throw new ArxiuException(
						"L'identificador " + document.getIdentificador() + " no correspon a un document");
			
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			DocumentDao documentOld = getFilesystemArxiuDAO().documentGet(document.getIdentificador());
			String documentId = getFilesystemArxiuDAO().getIdentificador();
			VersionResponse versionResponse = getFilesystemArxiuDAO().addVersion(
					w,
					documentOld.getGestorVersions(),
					documentId);
			DocumentDao documenttoUpdate = new DocumentDao(
					documentId,
					document.getNom(),
					versionResponse.getVersion(),
					documentOld.getGestorVersions(),
					documentOld.isEsborrany(),
					documentOld.getPare(),
					document.getMetadades().getIdentificador(),
					document.getMetadades().getVersioNti(),
					document.getMetadades().getOrgans(),
					document.getMetadades().getData(),
					document.getMetadades().getOrigen(),
					document.getMetadades().getEstatElaboracio(),
					document.getMetadades().getTipusDocumental(),
					document.getMetadades().getSerieDocumental(),
					document.getFirmes(),
					document.getContingut().getTipusMime(),
					document.getContingut().getIdentificadorOrigen());
			
			DocumentDao documentUpated = getFilesystemArxiuDAO().documentCreate(
					w,
					documenttoUpdate);
			
			InformacioItem itemDocument = new InformacioItem(
					documentUpated.getIdentificador(),
					documentUpated.getNom(),
					ContingutTipus.DOCUMENT,
					documentUpated.getVersio());
			if(getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, documentUpated.getPare()))
				getFilesystemArxiuDAO().fileAddSon(w, documentUpated.getPare(), itemDocument);
			else if(getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, documentUpated.getPare()))
				getFilesystemArxiuDAO().folderAddSon(w, documentUpated.getPare(), itemDocument);
			
			getFilesystemArxiuFilesystem().crearDocument(
					getDocumentPath(documentId),
					document.getContingut().getContingut());
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
			
			return new InformacioItem(
					documentUpated.getIdentificador(),
					documentUpated.getNom(),
					ContingutTipus.DOCUMENT,
					documentUpated.getVersio());
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error modificant un document.", e);
		}
	}

	@Override
	public void documentEsborrar(
			String identificador) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor esborrant un document.", e);
		}
		
		try {
			if(!getFilesystemArxiuDAO().conte(Tables.TABLE_DOCUMENT, identificador))
				throw new ArxiuException(
						"L'identificador " + identificador + " no correspon a un document");
			
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			DocumentDao document = getFilesystemArxiuDAO().documentGet(identificador);
			
			if(getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, document.getPare())) {
				getFilesystemArxiuDAO().fileDeleteSon(w, document.getPare(), identificador);
			} else if(getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, document.getPare())) {
				getFilesystemArxiuDAO().folderDeleteSon(w, document.getPare(), identificador);
			}
			
			getFilesystemArxiuDAO().documentDelete(w, identificador);
			
			getFilesystemArxiuFilesystem().esborrarDocument(
					getDocumentPath(identificador));
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error esborrant un document.", e);
		}
		
	}
	private void documentEsborrarRecursiu(
			IndexWriter w,
			String identificador) throws ArxiuException {
		
		try {
			getFilesystemArxiuDAO().documentDelete(w, identificador);
			
			getFilesystemArxiuFilesystem().esborrarDocument(
					getDocumentPath(identificador));			
		} catch (ArxiuException e) {
			throw new ArxiuException("Error esborrant un document.", e);
		}
		
	}

	@Override
	public Document documentDetalls(
			String identificador,
			String versio,
			boolean ambContingut) throws ArxiuException {
		
		if(!getFilesystemArxiuDAO().conte(Tables.TABLE_DOCUMENT, identificador))
			throw new ArxiuException(
					"L'identificador " + identificador + " no correspon a un document");
		
		if(versio == null) {
			DocumentDao d = getFilesystemArxiuDAO().documentGet(identificador);
			byte[] contingut = getFilesystemArxiuFilesystem().getDocumentContingut(
					getDocumentPath(identificador));
			DocumentContingut documentContingut = new DocumentContingut(
					contingut,
					d.getTipusMime(),
					d.getIdentificadorOrigen());
			return new Document(
					d.getIdentificador(),
					d.getNom(),
					ambContingut ? documentContingut : null,
					new DocumentMetadades(
							d.getMetadadesid(),
							d.getVersioNti(),
							d.getOrgans(),
							d.getData(),
							d.getOrigen(),
							d.getEstatElaboracio(),
							d.getTipusDocumental(),
							d.getSerieDocumental(),
							null,
							null,
							null),
					d.getFirmes());
		}
			
		DocumentDao document = getFilesystemArxiuDAO().documentGet(identificador);
		List<String> documentIds = getFilesystemArxiuDAO().versionManagerGetVersions(document.getGestorVersions());
		for(String id : documentIds) {
			DocumentDao d = getFilesystemArxiuDAO().documentGet(id);
			if(d.getVersio().equals(versio)) {
				byte[] contingut = getFilesystemArxiuFilesystem().getDocumentContingut(
						getDocumentPath(id));
				return new Document(
						d.getIdentificador(),
						d.getNom(),
						new DocumentContingut(
								contingut,
								d.getTipusMime(),
								d.getIdentificadorOrigen()),
						new DocumentMetadades(
								d.getMetadadesid(),
								d.getVersioNti(),
								d.getOrgans(),
								d.getData(),
								d.getOrigen(),
								d.getEstatElaboracio(),
								d.getTipusDocumental(),
								d.getSerieDocumental(),
								null,
								null,
								null),
						d.getFirmes());
			}
		}
		
		return null;
	}
	
	@Override
	public ConsultaResultat documentConsulta(
			List<ConsultaFiltre> filtres,
			Integer pagina,
			Integer itemsPerPagina) throws ArxiuException {
		
		List<InformacioItem> informacioItems = getFilesystemArxiuDAO().documentSearch(filtres);
		informacioItems.sort(new Comparator<InformacioItem>(){
			@Override
		     public int compare(InformacioItem item1, InformacioItem item2){
				int id1 = Integer.parseInt(item1.getIdentificador());
				int id2 = Integer.parseInt(item2.getIdentificador());
		        if(id1 == id2) return 0;
		        return id1 < id2 ? -1 : 1;
		     }
		});
		
		Integer numRetornat = new Integer(informacioItems.size());
		Integer numPagines = new Integer((numRetornat.intValue()-1) / itemsPerPagina.intValue() + 1);
		
		if(pagina > numPagines)
			throw new ArxiuException(
					"La pagina que es vol consultar " + pagina + " es major que el número de pàgines " + numPagines);
		
		int fromIndex = (pagina.intValue()-1) * itemsPerPagina.intValue();
		int toIndex = pagina.intValue() * itemsPerPagina.intValue();
		if(toIndex > numRetornat) toIndex = numRetornat;
		return new ConsultaResultat(
				itemsPerPagina,
				numPagines,
				numRetornat,
				pagina,
				new ArrayList<>(informacioItems.subList(fromIndex, toIndex)));
	}

	@Override
	public List<InformacioItem> documentVersions(
			String identificador) throws ArxiuException {
		
		if(!getFilesystemArxiuDAO().conte(Tables.TABLE_DOCUMENT, identificador))
			throw new ArxiuException(
					"L'identificador " + identificador + " no correspon a un document");
		
		DocumentDao document = getFilesystemArxiuDAO().documentGet(identificador);
		
		List<String> documentIds = getFilesystemArxiuDAO().versionManagerGetVersions(document.getGestorVersions());
		List<InformacioItem> informacioItems =  new ArrayList<InformacioItem>();
		for(String id : documentIds) {
			DocumentDao d = getFilesystemArxiuDAO().documentGet(id);
			informacioItems.add(new InformacioItem(
					d.getIdentificador(),
					d.getNom(),
					ContingutTipus.DOCUMENT,
					d.getVersio()));
		}
		
		return informacioItems;
	}

	@Override
	public String documentGenerarCsv() throws ArxiuException {
		
		throw new ArxiuException(
				"La funció generar csv no està disponible per a la implementació amb filesystem");
	}
	
	@Override
	public String documentCopiar(
			String identificador,
			String identificadorDesti) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor copiant un document.", e);
		}
		
		try {
			if(!getFilesystemArxiuDAO().conte(Tables.TABLE_DOCUMENT, identificador))
				throw new ArxiuException(
						"L'identificador " + identificador + " no correspon a un document");
			boolean pareEsExpedient = getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificadorDesti);
			boolean pareEsCarpeta = getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificadorDesti);
			if(!pareEsExpedient && !pareEsCarpeta)
				throw new ArxiuException(
						"L'identificador " + identificadorDesti + " no correspon a un contenidor");
			
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			DocumentDao documentCreat = documentCopiarRecursiu(
					w,
					identificador,
					identificadorDesti);
			
			if(pareEsExpedient)
				getFilesystemArxiuDAO().fileAddSon(
						w,
						identificadorDesti,
						new InformacioItem(
								documentCreat.getIdentificador(),
								documentCreat.getNom(),
								ContingutTipus.DOCUMENT,
								documentCreat.getVersio()));
			else if(pareEsCarpeta)
				getFilesystemArxiuDAO().folderAddSon(
						w,
						identificadorDesti,
						new InformacioItem(
								documentCreat.getIdentificador(),
								documentCreat.getNom(),
								ContingutTipus.DOCUMENT,
								documentCreat.getVersio()));
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
			
			return documentCreat.getIdentificador();
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error copiant un document.", e);
		}
	}
	private DocumentDao documentCopiarRecursiu(
			IndexWriter w,
			String identificador,
			String identificadorDesti) throws ArxiuException {
				
		DocumentDao document = getFilesystemArxiuDAO().documentGet(identificador);
		
		String documentId = getFilesystemArxiuDAO().getIdentificador();
		DocumentDao documentCreat = getFilesystemArxiuDAO().documentCreate(
				w,
				new DocumentDao(
						documentId,
						document.getNom(),
						null,
						null,
						document.isEsborrany(),
						identificadorDesti,
						document.getMetadadesid(),
						document.getVersioNti(),
						document.getOrgans(),
						document.getData(),
						document.getOrigen(),
						document.getEstatElaboracio(),
						document.getTipusDocumental(),
						document.getSerieDocumental(),
						document.getFirmes(),
						document.getTipusMime(),
						document.getIdentificador()));
		
		String pathDocOriginal = getDocumentPath(identificador);
		String pathDocCopiat = getDocumentPath(documentId);
		byte[] contingut = getFilesystemArxiuFilesystem().getDocumentContingut(pathDocOriginal);
		getFilesystemArxiuFilesystem().crearDocument(pathDocCopiat, contingut);
		
		return documentCreat;
	}

	@Override
	public void documentMoure(
			String identificador,
			String identificadorDesti) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor movent un document.", e);
		}
		
		try {
			if(!getFilesystemArxiuDAO().conte(Tables.TABLE_DOCUMENT, identificador))
				throw new ArxiuException(
						"L'identificador " + identificador + " no correspon a un document");
			
			boolean esDestiExpedient = getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificadorDesti);
			boolean esDestiCarpeta = getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificadorDesti);
			if(!esDestiExpedient && !esDestiCarpeta)
				throw new ArxiuException(
						"L'identificador " + identificadorDesti + " no correspon a un contenidor");
			
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			DocumentDao document = getFilesystemArxiuDAO().documentGet(identificador);
			
			if(getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, document.getPare())) {
				getFilesystemArxiuDAO().fileDeleteSon(w, document.getPare(), identificador);
			} else if(getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, document.getPare())) {
				getFilesystemArxiuDAO().folderDeleteSon(w, document.getPare(), identificador);
			}
			if(esDestiExpedient) {
				getFilesystemArxiuDAO().fileAddSon(
						w,
						identificadorDesti,
						new InformacioItem(
								document.getIdentificador(),
								document.getNom(),
								ContingutTipus.DOCUMENT,
								document.getVersio()));
			} else if(esDestiCarpeta) {
				getFilesystemArxiuDAO().folderAddSon(
						w,
						identificadorDesti,
						new InformacioItem(
								document.getIdentificador(),
								document.getNom(),
								ContingutTipus.DOCUMENT,
								document.getVersio()));
			}
			
			getFilesystemArxiuDAO().documentDelete(w, identificador);
			getFilesystemArxiuDAO().documentCreate(
					w, 
					new DocumentDao(
							document.getIdentificador(),
							document.getNom(),
							document.getVersio(),
							document.getGestorVersions(),
							document.isEsborrany(),
							identificadorDesti,
							document.getMetadadesid(),
							document.getVersioNti(),
							document.getOrgans(),
							document.getData(),
							document.getOrigen(),
							document.getEstatElaboracio(),
							document.getTipusDocumental(),
							document.getSerieDocumental(),
							document.getFirmes(),
							document.getTipusMime(),
							document.getIdentificadorOrigen()));
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error movent un document.", e);
		}
	}

	@Override
	public String documentExportarEni(
			String identificador) throws ArxiuException {
		
		throw new ArxiuException(
				"La funció get eni no està disponible per a la implementació amb filesystem");
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
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor creant una carpeta.", e);
		}
		
		try {
			boolean esPareExpedient = getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificadorPare);
			boolean esPareCarpeta = getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificadorPare);
			if(!esPareExpedient && !esPareCarpeta)
				throw new ArxiuException(
						"L'identificador " + identificadorPare + " no correspon a un contenidor");
					
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			String carpetaId = getFilesystemArxiuDAO().getIdentificador();
			
			String carpetaCreadaId = getFilesystemArxiuDAO().folderCreate(
					w,
					new CarpetaDao(
							carpetaId,
							nom,
							identificadorPare,
							new ArrayList<InformacioItem>()));
			
			if(esPareExpedient) {
				getFilesystemArxiuDAO().fileAddSon(
						w,
						identificadorPare,
						new InformacioItem(
								carpetaId,
								nom,
								ContingutTipus.CARPETA));
			} else if(esPareCarpeta) {
				getFilesystemArxiuDAO().folderAddSon(
						w,
						identificadorPare,
						new InformacioItem(
								carpetaId,
								nom,
								ContingutTipus.CARPETA));
			}
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
			
			return carpetaCreadaId;
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error creant una carpeta.", e);
		}
	}

	@Override
	public InformacioItem carpetaModificar(
			String identificador,
			String nom) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor modificant una carpeta.", e);
		}
		
		try {
			if(!getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificador))
				throw new ArxiuException(
						"L'identificador " + identificador + " no correspon a un carpeta");
			
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			CarpetaDao carpeta = getFilesystemArxiuDAO().folderGet(identificador);
			getFilesystemArxiuDAO().folderDelete(w, carpeta.getIdentificador());
			carpeta.setNom(nom);
			getFilesystemArxiuDAO().folderCreate(w, carpeta);
			
			if(getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, carpeta.getPare())) {
				getFilesystemArxiuDAO().fileDeleteSon(w, carpeta.getPare(), identificador);
				getFilesystemArxiuDAO().fileAddSon(
						w,
						carpeta.getPare(),
						new InformacioItem(
								identificador,
								nom,
								ContingutTipus.CARPETA));
			} else if(getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, carpeta.getPare())) {
				getFilesystemArxiuDAO().folderDeleteSon(w, carpeta.getPare(), identificador);
				getFilesystemArxiuDAO().folderAddSon(
						w,
						carpeta.getPare(),
						new InformacioItem(
								identificador,
								nom,
								ContingutTipus.CARPETA));
			}
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
			
			return new InformacioItem(
					identificador,
					nom,
					ContingutTipus.CARPETA);
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error modificant una carpeta.", e);
		}
	}

	@Override
	public void carpetaEsborrar(
			String identificador) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor esborrant una carpeta.", e);
		}
		
		try {
			if(!getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificador))
				throw new ArxiuException(
						"L'identificador " + identificador + " no correspon a un carpeta");
			
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			CarpetaDao carpeta = getFilesystemArxiuDAO().folderGet(identificador);
			
			if(getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, carpeta.getPare())) {
				getFilesystemArxiuDAO().fileDeleteSon(w, carpeta.getPare(), identificador);
			} else if(getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, carpeta.getPare())) {
				getFilesystemArxiuDAO().folderDeleteSon(w, carpeta.getPare(), identificador);
			}
			
			for(InformacioItem informacioItem : carpeta.getInformacioItems()) {
				if(informacioItem.getTipus() == ContingutTipus.CARPETA) {
					carpetaEsborrarRecursiu(w, informacioItem.getIdentificador());
				} else if(informacioItem.getTipus() == ContingutTipus.DOCUMENT) {
					documentEsborrarRecursiu(w, informacioItem.getIdentificador());
				}
			}
			
			getFilesystemArxiuDAO().folderDelete(w, identificador);
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error esborrant una carpeta.", e);
		}
	}
	private void carpetaEsborrarRecursiu(
			IndexWriter w,
			String identificador) throws ArxiuException {
		
		try {			
			CarpetaDao carpeta = getFilesystemArxiuDAO().folderGet(identificador);
			
			for(InformacioItem informacioItem : carpeta.getInformacioItems()) {
				if(informacioItem.getTipus() == ContingutTipus.CARPETA) {
					carpetaEsborrarRecursiu(w, informacioItem.getIdentificador());
				} else if(informacioItem.getTipus() == ContingutTipus.DOCUMENT) {
					documentEsborrarRecursiu(w, informacioItem.getIdentificador());
				}
			}
			
			getFilesystemArxiuDAO().folderDelete(w, identificador);
			
		} catch (ArxiuException e) {
			throw new ArxiuException("Error esborrant una carpeta.", e);
		}
	}

	@Override
	public Carpeta carpetaDetalls(
			String identificador) throws ArxiuException {
		
		if(!getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificador))
			throw new ArxiuException(
					"L'identificador " + identificador + " no correspon a un carpeta");
		
		return getFilesystemArxiuDAO().folderGet(identificador).getCarpeta();
	}
	
	@Override
	public String carpetaCopiar(
			String identificador,
			String identificadorDesti) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor copiant una carpeta.", e);
		}
		
		try {
			if(!getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificador))
				throw new ArxiuException(
						"L'identificador " + identificador + " no correspon a un carpeta");
			
			boolean esDestiExpedient = getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificadorDesti);
			boolean esDestiCarpeta = getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificadorDesti);
			if(!esDestiExpedient && !esDestiCarpeta)
				throw new ArxiuException(
						"L'identificador " + identificadorDesti + " no correspon a un contenidor");
			
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			String carpetaCopiadaId = getFilesystemArxiuDAO().getIdentificador();
			CarpetaDao carpetaOrigen = getFilesystemArxiuDAO().folderGet(identificador);
			
			if(esDestiExpedient) {
				getFilesystemArxiuDAO().fileAddSon(
						w,
						identificadorDesti,
						new InformacioItem(
								carpetaCopiadaId,
								carpetaOrigen.getNom(),
								ContingutTipus.CARPETA));
			} else if(esDestiCarpeta) {
				getFilesystemArxiuDAO().folderAddSon(
						w,
						identificadorDesti,
						new InformacioItem(
								carpetaCopiadaId,
								carpetaOrigen.getNom(),
								ContingutTipus.CARPETA));
			}
			
			List<InformacioItem> informacioItems = new ArrayList<InformacioItem>();
			for(InformacioItem informacioItem : carpetaOrigen.getInformacioItems()) {
				if(informacioItem.getTipus() == ContingutTipus.CARPETA) {
					String carpetaFillId = carpetaCopiarRecursiu(
							w,
							informacioItem.getIdentificador(),
							carpetaCopiadaId);
					informacioItems.add(new InformacioItem(
							carpetaFillId,
							informacioItem.getNom(),
							ContingutTipus.CARPETA));
				} else if(informacioItem.getTipus() == ContingutTipus.DOCUMENT) {
					DocumentDao documentFill = documentCopiarRecursiu(
							w,
							informacioItem.getIdentificador(),
							carpetaCopiadaId);
					informacioItems.add(new InformacioItem(
							documentFill.getIdentificador(),
							informacioItem.getNom(),
							ContingutTipus.DOCUMENT,
							documentFill.getVersio()));
				}
			}
			
			String carpetaCreadaId = getFilesystemArxiuDAO().folderCreate(
					w,
					new CarpetaDao(
							carpetaCopiadaId,
							carpetaOrigen.getNom(),
							identificadorDesti,
							informacioItems));
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
			
			return carpetaCreadaId;
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error copiant una carpeta.", e);
		}
	}
	private String carpetaCopiarRecursiu(
			IndexWriter w,
			String identificador,
			String identificadorDesti) throws ArxiuException {
		
		String carpetaId = getFilesystemArxiuDAO().getIdentificador();
		CarpetaDao carpeta = getFilesystemArxiuDAO().folderGet(identificador);
		
		List<InformacioItem> informacioItems = new ArrayList<InformacioItem>();
		for(InformacioItem informacioItem : carpeta.getInformacioItems()) {
			if(informacioItem.getTipus() == ContingutTipus.CARPETA) {
				String carpetaFillId = carpetaCopiarRecursiu(
						w,
						informacioItem.getIdentificador(),
						carpetaId);
				informacioItems.add(new InformacioItem(
						carpetaFillId,
						informacioItem.getNom(),
						ContingutTipus.CARPETA));
			} else if(informacioItem.getTipus() == ContingutTipus.DOCUMENT) {
				DocumentDao documentFill = documentCopiarRecursiu(
						w,
						informacioItem.getIdentificador(),
						carpetaId);
				informacioItems.add(new InformacioItem(
						documentFill.getIdentificador(),
						informacioItem.getNom(),
						ContingutTipus.DOCUMENT,
						documentFill.getVersio()));
			}
		}
		
		String carpetaCreadaId = getFilesystemArxiuDAO().folderCreate(
				w, 
				new CarpetaDao(
						carpetaId,
						carpeta.getNom(),
						identificadorDesti,
						informacioItems));
		
		return carpetaCreadaId;
	}

	@Override
	public void carpetaMoure(
			String identificador,
			String identificadorDesti) throws ArxiuException {
		
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor movent una carpeta.", e);
		}
		
		try {
			if(!getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificador))
				throw new ArxiuException(
						"L'identificador " + identificador + " no correspon a un carpeta");
			
			boolean esDestiExpedient = getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificadorDesti);
			boolean esDestiCarpeta = getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificadorDesti);
			if(!esDestiExpedient && !esDestiCarpeta)
				throw new ArxiuException(
						"L'identificador " + identificadorDesti + " no correspon a un contenidor");
			
			CarpetaDao carpeta = getFilesystemArxiuDAO().folderGet(identificador);
			
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			
			if(getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, carpeta.getPare())) {
				getFilesystemArxiuDAO().fileDeleteSon(w, carpeta.getPare(), identificador);
			} else if(getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, carpeta.getPare())) {
				getFilesystemArxiuDAO().folderDeleteSon(w, carpeta.getPare(), identificador);
			}
			if(esDestiExpedient) {
				getFilesystemArxiuDAO().fileAddSon(
						w,
						identificadorDesti,
						new InformacioItem(
								carpeta.getIdentificador(),
								carpeta.getNom(),
								ContingutTipus.CARPETA));
			} else if(esDestiCarpeta) {
				getFilesystemArxiuDAO().folderAddSon(
						w,
						identificadorDesti,
						new InformacioItem(
								carpeta.getIdentificador(),
								carpeta.getNom(),
								ContingutTipus.CARPETA));
			}
			
			getFilesystemArxiuDAO().folderDelete(w, carpeta.getIdentificador());
			getFilesystemArxiuDAO().folderCreate(w, new CarpetaDao(
					carpeta.getIdentificador(),
					carpeta.getNom(),
					identificadorDesti,
					carpeta.getInformacioItems()));
			
			getFilesystemArxiuDAO().closeWriter(w);
			
			MySemaphore.get().release();
		} catch (ArxiuException e) {
			MySemaphore.get().release();
			throw new ArxiuException("Error movent una carpeta.", e);
		}
	}
	
	
	/**
	 * ===============================================================
	 * ================ M E T O D E S   P R I V A T S ================
	 * ===============================================================
	 */
	
	private void inicialitzarSistema() throws ArxiuException {
		
		if(getFilesystemArxiuDAO().isSistemaInicialitzat()) return;
		
		IndexWriter w = getFilesystemArxiuDAO().getWriter();
		
		boolean emmagatzemamentCodificat = isEmmagatzemamentCodificat();
		
		if(emmagatzemamentCodificat) {
			for(int i = 0; i < NUM_CARPETES_CODIFICADES; i++) {
				String nom = Integer.toString(i);
				getFilesystemArxiuFilesystem().crearCarpeta("/" + nom);
			}
		};
		
		getFilesystemArxiuDAO().guardarMetainformacioFilesystem(
				w,
				emmagatzemamentCodificat);
		
		getFilesystemArxiuDAO().closeWriter(w);
	}
	
	private String getDocumentPath(
			String documentId) throws ArxiuException {
		
		if(isEmmagatzemamentCodificat())
			return "/" + String.valueOf(Integer.parseInt(documentId) % NUM_CARPETES_CODIFICADES) +
					   "/" + documentId;
		else
			return "/" + documentId;
	}
	
}