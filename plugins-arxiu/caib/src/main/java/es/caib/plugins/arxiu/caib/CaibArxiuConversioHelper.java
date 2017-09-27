package es.caib.plugins.arxiu.caib;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.io.FilenameUtils;

import com.sun.jersey.core.util.Base64;

import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.Content;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.DocumentNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.FileNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.FolderNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.Metadata;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.SummaryInfoNode;
import es.caib.arxiudigital.apirest.constantes.Aspectos;
import es.caib.arxiudigital.apirest.constantes.EstadosElaboracion;
import es.caib.arxiudigital.apirest.constantes.EstadosExpediente;
import es.caib.arxiudigital.apirest.constantes.ExtensionesFichero;
import es.caib.arxiudigital.apirest.constantes.FormatosFichero;
import es.caib.arxiudigital.apirest.constantes.MetadatosDocumento;
import es.caib.arxiudigital.apirest.constantes.MetadatosExpediente;
import es.caib.arxiudigital.apirest.constantes.OrigenesContenido;
import es.caib.arxiudigital.apirest.constantes.PerfilesFirma;
import es.caib.arxiudigital.apirest.constantes.TiposContenidosBinarios;
import es.caib.arxiudigital.apirest.constantes.TiposDocumentosENI;
import es.caib.arxiudigital.apirest.constantes.TiposFirma;
import es.caib.arxiudigital.apirest.constantes.TiposObjetoSGD;
import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.Aspectes;
import es.caib.plugins.arxiu.api.Capsalera;
import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ContingutTipus;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentContingut;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.Firma;
import es.caib.plugins.arxiu.api.InformacioItem;
import es.caib.plugins.arxiu.api.PerfilsFirma;
import es.caib.plugins.arxiu.api.TipusFirma;

