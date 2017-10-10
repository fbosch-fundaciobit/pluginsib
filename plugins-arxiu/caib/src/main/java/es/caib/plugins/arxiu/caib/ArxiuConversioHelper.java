/**
 * 
 */
package es.caib.plugins.arxiu.caib;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
import es.caib.arxiudigital.apirest.constantes.MetadatosDocumento;
import es.caib.arxiudigital.apirest.constantes.MetadatosExpediente;
import es.caib.arxiudigital.apirest.constantes.OrigenesContenido;
import es.caib.arxiudigital.apirest.constantes.TiposContenidosBinarios;
import es.caib.arxiudigital.apirest.constantes.TiposDocumentosENI;
import es.caib.arxiudigital.apirest.constantes.TiposObjetoSGD;
import es.caib.plugins.arxiu.api.ArxiuConstants;
import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ContingutArxiu;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentContingut;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.Firma;

/**
 * Mètodes d'ajuda per a la conversió de tipus entre l'API REST de
 * l'arxiu i el plugin.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuConversioHelper {
	
	
	public static FileNode expedientToFileNode(
			Expedient expedient,
			List<Metadata> metadadesPrevies,
			List<Aspectos> aspectesPrevis,
			String aplicacioCodi,
			boolean creacio) throws ArxiuException {
		FileNode node = new FileNode();
		node.setType(TiposObjetoSGD.EXPEDIENTE);
		node.setId(expedient.getIdentificador());
		node.setName(expedient.getNom());
		node.setMetadataCollection(
				toMetadataExpedient(
						expedient.getMetadades(),
						aplicacioCodi,
						metadadesPrevies));
		node.setAspects(
				generarAspectes(
						aspectesPrevis,
						creacio));
		return node;
	}

	public static Expedient fileNodeToExpedient(
			FileNode fileNode) throws ArxiuException {
		Expedient expedient = new Expedient();
		expedient.setIdentificador(
				fileNode.getId());
		expedient.setNom(
				fileNode.getName());
		expedient.setMetadades(
				toExpedientMetadades(fileNode.getMetadataCollection()));
		expedient.setContinguts(
				summaryInfoNodesToInformacioItems(fileNode.getChildObjects()));
		return expedient;
	}

	public static List<ContingutArxiu> fileNodesToFileContingutArxiu(
			List<FileNode> fileNodeList) {
		if (fileNodeList == null) {
			return null;
		}
		List<ContingutArxiu> informacioItemList = new ArrayList<ContingutArxiu>();
		for (FileNode fileNode: fileNodeList) {
			informacioItemList.add(
					crearContingutArxiu(
							fileNode.getId(),
							fileNode.getName(),
							toContingutTipus(fileNode.getType()),
							null));
		}
		return informacioItemList;
	}

	public static DocumentNode documentToDocumentNode(
			Document document,
			List<Metadata> metadadesPrevies,
			List<Aspectos> aspectesPrevis,
			String aplicacioCodi,
			String csv,
			String csvDef,
			boolean creacio) throws ArxiuException {
		DocumentNode node = new DocumentNode();
		node.setId(document.getIdentificador());
		node.setName(document.getNom());
		node.setType(TiposObjetoSGD.DOCUMENTO);
		node.setBinaryContents(
				toContents(document));
		node.setMetadataCollection(
				toMetadataDocument(
						document.getMetadades(),
						document.getFirmes(),
						aplicacioCodi,
						metadadesPrevies,
						csv,
						csvDef));
		node.setAspects(
				generarAspectes(
						aspectesPrevis,
						creacio));
		return node;
	}

	public static Document documentNodeToDocument(
			DocumentNode documentNode) throws ArxiuException {
		Document document = new Document();
		document.setIdentificador(
				documentNode.getId());
		document.setNom(
				documentNode.getName());
		document.setContingut(
				toDocumentContingut(
						documentNode.getBinaryContents()));
		document.setMetadades(
				toDocumentMetadades(
						documentNode.getMetadataCollection()));
		document.setFirmes(
				toDocumentFirmes(
						documentNode.getBinaryContents(),
						documentNode.getMetadataCollection()));
		return document;
	}

	public static List<ContingutArxiu> fileNodeToDocumentContingut(
			List<DocumentNode> documentNodeList) {
		List<ContingutArxiu> informacioItemList = new ArrayList<ContingutArxiu>();
		for (DocumentNode documentNode : documentNodeList) {
			informacioItemList.add(
					crearContingutArxiu(
							documentNode.getId(),
							documentNode.getName(),
							toContingutTipus(documentNode.getType()),
							null));
		}
		return informacioItemList;
	}

	public static FolderNode toFolderNode(
			String identificador,
			String nom) {
		FolderNode node = new FolderNode();
		node.setId(identificador);
		node.setName(nom);
		node.setType(TiposObjetoSGD.DIRECTORIO);
		return node;
	}

	public static Carpeta folderNodeToCarpeta(
			FolderNode folderNode) {
		Carpeta carpeta = new Carpeta();
		carpeta.setIdentificador(
				folderNode.getId());
		carpeta.setNom(
				folderNode.getName());
		carpeta.setContinguts(
				summaryInfoNodesToInformacioItems(folderNode.getChildObjects()));
		return carpeta;
	}



	private static List<Metadata> toMetadataExpedient(
			ExpedientMetadades expedientMetadades,
			String aplicacioCodi,
			List<Metadata> metadadesPrevies) throws ArxiuException {
		List<Metadata> metadades = new ArrayList<Metadata>();
		if (metadadesPrevies != null) {
			metadades.addAll(metadadesPrevies);
		}
		addMetadata(
				metadades,
				MetadatosExpediente.CODIGO_APLICACION_TRAMITE,
				aplicacioCodi);
		if (expedientMetadades != null) {
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
			if (expedientMetadades.getMetadadesAddicionals() != null) {
				for (String metadada: expedientMetadades.getMetadadesAddicionals().keySet()) {
					addMetadata(
							metadades,
							metadada,
							expedientMetadades.getMetadadesAddicionals().get(metadada));
				}
			}
		}
		return metadades;
	}

	private static void addMetadata(
			List<Metadata> metadades,
			String qname,
			Object value) {
		if (value != null) {
			boolean actualitzat = false;
			for (Metadata metadata: metadades) {
				if (metadata.getQname().equals(qname)) {
					metadata.setValue(value);
					actualitzat = true;
				}
			}
			if (!actualitzat) {
				Metadata metadata = new Metadata();
				metadata.setQname(qname);
				metadata.setValue(value);
				metadades.add(metadata);
			}
		}
	}

	private static List<Aspectos> generarAspectes(
			List<Aspectos> aspectesPrevis,
			boolean create) {
		List<Aspectos> aspectesCreats = null;
		if (aspectesPrevis != null && !aspectesPrevis.isEmpty()) {
			aspectesCreats = new ArrayList<Aspectos>();
			aspectesCreats.addAll(aspectesPrevis);
		}
		if (create && aspectesCreats == null) {
			aspectesCreats = new ArrayList<Aspectos>();
			aspectesCreats.add(Aspectos.INTEROPERABLE);
			aspectesCreats.add(Aspectos.TRANSFERIBLE);
		}
		return aspectesCreats;
	}

	@SuppressWarnings("unchecked")
	private static ExpedientMetadades toExpedientMetadades(
			List<Metadata> metadataList) throws ArxiuException {
		ExpedientMetadades expedientMetadades = new ExpedientMetadades();
		for (Metadata metadata: metadataList) {
			switch (metadata.getQname()) {
			case MetadatosDocumento.ID_ENI:
				expedientMetadades.setIdentificador(
						(String)metadata.getValue());
				break;
			case "eni:v_nti":
				expedientMetadades.setVersioNti(
						(String)metadata.getValue());
				break;
			case MetadatosExpediente.ORIGEN:
				Integer origen = (Integer)metadata.getValue();
				if (origen != null) {
					expedientMetadades.setOrigen(
							origen.toString());
				}
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
			case MetadatosExpediente.FECHA_INICIO:
				expedientMetadades.setDataObertura(parseDateIso8601((String) metadata.getValue()));
				break;
			case MetadatosExpediente.IDENTIFICADOR_PROCEDIMIENTO:
				expedientMetadades.setClassificacio(
						(String)metadata.getValue());
				break;
			case MetadatosExpediente.ESTADO_EXPEDIENTE:
				if (metadata.getValue() instanceof EstadosExpediente) {
					expedientMetadades.setEstat(
							((EstadosExpediente)metadata.getValue()).getValue());
				} else if (metadata.getValue() instanceof String) {
					expedientMetadades.setEstat(
							(String)metadata.getValue());
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
			case MetadatosExpediente.CODIGO_CLASIFICACION:
				expedientMetadades.setSerieDocumental(
						(String)metadata.getValue());
				break;
			}
		}	
		
		return expedientMetadades;
	}

	private static List<ContingutArxiu> summaryInfoNodesToInformacioItems(
			List<SummaryInfoNode> summaryInfoNodes) {
		if (summaryInfoNodes == null) {
			return null;
		}
		List<ContingutArxiu> informacioItems = new ArrayList<ContingutArxiu>();
		for (SummaryInfoNode summaryInfoNode : summaryInfoNodes) {
			informacioItems.add(
					crearContingutArxiu(
							summaryInfoNode.getId(),
							summaryInfoNode.getName(),
							toContingutTipus(summaryInfoNode.getType()),
							null));
		}
		return informacioItems;
	}

	private static List<Metadata> toMetadataDocument(
			DocumentMetadades documentMetadades,
			List<Firma> firmes,
			String aplicacioCodi,
			List<Metadata> metadadesPrevies,
			String csv,
			String csvDef) throws ArxiuException {
		List<Metadata> metadades = new ArrayList<Metadata>();
		if (metadadesPrevies != null) {
			metadades.addAll(metadadesPrevies);
		}
		addMetadata(
				metadades,
				MetadatosExpediente.CODIGO_APLICACION_TRAMITE,
				aplicacioCodi);
		if (documentMetadades != null) {
			addMetadata(
					metadades,
					MetadatosDocumento.ORIGEN,
					toOrigenesContenido(documentMetadades.getOrigen()));
			addMetadata(
					metadades,
					MetadatosDocumento.FECHA_INICIO,
					formatDateIso8601(documentMetadades.getDataCaptura()));
			addMetadata(
					metadades,
					MetadatosDocumento.ESTADO_ELABORACION,
					toEstadosElaboracion(documentMetadades.getEstatElaboracio()));
			addMetadata(
					metadades,
					MetadatosDocumento.TIPO_DOC_ENI,
					toTiposDocumentosEni(documentMetadades.getTipusDocumental()));
			addMetadata(
					metadades,
					MetadatosDocumento.NOMBRE_FORMATO,
					documentMetadades.getFormat());
			addMetadata(
					metadades,
					MetadatosDocumento.EXTENSION_FORMATO,
					documentMetadades.getExtensio());
			addMetadata(
					metadades,
					MetadatosDocumento.ORGANO,
					documentMetadades.getOrgans());
			addMetadata(
					metadades,
					MetadatosDocumento.CODIGO_CLASIFICACION,
					documentMetadades.getSerieDocumental());
			if (documentMetadades.getMetadadesAddicionals() != null) {
				for (String clau: documentMetadades.getMetadadesAddicionals().keySet()) {
					addMetadata(
							metadades,
							clau,
							documentMetadades.getMetadadesAddicionals().get(clau));
				}
			}
		}
		if (firmes != null) {
			boolean tipusFirmaConfigurat = false;
			for (Firma firma: firmes) {
				if (ArxiuConstants.FIRMA_TIPUS_CSV.equals(firma.getTipus())) {
					if (firma.getContingut() != null) {
						addMetadata(
								metadades,
								MetadatosDocumento.CSV,
								new String(firma.getContingut()));
					}
					addMetadata(
							metadades,
							MetadatosDocumento.DEF_CSV,
							firma.getCsvRegulacio());
				} else if (!tipusFirmaConfigurat) {
					addMetadata(
							metadades,
							MetadatosDocumento.TIPO_FIRMA,
							firma.getTipus());
					addMetadata(
							metadades,
							MetadatosDocumento.PERFIL_FIRMA,
							firma.getPerfil());
					tipusFirmaConfigurat = true;
				}
			}
		}
		addMetadata(
				metadades,
				MetadatosDocumento.CSV,
				csv);
		addMetadata(
				metadades,
				MetadatosDocumento.DEF_CSV,
				csvDef);
		return metadades;
	}

	private static List<Content> toContents(
			Document document) {
		List<Content> contents = null;
		if (document.getContingut() != null) {
			Content content = new Content();
			content.setBinaryType(TiposContenidosBinarios.CONTENT);
			content.setEncoding("UTF-8");
			content.setMimetype(document.getContingut().getTipusMime());
			content.setContent(
					new String(Base64.encode(document.getContingut().getContingut())));
			if (contents == null) {
				contents = new ArrayList<Content>();
			}
			contents.add(content);
		}
		if (document.getFirmes() != null) {
			for (Firma firma: document.getFirmes()) {
				if (!ArxiuConstants.FIRMA_TIPUS_CSV.equals(firma.getTipus())) {
					Content contenidofirma = new Content();
			        contenidofirma.setBinaryType(TiposContenidosBinarios.SIGNATURE);
			        if (firma.getContingut() != null) {
				        contenidofirma.setContent(
								new String(Base64.encode(firma.getContingut())));
			        }
			        contenidofirma.setEncoding("UTF-8");
			        contenidofirma.setMimetype(firma.getTipusMime());
			        if (contents == null) {
						contents = new ArrayList<Content>();
					}
			        contents.add(contenidofirma);
				}
		    }
		}
		return contents;
	}

	private static DocumentContingut toDocumentContingut(
			List<Content> contents) {
		if (contents == null) {
			return null;
		}
		for (Content content: contents) {
			if (TiposContenidosBinarios.CONTENT.equals(content.getBinaryType())) {
				DocumentContingut contingut = new DocumentContingut();
				contingut.setContingut(Base64.decode(content.getContent()));
				contingut.setTipusMime(content.getMimetype());
				return contingut;
			}
		}
		return null;
	}
	private static List<Firma> toDocumentFirmes(
			List<Content> contents,
			List<Metadata> metadades) {
		if (contents == null) {
			return null;
		}
		List<Firma> firmes = null;
		String firmaTipus = null;
		String firmaPerfil = null;
		String firmaCsv = null;
		String firmaCsvRegulacio = null;
		for (Metadata metadada: metadades) {
			if (MetadatosDocumento.TIPO_FIRMA.equals(metadada.getQname())) {
				firmaTipus = (String)metadada.getValue();
			} else if (MetadatosDocumento.PERFIL_FIRMA.equals(metadada.getQname())) {
				firmaPerfil = (String)metadada.getValue();
			} else if (MetadatosDocumento.CSV.equals(metadada.getQname())) {
				firmaCsv = (String)metadada.getValue();
			} else if (MetadatosDocumento.DEF_CSV.equals(metadada.getQname())) {
				firmaCsvRegulacio = (String)metadada.getValue();
			}
		}
		if (firmaCsv != null) {
			Firma firma = new Firma();
			firma.setTipus(ArxiuConstants.FIRMA_TIPUS_CSV);
			firma.setContingut(firmaCsv.getBytes());
			firma.setTipusMime("text/plain");
			firma.setCsvRegulacio(firmaCsvRegulacio);
			if (firmes == null) {
				 firmes = new ArrayList<Firma>();
			}
			firmes.add(firma);
		}
		for (Content content: contents) {
			if (TiposContenidosBinarios.SIGNATURE.equals(content.getBinaryType())) {
				Firma firma = new Firma();
				firma.setTipus(firmaTipus);
				firma.setPerfil(firmaPerfil);
				firma.setContingut(Base64.decode(content.getContent()));
				firma.setTipusMime(content.getMimetype());
				if (firmes == null) {
					 firmes = new ArrayList<Firma>();
				}
				firmes.add(firma);
			}
		}
		return firmes;
	}

	@SuppressWarnings("unchecked")
	private static DocumentMetadades toDocumentMetadades(
			List<Metadata> metadatas) throws ArxiuException {
		DocumentMetadades metadades = new DocumentMetadades();
		for (Metadata metadata: metadatas) {
			switch(metadata.getQname()) {
			case MetadatosDocumento.ID_ENI:
				metadades.setIdentificador(
						(String)metadata.getValue());
				break;
			case MetadatosDocumento.ORIGEN:
				metadades.setOrigen(
						String.valueOf(metadata.getValue()));
				break;
			case MetadatosDocumento.FECHA_INICIO:
				metadades.setDataCaptura(
						parseDateIso8601((String)metadata.getValue()));
				break;
			case MetadatosDocumento.ESTADO_ELABORACION:
				metadades.setEstatElaboracio(
						(String)metadata.getValue());
				break;
			case MetadatosDocumento.TIPO_DOC_ENI:
				metadades.setTipusDocumental(
						(String)metadata.getValue());
				break;
			case MetadatosDocumento.ORGANO:
				Object preValor = metadata.getValue();
				if (preValor instanceof List<?>) {
					metadades.setOrgans(
							(List<String>)metadata.getValue());
				} else {
					metadades.setOrgans(
							Arrays.asList((String)preValor));
				}
				break;
			case MetadatosDocumento.CODIGO_CLASIFICACION:
				metadades.setSerieDocumental(
						(String)metadata.getValue());
				break;
			case MetadatosDocumento.NOMBRE_FORMATO:
				metadades.setFormat(
						(String)metadata.getValue());
				break;
			case MetadatosDocumento.EXTENSION_FORMATO:
				metadades.setExtensio(
						(String)metadata.getValue());
				break;
			case MetadatosDocumento.CSV:
			case MetadatosDocumento.DEF_CSV:
				break;
			default:
				Map<String, Object> metadadesAddicionals = metadades.getMetadadesAddicionals();
				if (metadadesAddicionals == null) {
					metadadesAddicionals = new HashMap<String, Object>();
					metadades.setMetadadesAddicionals(metadadesAddicionals);
				}
				if (metadata.getValue() != null) {
					metadadesAddicionals.put(
							metadata.getQname(),
							metadata.getValue());
				}
			}
		}
		return metadades;
	}

	private static EstadosExpediente toEstadosExpediente(
			String estat) throws ArxiuException {
		if (estat == null) return null;
		switch (estat) {
			case "E03":
				return EstadosExpediente.INDICE_REMISION;
			case "E01":
				return EstadosExpediente.ABIERTO;
			case "E02":
				return EstadosExpediente.CERRADO;
			default:
				throw new ArxiuException(
						"No s'ha pogut convertir el valor per l'enumeració ArxiuExpedientEstat (" +
						"valor=" + estat + ")");
		}
	}
	
	private static String toContingutTipus(
			TiposObjetoSGD tiposObjetoSGD) {
		switch(tiposObjetoSGD) {
			case DIRECTORIO : return ArxiuConstants.CONTINGUT_TIPUS_CARPETA;
			case EXPEDIENTE : return ArxiuConstants.CONTINGUT_TIPUS_EXPEDIENT;
			case DOCUMENTO : return ArxiuConstants.CONTINGUT_TIPUS_DOCUMENT;
			case DOCUMENTO_MIGRADO : return ArxiuConstants.CONTINGUT_TIPUS_DOCUMENT;
			default : return null;
		}
	}

	private static OrigenesContenido toOrigenesContenido(
			String origen) throws ArxiuException {
		if (origen == null) return null;
		switch (origen) {
			case "1":
				return OrigenesContenido.ADMINISTRACION;
			case "0":
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
			case "EE99":
				return EstadosElaboracion.OTROS;
			case "EE02":
				return EstadosElaboracion.COPIA_AUTENTICA_FORMATO;
			case "EE03":
				return EstadosElaboracion.COPIA_AUTENTICA_PAPEL;
			case "EE04":
				return EstadosElaboracion.COPIA_AUTENTICA_PARCIAL;
			case "EE01":
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
			case "TD01":
				return TiposDocumentosENI.RESOLUCION;
			case "TD02":
				return TiposDocumentosENI.ACUERDO;
			case "TD03":
				return TiposDocumentosENI.CONTRATO;
			case "TD04":
				return TiposDocumentosENI.CONVENIO;
			case "TD05":
				return TiposDocumentosENI.DECLARACION;
			case "TD06":
				return TiposDocumentosENI.COMUNICACION;
			case "TD07":
				return TiposDocumentosENI.NOTIFICACION;
			case "TD08":
				return TiposDocumentosENI.PUBLICACION;
			case "TD09":
				return TiposDocumentosENI.ACUSE_DE_RECIBO;
			case "TD10":
				return TiposDocumentosENI.ACTA;
			case "TD11":
				return TiposDocumentosENI.CERTIFICADO;
			case "TD12":
				return TiposDocumentosENI.DILIGENCIA;
			case "TD13":
				return TiposDocumentosENI.INFORME;
			case "TD14":
				return TiposDocumentosENI.SOLICITUD;
			case "TD15":
				return TiposDocumentosENI.DENUNCIA;
			case "TD16":
				return TiposDocumentosENI.ALEGACION;
			case "TD17":
				return TiposDocumentosENI.RECURSOS;
			case "TD18":
				return TiposDocumentosENI.COMUNICACION_CIUDADANO;
			case "TD19":
				return TiposDocumentosENI.FACTURA;
			case "TD20":
				return TiposDocumentosENI.OTROS_INCAUTADOS;
			case "TD99":
				return TiposDocumentosENI.OTROS;
			default:
				throw new ArxiuException(
						"No s'ha pogut convertir el valor per l'enumeració ArxiuDocumentTipus (" +
						"valor=" + documentTipus + ")");
		}
	}
	
	/*private static FormatosFichero toFormatosFichero(
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
	}*/
	
	/*private static TiposFirma toTipoFirma(String tipoFirma) throws ArxiuException {
		if(tipoFirma == null) return null;
		switch (tipoFirma) {
			case "TF01":
				return TiposFirma.CSV;
			case "TF02":
				return TiposFirma.XADES_INTERNALLY;
			case "TF03":
				return TiposFirma.XADES_ENVELOPED;
			case "TF04":
				return TiposFirma.CADES_DETACHED;
			case "TF05":
				return TiposFirma.CADES_ATTACHED;
			case "TF06":
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
	}*/

	private static String formatDateIso8601(Date date) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(tz);
		return df.format(date);
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

	public static ContingutArxiu crearContingutArxiu(
			String identificador, 
			String nom,
			String tipus,
			String versio) {
		ContingutArxiu informacioItem = new ContingutArxiu(tipus);
		informacioItem.setIdentificador(identificador);
		informacioItem.setNom(nom);
		informacioItem.setVersio(versio);
		return informacioItem;
	}

}
