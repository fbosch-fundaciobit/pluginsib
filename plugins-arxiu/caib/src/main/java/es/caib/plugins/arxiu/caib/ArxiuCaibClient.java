/**
 * 
 */
package es.caib.plugins.arxiu.caib;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.FileAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.PersonIdentAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.ProceedingsAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.PublicServantAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.ServiceAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.ServiceHeader;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.ServiceSecurityInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.ExceptionResult;
import es.caib.arxiudigital.apirest.CSGD.peticiones.Request;
import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.ArxiuNotFoundException;

/**
 * Client per a accedir a la funcionalitat de l'arxiu digital de
 * la CAIB.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuCaibClient {

	private static final String SERVEI_VERSIO = "1.0";
	private static final int JERSEY_TIMEOUT_CONNECT = 5000;
	private static final int JERSEY_TIMEOUT_READ = 20000;

	private String url;
	private String aplicacioCodi;
	private String usuariSgd;
	private String contrasenyaSgd;

	private Client jerseyClient;
	private ObjectMapper mapper;

	public ArxiuCaibClient(
			String url,
			String aplicacioCodi,
			String usuariHttp,
			String contrasenyaHttp,
			String usuariSgd,
			String contrasenyaSgd) {
		super();
		if (url.endsWith("/")) {
			this.url = url.substring(0, url.length() - 1);
		} else {
			this.url = url;
		}
		this.aplicacioCodi = aplicacioCodi;
		this.usuariSgd = usuariSgd;
		this.contrasenyaSgd = contrasenyaSgd;
		jerseyClient = new Client();
		jerseyClient.setConnectTimeout(JERSEY_TIMEOUT_CONNECT);
		jerseyClient.setReadTimeout(JERSEY_TIMEOUT_READ);
		if (usuariHttp != null) {
			jerseyClient.addFilter(new HTTPBasicAuthFilter(usuariHttp, contrasenyaHttp));
		}
		mapper = new ObjectMapper();
		// Permite recibir un solo objeto donde debía haber una lista.
		// Lo transforma a una lista con un objeto
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		// Feature that determines standard deserialization mechanism used for Enum values: if enabled, Enums are assumed to have been 
		// serialized using return value of Enum.toString();
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		// To suppress serializing properties with null values
		mapper.setSerializationInclusion(Include.NON_NULL);
	}

	public ArxiuCaibClient(
			String url,
			String aplicacioCodi,
			String usuariSgd,
			String contrasenyaSgd) {
		this(
				url,
				aplicacioCodi,
				null,
				null,
				usuariSgd,
				contrasenyaSgd);
	}



	public <T, U, V> V generarEnviarPeticio(
			String metode,
			Class<T> peticioType,
			GeneradorParam<U> generador,
			Class<U> paramType,
			Class<V> respostaType) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, UniformInterfaceException, ClientHandlerException, IOException {
		Request<U> request = new Request<U>();
		Capsalera capsalera = generarCapsalera();
		request.setServiceHeader(
				generarServiceHeader(capsalera));
		if (generador != null) {
			request.setParam(generador.generar());
		}
		T peticio = peticioType.newInstance();
		for (Method method: peticioType.getMethods()) {
			if (method.getName().startsWith("set")) {
				method.invoke(peticio, request);
				break;
			}
		}
		JerseyResponse response = enviarPeticioRest(
				metode,
				peticio);
		if (response.getStatus() == 200) {
			return mapper.readValue(
					response.getJson(),
					respostaType);
		} else {
			throw generarExcepcioJson(
					metode,
					response);
		}
	}



	/*public Expedient fileCreate(
			String nom,
			ExpedientMetadades metadades) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.CREATE_FILE;
		try {
			CreateFile createFile = new CreateFile();
			Request<ParamCreateFile> request = new Request<ParamCreateFile>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateFile param = new ParamCreateFile();
			param.setFile(
					ArxiuConversioHelper.toFileNode(
							null,
							nom, 
							metadades,
							null,
							aplicacioCodi,
							null,
							true));
			param.setRetrieveNode(Boolean.TRUE.toString());
			request.setParam(param);
			createFile.setCreateFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					createFile);
			if (resposta.getStatus() == 200) {
				CreateFileResult result = mapper.readValue(
						resposta.getJson(),
						CreateFileResult.class);
				FileNode fileNode = result.getCreateFileResult().getResParam();
				return ArxiuConversioHelper.fileNodeToExpedient(fileNode);
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode + ": " + ex.getMessage(),
					ex);
		}
	}

	public void fileUpdate(
			String identificador, 
			String nom,
			ExpedientMetadades metadades) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();*/
		/*String metode;
		metode = Servicios.GET_FILE;
		FileNode fileNode;
		try {
			GetFile getFile = new GetFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(identificador);
			request.setParam(param);
			getFile.setGetFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					getFile);
			if (resposta.getStatus() == 200) {
				GetFileResult result = mapper.readValue(
						resposta.getJson(),
						GetFileResult.class);
				fileNode = result.getGetFileResult().getResParam();
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}*/
		/*String metode = Servicios.SET_FILE;
		try {
			SetFile setFile = new SetFile();
			Request<ParamSetFile> request = new Request<ParamSetFile>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamSetFile param = new ParamSetFile();
			param.setFile(
					ArxiuConversioHelper.toFileNode(
						identificador,
						nom, 
						metadades,
						new ArrayList<Metadata>(),
						aplicacioCodi,
						new ArrayList<Aspectos>(),
						false));
			request.setParam(param);
			setFile.setSetFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					setFile);
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public void fileDelete(
			String identificador) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.REMOVE_FILE;
		try {
			RemoveFile removeFile = new RemoveFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(identificador);
			request.setParam(param);
			removeFile.setRemoveFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					removeFile);
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public Expedient fileGet(
			String identificador) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.GET_FILE;
		try {
			GetFile getFile = new GetFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(identificador);
			request.setParam(param);
			getFile.setGetFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					getFile);
			if (resposta.getStatus() == 200) {
				GetFileResult result = mapper.readValue(
						resposta.getJson(),
						GetFileResult.class);
				FileNode fileNode = result.getGetFileResult().getResParam();
				return ArxiuConversioHelper.fileNodeToExpedient(fileNode);
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public List<ContingutArxiu> fileSearch(
			List<ConsultaFiltre> filtres,
			Integer pagina) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.SEARCH_FILE;
		try {	
			SearchFiles searchFiles = new SearchFiles();
			Request<ParamSearch> request = new Request<ParamSearch>();
			ParamSearch param = new ParamSearch();
			String query = ArxiuConsultaHelper.generarConsulta(
					"\"eni:expediente\" ",
					filtres);
			param.setQuery(query);
			param.setPageNumber(pagina);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			searchFiles.setSearchFilesRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					searchFiles);
			if (resposta.getStatus() == 200) {
				SearchFilesResult result = mapper.readValue(
						resposta.getJson(),
						SearchFilesResult.class);
				
				List<FileNode> files = new ArrayList<FileNode>();
				if (result.getSearchFilesResult().getResParam() != null &&
						result.getSearchFilesResult().getResParam().getFiles() != null)
					files = result.getSearchFilesResult().getResParam().getFiles();
				return ArxiuConversioHelper.fileNodesToFileContingutArxiu(files);
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public List<ContingutArxiu> fileVersionList(
			String identificador) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.GET_VERSION_FILE;
		try {
			GetFileVersionList getFileVersionList = new GetFileVersionList();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(identificador);
			request.setParam(param);
			getFileVersionList.setGetFileVersionListRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					getFileVersionList);
			if (resposta.getStatus() == 200) {
				GetFileVersionListResult result = mapper.readValue(
						resposta.getJson(),
						GetFileVersionListResult.class);
				List<VersionNode> vns = result.getGetFileVersionListResult().getResParam();
				Collections.sort(vns, new Comparator<VersionNode>() {
				    public int compare(VersionNode vn1, VersionNode vn2) {
				        return vn1.getDate().compareTo(vn2.getDate());
				    }});
				int versio = 1;
				List<ContingutArxiu> continguts = new ArrayList<ContingutArxiu>();
				for (int i = 0; i < vns.size(); i++) {
					continguts.add(
							ArxiuConversioHelper.crearContingutArxiu(
									identificador,
									null,
									ArxiuConstants.CONTINGUT_TIPUS_EXPEDIENT,
									String.valueOf(versio)));
					versio++;
				}
				return continguts;
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public void fileClose(
			String identificador) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.CLOSE_FILE;
		try {
			CloseFile closeFile = new CloseFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(identificador);
			request.setParam(param);
			closeFile.setCloseFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					closeFile);
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public void fileReopen(
			String identificador) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.REOPEN_FILE;
		try {
			ReopenFile reopenFile = new ReopenFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(identificador);
			request.setParam(param);
			reopenFile.setReopenFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					reopenFile);
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public String fileExport(
			String identificador) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.EXPORT_FILE;
		try {
			ExportFile exportFile = new ExportFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(identificador);
			request.setParam(param);
			exportFile.setExportFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					exportFile);
			if (resposta.getStatus() == 200) {
				ExportFileResult result = mapper.readValue(
						resposta.getJson(),
						ExportFileResult.class);
				String exportBase64 = result.getExportFileResult().getResParam();
				
				if (exportBase64 != null) {
					return new String(
							Base64.decodeBase64(exportBase64));
				} else {
					return null;
				}
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public Document documentFinalCreate(
			String pareIdentificador,
			Document document) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.CREATE_DOC;
		try {
			CreateDocument createDocument = new CreateDocument();
			Request<ParamCreateDocument> request = new Request<ParamCreateDocument>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateDocument param = new ParamCreateDocument();
			param.setParent(pareIdentificador);
			param.setDocument(ArxiuConversioHelper.toDocumentNode(
					document,
					aplicacioCodi));
			param.setRetrieveNode(Boolean.TRUE.toString());
			request.setParam(param);
			createDocument.setCreateDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					createDocument);
			if (resposta.getStatus() == 200) {
				CreateDraftDocumentResult result = mapper.readValue(
						resposta.getJson(),
						CreateDraftDocumentResult.class);
				DocumentNode documentNode = result.getCreateDraftDocumentResult().getResParam();
				return ArxiuConversioHelper.documentNodeToDocument(documentNode);
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public Document documentDraftCreate(
			String pareIdentificador,
			Document document) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.CREATE_DRAFT;
		try {
			CreateDraftDocument createDraftDocument = new CreateDraftDocument();
			Request<ParamCreateDraftDocument> request = new Request<ParamCreateDraftDocument>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateDraftDocument param = new ParamCreateDraftDocument();
			param.setParent(pareIdentificador);
			param.setDocument(ArxiuConversioHelper.toDocumentNode(
					document,
					aplicacioCodi));
			param.setRetrieveNode(Boolean.TRUE.toString());
			request.setParam(param);
			createDraftDocument.setCreateDraftDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					createDraftDocument);
			if (resposta.getStatus() == 200) {
				CreateDraftDocumentResult result = mapper.readValue(
						resposta.getJson(),
						CreateDraftDocumentResult.class);
				DocumentNode documentNode = result.getCreateDraftDocumentResult().getResParam();
				return ArxiuConversioHelper.documentNodeToDocument(documentNode);
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public void documentFinalSet(
			String identificador) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.SET_FINAL_DOC;
		try {
			SetFinalDocument setFinalDocument = new SetFinalDocument();
			Request<ParamSetDocument> request = new Request<ParamSetDocument>();
			ParamSetDocument param = new ParamSetDocument();
			Document document = new Document();
			document.setIdentificador(identificador);
			param.setDocument(ArxiuConversioHelper.toDocumentNode(
					document,
					aplicacioCodi));
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			setFinalDocument.setSetFinalDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					setFinalDocument);
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public void documentUpdate(
			Document document) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.SET_DOC;
		try {
			SetDocument setDocument = new SetDocument();
			Request<ParamSetDocument> request = new Request<ParamSetDocument>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamSetDocument param = new ParamSetDocument();
			param.setDocument(ArxiuConversioHelper.toDocumentNode(
					document,
					aplicacioCodi));
			request.setParam(param);
			setDocument.setSetDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					setDocument);
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public void documentDelete(
			String identificador) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.REMOVE_DOC;
		try {
			RemoveDocument removeDocument = new RemoveDocument();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(identificador);
			request.setParam(param);
			removeDocument.setRemoveDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					removeDocument);
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public Document documentGet(
			String identificador,
			String csv,
			boolean ambContingut) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.GET_DOC;
		try {
			GetDocument getDocument = new GetDocument();
			Request<ParamGetDocument> request = new Request<ParamGetDocument>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamGetDocument param = new ParamGetDocument();
			DocumentId documentId = new DocumentId();
			documentId.setNodeId(identificador);
			documentId.setCsv(csv);
			param.setDocumentId(documentId);
			param.setContent(new Boolean(ambContingut).toString());
			request.setParam(param);
			getDocument.setGetDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					getDocument);
			if (resposta.getStatus() == 200) {
				GetDocumentResult result = mapper.readValue(
						resposta.getJson(),
						GetDocumentResult.class);
				DocumentNode documentNode = result.getGetDocumentResult().getResParam();
				return ArxiuConversioHelper.documentNodeToDocument(documentNode);
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public List<ContingutArxiu> documentSearch(
			List<ConsultaFiltre> filtres,
			Integer pagina) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.SEARCH_DOC;
		try {	
			SearchDocs searchDocs = new SearchDocs();
			Request<ParamSearch> request = new Request<ParamSearch>();
			ParamSearch param = new ParamSearch();
			String query = ArxiuConsultaHelper.generarConsulta(
					"\"eni:documento\" ",
					filtres);
			param.setQuery(query);
			param.setPageNumber(pagina);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			searchDocs.setSearchDocsRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					searchDocs);
			if (resposta.getStatus() == 200) {
				SearchDocsResult result = mapper.readValue(
						resposta.getJson(),
						SearchDocsResult.class);
				List<DocumentNode> docs = new ArrayList<DocumentNode>();
				if (result.getSearchDocumentsResult().getResParam() != null &&
						result.getSearchDocumentsResult().getResParam().getDocuments() != null)
					docs = result.getSearchDocumentsResult().getResParam().getDocuments();
					 
				return ArxiuConversioHelper.fileNodeToDocumentContingut(docs);
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public List<ContingutArxiu> documentVersionList(
			String identificador) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.GET_VERSION_DOC;
		try {
			GetDocVersionList getDocVersionList = new GetDocVersionList();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(identificador);
			request.setParam(param);
			getDocVersionList.setGetDocVersionListRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					getDocVersionList);
			if (resposta.getStatus() == 200) {
				GetDocVersionListResult result = mapper.readValue(
						resposta.getJson(),
						GetDocVersionListResult.class);
				List<VersionNode> vns = result.getGetDocVersionListResult().getResParam();
				List<ContingutArxiu> informacioItems = null;
				if (vns != null) {
					Collections.sort(vns, new Comparator<VersionNode>() {
					    public int compare(VersionNode vn1, VersionNode vn2) {
					        return vn1.getDate().compareTo(vn2.getDate());
					    }});
					informacioItems = new ArrayList<ContingutArxiu>();
					for (VersionNode vn: vns) {
						informacioItems.add(
								ArxiuConversioHelper.crearContingutArxiu(
										identificador,
										null,
										ArxiuConstants.CONTINGUT_TIPUS_DOCUMENT,
										new Float(vn.getId()).toString()));
					}
				}
				return informacioItems;
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public String documentCsvGenerate() throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.GENERATE_CSV;
		try {
			GenerateDocCSV generateDocCSV = new GenerateDocCSV();
			Request<Object> request = new Request<Object>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			generateDocCSV.setGenerateDocCSVRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					generateDocCSV);
			if (resposta.getStatus() == 200) {
				GenerateDocCSVResult result = mapper.readValue(
						resposta.getJson(),
						GenerateDocCSVResult.class);
				return result.getGenerateDocCSVResult().getResParam();
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public String documentCopy(
			String nodeId,
			String targetNodeId) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.COPY_DOC;
		try {
			CopyDocument copyDocument = new CopyDocument();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent param = new ParamNodeID_TargetParent();
			param.setNodeId(nodeId);
			param.setTargetParent(targetNodeId);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			copyDocument.setCopyDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					copyDocument);
			if (resposta.getStatus() == 200) {
				CopyDocumentResult result = mapper.readValue(
						resposta.getJson(),
						CopyDocumentResult.class);
				return result.getCopyDocumentResult().getResParam();
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public void documentMove(
			String nodeId,
			String targetNodeId) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.MOVE_DOC;
		try {
			MoveDocument moveDocument = new MoveDocument();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent param = new ParamNodeID_TargetParent();
			param.setNodeId(nodeId);
			param.setTargetParent(targetNodeId);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			moveDocument.setMoveDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					moveDocument);
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public String documentEniGet(
			String identificador) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.GET_ENIDOC;
		try {
			GetENIDocument getEniDocument = new GetENIDocument();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(identificador);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			getEniDocument.setGetENIDocRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					getEniDocument);
			if (resposta.getStatus() == 200) {
				GetENIDocumentResult result = mapper.readValue(
						resposta.getJson(),
						GetENIDocumentResult.class);
				String exportBase64 = result.getGetENIDocResult().getResParam();
				if (exportBase64 != null) {
					return new String(
							Base64.decodeBase64(exportBase64));
				} else {
					return null;
				}
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public Carpeta folderCreate(
			String nom,
			String pareIdentificador) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.CREATE_FOLDER;
		try {
			CreateFolder createFolder = new CreateFolder();
			Request<ParamCreateFolder> request = new Request<ParamCreateFolder>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateFolder param = new ParamCreateFolder();
			param.setParent(pareIdentificador);
			param.setFolder(
					ArxiuConversioHelper.toFolderNode(
							null,
							nom));
			param.setRetrieveNode(Boolean.TRUE.toString());
			request.setParam(param);
			createFolder.setCreateFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					createFolder);
			if (resposta.getStatus() == 200) {
				CreateFolderResult result = mapper.readValue(
						resposta.getJson(),
						CreateFolderResult.class);
				FolderNode folderNode = result.getCreateFolderResult().getResParam();
				return ArxiuConversioHelper.folderNodeToCarpeta(folderNode);
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public void folderUpdate(
			String identificador,
			String nom) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.SET_FOLDER;
		try {
			SetFolder setFolder = new SetFolder();
			Request<ParamSetFolder> request = new Request<ParamSetFolder>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamSetFolder param = new ParamSetFolder();
			param.setFolder(ArxiuConversioHelper.toFolderNode(
					identificador,
					nom));
			request.setParam(param);
			setFolder.setSetFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					setFolder);
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public void folderDelete(
			String identificador) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.REMOVE_FOLDER;
		try {
			RemoveFolder removeFolder = new RemoveFolder();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId paramNodeId = new ParamNodeId();
			paramNodeId.setNodeId(identificador);
			request.setParam(paramNodeId);
			removeFolder.setRemoveFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					removeFolder);
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public Carpeta folderGet(
			String identificador) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.GET_FOLDER;
		try {
			GetFolder getFolder = new GetFolder();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId paramNodeId = new ParamNodeId();
			paramNodeId.setNodeId(identificador);
			request.setParam(paramNodeId);
			getFolder.setGetFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					getFolder);
			if (resposta.getStatus() == 200) {
				GetFolderResult result = mapper.readValue(
						resposta.getJson(),
						GetFolderResult.class);
				FolderNode folderNode = result.getGetFolderResult().getResParam();
				return ArxiuConversioHelper.folderNodeToCarpeta(folderNode);
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public String folderCopy(
			String nodeId,
			String targetNodeId) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		// TODO
		//String metode = Servicios.COPY_FOLDER;
		String metode = "/services/copyFolder";
		try {
			CopyFolder copyFolder = new CopyFolder();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent paramTarget = new ParamNodeID_TargetParent();
			paramTarget.setNodeId(nodeId);
			paramTarget.setTargetParent(targetNodeId);
			request.setParam(paramTarget);
			request.setServiceHeader(generarServiceHeader(capsalera));
			copyFolder.setcopyFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					copyFolder);
			if (resposta.getStatus() == 200) {
				CopyFolderResult result = mapper.readValue(
						resposta.getJson(),
						CopyFolderResult.class);
				return result.getCopyFolderResult().getResParam();
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	public void folderMove(
			String nodeId,
			String targetNodeId) throws ArxiuException {
		Capsalera capsalera = generarCapsalera();
		String metode = Servicios.MOVE_FOLDER;
		try {
			MoveFolder moveFolder = new MoveFolder();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent paramTarget = new ParamNodeID_TargetParent();
			paramTarget.setNodeId(nodeId);
			paramTarget.setTargetParent(targetNodeId);
			request.setParam(paramTarget);
			request.setServiceHeader(generarServiceHeader(capsalera));
			moveFolder.setMoveFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					moveFolder);
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}*/



	public static Capsalera generarCapsalera() {
		Capsalera capsalera = new Capsalera();
		//		header.setInteressatNom(capsalera.getInteressatNom());
		//		header.setInteressatNif(capsalera.getInteressatNif());
		//		header.setFuncionariNom(capsalera.getFuncionariNom());
		//		header.setFuncionariNif(capsalera.getFuncionariNif());
		//		header.setFuncionariOrgan(capsalera.getFuncionariOrgan());
		//		header.setProcedimentId(capsalera.getProcedimentId());
		//		header.setProcedimentNom(capsalera.getProcedimentNom());
		//		header.setExpedientId(capsalera.getExpedientId());
		return capsalera;
	}

	private ServiceHeader generarServiceHeader(Capsalera capsalera) {
		ServiceHeader serviceHeader = new ServiceHeader();
		ServiceAuditInfo auditInfo = null;
		if (capsalera.getInteressatNom() != null || capsalera.getInteressatNif() != null) {
			PersonIdentAuditInfo interessat = new PersonIdentAuditInfo();
			interessat.setName(capsalera.getInteressatNom());
			interessat.setDocument(capsalera.getInteressatNif());
			if (auditInfo == null) {
				auditInfo = new ServiceAuditInfo();
			}
			auditInfo.setApplicant(interessat);
		}
		PublicServantAuditInfo publicServant = null;
		if (capsalera.getFuncionariNom() != null || capsalera.getFuncionariNif() != null) {
			PersonIdentAuditInfo funcionari = new PersonIdentAuditInfo();
			funcionari.setName(capsalera.getFuncionariNom());
			funcionari.setDocument(capsalera.getFuncionariNif());
			if (publicServant == null) {
				publicServant = new PublicServantAuditInfo();
			}
			publicServant.setIdentificationData(funcionari);
		}
		if (capsalera.getFuncionariOrgan() != null) {
			if (publicServant == null) {
				publicServant = new PublicServantAuditInfo();
			}
			publicServant.setOrganization(capsalera.getFuncionariOrgan());
		}
		if (publicServant != null) {
			if (auditInfo == null) {
				auditInfo = new ServiceAuditInfo();
			}
			auditInfo.setPublicServant(publicServant);
		}
		FileAuditInfo expedient = null;
		if (capsalera.getExpedientId() != null) {
			if (expedient == null) {
				expedient = new FileAuditInfo();
			}
			expedient.setId(capsalera.getExpedientId());
		}
		if (capsalera.getProcedimentId() != null || capsalera.getProcedimentNom() != null) {
			ProceedingsAuditInfo procediment = new ProceedingsAuditInfo();
			procediment.setId(capsalera.getProcedimentId());
			procediment.setName(capsalera.getProcedimentNom());
			if (expedient == null) {
				expedient = new FileAuditInfo();
			}
			expedient.setProceedings(procediment);
		}
		if (expedient != null) {
			if (auditInfo == null) {
				auditInfo = new ServiceAuditInfo();
			}
			auditInfo.setFile(expedient);
		}
		if (aplicacioCodi != null) {
			if (auditInfo == null) {
				auditInfo = new ServiceAuditInfo();
			}
			auditInfo.setApplication(aplicacioCodi);
		}
		serviceHeader.setAuditInfo(auditInfo);
		serviceHeader.setServiceVersion(SERVEI_VERSIO);
		if (usuariSgd != null || contrasenyaSgd != null) {
			ServiceSecurityInfo securityInfo = new ServiceSecurityInfo();
			securityInfo.setUser(usuariSgd);
			securityInfo.setPassword(contrasenyaSgd);
			serviceHeader.setSecurityInfo(securityInfo);
		}
		return serviceHeader;
	}

	private ArxiuException generarExcepcioJson(
			String metode,
			JerseyResponse resposta)
		      throws JsonParseException, JsonMappingException, IOException {
		ExceptionResult exceptionResult = mapper.readValue(
				resposta.getJson(),
				ExceptionResult.class);
		String code = exceptionResult.getException().getCode();
		String description = exceptionResult.getException().getDescription();
		if ("COD_021".equals(code) && description.contains("not found")) {
			return new ArxiuNotFoundException();
		} else {
			return new ArxiuCaibException(
					metode,
					resposta.getStatus(),
					code,
					description);
		}
	}

	private JerseyResponse enviarPeticioRest(
			String metode,
			Object peticio) throws UniformInterfaceException, ClientHandlerException, JsonProcessingException {
		String urlAmbMetode = url + metode;
		String body = mapper.writeValueAsString(peticio);
		logger.debug("Enviant petició HTTP a l'arxiu (" +
				"url=" + urlAmbMetode + ", " +
				"tipus=application/json, " +
				"body=" + body + ")");
		//lastJsonRequest = body;
		ClientResponse response = jerseyClient.
				resource(urlAmbMetode).
				type("application/json").
				post(ClientResponse.class, body);
		String json = response.getEntity(String.class);
		logger.debug("Rebuda resposta HTTP de l'arxiu (" +
				"status=" + response.getStatus() + ", " +
				"body=" + json + ")");
		//lastJsonResponse = json;
		return new JerseyResponse(
				json,
				response.getStatus());
	}

	private static class JerseyResponse {
		String json;
		int status;
		public JerseyResponse(String json, int status) {
			this.json = json;
			this.status = status;
		}
		public String getJson() {
			return json;
		}
		public int getStatus() {
			return status;
		}
	}

	@SuppressWarnings("unused")
	private static class Capsalera {
		private String interessatNom;
		private String interessatNif;
		private String funcionariNom;
		private String funcionariNif;
		private String funcionariOrgan;
		private String procedimentId;
		private String procedimentNom;
		private String expedientId;
		public String getInteressatNom() {
			return interessatNom;
		}
		public void setInteressatNom(String interessatNom) {
			this.interessatNom = interessatNom;
		}
		public String getInteressatNif() {
			return interessatNif;
		}
		public void setInteressatNif(String interessatNif) {
			this.interessatNif = interessatNif;
		}
		public String getFuncionariNom() {
			return funcionariNom;
		}
		public void setFuncionariNom(String funcionariNom) {
			this.funcionariNom = funcionariNom;
		}
		public String getFuncionariNif() {
			return funcionariNif;
		}
		public void setFuncionariNif(String funcionariNif) {
			this.funcionariNif = funcionariNif;
		}
		public String getFuncionariOrgan() {
			return funcionariOrgan;
		}
		public void setFuncionariOrgan(String funcionariOrgan) {
			this.funcionariOrgan = funcionariOrgan;
		}
		public String getProcedimentId() {
			return procedimentId;
		}
		public void setProcedimentId(String procedimentId) {
			this.procedimentId = procedimentId;
		}
		public String getProcedimentNom() {
			return procedimentNom;
		}
		public void setProcedimentNom(String procedimentNom) {
			this.procedimentNom = procedimentNom;
		}
		public String getExpedientId() {
			return expedientId;
		}
		public void setExpedientId(String expedientId) {
			this.expedientId = expedientId;
		}
	}

	public interface GeneradorParam<T> {
		public T generar();
	}

	private static final Logger logger = LoggerFactory.getLogger(ArxiuCaibClient.class);

}