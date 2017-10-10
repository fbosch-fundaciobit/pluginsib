/**
 * 
 */
package es.caib.plugins.arxiu.caib;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.DocumentId;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.DocumentNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.FileNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.VersionNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateDraftDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateFile;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateFolder;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamGetDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamNodeID_TargetParent;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamNodeId;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamSearch;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamSetDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamSetFile;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamSetFolder;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CloseFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CopyDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CopyFolderResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateDraftDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateFolderResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.ExportFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GenerateDocCSVResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetDocVersionListResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetENIDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetFileVersionListResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetFolderResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.MoveDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.MoveFolderResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.RemoveDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.RemoveFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.RemoveFolderResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.ReopenFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.SearchDocsResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.SearchFilesResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.SetDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.SetFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.SetFinalDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.SetFolderResult;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CloseFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CopyDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CopyFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateDraftDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.ExportFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GenerateDocCSV;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetDocVersionList;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetENIDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetFileVersionList;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.MoveDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.MoveFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.RemoveDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.RemoveFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.RemoveFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.ReopenFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SearchDocs;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SearchFiles;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetFinalDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetFolder;
import es.caib.arxiudigital.apirest.constantes.MetadatosDocumento;
import es.caib.arxiudigital.apirest.constantes.Servicios;
import es.caib.plugins.arxiu.api.ArxiuConstants;
import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.ArxiuValidacioException;
import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ConsultaResultat;
import es.caib.plugins.arxiu.api.ContingutArxiu;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentContingut;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.IArxiuPlugin;
import es.caib.plugins.arxiu.caib.ArxiuCaibClient.GeneradorParam;

