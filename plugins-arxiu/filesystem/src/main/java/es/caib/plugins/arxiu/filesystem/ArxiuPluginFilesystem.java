package es.caib.plugins.arxiu.filesystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.lucene.index.IndexWriter;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ConsultaResultat;
import es.caib.plugins.arxiu.api.ContingutArxiu;
import es.caib.plugins.arxiu.api.ContingutTipus;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentContingut;
import es.caib.plugins.arxiu.api.DocumentEstat;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.IArxiuPlugin;

public class ArxiuPluginFilesystem extends AbstractPluginProperties implements IArxiuPlugin {

	private static final String FILESYSTEM_PROPERTY_BASE = ARXIVE_BASE_PROPERTY + "filesystem.";
	private static final int NUM_CARPETES_CODIFICADES = 256;

	private String base;
	private Boolean emmagatzemamentCodificat;
	private FilesystemArxiuDAO filesystemArxiuDAO;
	private FilesystemArxiuFilesystem filesystemArxiuFilesystem;

	public ArxiuPluginFilesystem() {
		super();
		inicialitzarSistema();
	}
	public ArxiuPluginFilesystem(String propertyKeyBase, Properties properties) {
		super(propertyKeyBase, properties);
		inicialitzarSistema();
	}
	public ArxiuPluginFilesystem(String propertyKeyBase) {
		super(propertyKeyBase);
		inicialitzarSistema();
	}

