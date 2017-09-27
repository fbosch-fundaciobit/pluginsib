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
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.caib.arxiudigital.apirest.constantes.Aspectos;
import es.caib.arxiudigital.apirest.constantes.EstadosElaboracion;
import es.caib.arxiudigital.apirest.constantes.EstadosExpediente;
import es.caib.arxiudigital.apirest.constantes.PerfilesFirma;
import es.caib.arxiudigital.apirest.constantes.TiposFirma;
import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.Capsalera;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ConsultaResultat;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentContingut;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.Firma;
import es.caib.plugins.arxiu.api.InformacioItem;
import es.caib.plugins.arxiu.api.Operacio;
import es.caib.plugins.arxiu.api.Origen;
import es.caib.plugins.arxiu.caib.CaibArxiuPlugin;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CaibArxiuPluginTest {

	private static final String SERIE_DOCUMENTAL = "S0001";
	private static CaibArxiuPlugin caibArxiuPlugin;
	
	private static Capsalera capsaleraTest;
	private static List<String> organsTest;
	private static List<String> interessatsTest;
	
	private static String expedientIdPerConsultar;
	private static String expedientNomPerConsultar;
	private static ExpedientMetadades expedientMetadadesConsultades;
	
	private static String documentIdPerConsultar;
//	private static String documentNomPerConsultar;
	
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
			System.out.println("No s'ha trobat l'expedient. Eliminació correcte");
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
//		expedient.getContinguts();
//		assertEquals("Les metadades de l'expedient no coincideixen amb les utilitzades al crear l'expedient", expedient.getMetadades(), expedientMetadadesConsultades);
	}

	@Test
	public void a3_testExpedientConsulta() {
		ConsultaResultat resultat = null;
		try {
			List<ConsultaFiltre> filtres = new ArrayList<ConsultaFiltre>();
			filtres.add(new ConsultaFiltre(
					"ex_versio",
					Operacio.IGUAL,
					"1"));
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
	

	@Test
	public void a12_testDocumentCrear() {
		try {
			Long time = System.currentTimeMillis();
			String nomDocument = getDocumentName(time);
			String documentContingutDirectori = System.getProperty("plugins.arxiu.caib.dirs.documents");
			String documentContingutNom = System.getProperty("plugins.arxiu.caib.name.doc1");
			
			Document document = new Document();
			document.setNom(nomDocument);
			document.setContingut(llegirFitxer(documentContingutDirectori + "/" + documentContingutNom));
			document.setMetadades(getDocumentMetadades(time));
			document.setAspectes(getDocumentAspectes());
//			document.setFirmes(getDocumentFirmes());
			
			String cosa = caibArxiuPlugin.documentCrear(document, expedientIdPerConsultar);
		} catch (ArxiuException | IOException e) {
			fail("S'ha produit una excepció al crear un document");
		}
	}

	@Test
	public void a11_testDocumentEsborranyCrear() {
		try {
			Long time = System.currentTimeMillis();
			String nomDocumentDraft = getDocumentName(time);
//			String documentContingutDirectori = System.getProperty("plugins.arxiu.caib.dirs.documents");
//			String documentContingutNom = System.getProperty("plugins.arxiu.caib.name.doc1");
			
			Document document = new Document();
			document.setNom(nomDocumentDraft);
			document.setContingut(llegirFitxerDraft());
			document.setMetadades(getDocumentDraftMetadades(time));
			document.setAspectes(getDocumentAspectes());
			
			caibArxiuPlugin.documentEsborranyCrear(document, expedientIdPerConsultar);
		} catch (ArxiuException | IOException e) {
			fail("S'ha produit una excepció al crear un document");
		}
	}

	@Test
	public void testDocumentEstablirDefinitiu() {
		fail("Not yet implemented");
	}

	@Test
	public void testDocumentModificar() {
		fail("Not yet implemented");
	}

	@Test
	public void testDocumentEsborrar() {
		fail("Not yet implemented");
	}

	@Test
	public void testDocumentDetalls() {
		fail("Not yet implemented");
	}

	@Test
	public void testDocumentConsulta() {
		fail("Not yet implemented");
	}

	@Test
	public void testDocumentVersions() {
		fail("Not yet implemented");
	}

	@Test
	public void testDocumentGenerarCsv() {
		fail("Not yet implemented");
	}

	@Test
	public void testDocumentCopiar() {
		fail("Not yet implemented");
	}

	@Test
	public void testDocumentMoure() {
		fail("Not yet implemented");
	}

	@Test
	public void testDocumentExportarEni() {
		fail("Not yet implemented");
	}

	@Test
	public void testCarpetaCrear() {
		fail("Not yet implemented");
	}

	@Test
	public void testCarpetaModificar() {
		fail("Not yet implemented");
	}

	@Test
	public void testCarpetaEsborrar() {
		fail("Not yet implemented");
	}

	@Test
	public void testCarpetaDetalls() {
		fail("Not yet implemented");
	}

	@Test
	public void testCarpetaCopiar() {
		fail("Not yet implemented");
	}

	@Test
	public void testCarpetaMoure() {
		fail("Not yet implemented");
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
				getEstadosExpediente(i, estatAleatori),
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
			EstadosExpediente[] estadosExpediente = EstadosExpediente.values();
			return estadosExpediente[(int) (i % 3)].name();
		} else {
			return EstadosExpediente.ABIERTO.name();
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
				getEstadosElaboracion(i), 
				"ALTRES", 
				SERIE_DOCUMENTAL, 
				TiposFirma.PADES,
				PerfilesFirma.EPES,
				null);
	}
	
	private static DocumentMetadades getDocumentDraftMetadades(Long i) {
		
//		Map<String, String> metadadesAdicionals = new HashMap<String, String>();
//		metadadesAdicionals.put(MetadatosDocumento.CSV, value);
		
		return new DocumentMetadades(
				"IDDOCDRAFT_" + i, 
				"VNTIDOCDRAFT_" + i,
				organsTest, 
				new Date(i), 
				Origen.ADMINISTRACIO.getText(), 
				getEstadosElaboracion(i), 
				"ALTRES", 
				SERIE_DOCUMENTAL, 
				null,
				null,
				null);
	}
	
	private static List<Aspectos> getDocumentAspectes() {
			
		List<Aspectos> aspects = new ArrayList<Aspectos>();
	    aspects.add(Aspectos.INTEROPERABLE);
	    aspects.add(Aspectos.TRANSFERIBLE);
	    
	    return aspects;
	}
	
	private static List<Firma> getDocumentFirmes() {
		
		List<Firma> firmes = new ArrayList<Firma>();
		Firma firma = new Firma();
		firma.setTipus(TiposFirma.PADES.getValue());
		firmes.add(firma);
	    
	    return firmes;
	}
	
	private static String getEstadosElaboracion(Long i) {
		EstadosElaboracion[] estadosElaboracion = EstadosElaboracion.values();
		return estadosElaboracion[(int) (i % 5)].name();
	}
}
