package es.caib.plugins.arxiu.caib.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

<<<<<<< HEAD
=======
import es.caib.arxiudigital.apirest.constantes.MetadatosDocumento;
>>>>>>> branch 'pluginsib-1.0' of https://github.com/GovernIB/pluginsib.git
import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.Aspectes;
import es.caib.plugins.arxiu.api.Capsalera;
import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ConsultaResultat;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentContingut;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.Estat;
import es.caib.plugins.arxiu.api.EstatElaboracio;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.Fields;
import es.caib.plugins.arxiu.api.Firma;
import es.caib.plugins.arxiu.api.InformacioItem;
import es.caib.plugins.arxiu.api.ConsultaOperacio;
import es.caib.plugins.arxiu.api.Origen;
import es.caib.plugins.arxiu.api.PerfilsFirma;
<<<<<<< HEAD
import es.caib.plugins.arxiu.api.FirmaTipus;
=======
import es.caib.plugins.arxiu.api.TipusDocumental;
import es.caib.plugins.arxiu.api.TipusFirma;
>>>>>>> branch 'pluginsib-1.0' of https://github.com/GovernIB/pluginsib.git
import es.caib.plugins.arxiu.caib.CaibArxiuPlugin;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CaibArxiuPluginTest {

	private static final String SERIE_DOCUMENTAL = "S0001";
	private static CaibArxiuPlugin caibArxiuPlugin;
	
	private static Capsalera capsaleraTest;
	private static List<String> organsTest;
	private static List<String> interessatsTest;
	
	private static String expedientIdPerConsultar;
	private static String expedientIdNoBorrat;
	private static String expedientNomPerConsultar;
	private static ExpedientMetadades expedientMetadadesConsultades;
	
	private static String documentIdPerConsultar;
	private static String documentIdPerBorrar;
	private static String documentNomPerConsultar;
	
	private static String carpetaIdPerConsultar;
	private static String carpetaNomPerConsultar;
	
	private static String CONTINGUT_FITXER_TEXT_PLA;
	
	private static Integer pagina;
	private static Integer itemsPerPagina;
	
	@BeforeClass
	public static void setUp() {
		System.setProperty("plugins.arxiu.caib.base.url", "https://afirmades.caib.es:4430/esb");
		System.setProperty("plugins.arxiu.caib.aplicacio.codi", "ARXIUTEST");
		System.setProperty("plugins.arxiu.caib.usuari", "app1");
		System.setProperty("plugins.arxiu.caib.contrasenya", "app1");
		
		System.setProperty("javax.net.ssl.trustStore", "C:/Feina/RIPEA/truststore.jks"); 
		System.setProperty("javax.net.ssl.trustStorePassword", "tecnologies");
		
		System.setProperty("plugins.arxiu.caib.dirs.documents", "C:/Feina/PLUGIN_ARXIU/docs");
		System.setProperty("plugins.arxiu.caib.name.doc1", "test-signed-BES.pdf");
		
		caibArxiuPlugin = new CaibArxiuPlugin();
		
		pagina = 1;
		itemsPerPagina = 10;
		expedientIdNoBorrat = "d2a6e047-d026-49e3-8e2c-2e77641e27dd";
		
		capsaleraTest = new Capsalera();
		capsaleraTest.setInteressatNom("Limit Tecnologies");
		capsaleraTest.setInteressatNif("123456789Z");
		capsaleraTest.setFuncionariNom("u101334");
		capsaleraTest.setFuncionariOrgan("CAIB");
		capsaleraTest.setProcedimentNom("Proves amb RIPEA");
		organsTest = new ArrayList<String>();
		organsTest.add("A04013511");
		interessatsTest = new ArrayList<String>();
		interessatsTest.add("12345678Z");
		interessatsTest.add("00000000T");
		
		CONTINGUT_FITXER_TEXT_PLA = "Contingut text pla per a docs esborranys";
	}
	
	@Test
	public void a0_testExpedientCrear() {
		String expedientId = null;
		
		Long time = System.currentTimeMillis();
		expedientNomPerConsultar = getExpedientName(time);
		expedientMetadadesConsultades = getExpedientMetadades(time, true);
		
		try {
			expedientId = caibArxiuPlugin.expedientCrear(
					expedientNomPerConsultar,
					expedientMetadadesConsultades);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar crear un expedient.");
		}
		
		assertNotNull("No s'ha pogut crear l'expedient", expedientId);
		
		expedientIdPerConsultar = expedientId;
	}

	@Test
	public void a1_testExpedientModificar() {
		Long time = System.currentTimeMillis();
		String nomModificat = expedientNomPerConsultar + "_mod";
		ExpedientMetadades expedientMetadadesModificades = getExpedientMetadades(time, false);
		
		InformacioItem modificacioExpedient = null;
		try {
			modificacioExpedient = caibArxiuPlugin.expedientModificar(expedientIdPerConsultar, nomModificat, expedientMetadadesModificades);
			expedientNomPerConsultar = modificacioExpedient.getNom();
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar modificar un expedient.");
		}
		
		assertNotNull("No s'ha pogut modificar l'expedient", modificacioExpedient);
	}

	@Test
	public void a8_testExpedientEsborrar() {
		try {
			caibArxiuPlugin.expedientEsborrar(expedientIdPerConsultar);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar esborrar expedient.");
		}
		
		try {
			Expedient expedient = caibArxiuPlugin.expedientDetalls(expedientIdPerConsultar, null);
			if (expedient != null)
				fail("No s'ha pogut esborrar l'expedient amb identificador " + expedientIdPerConsultar);
		} catch (ArxiuException e) {
		}
	}

	@Test
	public void a2_testExpedientDetalls() {
		Expedient expedient = null;
		
		try {
			expedient = caibArxiuPlugin.expedientDetalls(expedientIdPerConsultar, null);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar obtenir els detalls d'un expedient.");
		}
		assertNotNull("No s'ha pogut crear l'expedient", expedient);
		assertEquals("El nom de l'expedient no coincideix amb l'utilitzat al crear l'expedient", expedient.getNom(), expedientNomPerConsultar);
	}

	@Test
	public void a3_testExpedientConsulta() {
		ConsultaResultat resultat = null;
		try {
			List<ConsultaFiltre> filtres = new ArrayList<ConsultaFiltre>();
			filtres.add(new ConsultaFiltre(
<<<<<<< HEAD
					"ex_versio",
					ConsultaOperacio.IGUAL,
					"1"));
=======
					Fields.EX_SERIE_DOCUMENTAL,
					Operacio.IGUAL,
					SERIE_DOCUMENTAL));
>>>>>>> branch 'pluginsib-1.0' of https://github.com/GovernIB/pluginsib.git
			resultat = caibArxiuPlugin.expedientConsulta(filtres, pagina, itemsPerPagina);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar obtenir els detalls d'un expedient.");
		}
		assertNotNull("No s'ha obtingut cap resultat", resultat);
	}

	@Test
	public void a4_testExpedientVersions() {
		List<InformacioItem> informacioVersions = null;
		
		try {
			informacioVersions = caibArxiuPlugin.expedientVersions(expedientIdPerConsultar);
			assertNotNull("No s'han trobat versions de l'expedient", informacioVersions);
			if (informacioVersions.size() < 1)
				fail("No s'han trobat versions de l'expedient");
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar obtenir les versions de l'expedient.");
		}
	}

	@Test
	public void a5_testExpedientTancar() {
		try {
			caibArxiuPlugin.expedientTancar(expedientIdPerConsultar);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar tancar expedient.");
		}
	}

	@Test
	public void a6_testExpedientReobrir() {
		fail("Not yet implemented");
	}

	@Test
	public void a7_testExpedientExportarEni() {
		try {
			String exportacio = caibArxiuPlugin.expedientExportarEni(expedientIdPerConsultar);
			System.out.println(exportacio);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar exportar l'expedient");
		}
	}
	

