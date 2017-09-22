package es.caib.plugins.arxiu.filesystem.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ConsultaResultat;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentContingut;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.Firma;
import es.caib.plugins.arxiu.api.Operacio;
import es.caib.plugins.arxiu.filesystem.Fields;
import es.caib.plugins.arxiu.filesystem.FilesystemArxiuFilesystem;
import es.caib.plugins.arxiu.filesystem.FilesystemArxiuPlugin;

public class Test01 {
	
	private static void setProperties() {
		System.setProperty(
				"plugins.arxiu.filesystem.base.path",
				"/home/moisesp/Limit/programs/arxiu/test01/");
		System.setProperty(
				"plugins.arxiu.filesystem.emmagatzemament.codificat",
				"true");
	}
	
	private static ExpedientMetadades getExpedientMetadades(int i) {
		
		return new ExpedientMetadades(
				"ex_id_metadades_" + i,
				"ex_versioNti_" + i,
				"ex_origen_" + i,
				Arrays.asList("ex_organ_a_" + i, "ex_organ_b_" + i, "ex_organ_c_" + i),
				new Date(i),
				"ex_classificacio_" + i,
				"ex_estat_" + i,
				Arrays.asList("ex_interess_a_" + i, "ex_interess_b_" + i, "ex_interess_c_" + i),
				"ex_metadadesadd_" + i);
	}
	
	private static String getNomDocument() throws ArxiuException {
		
		String name = "/prova.pdf";
		
		return FilenameUtils.getBaseName(name);
	}
	
	private static DocumentContingut getDocumentContingut() throws ArxiuException {
		
		String name = "/prova.pdf";
		String path = "/home/moisesp/Limit/programs/arxiu";
		FilesystemArxiuFilesystem fsys = new FilesystemArxiuFilesystem(path);
		
		return new DocumentContingut(
				fsys.getDocumentContingut(name),
				FilenameUtils.getExtension(name),
				null);
	}
	
	private static DocumentMetadades getMetadadesDocument(int i) {
		
		return new DocumentMetadades(
				"doc_id_metadades_" + i,
				"doc_versioNti_" + i,
				Arrays.asList("doc_organ_a_" + i, "doc_organ_b_" + i, "doc_organ_c_" + i),
				new Date(i),
				"doc_origen_" + i,
				"doc_estatElaboracio_" + i,
				"doc_tipusDocumental_" + i,
				"doc_serieDocumental_" + i,
				null);
	}
	
	private static List<Firma> getListFirmes() throws ArxiuException {
		
		String path = "/home/moisesp/Limit/programs/arxiu";
		List<Firma> firmes = new ArrayList<Firma>();
		FilesystemArxiuFilesystem fsys = new FilesystemArxiuFilesystem(path);
		
		String firma1 = "/prova.pdf.1.sig";
		firmes.add(new Firma(
				fsys.getDocumentContingut(firma1),
				FilenameUtils.getExtension(firma1),
				"TF01",
				FilenameUtils.getBaseName(firma1),
				"CSV1"));
		String firma2 = "/prova.pdf.2.sig";
		firmes.add(new Firma(
				fsys.getDocumentContingut(firma2),
				FilenameUtils.getExtension(firma2),
				"TF02",
				FilenameUtils.getBaseName(firma2),
				"CSV2"));
		String firma3 = "/prova.pdf.3.sig";
		firmes.add(new Firma(
				fsys.getDocumentContingut(firma3),
				FilenameUtils.getExtension(firma3),
				"TF03",
				FilenameUtils.getBaseName(firma3),
				"CSV3"));
		
		return firmes;
	}
	
