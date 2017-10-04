/**
 * 
 */
package es.caib.plugins.arxiu.caib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
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

import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.DocumentId;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.DocumentNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.FileAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.FileNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.FolderNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.PersonIdentAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.ProceedingsAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.PublicServantAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.ServiceAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.ServiceHeader;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.ServiceSecurityInfo;
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
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CopyDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CopyFolderResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateDraftDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateFolderResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.ExceptionResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.ExportFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GenerateDocCSVResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetDocVersionListResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetENIDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetFileVersionListResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetFolderResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.SearchDocsResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.SearchFilesResult;
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
import es.caib.arxiudigital.apirest.CSGD.peticiones.Request;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SearchDocs;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SearchFiles;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetFinalDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetFolder;
import es.caib.plugins.arxiu.api.ArxiuConstants;
import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ContingutTipus;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.InformacioItem;
import es.caib.plugins.arxiu.api.Tables;

/**
 * Interfície del client per a accedir a la funcionalitat de
 * l'arxiu digital.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class CaibArxiuClientImpl implements CaibArxiuClient {
	

	private static final String SERVEI_VERSIO = "1.0";
	

	private String url;
	private String aplicacioCodi;
	private String usuariSgd;
	private String contrasenyaSgd;

	private Client jerseyClient;
	private ObjectMapper mapper;
	private String lastJsonRequest;
	private String lastJsonResponse;
	
	
	public CaibArxiuClientImpl(
			String url,
			String aplicacioCodi,
			String usuariHttp,
			String contrasenyaHttp,
			String usuariSgd,
			String contrasenyaSgd) {
		super();
		this.url = url;
		this.aplicacioCodi = aplicacioCodi;
		this.usuariSgd = usuariSgd;
		this.contrasenyaSgd = contrasenyaSgd;
		jerseyClient = new Client();
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
	
	public CaibArxiuClientImpl(
			String url,
			String aplicacioCodi,
			String usuariSgd,
			String contrasenyaSgd) {
		this(	url,
				aplicacioCodi,
				null,
				null,
				usuariSgd,
				contrasenyaSgd);
	}
	
	public String getLastJsonRequest() {
		return lastJsonRequest;
	}
	public String getLastJsonResponse() {
		return lastJsonResponse;
	}
	
	
	/**
	 * ================= M E T O D E S   E X P E D I E N T S =================
	 */

	@Override
	public Expedient fileCreate(
			String nom,
			ExpedientMetadades metadades,
			String aplicacioCodi,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.FILE_CREATE;
		
		try {
			
			CreateFile createFile = new CreateFile();
			Request<ParamCreateFile> request = new Request<ParamCreateFile>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateFile param = new ParamCreateFile();
			param.setFile(
					CaibArxiuConversioHelper.toFileNode(
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
				
				return CaibArxiuConversioHelper.toExpedient(fileNode);
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

	@Override
	public void fileUpdate(
			String identificador, 
			String nom,
			ExpedientMetadades metadades,
			String aplicacioCodi,
			Capsalera capsalera) throws ArxiuException {
		
		String metode;
		metode = CaibArxiuMethods.FILE_GET;
		
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
		}
		
		metode = CaibArxiuMethods.FILE_SET;
		
		try {
			SetFile setFile = new SetFile();
			Request<ParamSetFile> request = new Request<ParamSetFile>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamSetFile param = new ParamSetFile();
			param.setFile(
					CaibArxiuConversioHelper.toFileNode(
						identificador,
						nom, 
						metadades,
						fileNode.getMetadataCollection(),
						aplicacioCodi,
						fileNode.getAspects(),
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


	@Override
	public void fileDelete(
			String identificador,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.FILE_REMOVE;
		
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

	@Override
	public Expedient fileGet(
			String identificador,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.FILE_GET;
		
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
				
				return CaibArxiuConversioHelper.toExpedient(fileNode);
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
	
	@Override
	public List<InformacioItem> fileSearch(
			List<ConsultaFiltre> filtres,
			Integer pagina,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.FILE_SEARCH;
		
		try {	
			SearchFiles searchFiles = new SearchFiles();
			Request<ParamSearch> request = new Request<ParamSearch>();
			ParamSearch param = new ParamSearch();
			
			String query = Utils.getQuery(Tables.TABLE_EXPEDIENT, filtres);	
			
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
					 
				return CaibArxiuConversioHelper.fileNodeToFileInformacioItem(files);
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

	@Override
	public List<InformacioItem> fileVersionList(
			String identificador,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.FILE_GET_VERSION;
		
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
				List<InformacioItem> informacioItems = new ArrayList<InformacioItem>();
				for (int i = 0; i < vns.size(); i++) {
					informacioItems.add(
							new InformacioItem(
									identificador,
									null,
									ArxiuConstants.CONTINGUT_TIPUS_EXPEDIENT,
									String.valueOf(versio)));
					versio++;
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

	@Override
	public void fileClose(
			String identificador,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.FILE_CLOSE;
		
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

	@Override
	public void fileReopen(
			String identificador,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.FILE_REOPEN;
		
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
	
	@Override
	public String fileExport(
			String identificador,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.FILE_EXPORT;
		
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
	
	
	/**
	 * ================= M E T O D E S   D O C U M E N T S =================
	 */
	
	@Override
	public Document documentFinalCreate(
			String pareIdentificador,
			Document document,
			String aplicacioCodi,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.DOCUMENT_CREATE;
		
		try {
			CreateDocument createDocument = new CreateDocument();
			Request<ParamCreateDocument> request = new Request<ParamCreateDocument>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateDocument param = new ParamCreateDocument();
			param.setParent(pareIdentificador);
			param.setDocument(CaibArxiuConversioHelper.toDocumentNode(
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
				
				return CaibArxiuConversioHelper.toDocument(documentNode);
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
	
	@Override
	public Document documentDraftCreate(
			String pareIdentificador,
			Document document,
			String aplicacioCodi,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.DOCUMENT_CREATE_DRAFT;
		
		try {
			CreateDraftDocument createDraftDocument = new CreateDraftDocument();
			Request<ParamCreateDraftDocument> request = new Request<ParamCreateDraftDocument>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateDraftDocument param = new ParamCreateDraftDocument();
			param.setParent(pareIdentificador);
			param.setDocument(CaibArxiuConversioHelper.toDocumentNode(
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
				
				return CaibArxiuConversioHelper.toDocument(documentNode);
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
	
	@Override
	public void documentFinalSet(
			Document document,
			String aplicacioCodi,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.DOCUMENT_SET_FINAL;
		
		try {
			SetFinalDocument setFinalDocument = new SetFinalDocument();
			Request<ParamSetDocument> request = new Request<ParamSetDocument>();
			ParamSetDocument param = new ParamSetDocument();
			param.setDocument(CaibArxiuConversioHelper.toDocumentNode(
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

	@Override
	public void documentUpdate(
			Document document,
			String aplicacioCodi,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.DOCUMENT_SET;
		
		try {
			SetDocument setDocument = new SetDocument();
			Request<ParamSetDocument> request = new Request<ParamSetDocument>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamSetDocument param = new ParamSetDocument();
			param.setDocument(CaibArxiuConversioHelper.toDocumentNode(
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

	@Override
	public void documentDelete(
			String identificador,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.DOCUMENT_REMOVE;
		
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

	@Override
	public Document documentGet(
			String identificador,
			String csv,
			boolean ambContingut,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.DOCUMENT_GET;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + identificador + ", " +
				"capsalera=" + capsalera + ")");
		
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
				
				return CaibArxiuConversioHelper.toDocument(documentNode);
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
	
	@Override
	public List<InformacioItem> documentSearch(
			List<ConsultaFiltre> filtres,
			Integer pagina,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.DOCUMENT_SEARCH;
		
		try {	
			SearchDocs searchDocs = new SearchDocs();
			Request<ParamSearch> request = new Request<ParamSearch>();
			ParamSearch param = new ParamSearch();
			
			String query = Utils.getQuery(Tables.TABLE_DOCUMENT, filtres);	
			
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
					 
				return CaibArxiuConversioHelper.fileNodeToDocumentInformacioItem(docs);
				
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
	
	@Override
	public List<InformacioItem> documentVersionList(
			String identificador,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.DOCUMENT_GET_VERSION;
		
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
				Collections.sort(vns, new Comparator<VersionNode>() {
				    public int compare(VersionNode vn1, VersionNode vn2) {
				        return vn1.getDate().compareTo(vn2.getDate());
				    }});
				
				int versio = 1;
				List<InformacioItem> informacioItems = new ArrayList<InformacioItem>();
				for (VersionNode vn: vns) {
					informacioItems.add(
							new InformacioItem(
									vn.getId(),
									null,
									ArxiuConstants.CONTINGUT_TIPUS_DOCUMENT,
									String.valueOf(versio)));
					
					versio++;
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
	
	@Override
	public String documentCsvGenerate(
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.DOCUMENT_GENERATE_CSV;
		
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
	
	@Override
	public String documentCopy(
			String nodeId,
			String targetNodeId,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.DOCUMENT_COPY;
		
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
	
	@Override
	public void documentMove(
			String nodeId,
			String targetNodeId,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.DOCUMENT_MOVE;
		
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
	
	@Override
	public String documentEniGet(
			String identificador,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.DOCUMENT_GET_ENIDOC;
		
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
	
	
	/**
	 * ================= M E T O D E S   C A R P E T E S =================
	 */
	
	@Override
	public String folderCreate(
			String pareIdentificador,
			String nom,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.FOLDER_CREATE;
		
		try {
			CreateFolder createFolder = new CreateFolder();
			Request<ParamCreateFolder> request = new Request<ParamCreateFolder>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateFolder param = new ParamCreateFolder();
			param.setParent(pareIdentificador);
			param.setFolder(
					CaibArxiuConversioHelper.toFolderNode(
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
				
				return folderNode.getId();
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
	
	@Override
	public void folderUpdate(
			String identificador,
			String nom,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.FOLDER_SET;
		
		try {
			SetFolder setFolder = new SetFolder();
			Request<ParamSetFolder> request = new Request<ParamSetFolder>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamSetFolder param = new ParamSetFolder();
			param.setFolder(CaibArxiuConversioHelper.toFolderNode(
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
	
	@Override
	public void folderDelete(
			String identificador,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.FOLDER_REMOVE;
		
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
	
	@Override
	public Carpeta folderGet(
			String identificador,
			Capsalera capsalera) throws ArxiuException {
		String metode = CaibArxiuMethods.FOLDER_GET;
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
				
				return CaibArxiuConversioHelper.toCarpeta(folderNode);
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
	
	@Override
	public String folderCopy(
			String nodeId,
			String targetNodeId,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.FOLDER_COPY;
		
		try {
			CopyFolder copyFolder = new CopyFolder();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent paramTarget = new ParamNodeID_TargetParent();
			paramTarget.setNodeId(nodeId);
			paramTarget.setTargetParent(targetNodeId);
			request.setParam(paramTarget);
			request.setServiceHeader(generarServiceHeader(capsalera));
<<<<<<< HEAD
			copyFolder.setcopyFolderRequest(request);
=======
			copyFolder.setCopyFolderRequest(request);
>>>>>>> branch 'pluginsib-1.0' of https://github.com/GovernIB/pluginsib.git
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
	
	@Override
	public void folderMove(
			String nodeId,
			String targetNodeId,
			Capsalera capsalera) throws ArxiuException {
		
		String metode = CaibArxiuMethods.FOLDER_MOVE;
		
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
	}
	
	
	/**
	 * ================ M E T O D E S   P R I V A T S ================
	 */
	
	private ServiceHeader generarServiceHeader(Capsalera capsalera) {
		ServiceHeader serviceHeader = new ServiceHeader();
		ServiceAuditInfo auditInfo = new ServiceAuditInfo();
		PersonIdentAuditInfo interessat = new PersonIdentAuditInfo();
		interessat.setName(capsalera.getInteressatNom());
		interessat.setDocument(capsalera.getInteressatNif());
		auditInfo.setApplicant(interessat);
		PublicServantAuditInfo publicServant = new PublicServantAuditInfo();
		PersonIdentAuditInfo funcionari = new PersonIdentAuditInfo();
		funcionari.setName(capsalera.getFuncionariNom());
		funcionari.setDocument(capsalera.getFuncionariNif());
		publicServant.setIdentificationData(funcionari);
		publicServant.setOrganization(capsalera.getFuncionariOrgan());
		auditInfo.setPublicServant(publicServant);
		FileAuditInfo expedient = new FileAuditInfo();
		expedient.setId(capsalera.getExpedientId());
		ProceedingsAuditInfo procediment = new ProceedingsAuditInfo();
		procediment.setId(capsalera.getProcedimentId());
		procediment.setName(capsalera.getProcedimentNom());
		expedient.setProceedings(procediment);
		auditInfo.setFile(expedient);
		auditInfo.setApplication(aplicacioCodi);
		serviceHeader.setAuditInfo(auditInfo);
		serviceHeader.setServiceVersion(SERVEI_VERSIO);
		ServiceSecurityInfo securityInfo = new ServiceSecurityInfo();
		securityInfo.setUser(usuariSgd);
		securityInfo.setPassword(contrasenyaSgd);
		serviceHeader.setSecurityInfo(securityInfo);
		return serviceHeader;
	}
	
	private ArxiuException generarExcepcioJson(
			String metode,
			JerseyResponse resposta) throws JsonParseException, JsonMappingException, IOException {
		
		ExceptionResult exceptionResult = mapper.readValue(
				resposta.getJson(),
				ExceptionResult.class);
		String code = exceptionResult.getException().getCode();
		String description = exceptionResult.getException().getDescription();

		return new ArxiuException(
				"S'ha produit un error (" + resposta.getStatus() + ") cridant el mètode " + metode +
				"\nCODI: " + code + "\nDESCRIPCIO: " + description);
	}
	
	private JerseyResponse enviarPeticioRest(
			String metode,
			Object peticio) throws UniformInterfaceException, ClientHandlerException, JsonProcessingException {
		String urlAmbMetode = (url.endsWith("/")) ? url + "services/" + metode : url + "/services/" + metode;
		String body = mapper.writeValueAsString(peticio);
		logger.debug("Enviant petició HTTP a l'arxiu (" +
				"url=" + urlAmbMetode + ", " +
				"tipus=application/json, " +
				"body=" + body + ")");
		lastJsonRequest = body;
		ClientResponse response = jerseyClient.
				resource(urlAmbMetode).
				type("application/json").
				post(ClientResponse.class, body);
		String json = response.getEntity(String.class);
		logger.debug("Rebuda resposta HTTP de l'arxiu (" +
				"status=" + response.getStatus() + ", " +
				"body=" + json + ")");
		lastJsonResponse = json;
		return new JerseyResponse(
				json,
				response.getStatus());
	}
	
	private class JerseyResponse {
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
	
	
	private static final Logger logger = LoggerFactory.getLogger(CaibArxiuClientImpl.class);
	
}