/**
 * Implementació de l'API genèrica de l'arxiu per a accedir
 * a l'arxiu de la CAIB mitjançant l'API REST.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuPluginCaib extends AbstractPluginProperties implements IArxiuPlugin {

	public static final String ARXIUCAIB_PROPERTY_BASE = ARXIVE_BASE_PROPERTY + "caib.";
	private static final int NUM_PAGINES_RESULTAT_CERCA = 100;

	private ArxiuCaibClient arxiuClient;



	public ArxiuPluginCaib() {
		super();
	}
	public ArxiuPluginCaib(String propertyKeyBase, Properties properties) {
		super(propertyKeyBase, properties);
	}
	public ArxiuPluginCaib(String propertyKeyBase) {
		super(propertyKeyBase);
	}

	@Override
	public ContingutArxiu expedientCrear(
			final Expedient expedient) throws ArxiuException {
		String metode = Servicios.CREATE_FILE;
		try {
			CreateFileResult resposta = getArxiuClient().generarEnviarPeticio(
					metode,
					CreateFile.class,
					new GeneradorParam<ParamCreateFile>() {
						@Override
						public ParamCreateFile generar() {
							ParamCreateFile param = new ParamCreateFile();
							param.setFile(
									ArxiuConversioHelper.expedientToFileNode(
											expedient,
											null,
											null,
											null,
											true));
							param.setRetrieveNode(Boolean.TRUE.toString());
							return param;
						}
					},
					ParamCreateFile.class,
					CreateFileResult.class);
			Expedient expedientCreat = ArxiuConversioHelper.fileNodeToExpedient(
					resposta.getCreateFileResult().getResParam());
			return crearContingutArxiu(
					expedientCreat.getIdentificador(), 
					expedientCreat.getNom(),
					ArxiuConstants.CONTINGUT_TIPUS_EXPEDIENT,
					"1.0");
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode + ": " + ex.getMessage(),
					ex);
		}
	}

	@Override
	public ContingutArxiu expedientModificar(
			final Expedient expedient) throws ArxiuException {
		String metode = null;
		try {
			/*metode = Servicios.GET_FILE;
			final GetFileResult resposta = getArxiuClient().generarEnviarPeticio(
					metode,
					GetFile.class,
					new GeneradorParam<ParamNodeId>() {
						@Override
						public ParamNodeId generar() {
							ParamNodeId param = new ParamNodeId();
							param.setNodeId(expedient.getIdentificador());
							return param;
						}
					},
					ParamNodeId.class,
					GetFileResult.class);*/
			metode = Servicios.SET_FILE;
			getArxiuClient().generarEnviarPeticio(
					metode,
					SetFile.class,
					new GeneradorParam<ParamSetFile>() {
						@Override
						public ParamSetFile generar() {
							ParamSetFile param = new ParamSetFile();
							param.setFile(
									ArxiuConversioHelper.expedientToFileNode(
											expedient,
											null, //resposta.getGetFileResult().getResParam().getMetadataCollection(),
											null, //resposta.getGetFileResult().getResParam().getAspects(),
											null,
											false));
							return param;
						}
					},
					ParamSetFile.class,
					SetFileResult.class);
			String versio = null;
			List<ContingutArxiu> versions = expedientVersionsComu(
					expedient.getIdentificador());
			if (versions != null && !versions.isEmpty()) {
				versio = versions.get(versions.size() - 1).getVersio();
			}
			return crearContingutArxiu(
					expedient.getIdentificador(), 
					expedient.getNom(),
					ArxiuConstants.CONTINGUT_TIPUS_EXPEDIENT,
					versio);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void expedientEsborrar(
			final String identificador) throws ArxiuException {
		String metode = Servicios.REMOVE_FILE;
		try {
			getArxiuClient().generarEnviarPeticio(
					metode,
					RemoveFile.class,
					new GeneradorParam<ParamNodeId>() {
						@Override
						public ParamNodeId generar() {
							ParamNodeId param = new ParamNodeId();
							param.setNodeId(identificador);
							return param;
						}
					},
					ParamNodeId.class,
					RemoveFileResult.class);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public Expedient expedientDetalls(
			final String identificador,
			final String versio) throws ArxiuException {
		String metode = Servicios.GET_FILE;
		try {
			GetFileResult resposta = getArxiuClient().generarEnviarPeticio(
					metode,
					GetFile.class,
					new GeneradorParam<ParamNodeId>() {
						@Override
						public ParamNodeId generar() {
							ParamNodeId param = new ParamNodeId();
							String identificadorAmbVersio;
							if (versio != null) {
								identificadorAmbVersio = versio + "@" + identificador;
							} else {
								identificadorAmbVersio = identificador;
							}
							param.setNodeId(identificadorAmbVersio);
							return param;
						}
					},
					ParamNodeId.class,
					GetFileResult.class);
			return ArxiuConversioHelper.fileNodeToExpedient(
					resposta.getGetFileResult().getResParam());
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public ConsultaResultat expedientConsulta(
			final List<ConsultaFiltre> filtres,
			final Integer pagina,
			final Integer itemsPerPagina) throws ArxiuException {
		String metode = Servicios.SEARCH_FILE;
		try {
			List<ContingutArxiu> resultatConsulta = new ArrayList<ContingutArxiu>();
			List<ContingutArxiu> continguts = null;
			while (continguts == null || continguts.size() == NUM_PAGINES_RESULTAT_CERCA) {
				SearchFilesResult resposta = getArxiuClient().generarEnviarPeticio(
						metode,
						SearchFiles.class,
						new GeneradorParam<ParamSearch>() {
							@Override
							public ParamSearch generar() {
								ParamSearch param = new ParamSearch();
								String query = ArxiuConsultaHelper.generarConsulta(
										"\"eni:expediente\" ",
										filtres);
								param.setQuery(query);
								param.setPageNumber(pagina);
								return param;
							}
						},
						ParamSearch.class,
						SearchFilesResult.class);
				List<FileNode> files = new ArrayList<FileNode>();
				if (	resposta.getSearchFilesResult().getResParam() != null &&
						resposta.getSearchFilesResult().getResParam().getFiles() != null) {
					files = resposta.getSearchFilesResult().getResParam().getFiles();
				}
				continguts = ArxiuConversioHelper.fileNodesToFileContingutArxiu(files);
				resultatConsulta.addAll(continguts);
			}
			int fromIndex = pagina.intValue() * itemsPerPagina.intValue();
			int toIndex = (pagina.intValue() + 1) * itemsPerPagina.intValue();
			if (toIndex > resultatConsulta.size()) toIndex = resultatConsulta.size();
			return new ConsultaResultat(
					itemsPerPagina,
					new Integer(resultatConsulta.size() / itemsPerPagina.intValue()),
					new Integer(resultatConsulta.size()),
					pagina,
					resultatConsulta.size() == 0 ? new ArrayList<ContingutArxiu>() : resultatConsulta.subList(fromIndex, toIndex));
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public List<ContingutArxiu> expedientVersions(
			final String identificador) throws ArxiuException {
		String metode = Servicios.GET_VERSION_FILE;
		try {
			return expedientVersionsComu(identificador);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void expedientTancar(
			final String identificador) throws ArxiuException {
		String metode = Servicios.CLOSE_FILE;
		try {
			getArxiuClient().generarEnviarPeticio(
					metode,
					CloseFile.class,
					new GeneradorParam<ParamNodeId>() {
						@Override
						public ParamNodeId generar() {
							ParamNodeId param = new ParamNodeId();
							param.setNodeId(identificador);
							return param;
						}
					},
					ParamNodeId.class,
					CloseFileResult.class);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void expedientReobrir(
			final String identificador) throws ArxiuException {
		String metode = Servicios.REOPEN_FILE;
		try {
			getArxiuClient().generarEnviarPeticio(
					metode,
					ReopenFile.class,
					new GeneradorParam<ParamNodeId>() {
						@Override
						public ParamNodeId generar() {
							ParamNodeId param = new ParamNodeId();
							param.setNodeId(identificador);
							return param;
						}
					},
					ParamNodeId.class,
					ReopenFileResult.class);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public String expedientExportarEni(
			final String identificador) throws ArxiuException {
		String metode = Servicios.EXPORT_FILE;
		try {
			ExportFileResult resposta = getArxiuClient().generarEnviarPeticio(
					metode,
					ExportFile.class,
					new GeneradorParam<ParamNodeId>() {
						@Override
						public ParamNodeId generar() {
							ParamNodeId param = new ParamNodeId();
							param.setNodeId(identificador);
							return param;
						}
					},
					ParamNodeId.class,
					ExportFileResult.class);
			String exportBase64 = resposta.getExportFileResult().getResParam();
			if (exportBase64 != null) {
				return new String(
						Base64.decodeBase64(exportBase64));
			} else {
				return null;
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public ContingutArxiu documentCrear(
			final Document document,
			final String identificadorPare) throws ArxiuException {
		String metode = Servicios.CREATE_DOC;
		try {
			comprovarAbsenciaMetadadaCsv(document.getMetadades());
			Document creat = null;
			if (ArxiuConstants.DOCUMENT_ESTAT_ESBORRANY.equals(document.getEstat())) {
				metode = Servicios.CREATE_DRAFT;
				CreateDraftDocumentResult resposta = getArxiuClient().generarEnviarPeticio(
						metode,
						CreateDraftDocument.class,
						new GeneradorParam<ParamCreateDraftDocument>() {
							@Override
							public ParamCreateDraftDocument generar() {
								ParamCreateDraftDocument param = new ParamCreateDraftDocument();
								param.setParent(identificadorPare);
								param.setDocument(
										ArxiuConversioHelper.documentToDocumentNode(
												document,
												null,
												null,
												null,
												null,
												null,
												true));
								param.setRetrieveNode(Boolean.TRUE.toString());
								return param;
							}
						},
						ParamCreateDraftDocument.class,
						CreateDraftDocumentResult.class);
				creat = ArxiuConversioHelper.documentNodeToDocument(
						resposta.getCreateDraftDocumentResult().getResParam());
			} else if (ArxiuConstants.DOCUMENT_ESTAT_DEFINITIU.equals(document.getEstat())) {
				metode = Servicios.CREATE_DOC;
				CreateDocumentResult resposta = getArxiuClient().generarEnviarPeticio(
						metode,
						CreateDocument.class,
						new GeneradorParam<ParamCreateDocument>() {
							@Override
							public ParamCreateDocument generar() {
								ParamCreateDocument param = new ParamCreateDocument();
								param.setParent(identificadorPare);
								param.setDocument(
										ArxiuConversioHelper.documentToDocumentNode(
												document,
												null,
												null,
												null,
												null,
												null,
												true));
								param.setRetrieveNode(Boolean.TRUE.toString());
								return param;
							}
						},
						ParamCreateDocument.class,
						CreateDocumentResult.class);
				creat = ArxiuConversioHelper.documentNodeToDocument(
						resposta.getCreateDocumentResult().getResParam());
			} else {
				throw new ArxiuValidacioException(
						"No s'ha emplenat l'estat del document o el document no conte un estat reconegut (ESBORRANY o DEFINITIU)");
			}
			return crearContingutArxiu(
					creat.getIdentificador(), 
					creat.getNom(),
					ArxiuConstants.CONTINGUT_TIPUS_DOCUMENT,
					"1.0");
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public ContingutArxiu documentModificar(
			final Document document,
			final boolean marcarDefinitiu) throws ArxiuException {
		String metode = null;
		try {
			comprovarAbsenciaMetadadaCsv(document.getMetadades());
			if (marcarDefinitiu) {
				metode = Servicios.GENERATE_CSV;
				GenerateDocCSVResult respostaCsv = getArxiuClient().generarEnviarPeticio(
						metode,
						GenerateDocCSV.class,
						null,
						Object.class,
						GenerateDocCSVResult.class);
				final String csv = respostaCsv.getGenerateDocCSVResult().getResParam();
				metode = Servicios.SET_FINAL_DOC;
				getArxiuClient().generarEnviarPeticio(
						metode,
						SetFinalDocument.class,
						new GeneradorParam<ParamSetDocument>() {
							@Override
							public ParamSetDocument generar() {
								ParamSetDocument param = new ParamSetDocument();
								param.setDocument(
										ArxiuConversioHelper.documentToDocumentNode(
												document,
												null, //respostaGet.getGetDocumentResult().getResParam().getMetadataCollection(),
												null, //respostaGet.getGetDocumentResult().getResParam().getAspects(),
												null,
												csv,
												getPropertyDefinicioCsv(),
												false));
								return param;
							}
						},
						ParamSetDocument.class,
						SetFinalDocumentResult.class);
			} else {
				metode = Servicios.SET_DOC;
				getArxiuClient().generarEnviarPeticio(
						metode,
						SetDocument.class,
						new GeneradorParam<ParamSetDocument>() {
							@Override
							public ParamSetDocument generar() {
								ParamSetDocument param = new ParamSetDocument();
								param.setDocument(
										ArxiuConversioHelper.documentToDocumentNode(
												document,
												null, //resposta.getGetDocumentResult().getResParam().getMetadataCollection(),
												null, //resposta.getGetDocumentResult().getResParam().getAspects(),
												null,
												null,
												null,
												false));
								return param;
							}
						},
						ParamSetDocument.class,
						SetDocumentResult.class);
			}
			String versio = null;
			List<ContingutArxiu> versions = documentVersionsComu(
					document.getIdentificador());
			if (versions != null && !versions.isEmpty()) {
				versio = versions.get(versions.size() - 1).getVersio();
			}
			return crearContingutArxiu(
					document.getIdentificador(), 
					document.getNom(),
					ArxiuConstants.CONTINGUT_TIPUS_DOCUMENT,
					versio);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void documentEsborrar(
			final String identificador) throws ArxiuException {
		String metode = Servicios.REMOVE_DOC;
		try {
			getArxiuClient().generarEnviarPeticio(
					metode,
					RemoveDocument.class,
					new GeneradorParam<ParamNodeId>() {
						@Override
						public ParamNodeId generar() {
							ParamNodeId param = new ParamNodeId();
							param.setNodeId(identificador);
							return param;
						}
					},
					ParamNodeId.class,
					RemoveDocumentResult.class);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public Document documentDetalls(
			final String identificador,
			final String versio,
			final boolean ambContingut) throws ArxiuException {
		String metode = Servicios.GET_DOC;
		try {
			GetDocumentResult resposta = getArxiuClient().generarEnviarPeticio(
					metode,
					GetDocument.class,
					new GeneradorParam<ParamGetDocument>() {
						@Override
						public ParamGetDocument generar() {
							ParamGetDocument param = new ParamGetDocument();
							DocumentId documentId = new DocumentId();
							String identificadorAmbVersio;
							if (versio != null) {
								identificadorAmbVersio = versio + "@" + identificador;
							} else {
								identificadorAmbVersio = identificador;
							}
							documentId.setNodeId(identificadorAmbVersio);
							param.setDocumentId(documentId);
							param.setContent(new Boolean(ambContingut).toString());
							return param;
						}
					},
					ParamGetDocument.class,
					GetDocumentResult.class);
			return ArxiuConversioHelper.documentNodeToDocument(
					resposta.getGetDocumentResult().getResParam());
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public ConsultaResultat documentConsulta(
			final List<ConsultaFiltre> filtres,
			final Integer pagina,
			final Integer itemsPerPagina) throws ArxiuException {
		String metode = Servicios.SEARCH_DOC;
		try {
			List<ContingutArxiu> resultatConsulta = new ArrayList<ContingutArxiu>();
			List<ContingutArxiu> continguts = null;
			while (continguts == null || continguts.size() == NUM_PAGINES_RESULTAT_CERCA) {
				SearchDocsResult resposta = getArxiuClient().generarEnviarPeticio(
						metode,
						SearchDocs.class,
						new GeneradorParam<ParamSearch>() {
							@Override
							public ParamSearch generar() {
								ParamSearch param = new ParamSearch();
								String query = ArxiuConsultaHelper.generarConsulta(
										"\"eni:documento\" ",
										filtres);
								param.setQuery(query);
								param.setPageNumber(pagina);
								return param;
							}
						},
						ParamSearch.class,
						SearchDocsResult.class);
				List<DocumentNode> documents = new ArrayList<DocumentNode>();
				if (	resposta.getSearchDocumentsResult().getResParam() != null &&
						resposta.getSearchDocumentsResult().getResParam().getDocuments() != null) {
					documents = resposta.getSearchDocumentsResult().getResParam().getDocuments();
				}
				continguts = ArxiuConversioHelper.fileNodeToDocumentContingut(documents);
				resultatConsulta.addAll(continguts);
			}
			int fromIndex = pagina.intValue() * itemsPerPagina.intValue();
			int toIndex = (pagina.intValue() + 1) * itemsPerPagina.intValue();
			if (toIndex > resultatConsulta.size()) toIndex = resultatConsulta.size();
			return new ConsultaResultat(
					itemsPerPagina,
					new Integer(resultatConsulta.size() / itemsPerPagina.intValue()),
					new Integer(resultatConsulta.size()),
					pagina,
					resultatConsulta.size() == 0 ? new ArrayList<ContingutArxiu>() : resultatConsulta.subList(fromIndex, toIndex));
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void documentEstablirDefinitiu(
			final String identificador) throws ArxiuException {
		String metode = null;
		try {
			metode = Servicios.GENERATE_CSV;
			GenerateDocCSVResult respostaCsv = getArxiuClient().generarEnviarPeticio(
					metode,
					GenerateDocCSV.class,
					null,
					Object.class,
					GenerateDocCSVResult.class);
			final String csv = respostaCsv.getGenerateDocCSVResult().getResParam();
			/*metode = Servicios.GET_DOC;
			final GetDocumentResult respostaGet = getArxiuClient().generarEnviarPeticio(
					metode,
					GetDocument.class,
					new GeneradorParam<ParamGetDocument>() {
						@Override
						public ParamGetDocument generar() {
							ParamGetDocument param = new ParamGetDocument();
							DocumentId documentId = new DocumentId();
							documentId.setNodeId(identificador);
							param.setDocumentId(documentId);
							param.setContent(new Boolean(false).toString());
							return param;
						}
					},
					ParamGetDocument.class,
					GetDocumentResult.class);*/
			metode = Servicios.SET_FINAL_DOC;
			getArxiuClient().generarEnviarPeticio(
					metode,
					SetFinalDocument.class,
					new GeneradorParam<ParamSetDocument>() {
						@Override
						public ParamSetDocument generar() {
							ParamSetDocument param = new ParamSetDocument();
							Document document = new Document();
							document.setIdentificador(identificador);
							param.setDocument(
									ArxiuConversioHelper.documentToDocumentNode(
											document,
											null, //respostaGet.getGetDocumentResult().getResParam().getMetadataCollection(),
											null, //respostaGet.getGetDocumentResult().getResParam().getAspects(),
											null,
											csv,
											getPropertyDefinicioCsv(),
											false));
							return param;
						}
					},
					ParamSetDocument.class,
					SetFinalDocumentResult.class);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public List<ContingutArxiu> documentVersions(
			String identificador) throws ArxiuException {
		String metode = Servicios.GET_VERSION_DOC;
		try {
			return documentVersionsComu(identificador);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void documentCopiar(
			final String identificador,
			final String identificadorDesti) throws ArxiuException {
		String metode = Servicios.COPY_DOC;
		try {
			getArxiuClient().generarEnviarPeticio(
					metode,
					CopyDocument.class,
					new GeneradorParam<ParamNodeID_TargetParent>() {
						@Override
						public ParamNodeID_TargetParent generar() {
							ParamNodeID_TargetParent param = new ParamNodeID_TargetParent();
							param.setNodeId(identificador);
							param.setTargetParent(identificadorDesti);
							return param;
						}
					},
					ParamNodeID_TargetParent.class,
					CopyDocumentResult.class);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void documentMoure(
			final String identificador,
			final String identificadorDesti) throws ArxiuException {
		String metode = Servicios.MOVE_DOC;
		try {
			getArxiuClient().generarEnviarPeticio(
					metode,
					MoveDocument.class,
					new GeneradorParam<ParamNodeID_TargetParent>() {
						@Override
						public ParamNodeID_TargetParent generar() {
							ParamNodeID_TargetParent param = new ParamNodeID_TargetParent();
							param.setNodeId(identificador);
							param.setTargetParent(identificadorDesti);
							return param;
						}
					},
					ParamNodeID_TargetParent.class,
					MoveDocumentResult.class);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public String documentExportarEni(
			final String identificador) throws ArxiuException {
		String metode = Servicios.GET_ENIDOC;
		try {
			GetENIDocumentResult resposta = getArxiuClient().generarEnviarPeticio(
					metode,
					GetENIDocument.class,
					new GeneradorParam<ParamNodeId>() {
						@Override
						public ParamNodeId generar() {
							ParamNodeId param = new ParamNodeId();
							param.setNodeId(identificador);
							return param;
						}
					},
					ParamNodeId.class,
					GetENIDocumentResult.class);
			String exportBase64 = resposta.getGetENIDocResult().getResParam();
			if (exportBase64 != null) {
				return new String(
						Base64.decodeBase64(exportBase64));
			} else {
				return null;
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public DocumentContingut documentImprimible() throws ArxiuException {
		// TODO Auto-generated method stub
		/*
		La URL de consulta es la següent:
			https://intranet.caib.es/concsv/rest/printable/IDENTIFICADOR?metadata1=METADADA_1&metadata2=METADADA_2&watermark=MARCA_AIGUA
		A on:
			- IDENTIFICADOR és l'id del document a consultar [OBLIGATORI]
			- METADADA_1 és la primera metadada [OPCIONAL]
			- METADADA_2 és la segona metadada [OPCIONAL]
			- MARCA_AIGUA és el text de la marca d'aigua que apareixerà impresa a cada fulla [OPCIONAL]
		Només es obligatori informa la HASH, la resta d'elements son opcionals. Si no s'informen metadades s'imprimeix l'hora i dia de la generació del document imprimible.
		 */
		return null;
	}

	@Override
	public ContingutArxiu carpetaCrear(
			final Carpeta carpeta,
			final String identificadorPare) throws ArxiuException {
		String metode = Servicios.CREATE_FOLDER;
		try {
			CreateFolderResult resposta = getArxiuClient().generarEnviarPeticio(
					metode,
					CreateFolder.class,
					new GeneradorParam<ParamCreateFolder>() {
						@Override
						public ParamCreateFolder generar() {
							ParamCreateFolder param = new ParamCreateFolder();
							param.setParent(identificadorPare);
							param.setFolder(
									ArxiuConversioHelper.toFolderNode(
											null,
											carpeta.getNom()));
							param.setRetrieveNode(Boolean.TRUE.toString());
							param.setRetrieveNode(Boolean.TRUE.toString());
							return param;
						}
					},
					ParamCreateFolder.class,
					CreateFolderResult.class);
			Carpeta carpetaCreada = ArxiuConversioHelper.folderNodeToCarpeta(
					resposta.getCreateFolderResult().getResParam());
			return crearContingutArxiu(
					carpetaCreada.getIdentificador(), 
					carpetaCreada.getNom(),
					ArxiuConstants.CONTINGUT_TIPUS_CARPETA,
					null);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode + ": " + ex.getMessage(),
					ex);
		}
	}

	@Override
	public ContingutArxiu carpetaModificar(
			final Carpeta carpeta) throws ArxiuException {
		String metode = Servicios.SET_FOLDER;
		try {
			getArxiuClient().generarEnviarPeticio(
					metode,
					SetFolder.class,
					new GeneradorParam<ParamSetFolder>() {
						@Override
						public ParamSetFolder generar() {
							ParamSetFolder param = new ParamSetFolder();
							param.setFolder(ArxiuConversioHelper.toFolderNode(
									carpeta.getIdentificador(),
									carpeta.getNom()));
							return param;
						}
					},
					ParamSetFolder.class,
					SetFolderResult.class);
			return crearContingutArxiu(
					carpeta.getIdentificador(),
					carpeta.getNom(),
					ArxiuConstants.CONTINGUT_TIPUS_CARPETA,
					null);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void carpetaEsborrar(
			final String identificador) throws ArxiuException {
		String metode = Servicios.REMOVE_FOLDER;
		try {
			getArxiuClient().generarEnviarPeticio(
					metode,
					RemoveFolder.class,
					new GeneradorParam<ParamNodeId>() {
						@Override
						public ParamNodeId generar() {
							ParamNodeId param = new ParamNodeId();
							param.setNodeId(identificador);
							return param;
						}
					},
					ParamNodeId.class,
					RemoveFolderResult.class);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public Carpeta carpetaDetalls(
			final String identificador) throws ArxiuException {
		String metode = Servicios.GET_FOLDER;
		try {
			GetFolderResult resposta = getArxiuClient().generarEnviarPeticio(
					metode,
					GetFolder.class,
					new GeneradorParam<ParamNodeId>() {
						@Override
						public ParamNodeId generar() {
							ParamNodeId param = new ParamNodeId();
							param.setNodeId(identificador);
							return param;
						}
					},
					ParamNodeId.class,
					GetFolderResult.class);
			return ArxiuConversioHelper.folderNodeToCarpeta(
					resposta.getGetFolderResult().getResParam());
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void carpetaCopiar(
			final String identificador,
			final String identificadorDesti) throws ArxiuException {
		//String metode = Servicios.COPY_FOLDER;
		String metode = "/services/copyFolder";
		try {
			getArxiuClient().generarEnviarPeticio(
					metode,
					CopyFolder.class,
					new GeneradorParam<ParamNodeID_TargetParent>() {
						@Override
						public ParamNodeID_TargetParent generar() {
							ParamNodeID_TargetParent param = new ParamNodeID_TargetParent();
							param.setNodeId(identificador);
							param.setTargetParent(identificadorDesti);
							return param;
						}
					},
					ParamNodeID_TargetParent.class,
					CopyFolderResult.class);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void carpetaMoure(
			final String identificador,
			final String identificadorDesti) throws ArxiuException {
		String metode = Servicios.MOVE_FOLDER;
		try {
			getArxiuClient().generarEnviarPeticio(
					metode,
					MoveFolder.class,
					new GeneradorParam<ParamNodeID_TargetParent>() {
						@Override
						public ParamNodeID_TargetParent generar() {
							ParamNodeID_TargetParent param = new ParamNodeID_TargetParent();
							param.setNodeId(identificador);
							param.setTargetParent(identificadorDesti);
							return param;
						}
					},
					ParamNodeID_TargetParent.class,
					MoveFolderResult.class);
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
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



	private ContingutArxiu crearContingutArxiu(
			String identificador, 
			String nom,
			String tipus,
			String versio) {
		ContingutArxiu informacioItem = new ContingutArxiu(tipus);
		informacioItem.setIdentificador(identificador);
		informacioItem.setNom(nom);
		return informacioItem;
	}

	private List<ContingutArxiu> expedientVersionsComu(
			final String identificador) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, UniformInterfaceException, ClientHandlerException, IOException {
		String metode = Servicios.GET_VERSION_FILE;
		GetFileVersionListResult resposta = getArxiuClient().generarEnviarPeticio(
				metode,
				GetFileVersionList.class,
				new GeneradorParam<ParamNodeId>() {
					@Override
					public ParamNodeId generar() {
						ParamNodeId param = new ParamNodeId();
						param.setNodeId(identificador);
						return param;
					}
				},
				ParamNodeId.class,
				GetFileVersionListResult.class);
		List<VersionNode> versions = resposta.getGetFileVersionListResult().getResParam();
		Collections.sort(
				versions,
				new Comparator<VersionNode>() {
					public int compare(VersionNode vn1, VersionNode vn2) {
						return vn1.getDate().compareTo(vn2.getDate());
					}
				});
		int versio = 1;
		List<ContingutArxiu> continguts  = new ArrayList<ContingutArxiu>();
		for (int i = 0; i < versions.size(); i++) {
			continguts.add(
					ArxiuConversioHelper.crearContingutArxiu(
							identificador,
							null,
							ArxiuConstants.CONTINGUT_TIPUS_EXPEDIENT,
							String.valueOf(versio)));
			versio++;
		}
		return continguts;
	}

	private List<ContingutArxiu> documentVersionsComu(
			final String identificador) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, UniformInterfaceException, ClientHandlerException, IOException {
		String metode = Servicios.GET_VERSION_DOC;
		GetDocVersionListResult resposta = getArxiuClient().generarEnviarPeticio(
				metode,
				GetDocVersionList.class,
				new GeneradorParam<ParamNodeId>() {
					@Override
					public ParamNodeId generar() {
						ParamNodeId param = new ParamNodeId();
						param.setNodeId(identificador);
						return param;
					}
				},
				ParamNodeId.class,
				GetDocVersionListResult.class);
		List<VersionNode> versions = resposta.getGetDocVersionListResult().getResParam();
		Collections.sort(
				versions,
				new Comparator<VersionNode>() {
					public int compare(VersionNode vn1, VersionNode vn2) {
						return vn1.getDate().compareTo(vn2.getDate());
					}
				});
		int versio = 1;
		List<ContingutArxiu> continguts  = new ArrayList<ContingutArxiu>();
		for (int i = 0; i < versions.size(); i++) {
			continguts.add(
					ArxiuConversioHelper.crearContingutArxiu(
							identificador,
							null,
							ArxiuConstants.CONTINGUT_TIPUS_DOCUMENT,
							String.valueOf(versio)));
			versio++;
		}
		return continguts;
	}

	private void comprovarAbsenciaMetadadaCsv(
			DocumentMetadades metadades) {
		if (metadades != null && metadades.getMetadadesAddicionals() != null) {
			if (metadades.getMetadadesAddicionals().containsKey(MetadatosDocumento.CSV)) {
				throw new ArxiuValidacioException(
						"No és possible especificar la metadada " + MetadatosDocumento.CSV + " per als documents. " +
						"Aquesta metadada la gestiona l'arxiu de forma automàtica.");
			}
			if (metadades.getMetadadesAddicionals().containsKey(MetadatosDocumento.DEF_CSV)) {
				throw new ArxiuValidacioException(
						"No és possible especificar la metadada " + MetadatosDocumento.DEF_CSV + " per als documents. " +
						"Aquesta metadada la gestiona l'arxiu de forma automàtica.");
			}
		}
	}

	private ArxiuCaibClient getArxiuClient() {
		if (arxiuClient == null) {
			arxiuClient = new ArxiuCaibClient(
					getPropertyBaseUrl(),
					getPropertyAplicacioCodi(),
					getPropertyUsuari(),
					getPropertyContrasenya());
		}
		return arxiuClient;
	}

	private String getPropertyBaseUrl() {
		return getProperty(ARXIUCAIB_PROPERTY_BASE + "base.url");
	}
	private String getPropertyAplicacioCodi() {
		return getProperty(ARXIUCAIB_PROPERTY_BASE + "aplicacio.codi");
	}
	private String getPropertyUsuari() {
		return getProperty(ARXIUCAIB_PROPERTY_BASE + "usuari");
	}
	private String getPropertyContrasenya() {
		return getProperty(ARXIUCAIB_PROPERTY_BASE + "contrasenya");
	}
	private String getPropertyDefinicioCsv() {
		return getProperty(ARXIUCAIB_PROPERTY_BASE + "csv.definicio");
	}

}
