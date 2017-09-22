package es.caib.plugins.arxiu.caib.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.caib.arxiudigital.apirest.constantes.EstadosExpediente;
import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.Capsalera;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ConsultaResultat;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
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
	}
	
	@Test
	public void a0_testExpedientCrear() {
		String expedientId = null;
		
		Long time = System.currentTimeMillis();
		expedientNomPerConsultar = getExpedientName(time);
		expedientMetadadesConsultades = getExpedientMetadades(time);
		
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
		fail("Not yet implemented");
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
		expedient.getContinguts();
		assertEquals("Les metadades de l'expedient no coincideixen amb les utilitzades al crear l'expedient", expedient.getMetadades(), expedientMetadadesConsultades);
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
		fail("Not yet implemented");
	}

	@Test
	public void testDocumentCrear() {
		fail("Not yet implemented");
	}

	@Test
	public void testDocumentEsborranyCrear() {
		fail("Not yet implemented");
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
	
	private static String getFolderName(Long i) {
		return "Carpeta_" + i;
	}
	
	private static ExpedientMetadades getExpedientMetadades(Long i) {
		
		return new ExpedientMetadades(
				"IDEX_" + i, 
				"VNTIEX_" + i, 
				Origen.ADMINISTRACIO.getText(), 
				//Arrays.asList("OEX1_" + i, "OEX2_" + i, "OEX3_" + i),
				organsTest,
				new Date(i), 
				"CLASSEX_" + i, 
				getEstadosExpediente(i),
				interessatsTest,
				//Arrays.asList("IEX1_" + i, "IEX2_" + i, "IEX3_" + i),
				SERIE_DOCUMENTAL, 
				null);
	}
	private static String getEstadosExpediente(Long i) {
		
		EstadosExpediente[] estadosExpediente = EstadosExpediente.values();
		return estadosExpediente[(int) (i % 3)].name();
	}
}