	@Override
	public ContingutArxiu expedientCrear(
			Expedient expedient) throws ArxiuException {
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor creant un expedient.", e);
		}
		try {
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			String expedientId = getFilesystemArxiuDAO().getIdentificador();
			ExpedientMetadades metadades = expedient.getMetadades();
			String identificador = metadades.getIdentificador();
			if (identificador == null) {
				identificador = this.generarIdentificadorNti();
			}
			ExpedientDao expedientCreat = getFilesystemArxiuDAO().fileCreate(
					w,
					new ExpedientDao(
							expedientId,
							expedient.getNom(),
							null,
							null,
							true,
							identificador,
							metadades.getVersioNti(),
							metadades.getOrgans(),
							metadades.getDataObertura(),
							metadades.getClassificacio(),
							metadades.getEstat(),
							metadades.getInteressats(),
							metadades.getSerieDocumental(),
							null));
			getFilesystemArxiuDAO().closeWriter(w);
			return Utils.crearContingutArxiu(
					expedientCreat.getIdentificador(), 
					expedientCreat.getNom(),
					ContingutTipus.EXPEDIENT,
					expedientCreat.getVersio());
		} catch (Exception ex) {
			throw new ArxiuException("Error creant l'expedient", ex);
		} finally {
			MySemaphore.get().release();
		}
	}

	@Override
	public ContingutArxiu expedientModificar(
			Expedient expedient) throws ArxiuException {
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor modificant un expedient.", e);
		}
		try {
			comprovarExpedient(expedient.getIdentificador());
			ExpedientDao expedientDao = getFilesystemArxiuDAO().fileGet(
					expedient.getIdentificador());
			if (!expedientDao.isObert())
				throw new ArxiuException(
						"L'expedient amb id=" + expedient.getIdentificador() + " no es troba obert");
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			String expedientId = getFilesystemArxiuDAO().getIdentificador();
			VersionResponse versionResponse = getFilesystemArxiuDAO().addVersion(
					w,
					expedientDao.getGestorVersions(),
					expedientId);
			ExpedientMetadades metadades = expedient.getMetadades();
			ExpedientDao expedientUpdate = new ExpedientDao(
					expedientId,
					expedient.getNom(),
					versionResponse.getVersion(),
					expedientDao.getGestorVersions(),
					expedientDao.isObert(),
					metadades.getIdentificador(),
					metadades.getVersioNti(),
					metadades.getOrgans(),
					metadades.getDataObertura(),
					metadades.getClassificacio(),
					metadades.getEstat(),
					metadades.getInteressats(),
					metadades.getSerieDocumental(),
					expedientDao.getContinguts());
			getFilesystemArxiuDAO().fileCreate(w, expedientUpdate);
			ExpedientDao expedientOriginal = getFilesystemArxiuDAO().fileGet(
					expedient.getIdentificador());
			expedientOriginal.setContinguts(null);
			getFilesystemArxiuDAO().fileDelete(w, expedient.getIdentificador());
			getFilesystemArxiuDAO().fileCreate(w, expedientOriginal);
			getFilesystemArxiuDAO().closeWriter(w);
			return Utils.crearContingutArxiu(
					expedientUpdate.getIdentificador(), 
					expedientUpdate.getNom(),
					ContingutTipus.EXPEDIENT,
					expedientUpdate.getVersio());
		} catch (Exception ex) {
			throw new ArxiuException("Error modificant l'expedient", ex);
		} finally {
			MySemaphore.get().release();
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
			comprovarExpedient(identificador);
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			ExpedientDao expedient = getFilesystemArxiuDAO().fileGet(identificador);
			for (ContingutArxiu informacioItem : expedient.getContinguts()) {
				if (informacioItem.getTipus() == ContingutTipus.CARPETA) {
					carpetaEsborrarRecursiu(w, informacioItem.getIdentificador());
				} else if (informacioItem.getTipus() == ContingutTipus.DOCUMENT) {
					documentEsborrarRecursiu(w, informacioItem.getIdentificador());
				}
			}
			getFilesystemArxiuDAO().fileDelete(w, identificador);
			getFilesystemArxiuDAO().closeWriter(w);
		} catch (Exception ex) {
			throw new ArxiuException("Error esborrant l'expedient", ex);
		} finally {
			MySemaphore.get().release();
		}
	}

	@Override
	public Expedient expedientDetalls(
			String identificador,
			String versio) throws ArxiuException {
		comprovarExpedient(identificador);
		if (versio == null) {
			ExpedientDao e = getFilesystemArxiuDAO().fileGet(identificador);
			return e.getExpedient();
		}
		ExpedientDao expedient = getFilesystemArxiuDAO().fileGet(identificador);
		List<String> expedientIds =
				getFilesystemArxiuDAO().versionManagerGetVersions(expedient.getGestorVersions());
		for (String id : expedientIds) {
			ExpedientDao e = getFilesystemArxiuDAO().fileGet(id);
			if (e.getVersio().equals(versio)) {
				return e.getExpedient();
			}
		}
		return null;
	}

	@Override
	public ConsultaResultat expedientConsulta(
			List<ConsultaFiltre> filtres,
			Integer pagina,
			Integer itemsPerPagina) throws ArxiuException {
		List<ContingutArxiu> informacioItems = getFilesystemArxiuDAO().fileSearch(filtres);
		Collections.sort(
				informacioItems,
				new Comparator<ContingutArxiu>(){
					@Override
				     public int compare(ContingutArxiu item1, ContingutArxiu item2){
						int id1 = Integer.parseInt(item1.getIdentificador());
						int id2 = Integer.parseInt(item2.getIdentificador());
				        if(id1 == id2) return 0;
				        return id1 < id2 ? -1 : 1;
				     }
				});
		Integer numRetornat = new Integer(informacioItems.size());
		Integer numPagines = new Integer((numRetornat.intValue()-1) / itemsPerPagina.intValue() + 1);
		if (pagina > numPagines) {
			throw new ArxiuException(
					"La pagina que es vol consultar " + pagina + " es major que el número de pàgines " + numPagines);
		}
		int fromIndex = (pagina.intValue()-1) * itemsPerPagina.intValue();
		int toIndex = pagina.intValue() * itemsPerPagina.intValue();
		if (toIndex > numRetornat) toIndex = numRetornat;
		return new ConsultaResultat(
				toIndex - fromIndex,
				numPagines,
				numRetornat,
				pagina,
				new ArrayList<ContingutArxiu>(informacioItems.subList(fromIndex, toIndex)));
	}

	@Override
	public List<ContingutArxiu> expedientVersions(
			String identificador) throws ArxiuException {
		comprovarExpedient(identificador);
		ExpedientDao expedient = getFilesystemArxiuDAO().fileGet(identificador);
		List<String> fileIds = getFilesystemArxiuDAO().versionManagerGetVersions(expedient.getGestorVersions());
		List<ContingutArxiu> informacioItems =  new ArrayList<ContingutArxiu>();
		for(String id : fileIds) {
			ExpedientDao expedientDao = getFilesystemArxiuDAO().fileGet(id);
			informacioItems.add(
					Utils.crearContingutArxiu(
							expedientDao.getIdentificador(), 
							expedientDao.getNom(),
							ContingutTipus.EXPEDIENT,
							expedientDao.getVersio()));
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
			comprovarExpedient(identificador);
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			getFilesystemArxiuDAO().fileClose(
					w,
					identificador);
			getFilesystemArxiuDAO().closeWriter(w);
			MySemaphore.get().release();
		} catch (Exception ex) {
			throw new ArxiuException("Error al tancar l'expedient", ex);
		} finally {
			MySemaphore.get().release();
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
			comprovarExpedient(identificador);
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			getFilesystemArxiuDAO().fileReopen(w, identificador);
			getFilesystemArxiuDAO().closeWriter(w);
		} catch (Exception ex) {
			throw new ArxiuException("Error reobrint l'expedient", ex);
		} finally {
			MySemaphore.get().release();
		}
	}

	@Override
	public String expedientExportarEni(
			String identificador) throws ArxiuException {
		throw new ArxiuException(
				"El mètode expedientExportarEni no està disponible");
	}

	@Override
	public ContingutArxiu documentCrear(
			Document document,
			String identificadorPare) throws ArxiuException {
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor creant un document.", e);
		}
		try {
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			boolean isDraft = DocumentEstat.ESBORRANY.equals(document.getEstat());
			String documentId = getFilesystemArxiuDAO().getIdentificador();
			DocumentMetadades metadades = document.getMetadades();
			String identificador = metadades.getIdentificador();
			if (identificador == null) {
				identificador = this.generarIdentificadorNti();
			}
			DocumentDao documentCreat = getFilesystemArxiuDAO().documentCreate(
					w, 
					new DocumentDao(
							documentId,
							document.getNom(),
							null,
							null,
							isDraft,
							identificadorPare,
							metadades.getIdentificador(),
							metadades.getVersioNti(),
							metadades.getOrgans(),
							metadades.getDataCaptura(),
							metadades.getOrigen(),
							metadades.getEstatElaboracio(),
							metadades.getTipusDocumental(),
							metadades.getSerieDocumental(),
							document.getFirmes(),
							document.getContingut().getTipusMime(),
							metadades.getIdentificadorOrigen()));
			ContingutArxiu contingutArxiu = Utils.crearContingutArxiu(
					documentId,
					document.getNom(),
					ContingutTipus.DOCUMENT,
					documentCreat.getVersio());
			if (getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificadorPare)) {
				getFilesystemArxiuDAO().fileAddSon(
						w,
						identificadorPare,
						contingutArxiu);
			} else if (getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificadorPare)) {
				getFilesystemArxiuDAO().folderAddSon(
						w,
						identificadorPare,
						contingutArxiu);
			}
			getFilesystemArxiuFilesystem().crearDocument(
					getDocumentPath(documentId),
					document.getContingut().getContingut());	
			getFilesystemArxiuDAO().closeWriter(w);
			return contingutArxiu;
		} catch (Exception ex) {
			throw new ArxiuException("Error creant el document", ex);
		} finally {
			MySemaphore.get().release();
		}
	}

	@Override
	public ContingutArxiu documentModificar(
			Document document,
			final boolean marcarDefinitiu) throws ArxiuException {
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor modificant un document.", e);
		}
		try {
			comprovarDocument(document.getIdentificador());
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
					document.getMetadades().getDataCaptura(),
					document.getMetadades().getOrigen(),
					document.getMetadades().getEstatElaboracio(),
					document.getMetadades().getTipusDocumental(),
					document.getMetadades().getSerieDocumental(),
					document.getFirmes(),
					document.getContingut().getTipusMime(),
					document.getMetadades().getIdentificadorOrigen());
			DocumentDao documentUpated = getFilesystemArxiuDAO().documentCreate(
					w,
					documenttoUpdate);
			ContingutArxiu contingutArxiu = Utils.crearContingutArxiu(
					documentUpated.getIdentificador(),
					documentUpated.getNom(),
					ContingutTipus.DOCUMENT,
					documentUpated.getVersio());
			if (getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, documentUpated.getPare())) {
				getFilesystemArxiuDAO().fileAddSon(
						w,
						documentUpated.getPare(),
						contingutArxiu);
			} else if (getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, documentUpated.getPare())) {
				getFilesystemArxiuDAO().folderAddSon(
						w,
						documentUpated.getPare(),
						contingutArxiu);
			}
			getFilesystemArxiuFilesystem().crearDocument(
					getDocumentPath(documentId),
					document.getContingut().getContingut());
			getFilesystemArxiuDAO().closeWriter(w);
			return contingutArxiu;
		} catch (Exception ex) {
			MySemaphore.get().release();
			throw new ArxiuException("Error modificant el document", ex);
		} finally {
			MySemaphore.get().release();
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
			comprovarDocument(identificador);
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			DocumentDao document = getFilesystemArxiuDAO().documentGet(identificador);
			if (getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, document.getPare())) {
				getFilesystemArxiuDAO().fileDeleteSon(w, document.getPare(), identificador);
			} else if(getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, document.getPare())) {
				getFilesystemArxiuDAO().folderDeleteSon(w, document.getPare(), identificador);
			}
			getFilesystemArxiuDAO().documentDelete(w, identificador);
			getFilesystemArxiuFilesystem().esborrarDocument(
					getDocumentPath(identificador));
			getFilesystemArxiuDAO().closeWriter(w);
		} catch (Exception ex) {
			throw new ArxiuException("Error esborrant el document", ex);
		} finally {
			MySemaphore.get().release();
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
			Document document = new Document();
			document.setIdentificador(d.getIdentificador());
			document.setNom(d.getNom());
			if (ambContingut) {
				DocumentContingut documentContingut = new DocumentContingut();
				documentContingut.setContingut(contingut);
				documentContingut.setTipusMime(d.getTipusMime());
				document.setContingut(documentContingut);
			}
			DocumentMetadades metadades = new DocumentMetadades();
			metadades.setIdentificador(d.getMetadadesid());
			metadades.setVersioNti(d.getVersioNti());
			metadades.setOrigen(d.getOrigen());
			metadades.setOrgans(d.getOrgans());
			metadades.setDataCaptura(d.getData());
			metadades.setEstatElaboracio(d.getEstatElaboracio());
			metadades.setTipusDocumental(d.getTipusDocumental());
			//metadades.setFormat(format);
			//metadades.setExtensio(extensio);
			metadades.setSerieDocumental(d.getSerieDocumental());
			metadades.setIdentificadorOrigen(d.getIdentificadorOrigen());
			document.setMetadades(metadades);
			document.setFirmes(d.getFirmes());
			return document;
		}
		DocumentDao doc = getFilesystemArxiuDAO().documentGet(identificador);
		List<String> documentIds = getFilesystemArxiuDAO().versionManagerGetVersions(doc.getGestorVersions());
		for(String id : documentIds) {
			DocumentDao d = getFilesystemArxiuDAO().documentGet(id);
			if(d.getVersio().equals(versio)) {
				byte[] contingut = getFilesystemArxiuFilesystem().getDocumentContingut(
						getDocumentPath(id));
				Document document = new Document();
				document.setIdentificador(d.getIdentificador());
				document.setNom(d.getNom());
				DocumentContingut documentContingut = new DocumentContingut();
				documentContingut.setContingut(contingut);
				documentContingut.setTipusMime(d.getTipusMime());
				document.setContingut(documentContingut);
				DocumentMetadades metadades = new DocumentMetadades();
				metadades.setIdentificador(d.getMetadadesid());
				metadades.setVersioNti(d.getVersioNti());
				metadades.setOrigen(d.getOrigen());
				metadades.setOrgans(d.getOrgans());
				metadades.setDataCaptura(d.getData());
				metadades.setEstatElaboracio(d.getEstatElaboracio());
				metadades.setTipusDocumental(d.getTipusDocumental());
				//metadades.setFormat(format);
				//metadades.setExtensio(extensio);
				metadades.setSerieDocumental(d.getSerieDocumental());
				metadades.setIdentificadorOrigen(d.getIdentificadorOrigen());
				document.setMetadades(metadades);
				document.setFirmes(d.getFirmes());
				return document;
			}
		}
		return null;
	}

	@Override
	public ConsultaResultat documentConsulta(
			List<ConsultaFiltre> filtres,
			Integer pagina,
			Integer itemsPerPagina) throws ArxiuException {
		List<ContingutArxiu> informacioItems = getFilesystemArxiuDAO().documentSearch(filtres);
		Collections.sort(
				informacioItems,
				new Comparator<ContingutArxiu>(){
					@Override
				     public int compare(ContingutArxiu item1, ContingutArxiu item2){
						int id1 = Integer.parseInt(item1.getIdentificador());
						int id2 = Integer.parseInt(item2.getIdentificador());
				        if(id1 == id2) return 0;
				        return id1 < id2 ? -1 : 1;
				     }
				});
		Integer numRetornat = new Integer(informacioItems.size());
		Integer numPagines = new Integer((numRetornat.intValue()-1) / itemsPerPagina.intValue() + 1);
		if (pagina > numPagines) {
			throw new ArxiuException(
					"La pagina que es vol consultar " + pagina + " es major que el número de pàgines " + numPagines);
		}
		int fromIndex = (pagina.intValue()-1) * itemsPerPagina.intValue();
		int toIndex = pagina.intValue() * itemsPerPagina.intValue();
		if(toIndex > numRetornat) toIndex = numRetornat;
		return new ConsultaResultat(
				itemsPerPagina,
				numPagines,
				numRetornat,
				pagina,
				informacioItems.subList(fromIndex, toIndex));
	}

	@Override
	public List<ContingutArxiu> documentVersions(
			String identificador) throws ArxiuException {
		if(!getFilesystemArxiuDAO().conte(Tables.TABLE_DOCUMENT, identificador))
			throw new ArxiuException(
					"L'identificador " + identificador + " no correspon a un document");
		DocumentDao document = getFilesystemArxiuDAO().documentGet(identificador);
		List<String> documentIds = getFilesystemArxiuDAO().versionManagerGetVersions(document.getGestorVersions());
		List<ContingutArxiu> continguts =  new ArrayList<ContingutArxiu>();
		for(String id : documentIds) {
			DocumentDao d = getFilesystemArxiuDAO().documentGet(id);
			continguts.add(Utils.crearContingutArxiu(
					d.getIdentificador(),
					d.getNom(),
					ContingutTipus.DOCUMENT,
					d.getVersio()));
		}
		return continguts;
	}

	@Override
	public ContingutArxiu documentCopiar(
			String identificador,
			String identificadorDesti) throws ArxiuException {
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor copiant un document.", e);
		}
		try {
			comprovarDocument(identificador);
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
						Utils.crearContingutArxiu(
								documentCreat.getIdentificador(),
								documentCreat.getNom(),
								ContingutTipus.DOCUMENT,
								documentCreat.getVersio()));
			else if(pareEsCarpeta)
				getFilesystemArxiuDAO().folderAddSon(
						w,
						identificadorDesti,
						Utils.crearContingutArxiu(
								documentCreat.getIdentificador(),
								documentCreat.getNom(),
								ContingutTipus.DOCUMENT,
								documentCreat.getVersio()));
			getFilesystemArxiuDAO().closeWriter(w);
			return Utils.crearContingutArxiu(
					documentCreat.getIdentificador(),
					documentCreat.getNom(),
					ContingutTipus.DOCUMENT,
					documentCreat.getVersio());
		} catch (Exception ex) {
			throw new ArxiuException("Error copiant un document", ex);
		} finally {
			MySemaphore.get().release();
		}
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
			comprovarDocument(identificador);
			boolean esDestiExpedient = getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificadorDesti);
			boolean esDestiCarpeta = getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificadorDesti);
			if(!esDestiExpedient && !esDestiCarpeta)
				throw new ArxiuException(
						"L'identificador " + identificadorDesti + " no correspon a un contenidor");
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			DocumentDao document = getFilesystemArxiuDAO().documentGet(identificador);
			if (getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, document.getPare())) {
				getFilesystemArxiuDAO().fileDeleteSon(w, document.getPare(), identificador);
			} else if(getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, document.getPare())) {
				getFilesystemArxiuDAO().folderDeleteSon(w, document.getPare(), identificador);
			}
			if(esDestiExpedient) {
				getFilesystemArxiuDAO().fileAddSon(
						w,
						identificadorDesti,
						Utils.crearContingutArxiu(
								document.getIdentificador(),
								document.getNom(),
								ContingutTipus.DOCUMENT,
								document.getVersio()));
			} else if(esDestiCarpeta) {
				getFilesystemArxiuDAO().folderAddSon(
						w,
						identificadorDesti,
						Utils.crearContingutArxiu(
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
		} catch (Exception ex) {
			throw new ArxiuException("Error movent un document", ex);
		} finally {
			MySemaphore.get().release();
		}
	}

	@Override
	public String documentExportarEni(
			String identificador) throws ArxiuException {
		throw new ArxiuException(
				"La funció get eni no està disponible per a la implementació amb filesystem");
	}

	@Override
	public DocumentContingut documentImprimible(String identificador) throws ArxiuException {
		throw new ArxiuException(
				"La funció de generar document imprimible no està disponible per a la implementació amb filesystem");
	}

	@Override
	public ContingutArxiu carpetaCrear(
			Carpeta carpeta,
			String identificadorPare) throws ArxiuException {
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor creant una carpeta.", e);
		}
		try {
			boolean esPareExpedient = getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificadorPare);
			boolean esPareCarpeta = getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificadorPare);
			if (!esPareExpedient && !esPareCarpeta)
				throw new ArxiuException(
						"L'identificador " + identificadorPare + " no correspon a un contenidor");
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			String carpetaId = getFilesystemArxiuDAO().getIdentificador();
			getFilesystemArxiuDAO().folderCreate(
					w,
					new CarpetaDao(
							carpetaId,
							carpeta.getNom(),
							identificadorPare,
							new ArrayList<ContingutArxiu>()));
			ContingutArxiu contingut = Utils.crearContingutArxiu(
					carpetaId,
					carpeta.getNom(),
					ContingutTipus.CARPETA,
					null);
			if (esPareExpedient) {
				getFilesystemArxiuDAO().fileAddSon(
						w,
						identificadorPare,
						contingut);
			} else if (esPareCarpeta) {
				getFilesystemArxiuDAO().folderAddSon(
						w,
						identificadorPare,
						contingut);
			}
			getFilesystemArxiuDAO().closeWriter(w);
			return contingut;
		} catch (Exception ex) {
			throw new ArxiuException("Error creant una carpeta", ex);
		} finally {
			MySemaphore.get().release();
		}
	}

	@Override
	public ContingutArxiu carpetaModificar(
			Carpeta carpeta) throws ArxiuException {
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor modificant una carpeta.", e);
		}
		try {
			comprovarCarpeta(carpeta.getIdentificador());
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			CarpetaDao c = getFilesystemArxiuDAO().folderGet(carpeta.getIdentificador());
			getFilesystemArxiuDAO().folderDelete(w, c.getIdentificador());
			c.setNom(carpeta.getNom());
			getFilesystemArxiuDAO().folderCreate(w, c);
			ContingutArxiu contingut = Utils.crearContingutArxiu(
					carpeta.getIdentificador(),
					carpeta.getNom(),
					ContingutTipus.CARPETA,
					null);
			if (getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, c.getPare())) {
				getFilesystemArxiuDAO().fileDeleteSon(w, c.getPare(), carpeta.getIdentificador());
				getFilesystemArxiuDAO().fileAddSon(
						w,
						c.getPare(),
						contingut);
			} else if (getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, c.getPare())) {
				getFilesystemArxiuDAO().folderDeleteSon(w, c.getPare(), carpeta.getIdentificador());
				getFilesystemArxiuDAO().folderAddSon(
						w,
						c.getPare(),
						contingut);
			}
			getFilesystemArxiuDAO().closeWriter(w);
			return contingut;
		} catch (Exception ex) {
			MySemaphore.get().release();
			throw new ArxiuException("Error modificant una carpeta", ex);
		} finally {
			MySemaphore.get().release();
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
			comprovarCarpeta(identificador);
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			CarpetaDao carpeta = getFilesystemArxiuDAO().folderGet(identificador);
			if (getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, carpeta.getPare())) {
				getFilesystemArxiuDAO().fileDeleteSon(w, carpeta.getPare(), identificador);
			} else if (getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, carpeta.getPare())) {
				getFilesystemArxiuDAO().folderDeleteSon(w, carpeta.getPare(), identificador);
			}
			for (ContingutArxiu contingut: carpeta.getInformacioItems()) {
				if (contingut.getTipus().equals(ContingutTipus.CARPETA)) {
					carpetaEsborrarRecursiu(w, contingut.getIdentificador());
				} else if (contingut.getTipus().equals(ContingutTipus.DOCUMENT)) {
					documentEsborrarRecursiu(w, contingut.getIdentificador());
				}
			}
			getFilesystemArxiuDAO().folderDelete(w, identificador);
			getFilesystemArxiuDAO().closeWriter(w);
		} catch (Exception ex) {
			throw new ArxiuException("Error esborrant una carpeta", ex);
		} finally {
			MySemaphore.get().release();
		}
	}

	@Override
	public Carpeta carpetaDetalls(
			String identificador) throws ArxiuException {
		comprovarCarpeta(identificador);
		return getFilesystemArxiuDAO().folderGet(identificador).getCarpeta();
	}

	@Override
	public ContingutArxiu carpetaCopiar(
			String identificador,
			String identificadorDesti) throws ArxiuException {
		try {
			MySemaphore.get().acquire();
		} catch (InterruptedException e) {
			throw new ArxiuException("Error al entrar al semàfor copiant una carpeta.", e);
		}
		try {
			comprovarCarpeta(identificador);
			boolean esDestiExpedient = getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificadorDesti);
			boolean esDestiCarpeta = getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificadorDesti);
			if (!esDestiExpedient && !esDestiCarpeta) {
				throw new ArxiuException(
						"L'identificador " + identificadorDesti + " no correspon a un contenidor");
			}
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			String carpetaCopiadaId = getFilesystemArxiuDAO().getIdentificador();
			CarpetaDao carpetaOrigen = getFilesystemArxiuDAO().folderGet(identificador);
			if (esDestiExpedient) {
				getFilesystemArxiuDAO().fileAddSon(
						w,
						identificadorDesti,
						Utils.crearContingutArxiu(
								carpetaCopiadaId,
								carpetaOrigen.getNom(),
								ContingutTipus.CARPETA,
								null));
			} else if (esDestiCarpeta) {
				getFilesystemArxiuDAO().folderAddSon(
						w,
						identificadorDesti,
						Utils.crearContingutArxiu(
								carpetaCopiadaId,
								carpetaOrigen.getNom(),
								ContingutTipus.CARPETA,
								null));
			}
			List<ContingutArxiu> informacioItems = new ArrayList<ContingutArxiu>();
			for(ContingutArxiu informacioItem : carpetaOrigen.getInformacioItems()) {
				if (informacioItem.getTipus().equals(ContingutTipus.CARPETA)) {
					String carpetaFillId = carpetaCopiarRecursiu(
							w,
							informacioItem.getIdentificador(),
							carpetaCopiadaId);
					informacioItems.add(Utils.crearContingutArxiu(
							carpetaFillId,
							informacioItem.getNom(),
							ContingutTipus.CARPETA,
							null));
				} else if (informacioItem.getTipus().equals(ContingutTipus.DOCUMENT)) {
					DocumentDao documentFill = documentCopiarRecursiu(
							w,
							informacioItem.getIdentificador(),
							carpetaCopiadaId);
					informacioItems.add(Utils.crearContingutArxiu(
							documentFill.getIdentificador(),
							informacioItem.getNom(),
							ContingutTipus.DOCUMENT,
							documentFill.getVersio()));
				}
			}
			getFilesystemArxiuDAO().folderCreate(
					w,
					new CarpetaDao(
							carpetaCopiadaId,
							carpetaOrigen.getNom(),
							identificadorDesti,
							informacioItems));
			getFilesystemArxiuDAO().closeWriter(w);
			return Utils.crearContingutArxiu(
					carpetaCopiadaId,
					carpetaOrigen.getNom(),
					ContingutTipus.CARPETA,
					null);
		} catch (Exception ex) {
			throw new ArxiuException("Error copiant una carpeta", ex);
		} finally {
			MySemaphore.get().release();
		}
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
			comprovarCarpeta(identificador);
			boolean esDestiExpedient = getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificadorDesti);
			boolean esDestiCarpeta = getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificadorDesti);
			if(!esDestiExpedient && !esDestiCarpeta)
				throw new ArxiuException(
						"L'identificador " + identificadorDesti + " no correspon a un contenidor");
			CarpetaDao carpeta = getFilesystemArxiuDAO().folderGet(identificador);
			IndexWriter w = getFilesystemArxiuDAO().getWriter();
			if (getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, carpeta.getPare())) {
				getFilesystemArxiuDAO().fileDeleteSon(w, carpeta.getPare(), identificador);
			} else if (getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, carpeta.getPare())) {
				getFilesystemArxiuDAO().folderDeleteSon(w, carpeta.getPare(), identificador);
			}
			if(esDestiExpedient) {
				getFilesystemArxiuDAO().fileAddSon(
						w,
						identificadorDesti,
						Utils.crearContingutArxiu(
								carpeta.getIdentificador(),
								carpeta.getNom(),
								ContingutTipus.CARPETA,
								null));
			} else if(esDestiCarpeta) {
				getFilesystemArxiuDAO().folderAddSon(
						w,
						identificadorDesti,
						Utils.crearContingutArxiu(
								carpeta.getIdentificador(),
								carpeta.getNom(),
								ContingutTipus.CARPETA,
								null));
			}
			getFilesystemArxiuDAO().folderDelete(w, carpeta.getIdentificador());
			getFilesystemArxiuDAO().folderCreate(w, new CarpetaDao(
					carpeta.getIdentificador(),
					carpeta.getNom(),
					identificadorDesti,
					carpeta.getInformacioItems()));
			getFilesystemArxiuDAO().closeWriter(w);
		} catch (Exception ex) {
			throw new ArxiuException("Error movent una carpeta", ex);
		} finally {
			MySemaphore.get().release();
		}
	}

	@Override
	public boolean suportaVersionatExpedient() {
		return true;
	}

	@Override
	public boolean suportaVersionatDocument() {
		return true;
	}

	@Override
	public boolean suportaVersionatCarpeta() {
		return false;
	}

	@Override
	public boolean suportaMetadadesNti() {
		return true;
	}

	@Override
	public boolean generaIdentificadorNti() {
		return true;
	}



	private void inicialitzarSistema() throws ArxiuException {
		if (getFilesystemArxiuDAO().isSistemaInicialitzat()) return;
		IndexWriter w = getFilesystemArxiuDAO().getWriter();
		boolean emmagatzemamentCodificat = isEmmagatzemamentCodificat();
		if (emmagatzemamentCodificat) {
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

	private void comprovarExpedient(String identificador) {
		if (!getFilesystemArxiuDAO().conte(Tables.TABLE_EXPEDIENT, identificador)) {
			throw new ArxiuException(
					"No s'ha trobat cap expedient amb identificador " + identificador);
		}
	}
	private void comprovarDocument(String identificador) {
		if (!getFilesystemArxiuDAO().conte(Tables.TABLE_DOCUMENT, identificador)) {
			throw new ArxiuException(
					"No s'ha trobat cap document amb identificador " + identificador);
		}
	}
	private void comprovarCarpeta(String identificador) {
		if (!getFilesystemArxiuDAO().conte(Tables.TABLE_CARPETA, identificador)) {
			throw new ArxiuException(
					"No s'ha trobat cap carpeta amb identificador " + identificador);
		}
	}

	private void documentEsborrarRecursiu(
			IndexWriter w,
			String identificador) {
		getFilesystemArxiuDAO().documentDelete(w, identificador);
		getFilesystemArxiuFilesystem().esborrarDocument(
				getDocumentPath(identificador));			
	}
	private void carpetaEsborrarRecursiu(
			IndexWriter w,
		String identificador) {
		CarpetaDao carpeta = getFilesystemArxiuDAO().folderGet(identificador);
		for (ContingutArxiu informacioItem : carpeta.getInformacioItems()) {
			if (informacioItem.getTipus().equals(ContingutTipus.CARPETA)) {
				carpetaEsborrarRecursiu(w, informacioItem.getIdentificador());
			} else if(informacioItem.getTipus().equals(ContingutTipus.DOCUMENT)) {
				documentEsborrarRecursiu(w, informacioItem.getIdentificador());
			}
		}
		getFilesystemArxiuDAO().folderDelete(w, identificador);
	}

	private DocumentDao documentCopiarRecursiu(
			IndexWriter w,
			String identificador,
			String identificadorDesti) throws IOException {
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

	private String carpetaCopiarRecursiu(
			IndexWriter w,
			String identificador,
			String identificadorDesti) throws IOException {
		String carpetaId = getFilesystemArxiuDAO().getIdentificador();
		CarpetaDao carpeta = getFilesystemArxiuDAO().folderGet(identificador);
		List<ContingutArxiu> informacioItems = new ArrayList<ContingutArxiu>();
		for(ContingutArxiu informacioItem : carpeta.getInformacioItems()) {
			if(informacioItem.getTipus().equals(ContingutTipus.CARPETA)) {
				String carpetaFillId = carpetaCopiarRecursiu(
						w,
						informacioItem.getIdentificador(),
						carpetaId);
				informacioItems.add(Utils.crearContingutArxiu(
						carpetaFillId,
						informacioItem.getNom(),
						ContingutTipus.CARPETA,
						null));
			} else if(informacioItem.getTipus().equals(ContingutTipus.DOCUMENT)) {
				DocumentDao documentFill = documentCopiarRecursiu(
						w,
						informacioItem.getIdentificador(),
						carpetaId);
				informacioItems.add(Utils.crearContingutArxiu(
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

	public String generarIdentificadorNti() throws Exception {
		UUID uuid =  UUID.randomUUID();
		String uuidHex = uuid.toString().replaceAll("-", "");
		byte[] rnd = new byte[5];
		new Random().nextBytes(rnd);
		uuidHex += new String(Hex.encodeHex(rnd));
		byte[] bytes = Hex.decodeHex(uuidHex.toCharArray());
		String uuidBase64 = "FS" + new String(Base64.encodeBase64(bytes));
		int anyActual = Calendar.getInstance().get(Calendar.YEAR);
		return "ES_" + getPropertyIdentificadorNtiOrgan() + "_" + anyActual + "_" + uuidBase64;
	}

	private String getPropertyBase() {
		return FILESYSTEM_PROPERTY_BASE;
	}

	private String getBasePath() throws ArxiuException {
		if (base != null) {
			return base;
		}
		String aux = getProperty(getPropertyBase() + "base.path");
		if (!FilesystemArxiuFilesystem.esCarpeta(aux)) {
			throw new ArxiuException(
					"La ruta base configurada pel sistema de fitxers no correspon a una carpeta");
		}
		if (aux.charAt(aux.length()-1) == '/' || aux.charAt(aux.length()-1) == '\'') {
			aux = aux.substring(0, aux.length()-1);
		}
		base = aux;
		return base;
	}

	private boolean isEmmagatzemamentCodificat() throws ArxiuException {
		if (emmagatzemamentCodificat != null) {
			return emmagatzemamentCodificat;
		}
		Boolean result = getFilesystemArxiuDAO().isEmmagatzemamentCodificat();
		if (result == null) {
			emmagatzemamentCodificat = "true".equals(
				getProperty(getPropertyBase() + "emmagatzemament.codificat"));
			
			return emmagatzemamentCodificat;
		} else {
			return result.booleanValue();
		}
	}

	private String getPropertyIdentificadorNtiOrgan() {
		return getProperty(getPropertyBase() + "identificador.nti.organ");
	}

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

}