	private static void testExpedient() throws ArxiuException {
		
		FilesystemArxiuPlugin filesystemArxiuPlugin = new FilesystemArxiuPlugin();
			
//		String[] ids = new String[100];
//		for(int i = 0; i < ids.length; i++) {
//			ids[i] = filesystemArxiuPlugin.expedientCrear(
//					"nom_" + i,
//					getExpedientMetadades(i));
//		}
		
		List<ConsultaFiltre> filtres = new ArrayList<ConsultaFiltre>();
		filtres.add(new ConsultaFiltre(
				Fields.EX_DATA_OBERTURA,
				Operacio.MENOR,
				"9"));
		ConsultaResultat consulta = filesystemArxiuPlugin.expedientConsulta(
				filtres,
				2,
				5);
		
		// Provar modificar document, obrir i tancar documents
//		FilesystemArxiuPlugin filesystemArxiuPlugin = new FilesystemArxiuPlugin();
//		int ex = 0;
//		String expedientId = filesystemArxiuPlugin.expedientCrear(
//				"nom_" + ex,
//				getExpedientMetadades(ex));
//		
//		Expedient expedient = filesystemArxiuPlugin.expedientDetalls(expedientId, null);
//		
//		filesystemArxiuPlugin.expedientTancar(expedientId);
//		filesystemArxiuPlugin.expedientReobrir(expedientId);
//		filesystemArxiuPlugin.expedientReobrir(expedientId);
//		
//		ex = 1;
//		InformacioItem item = filesystemArxiuPlugin.expedientModificar(
//				expedientId,
//				"nom_" + ex,
//				getExpedientMetadades(ex));
//		expedient = filesystemArxiuPlugin.expedientDetalls(item.getIdentificador(), null);
		
		// Provar Get Versions
//		List<InformacioItem> items = filesystemArxiuPlugin.expedientVersions(expedientId);
		
		// Provar select
//		Expedient expedient = filesystemArxiuPlugin.expedientDetalls(expedientId, null);
		
		// Provar delete
//		filesystemArxiuPlugin.expedientEsborrar(expedientId);
//		
//		expedient = filesystemArxiuPlugin.expedientDetalls(expedientId, null);
	}
	private static void testDocument() throws ArxiuException {
		
		FilesystemArxiuPlugin filesystemArxiuPlugin = new FilesystemArxiuPlugin();
		
		int ex0 = 0;
		String expedientId0 = filesystemArxiuPlugin.expedientCrear(
				"nom_" + ex0,
				getExpedientMetadades(ex0));
		int ex1 = 1;
		String expedientId1 = filesystemArxiuPlugin.expedientCrear(
				"nom_" + ex1,
				getExpedientMetadades(ex1));
		
		int doc1 = 1;
		String documentId1 = filesystemArxiuPlugin.documentEsborranyCrear(
				new Document(
						null,
						getNomDocument(),
						getDocumentContingut(),
						getMetadadesDocument(doc1),
						getListFirmes()),
				expedientId0);
		int doc2 = 2;
		String documentId2 = filesystemArxiuPlugin.documentEsborranyCrear(
				new Document(
						null,
						getNomDocument(),
						getDocumentContingut(),
						getMetadadesDocument(doc2),
						getListFirmes()),
				expedientId0);
		
		Expedient expedient0 = filesystemArxiuPlugin.expedientDetalls(expedientId0, null);
		Expedient expedient1 = filesystemArxiuPlugin.expedientDetalls(expedientId1, null);
		
		filesystemArxiuPlugin.documentCopiar(documentId1, expedientId1);
		
		expedient0 = filesystemArxiuPlugin.expedientDetalls(expedientId0, null);
		expedient1 = filesystemArxiuPlugin.expedientDetalls(expedientId1, null);
		
		filesystemArxiuPlugin.documentMoure(documentId2, expedientId1);
		
		expedient0 = filesystemArxiuPlugin.expedientDetalls(expedientId0, null);
		expedient1 = filesystemArxiuPlugin.expedientDetalls(expedientId1, null);
		
//		String[] docids = new String[50];
//		for(int doc = 1; doc <= 50 ; doc++) {
//			docids[doc-1] = filesystemArxiuPlugin.documentEsborranyCrear(
//					new Document(
//							null,
//							getNomDocument(),
//							getDocumentContingut(),
//							getMetadadesDocument(doc),
//							getListFirmes()),
//					idExpedient);
//		}
		
//		List<ConsultaFiltre> filtres = new ArrayList<ConsultaFiltre>();
//		filtres.add(new ConsultaFiltre(
//				Fields.DOC_ORIGEN,
//				Operacio.CONTE,
//				"2"));
//		ConsultaResultat consultaResultat = filesystemArxiuPlugin.documentConsulta(
//				filtres,
//				1,
//				5);
		
//		filesystemArxiuPlugin.documentEsborrar(idDocument);
		
//		Document document1 = filesystemArxiuPlugin.documentDetalls(
//				idDocument,
//				null,
//				true);
		
//		int doc2 = 2;
//		InformacioItem item = filesystemArxiuPlugin.documentModificar(new Document(
//				document1.getIdentificador(),
//				document1.getNom() + ".2",
//				document1.getContingut(),
//				getMetadadesDocument(doc2),
//				document1.getFirmes()));
//		
//		Document document2 = filesystemArxiuPlugin.documentDetalls(
//				item.getIdentificador(),
//				null,
//				true);
		
//		List<InformacioItem> items = filesystemArxiuPlugin.documentVersions(idDocument);
//		
//		Document document3 = filesystemArxiuPlugin.documentDetalls(
//				items.get(0).getIdentificador(),
//				items.get(1).getVersio(),
//				true);
		
//		String path = "/home/moisesp/Limit/programs/arxiu/resultat";
//		FilesystemArxiuFilesystem fsys = new FilesystemArxiuFilesystem(path);
//		fsys.crearDocument(
//				"/" + document.getNom() + "." + document.getContingut().getTipusMime(),
//				document.getContingut().getContingut());
//		for(Firma firma : document.getFirmes()) {
//			fsys.crearDocument(
//					"/" + firma.getNom() + "." + firma.getTipusMime(),
//					firma.getContingut());
//		}
		
//		filesystemArxiuPlugin.documentEstablirDefinitiu(idDocument);
//		filesystemArxiuPlugin.documentEstablirDefinitiu(idDocument);
		
		System.out.println("Test ended succesfully");
	}
	private static void testCarpeta() throws ArxiuException {
		
		FilesystemArxiuPlugin filesystemArxiuPlugin = new FilesystemArxiuPlugin();
		
		int ex0 = 0;
		String expedientId0 = filesystemArxiuPlugin.expedientCrear(
				"nom_" + ex0,
				getExpedientMetadades(ex0));
		int ex1 = 1;
		String expedientId1 = filesystemArxiuPlugin.expedientCrear(
				"nom_" + ex1,
				getExpedientMetadades(ex1));
		
		int cp0 = 0;
		String carpetaId0 = filesystemArxiuPlugin.carpetaCrear(
				"nom_" + cp0,
				expedientId0);
		int cp1 = 1;
		String carpetaId1 = filesystemArxiuPlugin.carpetaCrear(
				"nom_" + cp1,
				expedientId1);
		
		int doc1 = 1;
		String documentId1 = filesystemArxiuPlugin.documentEsborranyCrear(
				new Document(
						null,
						getNomDocument(),
						getDocumentContingut(),
						getMetadadesDocument(doc1),
						getListFirmes()),
				carpetaId0);
		
		Carpeta carpeta = filesystemArxiuPlugin.carpetaDetalls(carpetaId0);
		
		carpeta = filesystemArxiuPlugin.carpetaDetalls(carpetaId1);
		
//		String carpetaId3 = filesystemArxiuPlugin.carpetaCopiar(carpetaId0, carpetaId1);
		
		filesystemArxiuPlugin.carpetaMoure(carpetaId0, carpetaId1);
		
		carpeta = filesystemArxiuPlugin.carpetaDetalls(carpetaId1);
		
		carpeta = filesystemArxiuPlugin.carpetaDetalls(carpetaId0);
		
		Expedient expedient = filesystemArxiuPlugin.expedientDetalls(expedientId0, null);
		
//		Carpeta carpeta = filesystemArxiuPlugin.carpetaDetalls(carpetaId0);
//		
//		filesystemArxiuPlugin.carpetaModificar(carpetaId0, "nom_x");
//		
//		carpeta = filesystemArxiuPlugin.carpetaDetalls(carpetaId0);
//		
//		filesystemArxiuPlugin.carpetaEsborrar(carpetaId0);
//		
//		carpeta = filesystemArxiuPlugin.carpetaDetalls(carpetaId0);
		
		System.out.println("Test ended succesfully");
	}
	public static void main(String[] args) throws ArxiuException {
		
		setProperties();
		
		testCarpeta();
		
	}

}