//	@Test
//	public void a12_testDocumentCrear() {
//		try {
//			Long time = System.currentTimeMillis();
//			String nomDocument = getDocumentName(time);
//			String documentContingutDirectori = System.getProperty("plugins.arxiu.caib.dirs.documents");
//			String documentContingutNom = System.getProperty("plugins.arxiu.caib.name.doc1");
//			
//			Document document = new Document();
//			document.setNom(nomDocument);
//			document.setContingut(llegirFitxer(documentContingutDirectori + "/" + documentContingutNom));
//			document.setMetadades(getDocumentMetadades(time));
//			document.setAspectes(getDocumentAspectes());
////			document.setFirmes(getDocumentFirmes());
//			
//			String cosa = caibArxiuPlugin.documentCrear(document, expedientIdPerConsultar);
//		} catch (ArxiuException | IOException e) {
//			fail("S'ha produit una excepció al crear un document");
//		}
//	}

	@Test
	public void a11_testDocumentEsborranyCrear() {
		try {
			Long time = System.currentTimeMillis();
			documentNomPerConsultar = getDocumentName(time);
//			String documentContingutDirectori = System.getProperty("plugins.arxiu.caib.dirs.documents");
//			String documentContingutNom = System.getProperty("plugins.arxiu.caib.name.doc1");
			
			Document document = new Document();
			document.setNom(documentNomPerConsultar);
			document.setContingut(llegirFitxerDraft());
			document.setMetadades(getDocumentDraftMetadades(time));
			document.setAspectes(getDocumentAspectes());
			
			documentIdPerConsultar = caibArxiuPlugin.documentEsborranyCrear(document, expedientIdPerConsultar);
			
			
			time = System.currentTimeMillis();
			
			Document document2 = new Document();
			document2.setNom(getDocumentName(time));
			document2.setContingut(llegirFitxerDraft());
			document2.setMetadades(getDocumentDraftMetadades(time));
			document2.setAspectes(getDocumentAspectes());
			
			documentIdPerBorrar = caibArxiuPlugin.documentEsborranyCrear(document2, expedientIdPerConsultar);
			
		} catch (ArxiuException | IOException e) {
			fail("S'ha produit una excepció al crear un document");
		}
	}

	@Test
	public void a190_testDocumentEstablirDefinitiu() {
		InformacioItem informacioDefinitiu = null; 
		try {
			informacioDefinitiu = caibArxiuPlugin.documentEstablirDefinitiu(documentIdPerConsultar);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar exportar l'expedient");
		}
		assertNotNull("No s'ha establert el document com a definitiu", informacioDefinitiu);
	}

	@Test
	public void a13_testDocumentModificar() {
		InformacioItem infoDocumentModificat = null;
		try {
			documentNomPerConsultar = documentNomPerConsultar + "_mod";
			
			Document document = new Document();
			document.setIdentificador(documentIdPerConsultar);
			document.setNom(documentNomPerConsultar);
			
			infoDocumentModificat = caibArxiuPlugin.documentModificar(document);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al crear un document");
		}
		
		
		assertNotNull("No s'ha pogut modificar el document", infoDocumentModificat);
	}

	@Test
	public void a193_testDocumentEsborrar() {
		try {
			caibArxiuPlugin.documentEsborrar(documentIdPerBorrar);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar esborrar el document.");
		}
		
		try {
			Document document = caibArxiuPlugin.documentDetalls(documentIdPerBorrar, null, true);
			if (document != null)
				fail("No s'ha pogut esborrar el document amb identificador " + expedientIdPerConsultar);
		} catch (ArxiuException e) {
			
		}
	}

	@Test
	public void a16_testDocumentDetalls() {
		Document document = null;
		
		try {
			document = caibArxiuPlugin.documentDetalls(documentIdPerConsultar, null, false);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar obtenir els detalls d'un document.");
		}
		assertNotNull("No s'ha pogut consultar el document", document);
		assertEquals("El nom de l'expedient no coincideix amb l'utilitzat al crear l'expedient", document.getNom(), documentNomPerConsultar);
	}

	@Test
	public void a14_testDocumentConsulta() {
		ConsultaResultat resultat = null;
		try {
			List<ConsultaFiltre> filtres = new ArrayList<ConsultaFiltre>();
			filtres.add(new ConsultaFiltre(
					Fields.DOC_SERIE_DOCUMENTAL,
					Operacio.IGUAL,
					SERIE_DOCUMENTAL));
			resultat = caibArxiuPlugin.documentConsulta(filtres, pagina, itemsPerPagina);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar obtenir el llistat de documents.");
		}
		assertNotNull("No s'ha obtingut cap resultat", resultat);
	}

	@Test
	public void a15_testDocumentVersions() {
		List<InformacioItem> informacioVersions = null;
		
		try {
			informacioVersions = caibArxiuPlugin.documentVersions(documentIdPerConsultar);
			assertNotNull("No s'han trobat versions del document", informacioVersions);
			if (informacioVersions.size() < 1)
				fail("No s'han trobat versions del document");
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar obtenir les versions del document.");
		}
	}

	@Test
	public void a18_testDocumentGenerarCsv() {
		String csv = null;
		try {
			csv = caibArxiuPlugin.documentGenerarCsv();
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció copiant el document");
		}
		assertNotNull("No s'ha recuperat contingut CSV", csv);
	}

	@Test
	public void a17_testDocumentCopiar() {
		String infoCopia = null;
		try {
			infoCopia = caibArxiuPlugin.documentCopiar(documentIdPerConsultar, expedientIdNoBorrat);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció copiant el document");
		}
		
		assertNotNull("No s'ha pogut copiar el document", infoCopia);
	}

	@Test
	public void a191_testDocumentMoure() {
		try {
			caibArxiuPlugin.documentMoure(documentIdPerConsultar, expedientIdNoBorrat);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al moguent un document");
		}
	}

	@Test
	public void a192_testDocumentExportarEni() {
		String exportacio = null;
		try {
			exportacio = caibArxiuPlugin.documentExportarEni(documentIdPerConsultar);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció exportant");
		}
		assertNotNull("No s'ha recuperat contingut exportat", exportacio);
	}

	@Test
	public void a51_testCarpetaCrear() {
		String carpetaId = null;
		
		Long time = System.currentTimeMillis();
		carpetaNomPerConsultar = getFolderName(time);
		
		try {
			carpetaId = caibArxiuPlugin.carpetaCrear(carpetaNomPerConsultar, expedientIdPerConsultar);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar crear una carpeta.");
		}
		
		assertNotNull("No s'ha pogut crear l'expedient", carpetaId);
		
		carpetaIdPerConsultar = carpetaId;
	}

	@Test
	public void a52_testCarpetaModificar() {
		InformacioItem infoCarpetaModificada = null;
		try {
			carpetaNomPerConsultar = carpetaNomPerConsultar + "_mod";
			
			infoCarpetaModificada = caibArxiuPlugin.carpetaModificar(carpetaIdPerConsultar, carpetaNomPerConsultar);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al modificar la carpeta");
		}
		
		assertNotNull("No s'ha pogut modificar la carpeta", infoCarpetaModificada);
	}

	@Test
	public void a56_testCarpetaEsborrar() {
		try {
			caibArxiuPlugin.carpetaEsborrar(carpetaIdPerConsultar);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar esborrar la carpeta.");
		}
		
		try {
			Carpeta carpeta = caibArxiuPlugin.carpetaDetalls(carpetaIdPerConsultar);
			if (carpeta != null)
				fail("No s'ha pogut esborrar la carpeta amb identificador " + carpetaIdPerConsultar);
		} catch (ArxiuException e) {
			
		}
	}

	@Test
	public void a53_testCarpetaDetalls() {
		Carpeta carpeta = null;
		
		try {
			carpeta = caibArxiuPlugin.carpetaDetalls(carpetaIdPerConsultar);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al intentar obtenir els detalls d'una carpeta.");
		}
		assertNotNull("No s'ha pogut consultar la carpeta", carpeta);
		assertEquals("El nom de l'expedient no coincideix amb l'utilitzat al crear l'expedient", carpeta.getNom(), carpetaNomPerConsultar);
	}

	@Test
	public void a54_testCarpetaCopiar() {
		String infoCopia = null;
		try {
			infoCopia = caibArxiuPlugin.carpetaCopiar(carpetaIdPerConsultar, expedientIdNoBorrat);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció copiant la carpeta");
		}
		
		assertNotNull("No s'ha pogut copiar la carpeta", infoCopia);
	}

	@Test
	public void a55_testCarpetaMoure() {
		try {
			caibArxiuPlugin.carpetaMoure(carpetaIdPerConsultar, expedientIdNoBorrat);
		} catch (ArxiuException e) {
			fail("S'ha produit una excepció al moguent un document");
		}
	}
	
	private static String getExpedientName(Long i) {
		return "Expedient_" + i;
	}
	private static String getDocumentName(Long i) {
		return "Document_" + i;
	}
	private static String getFolderName(Long i) {
		return "Carpeta_" + i;
	}
	private static ExpedientMetadades getExpedientMetadades(Long i, boolean estatAleatori) {
		
		return new ExpedientMetadades(
				"IDEX_" + i, 
				"VNTIEX_" + i, 
				Origen.ADMINISTRACIO.getText(), 
				//Arrays.asList("OEX1_" + i, "OEX2_" + i, "OEX3_" + i),
				organsTest,
				new Date(i), 
				"CLASSEX_" + i, 
				Estat.E01.name(),
				interessatsTest,
				//Arrays.asList("IEX1_" + i, "IEX2_" + i, "IEX3_" + i),
				SERIE_DOCUMENTAL, 
				null);
	}
	private static ExpedientMetadades getExpedientMetadadesModificaes(Long i, boolean estatAleatori) {
		
		return new ExpedientMetadades(
				null, 
				null, 
				Origen.ADMINISTRACIO.getText(), 
				//Arrays.asList("OEX1_" + i, "OEX2_" + i, "OEX3_" + i),
				organsTest,
				new Date(i), 
				"CLASSEX_" + i, 
				null,
				interessatsTest,
				//Arrays.asList("IEX1_" + i, "IEX2_" + i, "IEX3_" + i),
				SERIE_DOCUMENTAL, 
				null);
	}
	private static String getEstadosExpediente(Long i, boolean aleatori) {
		if (aleatori) {
			Estat[] estadosExpediente = Estat.values();
			return estadosExpediente[(int) (i % 3)].name();
		} else {
			return Estat.E01.name();
		}
	}
	
	private DocumentContingut llegirFitxer(String ubicacio) throws IOException{
		
		Path fileLocation = Paths.get(ubicacio);
		byte[] data = Files.readAllBytes(fileLocation);
		String mimeType = FilenameUtils.getExtension(ubicacio);
		
		DocumentContingut contingut = new DocumentContingut(data, mimeType);
		
		return contingut;
	}
	
	private DocumentContingut llegirFitxerDraft() throws IOException{
		
		byte[] data = CONTINGUT_FITXER_TEXT_PLA.getBytes();
		String mimeType = "text/plain";
		
		DocumentContingut contingut = new DocumentContingut(data, mimeType);
		
		return contingut;
	}
	
	private static DocumentMetadades getDocumentMetadades(Long i) {
		
		return new DocumentMetadades(
				"IDDOC_" + i, 
				"VNTIDOC_" + i,
				organsTest, 
				new Date(i), 
				Origen.ADMINISTRACIO.getText(), 
				EstatElaboracio.EE01.name(), 
				TipusDocumental.TD99.name(), 
				SERIE_DOCUMENTAL, 
<<<<<<< HEAD
				FirmaTipus.TF06,
				PerfilsFirma.EPES,
=======
				TipusFirma.TF06.name(),
				PerfilsFirma.EPES.name(),
>>>>>>> branch 'pluginsib-1.0' of https://github.com/GovernIB/pluginsib.git
				null);
	}
	
	private static DocumentMetadades getDocumentDraftMetadades(Long i) {
		
		Map<String, String> metadadesAdicionals = new HashMap<String, String>();
		metadadesAdicionals.put(MetadatosDocumento.NOMBRE_FORMATO, FormatsFitxer.CSV.name());
		metadadesAdicionals.put(MetadatosDocumento.EXTENSION_FORMATO, ExtensionsFitxer.CSV.name());
		
		String csv = null;
		try {
			csv = caibArxiuPlugin.documentGenerarCsv();
			metadadesAdicionals.put(MetadatosDocumento.CSV, csv);
		} catch (ArxiuException e) {
			fail("No s'ha pogut generar el CSV");
		}
		
		return new DocumentMetadades(
				"IDDOCDRAFT_" + i, 
				"VNTIDOCDRAFT_" + i,
				organsTest, 
				new Date(i), 
				Origen.ADMINISTRACIO.getText(), 
				EstatElaboracio.EE01.name(), 
				TipusDocumental.TD99.name(), 
				SERIE_DOCUMENTAL, 
				TipusFirma.TF01.name(),
				PerfilsFirma.A.name(),
				metadadesAdicionals);
	}
	
	private static List<Aspectes> getDocumentAspectes() {
			
		List<Aspectes> aspects = new ArrayList<Aspectes>();
	    aspects.add(Aspectes.INTEROPERABLE);
	    aspects.add(Aspectes.TRANSFERIBLE);
	    
	    return aspects;
	}
	
	private static List<Firma> getDocumentFirmes() {
		
		List<Firma> firmes = new ArrayList<Firma>();
		Firma firma = new Firma();
<<<<<<< HEAD
		firma.setTipus(FirmaTipus.TF06.name());
=======
		firma.setTipus(TipusFirma.TF06.name());
>>>>>>> branch 'pluginsib-1.0' of https://github.com/GovernIB/pluginsib.git
		firmes.add(firma);
	    
	    return firmes;
	}
	
	private static String getEstadosElaboracion(Long i) {
		EstatElaboracio[] estadosElaboracion = EstatElaboracio.values();
<<<<<<< HEAD
		return estadosElaboracion[(int) (i % 5)].name();
=======
		return estadosElaboracion[(int) (i % 4)].name();
>>>>>>> branch 'pluginsib-1.0' of https://github.com/GovernIB/pluginsib.git
	}
}
