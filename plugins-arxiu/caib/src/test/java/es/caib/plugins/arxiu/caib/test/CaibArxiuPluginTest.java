package es.caib.plugins.arxiu.caib.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.caib.arxiudigital.apirest.constantes.ExtensionesFichero;
import es.caib.arxiudigital.apirest.constantes.FormatosFichero;
import es.caib.arxiudigital.apirest.constantes.MetadatosDocumento;
import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ConsultaOperacio;
import es.caib.plugins.arxiu.api.ConsultaResultat;
import es.caib.plugins.arxiu.api.ContingutArxiu;
import es.caib.plugins.arxiu.api.ContingutOrigen;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentContingut;
import es.caib.plugins.arxiu.api.DocumentEstatElaboracio;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.DocumentTipus;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientEstat;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.caib.ArxiuConsultaHelper;
import es.caib.plugins.arxiu.caib.ArxiuPluginCaib;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CaibArxiuPluginTest {

	private static final String SERIE_DOCUMENTAL = "S0001";
	private static ArxiuPluginCaib caibArxiuPlugin;
	
	//private static Capsalera capsaleraTest;
	private static List<String> organsTest;
	private static List<String> interessatsTest;
	
	private static String expedientIdPerConsultar;
	private static String expedientIdNoBorrat;
	private static String expedientNomPerConsultar;
	@SuppressWarnings("unused")
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
	public static void setUp() throws IOException {
		/*System.setProperty(
				"plugins.arxiu.caib.base.url",
				"https://afirmades.caib.es:4430/esb");
		System.setProperty(
				"plugins.arxiu.caib.aplicacio.codi",
				"ARXIUTEST");
		System.setProperty(
				"plugins.arxiu.caib.usuari",
				"app1");
		System.setProperty(
				"plugins.arxiu.caib.contrasenya",
				"app1");
		System.setProperty(
				"javax.net.ssl.trustStore",
				"C:/Feina/RIPEA/truststore.jks"); 
		System.setProperty(
				"javax.net.ssl.trustStorePassword",
				"tecnologies");
		System.setProperty(
				"plugins.arxiu.caib.dirs.documents",
				"C:/Feina/PLUGIN_ARXIU/docs");
		System.setProperty(
				"plugins.arxiu.caib.name.doc1",
				"test-signed-BES.pdf");*/
		Properties properties = new Properties();
		properties.load(
				CaibArxiuPluginTest.class.getClassLoader().getResourceAsStream(
						"es/caib/plugins/arxiu/caib/test.properties"));
		caibArxiuPlugin = new ArxiuPluginCaib(
				"",
				properties);
		pagina = 1;
		itemsPerPagina = 10;
		expedientIdNoBorrat = "d2a6e047-d026-49e3-8e2c-2e77641e27dd";
		/*capsaleraTest = new Capsalera();
		capsaleraTest.setInteressatNom("Limit Tecnologies");
		capsaleraTest.setInteressatNif("123456789Z");
		capsaleraTest.setFuncionariNom("u101334");
		capsaleraTest.setFuncionariOrgan("CAIB");
		capsaleraTest.setProcedimentNom("Proves amb RIPEA");*/
		organsTest = new ArrayList<String>();
		organsTest.add("A04013511");
		interessatsTest = new ArrayList<String>();
		interessatsTest.add("12345678Z");
		interessatsTest.add("00000000T");
		CONTINGUT_FITXER_TEXT_PLA = "Contingut text pla per a docs esborranys";
	}
	
	@Test
	public void a0_testExpedientCrear() throws ArxiuException {
		Long time = System.currentTimeMillis();
		Expedient expedient = new Expedient();
		expedient.setNom(getExpedientName(time));
		expedient.setMetadades(getExpedientMetadades(time));
		ContingutArxiu expedientCreat = caibArxiuPlugin.expedientCrear(
				expedient);
		assertNotNull("No s'ha pogut crear l'expedient", expedientCreat);
		expedientIdPerConsultar = expedientCreat.getIdentificador();
	}

	@Test
	public void a1_testExpedientModificar() throws ArxiuException {
		Long time = System.currentTimeMillis();
		Expedient expedient = new Expedient();
		expedient.setNom(expedientNomPerConsultar + "_mod");
		expedient.setMetadades(getExpedientMetadades(time));
		ContingutArxiu modificacioExpedient = caibArxiuPlugin.expedientModificar(
				expedient);
		expedientNomPerConsultar = modificacioExpedient.getNom();
		assertNotNull("No s'ha pogut modificar l'expedient", modificacioExpedient);
	}

	@Test
	public void a8_testExpedientEsborrar() throws ArxiuException {
		caibArxiuPlugin.expedientEsborrar(expedientIdPerConsultar);
		Expedient expedient = caibArxiuPlugin.expedientDetalls(expedientIdPerConsultar, null);
		if (expedient != null)
			fail("No s'ha pogut esborrar l'expedient amb identificador " + expedientIdPerConsultar);
	}

	@Test
	public void a2_testExpedientDetalls() throws ArxiuException {
		Expedient expedient = caibArxiuPlugin.expedientDetalls(
				expedientIdPerConsultar,
				null);
		assertNotNull("No s'ha pogut crear l'expedient", expedient);
		assertEquals("El nom de l'expedient no coincideix amb l'utilitzat al crear l'expedient", expedient.getNom(), expedientNomPerConsultar);
	}

	@Test
	public void a3_testExpedientConsulta() throws ArxiuException {
		List<ConsultaFiltre> filtres = new ArrayList<ConsultaFiltre>();
		ConsultaFiltre filtre = new ConsultaFiltre();
		filtre.setMetadada(ArxiuConsultaHelper.EX_SERIE_DOCUMENTAL);
		filtre.setOperacio(ConsultaOperacio.IGUAL);
		filtre.setValorOperacio1(SERIE_DOCUMENTAL);
		filtres.add(filtre);
		ConsultaResultat resultat = caibArxiuPlugin.expedientConsulta(
				filtres,
				pagina,
				itemsPerPagina);
		assertNotNull("No s'ha obtingut cap resultat", resultat);
	}

	@Test
	public void a4_testExpedientVersions() throws ArxiuException {
		List<ContingutArxiu> informacioVersions = caibArxiuPlugin.expedientVersions(
				expedientIdPerConsultar);
		assertNotNull("No s'han trobat versions de l'expedient", informacioVersions);
		if (informacioVersions.size() < 1)
			fail("No s'han trobat versions de l'expedient");
	}

	@Test
	public void a5_testExpedientTancar() throws ArxiuException {
		caibArxiuPlugin.expedientTancar(expedientIdPerConsultar);
	}

	@Test
	public void a6_testExpedientReobrir() {
		fail("Not yet implemented");
	}

	@Test
	public void a7_testExpedientExportarEni() throws ArxiuException {
		String exportacio = caibArxiuPlugin.expedientExportarEni(expedientIdPerConsultar);
		System.out.println(exportacio);
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
	public void a11_testDocumentEsborranyCrear() throws IOException, ArxiuException {
		/*Long time = System.currentTimeMillis();
		documentNomPerConsultar = getDocumentName(time);
		Document document = new Document();
		document.setNom(documentNomPerConsultar);
		document.setContingut(llegirFitxerDraft());
		document.setMetadades(getDocumentDraftMetadades(time));
		documentIdPerConsultar = caibArxiuPlugin.documentEsborranyCrear(document, expedientIdPerConsultar);
		time = System.currentTimeMillis();
		Document document2 = new Document();
		document2.setNom(getDocumentName(time));
		document2.setContingut(llegirFitxerDraft());
		document2.setMetadades(getDocumentDraftMetadades(time));
		documentIdPerBorrar = caibArxiuPlugin.documentEsborranyCrear(document2, expedientIdPerConsultar);*/
	}

	@Test
	public void a190_testDocumentEstablirDefinitiu() throws ArxiuException {
		/*ContingutArxiu informacioDefinitiu = caibArxiuPlugin.documentEstablirDefinitiu(
				documentIdPerConsultar);
		assertNotNull("No s'ha establert el document com a definitiu", informacioDefinitiu);*/
	}

	@Test
	public void a13_testDocumentModificar() throws ArxiuException {
		ContingutArxiu infoDocumentModificat = null;
		documentNomPerConsultar = documentNomPerConsultar + "_mod";
		Document document = new Document();
		document.setIdentificador(documentIdPerConsultar);
		document.setNom(documentNomPerConsultar);
		infoDocumentModificat = caibArxiuPlugin.documentModificar(
				document,
				false);
		assertNotNull("No s'ha pogut modificar el document", infoDocumentModificat);
	}

	@Test
	public void a193_testDocumentEsborrar() throws ArxiuException {
		caibArxiuPlugin.documentEsborrar(documentIdPerBorrar);
		Document document = caibArxiuPlugin.documentDetalls(documentIdPerBorrar, null, true);
		if (document != null)
			fail("No s'ha pogut esborrar el document amb identificador " + expedientIdPerConsultar);
	}

	@Test
	public void a16_testDocumentDetalls() throws ArxiuException {
		Document document = caibArxiuPlugin.documentDetalls(
				documentIdPerConsultar,
				null,
				false);
		assertNotNull("No s'ha pogut consultar el document", document);
		assertEquals("El nom de l'expedient no coincideix amb l'utilitzat al crear l'expedient", document.getNom(), documentNomPerConsultar);
	}

	@Test
	public void a14_testDocumentConsulta() throws ArxiuException {
		ConsultaResultat resultat = null;
		List<ConsultaFiltre> filtres = new ArrayList<ConsultaFiltre>();
		ConsultaFiltre filtre = new ConsultaFiltre();
		filtre.setMetadada(ArxiuConsultaHelper.DOC_SERIE_DOCUMENTAL);
		filtre.setOperacio(ConsultaOperacio.IGUAL);
		filtre.setValorOperacio1(SERIE_DOCUMENTAL);
		filtres.add(filtre);
		resultat = caibArxiuPlugin.documentConsulta(filtres, pagina, itemsPerPagina);
		assertNotNull("No s'ha obtingut cap resultat", resultat);
	}

	@Test
	public void a15_testDocumentVersions() throws ArxiuException {
		List<ContingutArxiu> informacioVersions = caibArxiuPlugin.documentVersions(
				documentIdPerConsultar);
		assertNotNull("No s'han trobat versions del document", informacioVersions);
		if (informacioVersions.size() < 1)
			fail("No s'han trobat versions del document");
	}

	@Test
	public void a17_testDocumentCopiar() throws ArxiuException {
		/*String infoCopia = caibArxiuPlugin.documentCopiar(documentIdPerConsultar, expedientIdNoBorrat);
		assertNotNull("No s'ha pogut copiar el document", infoCopia);*/
	}

	@Test
	public void a191_testDocumentMoure() throws ArxiuException {
		caibArxiuPlugin.documentMoure(documentIdPerConsultar, expedientIdNoBorrat);
	}

	@Test
	public void a192_testDocumentExportarEni() throws ArxiuException {
		String exportacio = caibArxiuPlugin.documentExportarEni(
				documentIdPerConsultar);
		assertNotNull("No s'ha recuperat contingut exportat", exportacio);
	}

	@Test
	public void a51_testCarpetaCrear() throws ArxiuException {
		/*Long time = System.currentTimeMillis();
		carpetaNomPerConsultar = getFolderName(time);
		String carpetaId = caibArxiuPlugin.carpetaCrear(
				carpetaNomPerConsultar,
				expedientIdPerConsultar);
		assertNotNull("No s'ha pogut crear l'expedient", carpetaId);
		carpetaIdPerConsultar = carpetaId;*/
	}

	@Test
	public void a52_testCarpetaModificar() throws ArxiuException {
		/*carpetaNomPerConsultar = carpetaNomPerConsultar + "_mod";
		ContingutArxiu infoCarpetaModificada = caibArxiuPlugin.carpetaModificar(
				carpetaIdPerConsultar,
				carpetaNomPerConsultar);
		assertNotNull("No s'ha pogut modificar la carpeta", infoCarpetaModificada);*/
	}

	@Test
	public void a56_testCarpetaEsborrar() throws ArxiuException {
		caibArxiuPlugin.carpetaEsborrar(carpetaIdPerConsultar);
		Carpeta carpeta = caibArxiuPlugin.carpetaDetalls(carpetaIdPerConsultar);
		if (carpeta != null)
			fail("No s'ha pogut esborrar la carpeta amb identificador " + carpetaIdPerConsultar);
	}

	@Test
	public void a53_testCarpetaDetalls() throws ArxiuException {
		Carpeta carpeta = caibArxiuPlugin.carpetaDetalls(carpetaIdPerConsultar);
		assertNotNull("No s'ha pogut consultar la carpeta", carpeta);
		assertEquals("El nom de l'expedient no coincideix amb l'utilitzat al crear l'expedient", carpeta.getNom(), carpetaNomPerConsultar);
	}

	@Test
	public void a54_testCarpetaCopiar() throws ArxiuException {
		caibArxiuPlugin.carpetaCopiar(
				carpetaIdPerConsultar,
				expedientIdNoBorrat);
	}

	@Test
	public void a55_testCarpetaMoure() throws ArxiuException {
		caibArxiuPlugin.carpetaMoure(carpetaIdPerConsultar, expedientIdNoBorrat);
	}
	
	private String getExpedientName(Long i) {
		return "Expedient_" + i;
	}
	@SuppressWarnings("unused")
	private String getDocumentName(Long i) {
		return "Document_" + i;
	}
	@SuppressWarnings("unused")
	private String getFolderName(Long i) {
		return "Carpeta_" + i;
	}
	private ExpedientMetadades getExpedientMetadades(
			Long i) {
		ExpedientMetadades metadades = new ExpedientMetadades();
		metadades.setIdentificador("IDEX_" + i);
		metadades.setVersioNti("VNTIEX_" + i);
		metadades.setOrigen(ContingutOrigen.ADMINISTRACIO);
		metadades.setOrgans(organsTest);
		metadades.setDataObertura(new Date(i));
		metadades.setClassificacio("CLASSEX_" + i);
		metadades.setEstat(ExpedientEstat.OBERT);
		metadades.setInteressats(interessatsTest);
		metadades.setSerieDocumental(SERIE_DOCUMENTAL);
		return metadades;
	}
	/*private static ExpedientMetadades getExpedientMetadadesModificades(
			Long i) {
		ExpedientMetadades metadades = new ExpedientMetadades();
		metadades.setIdentificador("IDEX_" + i);
		metadades.setVersioNti("VNTIEX_" + i);
		metadades.setOrigen(ArxiuConstants.CONTINGUT_ORIGEN_ADM);
		metadades.setOrgans(organsTest);
		metadades.setDataObertura(new Date(i));
		metadades.setClassificacio("CLASSEX_" + i);
		metadades.setEstat(ArxiuConstants.EXPEDIENT_ESTAT_OBERT);
		metadades.setInteressats(interessatsTest);
		metadades.setSerieDocumental(SERIE_DOCUMENTAL);
		return metadades;
	}
	private static String getEstadosExpedientea(
			Long i,
			boolean aleatori) {
		if (aleatori) {
			Estat[] estadosExpediente = Estat.values();
			return estadosExpediente[(int) (i % 3)].name();
		} else {
			return ArxiuConstants.EXPEDIENT_ESTAT_OBERT;
		}
	}
	private DocumentContingut llegirFitxer(String ubicacio) throws IOException{
		Path fileLocation = Paths.get(ubicacio);
		byte[] data = Files.readAllBytes(fileLocation);
		String mimeType = FilenameUtils.getExtension(ubicacio);
		DocumentContingut contingut = new DocumentContingut(data, mimeType);
		return contingut;
	}*/

	@SuppressWarnings("unused")
	private DocumentContingut llegirFitxerDraft() throws IOException{
		DocumentContingut contingut = new DocumentContingut();
		contingut.setContingut(CONTINGUT_FITXER_TEXT_PLA.getBytes());
		contingut.setTipusMime("text/plain");
		return contingut;
	}

	/*private static DocumentMetadades getDocumentMetadades(Long i) {
		DocumentMetadades metadades = new DocumentMetadades();
		metadades.setIdentificador("IDEX_" + i);
		metadades.setVersioNti("VNTIEX_" + i);
		metadades.setOrigen(ArxiuConstants.CONTINGUT_ORIGEN_ADM);
		metadades.setOrgans(organsTest);
		metadades.setDataCaptura(new Date(i));
		metadades.setEstatElaboracio(ArxiuConstants.DOCUMENT_ESTAT_ELAB_ORIG);
		metadades.setTipusDocumental(ArxiuConstants.DOCUMENT_TIPUS_OTROS);
		metadades.setSerieDocumental(SERIE_DOCUMENTAL);
		return metadades;
	}*/
	
	@SuppressWarnings("unused")
	private DocumentMetadades getDocumentDraftMetadades(Long i) {
		Map<String, Object> metadadesAddicionals = new HashMap<String, Object>();
		metadadesAddicionals.put(
				MetadatosDocumento.NOMBRE_FORMATO,
				FormatosFichero.CSV.name());
		metadadesAddicionals.put(
				MetadatosDocumento.EXTENSION_FORMATO,
				ExtensionesFichero.CSV.name());
		DocumentMetadades metadades = new DocumentMetadades();
		metadades.setIdentificador("IDDOCDRAFT_" + i);
		metadades.setVersioNti("VNTIDOCDRAFT_" + i);
		metadades.setOrigen(ContingutOrigen.ADMINISTRACIO);
		metadades.setOrgans(organsTest);
		metadades.setDataCaptura(new Date(i));
		metadades.setEstatElaboracio(DocumentEstatElaboracio.ORIGINAL);
		metadades.setTipusDocumental(DocumentTipus.ALTRES);
		metadades.setSerieDocumental(SERIE_DOCUMENTAL);
		metadades.setMetadadesAddicionals(metadadesAddicionals);
		return metadades;
	}

	/*private static List<Aspectes> getDocumentAspectes() {
		List<Aspectes> aspects = new ArrayList<Aspectes>();
	    aspects.add(Aspectes.INTEROPERABLE);
	    aspects.add(Aspectes.TRANSFERIBLE);
	    
	    return aspects;
	}
	
	private static List<Firma> getDocumentFirmes() {
		List<Firma> firmes = new ArrayList<Firma>();
		Firma firma = new Firma();
		firma.setTipus(FirmaTipus.TF06.name());
		firma.setTipus(TipusFirma.TF06.name());
		firmes.add(firma);
	    
	    return firmes;
	}
	
	private static String getEstadosElaboracion(Long i) {
		EstatElaboracio[] estadosElaboracion = EstatElaboracio.values();
		return estadosElaboracion[(int) (i % 5)].name();
		return estadosElaboracion[(int) (i % 4)].name();
	}*/

}
