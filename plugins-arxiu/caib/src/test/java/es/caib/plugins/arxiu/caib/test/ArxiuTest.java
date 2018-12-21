/**
 * 
 */
package es.caib.plugins.arxiu.caib.test;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import es.caib.plugins.arxiu.api.ContingutArxiu;
import es.caib.plugins.arxiu.api.ContingutOrigen;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentContingut;
import es.caib.plugins.arxiu.api.DocumentEstat;
import es.caib.plugins.arxiu.api.DocumentEstatElaboracio;
import es.caib.plugins.arxiu.api.DocumentExtensio;
import es.caib.plugins.arxiu.api.DocumentFormat;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.DocumentTipus;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientEstat;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.IArxiuPlugin;
import es.caib.plugins.arxiu.caib.ArxiuPluginCaib;

/**
 * Test de la implementació de l'API de l'arxiu que utilitza
 * l'API REST de l'arxiu de la CAIB.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuTest {

	private static List<String> organsTest;
	private static List<String> interessatsTest;
	private static IArxiuPlugin arxiuPlugin;

	@BeforeClass
	public static void setUp() throws IOException {
		Properties properties = new Properties();
		properties.load(
				ArxiuTest.class.getClassLoader().getResourceAsStream(
						"es/caib/plugins/arxiu/caib/test.properties"));
		arxiuPlugin = new ArxiuPluginCaib(
				"",
				properties);
		organsTest = new ArrayList<String>();
		organsTest.add("A04013511");
		interessatsTest = new ArrayList<String>();
		interessatsTest.add("12345678Z");
		interessatsTest.add("00000000T");
	}

	@Test
	public void crearExpedientTest() throws Exception {
		String expedientIdentificador = null;
		try {
			final Expedient expedientPerCrear = nouExpedientPerTest();
			ContingutArxiu expedientCreat = arxiuPlugin.expedientCrear(
					expedientPerCrear);
			assertNotNull(expedientCreat);
			assertNotNull(expedientCreat.getIdentificador());
			expedientIdentificador = expedientCreat.getIdentificador();
			final Document documentPerCrear = nouDocumentPerTest(
					DocumentEstat.ESBORRANY,
					DocumentFormat.PDF,
					DocumentExtensio.PDF,
					"application/pdf",
					"/es/caib/plugins/arxiu/caib/firmapades.pdf");
			ContingutArxiu documentCreat = arxiuPlugin.documentCrear(
					documentPerCrear,
					expedientCreat.getIdentificador());
			assertNotNull(documentCreat);
			assertNotNull(documentCreat.getIdentificador());
		} finally {
			if (expedientIdentificador != null) {
				arxiuPlugin.expedientEsborrar(expedientIdentificador);
			}
		}
	}

	private Expedient nouExpedientPerTest() {
		String nomExp = "ARXIUAPI_prova_exp_" + System.currentTimeMillis();
		final Expedient expedient = new Expedient();
		expedient.setNom(nomExp);
		final ExpedientMetadades metadades = new ExpedientMetadades();
		metadades.setOrgans(organsTest);
		metadades.setDataObertura(new Date());
		metadades.setClassificacio("organo1_PRO_123456789");
		metadades.setEstat(ExpedientEstat.OBERT);
		metadades.setInteressats(interessatsTest);
		metadades.setSerieDocumental("S0001");
		expedient.setMetadades(metadades);
		return expedient;
	}

	private Document nouDocumentPerTest(
			DocumentEstat estat,
			DocumentFormat format,
			DocumentExtensio extensio,
			String tipusMime,
			String contingutResource) throws IOException {
		Document document = new Document();
		document.setNom("ARXIUAPI_prova_doc_" + System.currentTimeMillis());
		final DocumentMetadades documentMetadades = new DocumentMetadades();
		documentMetadades.setOrigen(ContingutOrigen.CIUTADA);
		documentMetadades.setOrgans(organsTest);
		documentMetadades.setDataCaptura(new Date());
		documentMetadades.setEstatElaboracio(DocumentEstatElaboracio.ORIGINAL);
		documentMetadades.setTipusDocumental(DocumentTipus.ALTRES);
		documentMetadades.setFormat(format);
		documentMetadades.setExtensio(extensio);
		document.setMetadades(documentMetadades);
		document.setEstat(estat);
		DocumentContingut contingut = new DocumentContingut();
		contingut.setArxiuNom(contingutResource);
		contingut.setTipusMime(tipusMime);
		InputStream is = getClass().getResourceAsStream(
				contingutResource);
		contingut.setContingut(
				IOUtils.toByteArray(is));
		document.setContingut(contingut);
		return document;
	}

}