public class CaibArxiuConversioHelper {
	
	
	public static Capsalera toCapsalera() {
		
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

	
	/**
	 * ================= M E T O D E S   E X P E D I E N T S ==================
	 */
	
	public static FileNode toFileNode(
			String identificador,
			String nom, 
			ExpedientMetadades metadades,
			List<Metadata> metadadesOriginals,
			String aplicacioCodi,
			List<Aspectos> aspectos,
			boolean create) throws ArxiuException {
		
		FileNode node = new FileNode();
		
		node.setType(TiposObjetoSGD.EXPEDIENTE);
		node.setId(identificador);
		node.setName(nom);
		
		node.setMetadataCollection(
				toMetadataExpedient(
						metadades,
						aplicacioCodi,
						metadadesOriginals));
		
		node.setAspects(
				toAspectos(
						aspectos,
						create));
		
		return node;
	}
	private static List<Metadata> toMetadataExpedient(
			ExpedientMetadades expedientMetadades,
			String aplicacioCodi,
			List<Metadata> metadades) throws ArxiuException{
		
		if(metadades == null)
			metadades = new ArrayList<Metadata>();
		
		addMetadata(
				metadades,
				MetadatosExpediente.CODIGO_APLICACION_TRAMITE,
				aplicacioCodi);
		addMetadata(
				metadades,
				MetadatosExpediente.CODIGO_CLASIFICACION,
				expedientMetadades.getSerieDocumental());
		addMetadata(
				metadades,
				MetadatosExpediente.IDENTIFICADOR_PROCEDIMIENTO,
				expedientMetadades.getClassificacio());
		addMetadata(
				metadades,
				MetadatosExpediente.ESTADO_EXPEDIENTE,
				toEstadosExpediente(expedientMetadades.getEstat()));
		addMetadata(
				metadades,
				MetadatosExpediente.ORGANO,
				expedientMetadades.getOrgans());
		addMetadata(
				metadades,
				MetadatosExpediente.INTERESADOS,
				expedientMetadades.getInteressats());
		addMetadata(
				metadades,
				MetadatosExpediente.FECHA_INICIO,
				formatDateIso8601(expedientMetadades.getDataObertura()));
		addMetadata(
				metadades,
				MetadatosExpediente.ORIGEN,
				toOrigenesContenido(expedientMetadades.getOrigen()));
		
		return metadades;
	}
	private static void addMetadata(
			List<Metadata> metadades,
			String qname,
			Object value) {
		
		if(value != null) {
			boolean actualitzat = false;
			for(Metadata metadata : metadades) {
				if(metadata.getQname().equals(qname)) {
					metadata.setValue(value);
					actualitzat = true;
				}
			}
			if(!actualitzat) {
				Metadata metadata = new Metadata();
				metadata.setQname(qname);
				metadata.setValue(value);
				metadades.add(metadata);
			}
		}
	}
	private static List<Aspectos> toAspectos(
			List<Aspectos> aspectos,
			boolean create) {
		
		List<Aspectos> asp = null;
		
		if (aspectos != null) {
			asp = new ArrayList<Aspectos>();
			asp.addAll(aspectos);
		}
		if (create) {
			if (asp == null) {
				asp = new ArrayList<Aspectos>();
			}
			if (asp.isEmpty()) {
				if (create) {
					asp.add(Aspectos.INTEROPERABLE);
					asp.add(Aspectos.TRANSFERIBLE);
				}
			}
		}
		
		return asp;
	}
	
	public static Expedient toExpedient(
			FileNode fileNode) throws ArxiuException {
		
		return new Expedient(
				fileNode.getId(),
				fileNode.getName(),
				toExpedientMetadades(fileNode.getMetadataCollection()),
				fileNode.getChildObjects() != null ? toInformacioItems(fileNode.getChildObjects()) : null
				);
	}
	private static ExpedientMetadades toExpedientMetadades(
			List<Metadata> metadataList) throws ArxiuException {
		
		ExpedientMetadades expedientMetadades = new ExpedientMetadades();
		
		for(Metadata metadata : metadataList) {
			switch(metadata.getQname()) {
				case MetadatosExpediente.CODIGO_CLASIFICACION:
					expedientMetadades.setSerieDocumental((String) metadata.getValue());
					break;
				case MetadatosExpediente.IDENTIFICADOR_PROCEDIMIENTO:
					expedientMetadades.setClassificacio((String) metadata.getValue());
					break;
				case MetadatosExpediente.ESTADO_EXPEDIENTE:
					if (metadata.getValue() instanceof EstadosExpediente)
						expedientMetadades.setEstat(((EstadosExpediente) metadata.getValue()).getValue());
					else if (metadata.getValue() instanceof String)
						expedientMetadades.setEstat((String) metadata.getValue());
					break;
				case MetadatosExpediente.ORGANO:
					Object preOrgan = metadata.getValue();
					if (preOrgan instanceof List<?>) {
						expedientMetadades.setOrgans((List<String>) preOrgan);
					} else if (preOrgan instanceof String) {
						List<String> organs = new ArrayList<String>();
						organs.add((String) preOrgan);
						expedientMetadades.setOrgans(organs);
					}
					break;
				case MetadatosExpediente.INTERESADOS:
					Object preInteressat = metadata.getValue();
					if (preInteressat instanceof List<?>) {
						expedientMetadades.setInteressats((List<String>) preInteressat);
					} else if (preInteressat instanceof String) {
						List<String> interessats = new ArrayList<String>();
						interessats.add((String) preInteressat);
						expedientMetadades.setInteressats(interessats);
					}
					break;
				case MetadatosExpediente.FECHA_INICIO:
					expedientMetadades.setDataObertura(parseDateIso8601((String) metadata.getValue()));
					break;
			}
		}	
		
		return expedientMetadades;
	}
	private static List<InformacioItem> toInformacioItems(
			List<SummaryInfoNode> summaryInfoNodes) {
		
		List<InformacioItem> informacioItems = new ArrayList<InformacioItem>();
		
		for(SummaryInfoNode summaryInfoNode : summaryInfoNodes) {
			informacioItems.add(new InformacioItem(
					summaryInfoNode.getId(),
					summaryInfoNode.getName(),
					toContingutTipus(
							summaryInfoNode.getType())));
		}
		
		return informacioItems;
	}
	
	public static List<InformacioItem> fileNodeToFileInformacioItem(
			List<FileNode> fileNodeList) {
		
		List<InformacioItem> informacioItemList = new ArrayList<InformacioItem>();
		
		for (FileNode fileNode : fileNodeList) {
			ContingutTipus tipus = null;
			switch (fileNode.getType()) {
				case EXPEDIENTE:
					tipus = ContingutTipus.EXPEDIENT;
					break;
				case DOCUMENTO:
					tipus = ContingutTipus.DOCUMENT;
					break;
				case DIRECTORIO:
					tipus = ContingutTipus.CARPETA;
					break;
				case DOCUMENTO_MIGRADO:
					tipus = ContingutTipus.DOCUMENT;
					break;
			}
			informacioItemList.add(
					new InformacioItem(
							fileNode.getId(),
							fileNode.getName(),
							tipus));
		}

		return informacioItemList;
	}
	
	public static String consultaFiltreToQueryString(List<ConsultaFiltre> filtres) {
//		String query = null;
//		for (ConsultaFiltre filtre: filtres) {
//			
//			switch(filtre.getOperacio()){
//			case IGUAL:
//				break;
//			case CONTE:
//				break;
//			case MENOR:
//				break;
//			case MAJOR:
//				break;
//			case ENTRE:
//				break;
//			}
//			
//		}
//		
//		return query;
		return null;
	}
	
	
	/**
	 * ================ M E T O D E S   D O C U M E N T S ================
	 */
	
	public static DocumentNode toDocumentNode(
			Document document,
			String aplicacioCodi) throws ArxiuException {
		
		DocumentNode node = new DocumentNode();
		
		node.setId(document.getIdentificador());
		node.setName(document.getNom());
		node.setType(TiposObjetoSGD.DOCUMENTO);
		node.setMetadataCollection(
				toMetadataDocument(
						document,
						aplicacioCodi));
		node.setBinaryContents(toContents(document));
		node.setAspects(toApiAspectes(document.getAspectes()));
		
		return node;
	}
	private static List<Metadata> toMetadataDocument(
			Document document,
			String aplicacioCodi) throws ArxiuException{
		
		DocumentMetadades documentMetadades = document.getMetadades();
		
		Metadata metadata;
		List<Metadata> metadades = new ArrayList<Metadata>();
		
		metadata = new Metadata();
		metadata.setQname(MetadatosExpediente.CODIGO_APLICACION_TRAMITE);
		metadata.setValue(aplicacioCodi);
		metadades.add(metadata);
		
		if (documentMetadades.getOrigen() != null) {
			metadata = new Metadata();
			metadata.setQname(MetadatosDocumento.ORIGEN);
			metadata.setValue(toOrigenesContenido(documentMetadades.getOrigen()));
			metadades.add(metadata);
		}
		
		if (documentMetadades.getData() != null){
			metadata = new Metadata();
			metadata.setQname(MetadatosDocumento.FECHA_INICIO);
			metadata.setValue(formatDateIso8601(documentMetadades.getData()));
			metadades.add(metadata);
		}
		
		if (documentMetadades.getEstatElaboracio() != null) {
			metadata = new Metadata();
			metadata.setQname(MetadatosDocumento.ESTADO_ELABORACION);
			metadata.setValue(toEstadosElaboracion(documentMetadades.getEstatElaboracio()));
			metadades.add(metadata);
		}
		
		if (documentMetadades.getTipusDocumental() != null) {
			metadata = new Metadata();
			metadata.setQname(MetadatosDocumento.TIPO_DOC_ENI);
			metadata.setValue(toTiposDocumentosEni(documentMetadades.getTipusDocumental()));
			metadades.add(metadata);
		}
		
		if (documentMetadades.getTipoFirma() != null) {
			metadata = new Metadata();
			metadata.setQname(MetadatosDocumento.TIPO_FIRMA);
			metadata.setValue(documentMetadades.getTipoFirma().name());
			metadades.add(metadata);
		}
		
		if (documentMetadades.getPerfilFirma() != null) {
			metadata = new Metadata();
			metadata.setQname(MetadatosDocumento.PERFIL_FIRMA);
			metadata.setValue(documentMetadades.getPerfilFirma().getValue());
			metadades.add(metadata);
		}
		
		String formatNom = FilenameUtils.getExtension(document.getNom()).toUpperCase();
		if (formatNom != null && !formatNom.isEmpty()) {
			metadata = new Metadata();
			metadata.setQname(MetadatosDocumento.NOMBRE_FORMATO);
			metadata.setValue(toFormatosFichero(formatNom));
			metadades.add(metadata);
			
			metadata = new Metadata();
			metadata.setQname(MetadatosDocumento.EXTENSION_FORMATO);
			metadata.setValue(toExtensionesFichero(formatNom));
			metadades.add(metadata);
		}
		
		if (documentMetadades.getOrgans() != null) {
			metadata = new Metadata();
			metadata.setQname(MetadatosDocumento.ORGANO);
			metadata.setValue(documentMetadades.getOrgans());
			metadades.add(metadata);
		}
		
//		Firma firma = null;
//		if(document.getFirmes() != null) firma = document.getFirmes().get(0);
//		String tipoFirma = null;
//		if(firma != null) tipoFirma = firma.getTipus();
//		if (tipoFirma != null) {
//			metadades.put(
//					MetadatosDocumento.TIPO_FIRMA,
//					tipoFirma);
//		}
//		String perfilFirma = null;
//		if(firma != null) perfilFirma = firma.getTipusMime();
//		if (perfilFirma != null) {
//			metadades.put(
//					MetadatosDocumento.PERFIL_FIRMA,
//					perfilFirma);
//		}
//		String csv = null;
//		if(firma != null) csv = firma.getCsvRegulacio();
//		if (csv != null) {
//			metadades.put(
//					MetadatosDocumento.CSV,
//					csv);
//		}
		
		if (documentMetadades.getSerieDocumental() != null) {
			metadata = new Metadata();
			metadata.setQname(MetadatosDocumento.CODIGO_CLASIFICACION);
			metadata.setValue(documentMetadades.getSerieDocumental());
			metadades.add(metadata);
		}
		
		return metadades;
	}	
	private static List<Content> toContents(
			Document document) {
		
		List<Content> contents = new ArrayList<Content>();
		if (document.getContingut() != null) {
			Content content = new Content();
			
			content.setEncoding("UTF-8");
			content.setMimetype(document.getContingut().getTipusMime());
			content.setContent(new String(Base64.encode(document.getContingut().getContingut())));
			
			contents.add(content);
		}
		
		if (document.getFirmes() != null) {
			
			for (Firma firma : document.getFirmes()) {
				Content contenidofirma = new Content();
		        contenidofirma.setBinaryType(TiposContenidosBinarios.SIGNATURE);
		        contenidofirma.setContent(firma.getContingut() != null ? new String(firma.getContingut()) : null);
		        contenidofirma.setEncoding("UTF-8");
		        contenidofirma.setMimetype(firma.getTipusMime());

		        contents.add(contenidofirma);
		    }
		}
		
		return contents;
	}
	
	public static Document toDocument(
			DocumentNode documentNode) throws ArxiuException {
		
		Document document = new Document(
				documentNode.getId(),
				documentNode.getName(),
				toDocumentContingut(
						documentNode.getBinaryContents()),
				toDocumentMetadades(
						documentNode.getMetadataCollection()),
				null);
		
		return document;
	}
	private static DocumentContingut toDocumentContingut(
			List<Content> contents) {
		
		if(contents == null || contents.size() <= 0) return null;
		Content content = contents.get(0);

		return new DocumentContingut(
				Base64.encode(content.getContent()), 
				content.getMimetype(), 
				null);
	}
	private static List<Aspectos> toApiAspectes(
			List<Aspectes> aspectes) {
		
		List<Aspectos> aspectsRetorn = new ArrayList<Aspectos>();
		for (Aspectes aspecte: aspectes) {
			aspectsRetorn.add(Aspectos.valueOf(aspecte.name()));
		}
		return aspectsRetorn;
	}
	private static DocumentMetadades toDocumentMetadades(
			List<Metadata> metadatas) throws ArxiuException {
		
		String 
				origen = null,
				estatElaboracio = null,
				tipusDocumental = null,
				serieDocumental = null;
				
		TipusFirma tipoFirma = null;
		PerfilsFirma perfilFirma = null;
		List<String> organs = null;
		Date data = null;
		for(Metadata metadata : metadatas) {
			switch(metadata.getQname()) {
				case MetadatosDocumento.ORIGEN:
					origen = String.valueOf(metadata.getValue());
					break;
				case MetadatosDocumento.FECHA_INICIO:
					data = parseDateIso8601((String) metadata.getValue());
					break;
				case MetadatosDocumento.ESTADO_ELABORACION:
					estatElaboracio = (String) metadata.getValue();
					break;
				case MetadatosDocumento.TIPO_DOC_ENI:
					estatElaboracio = (String) metadata.getValue();
					break;
				case MetadatosDocumento.ORGANO:
					Object preValor = metadata.getValue();
					if (preValor instanceof List<?>) {
						organs = (List<String>) metadata.getValue();
					} else {
						organs = new ArrayList<String>();
						organs.add((String) preValor);
					}
					break;
				case MetadatosDocumento.CODIGO_CLASIFICACION:
					serieDocumental = (String) metadata.getValue();
					break;
					
				case MetadatosDocumento.TIPO_FIRMA:
					tipoFirma = (TipusFirma) metadata.getValue();
					break;
				case MetadatosDocumento.PERFIL_FIRMA:
					perfilFirma = (PerfilsFirma) metadata.getValue();
					break;
			}
		}
		
		return new DocumentMetadades(
				null, //identificador
				null, //versioNti
				organs,
				data,
				origen,
				estatElaboracio,
				tipusDocumental,
				serieDocumental,
				tipoFirma,
				perfilFirma,
				null);//metadadesAddicionals
	}
	
	public static List<InformacioItem> fileNodeToDocumentInformacioItem(
			List<DocumentNode> documentNodeList) {
		
		List<InformacioItem> informacioItemList = new ArrayList<InformacioItem>();
		
		for (DocumentNode documentNode : documentNodeList) {
			ContingutTipus tipus = null;
			switch (documentNode.getType()) {
				case EXPEDIENTE:
					tipus = ContingutTipus.EXPEDIENT;
					break;
				case DOCUMENTO:
					tipus = ContingutTipus.DOCUMENT;
					break;
				case DIRECTORIO:
					tipus = ContingutTipus.CARPETA;
					break;
				case DOCUMENTO_MIGRADO:
					tipus = ContingutTipus.DOCUMENT;
					break;
			}
			informacioItemList.add(
					new InformacioItem(
							documentNode.getId(),
							documentNode.getName(),
							tipus));
		}

		return informacioItemList;
	}
	
	
	/**
	 * ================ M E T O D E S   C A R P E T E S ================
	 */
	
	public static FolderNode toFolderNode(
			String identificador,
			String nom) {
		
		FolderNode node = new FolderNode();
		
		node.setId(identificador);
		node.setName(nom);
		node.setType(TiposObjetoSGD.DIRECTORIO);
		
		return node;
	}
	
	public static Carpeta toCarpeta(
			FolderNode folderNode) {
		
		Carpeta carpeta = new Carpeta(
				folderNode.getId(),
				folderNode.getName(),
				toInformacioItems(folderNode.getChildObjects()));
		
		return carpeta;
	}
	
	
	/**
	 * ================ M E T O D E S   P R I V A T S ================
	 */
	
	private static EstadosExpediente toEstadosExpediente(
			String estat) throws ArxiuException {
		
		if (estat == null) return null;
		
		switch (estat) {
			case "INDICE_REMISION":
				return EstadosExpediente.INDICE_REMISION;
			case "ABIERTO":
				return EstadosExpediente.ABIERTO;
			case "CERRADO":
				return EstadosExpediente.CERRADO;
			default:
				throw new ArxiuException(
						"No s'ha pogut convertir el valor per l'enumeració ArxiuExpedientEstat (" +
						"valor=" + estat + ")");
		}
	}
	private static String formatDateIso8601(Date date) {
		
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(tz);
		
		return df.format(date);
	}
	
	private static ContingutTipus toContingutTipus(
			TiposObjetoSGD tiposObjetoSGD) {
		
		switch(tiposObjetoSGD) {
			case DIRECTORIO : return ContingutTipus.CARPETA;
			case EXPEDIENTE : return ContingutTipus.EXPEDIENT;
			case DOCUMENTO : return ContingutTipus.DOCUMENT;
			case DOCUMENTO_MIGRADO : return ContingutTipus.DOCUMENT;
			default : return null;
		}
	}
	
	private static OrigenesContenido toOrigenesContenido(String origen) throws ArxiuException {
		
		if(origen == null) return null;
		
		switch (origen) {
			case "ADMINISTRACIO":
				return OrigenesContenido.ADMINISTRACION;
			case "CIUTADA":
				return OrigenesContenido.CIUDADANO;
			default:
				throw new ArxiuException(
						"No s'ha pogut convertir el valor per l'enumeració Origen (" + "valor=" + origen + ")");
		}
	}
	
	private static EstadosElaboracion toEstadosElaboracion(
			String estatElaboracio) throws ArxiuException {
		
		if (estatElaboracio == null) return null;
		
		switch (estatElaboracio) {
			case "OTROS":
				return EstadosElaboracion.OTROS;
			case "COPIA_AUTENTICA_FORMATO":
				return EstadosElaboracion.COPIA_AUTENTICA_FORMATO;
			case "COPIA_AUTENTICA_PAPEL":
				return EstadosElaboracion.COPIA_AUTENTICA_PAPEL;
			case "COPIA_AUTENTICA_PARCIAL":
				return EstadosElaboracion.COPIA_AUTENTICA_PARCIAL;
			case "ORIGINAL":
				return EstadosElaboracion.ORIGINAL;
			default:
				throw new ArxiuException(
						"No s'ha pogut convertir el valor per l'enumeració ArxiuEstatElaboracio (" +
						"valor=" + estatElaboracio + ")");
		}
	}
	
	private static TiposDocumentosENI toTiposDocumentosEni(
			String documentTipus) throws ArxiuException {
		if (documentTipus == null) return null;
		
		switch (documentTipus) {
			case "ACORD":
				return TiposDocumentosENI.ACUERDO;
			case "ACTA":
				return TiposDocumentosENI.ACTA;
			case "ALEGACIO":
				return TiposDocumentosENI.ALEGACION;
			case "ALTRES":
				return TiposDocumentosENI.OTROS;
			case "ALTRES_INCAUTATS":
				return TiposDocumentosENI.OTROS_INCAUTADOS;
			case "CERTIFICAT":
				return TiposDocumentosENI.CERTIFICADO;
			case "COMUNICACIO":
				return TiposDocumentosENI.COMUNICACION;
			case "COMUNICACIO_CIUTADA":
				return TiposDocumentosENI.COMUNICACION_CIUDADANO;
			case "CONTRACTE":
				return TiposDocumentosENI.CONTRATO;
			case "CONVENI":
				return TiposDocumentosENI.CONVENIO;
			case "DECLARACIO":
				return TiposDocumentosENI.DECLARACION;
			case "DENUNCIA":
				return TiposDocumentosENI.DENUNCIA;
			case "DILIGENCIA":
				return TiposDocumentosENI.DILIGENCIA;
			case "FACTURA":
				return TiposDocumentosENI.FACTURA;
			case "INFORME":
				return TiposDocumentosENI.INFORME;
			case "JUSTIFICANT_RECEPCIO":
				return TiposDocumentosENI.ACUSE_DE_RECIBO;
			case "NOTIFICACIO":
				return TiposDocumentosENI.NOTIFICACION;
			case "PUBLICACIO":
				return TiposDocumentosENI.PUBLICACION;
			case "RECURSOS":
				return TiposDocumentosENI.RECURSOS;
			case "RESOLUCIO":
				return TiposDocumentosENI.RESOLUCION;
			case "SOLICITUD":
				return TiposDocumentosENI.SOLICITUD;
			default:
				throw new ArxiuException(
						"No s'ha pogut convertir el valor per l'enumeració ArxiuDocumentTipus (" +
						"valor=" + documentTipus + ")");
		}
	}
	
	private static FormatosFichero toFormatosFichero(
			String formatNom) throws ArxiuException {
		
		if (formatNom == null) return null;
		
		switch (formatNom) {
			case "AVI":
				return FormatosFichero.AVI;
			case "CSS":
				return FormatosFichero.CSS;
			case "CSV":
				return FormatosFichero.CSV;
			case "GML":
				return FormatosFichero.GML;
			case "GZIP":
				return FormatosFichero.GZIP;
			case "HTML":
				return FormatosFichero.HTML;
			case "JPEG":
				return FormatosFichero.JPEG;
			case "MHTML":
				return FormatosFichero.MHTML;
			case "MP3":
				return FormatosFichero.MP3;
			case "MP4A":
				return FormatosFichero.MP4A;
			case "MP4V":
				return FormatosFichero.MP4V;
			case "OASIS12":
				return FormatosFichero.OASIS12;
			case "OGG":
				return FormatosFichero.OGG;
			case "PDF":
				return FormatosFichero.PDF;
			case "PDFA":
				return FormatosFichero.PDFA;
			case "PNG":
				return FormatosFichero.PNG;
			case "RTF":
				return FormatosFichero.RTF;
			case "SOXML":
				return FormatosFichero.SOXML;
			case "SVG":
				return FormatosFichero.SVG;
			case "TIFF":
				return FormatosFichero.TIFF;
			case "TXT":
				return FormatosFichero.TXT;
			case "WEBM":
				return FormatosFichero.WEBM;
			case "WFS":
				return FormatosFichero.WFS;
			case "WMS":
				return FormatosFichero.WMS;
			case "XHTML":
				return FormatosFichero.XHTML;
			case "ZIP":
				return FormatosFichero.ZIP;
			default:
				throw new ArxiuException(
						"No s'ha pogut convertir el valor per l'enumeració ArxiuFormatNom (" +
						"valor=" + formatNom + ")");
		}
	}

	private static ExtensionesFichero toExtensionesFichero(
			String formatExtensio) throws ArxiuException {
		
		if (formatExtensio == null) return null;
		
		switch (formatExtensio) {
			case "AVI":
				return ExtensionesFichero.AVI;
			case "CSS":
				return ExtensionesFichero.CSS;
			case "CSV":
				return ExtensionesFichero.CSV;
			case "DOCX":
				return ExtensionesFichero.DOCX;
			case "GML":
				return ExtensionesFichero.GML;
			case "GZ":
				return ExtensionesFichero.GZ;
			case "HTM":
				return ExtensionesFichero.HTM;
			case "HTML":
				return ExtensionesFichero.HTML;
			case "JPEG":
				return ExtensionesFichero.JPEG;
			case "JPG":
				return ExtensionesFichero.JPG;
			case "MHT":
				return ExtensionesFichero.MHT;
			case "MHTML":
				return ExtensionesFichero.MHTML;
			case "MP3":
				return ExtensionesFichero.MP3;
			case "MP4":
				return ExtensionesFichero.MP4;
			case "MPEG":
				return ExtensionesFichero.MPEG;
			case "ODG":
				return ExtensionesFichero.ODG;
			case "ODP":
				return ExtensionesFichero.ODP;
			case "ODS":
				return ExtensionesFichero.ODS;
			case "ODT":
				return ExtensionesFichero.ODT;
			case "OGA":
				return ExtensionesFichero.OGA;
			case "OGG":
				return ExtensionesFichero.OGG;
			case "PDF":
				return ExtensionesFichero.PDF;
			case "PNG":
				return ExtensionesFichero.PNG;
			case "PPTX":
				return ExtensionesFichero.PPTX;
			case "RTF":
				return ExtensionesFichero.RTF;
			case "SVG":
				return ExtensionesFichero.SVG;
			case "TIFF":
				return ExtensionesFichero.TIFF;
			case "TXT":
				return ExtensionesFichero.TXT;
			case "WEBM":
				return ExtensionesFichero.WEBM;
			case "XLSX":
				return ExtensionesFichero.XLSX;
			case "ZIP":
				return ExtensionesFichero.ZIP;
			default:
				throw new ArxiuException(
						"No s'ha pogut convertir el valor per l'enumeració ArxiuFormatExtensio (" +
						"valor=" + formatExtensio + ")");
			}
	}
	
	private static TiposFirma toTipoFirma(String tipoFirma) throws ArxiuException {
		if(tipoFirma == null) return null;
		switch (tipoFirma) {
			case "CSV":
				return TiposFirma.CSV;
			case "XADES_INTERNALLY":
				return TiposFirma.XADES_INTERNALLY;
			case "XADES_ENVELOPED":
				return TiposFirma.XADES_ENVELOPED;
			case "CADES_DETACHED":
				return TiposFirma.CADES_DETACHED;
			case "CADES_ATTACHED":
				return TiposFirma.CADES_ATTACHED;
			case "PADES":
				return TiposFirma.PADES;
			default:
				throw new ArxiuException(
						"No s'ha pogut convertir el valor per l'enumeració TipoFirma (" + "valor=" + tipoFirma + ")");
		}
	}
	
	private static PerfilesFirma toPerfilFirma(String perfilFirma) throws ArxiuException {
		if(perfilFirma == null) return null;
		switch (perfilFirma) {
			case "EPES":
				return PerfilesFirma.EPES;
			case "LTV":
				return PerfilesFirma.LTV;
			case "T":
				return PerfilesFirma.T;
			case "C":
				return PerfilesFirma.C;
			case "X":
				return PerfilesFirma.X;
			case "XL":
				return PerfilesFirma.XL;
			case "A":
				return PerfilesFirma.A;
			default:
				throw new ArxiuException(
						"No s'ha pogut convertir el valor per l'enumeració PerfilesFirma (" + "valor=" + perfilFirma + ")");
		}
	}
	
	private static Date parseDateIso8601(String date) throws ArxiuException {
		
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		df.setTimeZone(tz);
		
		try {
			return df.parse(date);
		} catch (ParseException e) {
			throw new ArxiuException(
					"No s'ha pogut parsejar el valor per el camp Data (" +
					"valor=" + date + ")");
		}
	}
	
}
