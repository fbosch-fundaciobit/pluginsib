/**
 * 
 */
package es.caib.plugins.arxiu.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import es.caib.plugins.arxiu.api.ArxiuNotFoundException;
import es.caib.plugins.arxiu.api.ContingutArxiu;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientEstat;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.IArxiuPlugin;
import es.caib.plugins.arxiu.caib.ArxiuPluginCaib;

/**
 * Client per a accedir a l'API genèrica de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuCaibClient {

	private static final String ORGAN_CODI = "A04013511";
	private static final String INTERESSAT1_NIF = "12345678Z";
	private static final String INTERESSAT2_NIF = "00000000T";
	private static final String SERIE_DOCUMENTAL_CODI = "S0001";

	public static void main(String[] args) throws Exception {
		List<String> organsTest = new ArrayList<String>();
		organsTest.add(ORGAN_CODI);
		List<String> interessatsTest = new ArrayList<String>();
		interessatsTest.add(INTERESSAT1_NIF);
		interessatsTest.add(INTERESSAT2_NIF);
		String nom = "ARXIUAPI_prova_exp_" + System.currentTimeMillis();
		final Expedient expedient = new Expedient();
		expedient.setNom(nom);
		final ExpedientMetadades metadades = new ExpedientMetadades();
		metadades.setOrgans(organsTest);
		metadades.setDataObertura(new Date());
		metadades.setClassificacio("organo1_PRO_123456789");
		metadades.setEstat(ExpedientEstat.OBERT);
		metadades.setInteressats(interessatsTest);
		metadades.setSerieDocumental(SERIE_DOCUMENTAL_CODI);
		expedient.setMetadades(metadades);
		IArxiuPlugin arxiuClient = getArxiuPlugin();
		System.out.println("Prova del client per a l'arxiu digital de la CAIB");
		ContingutArxiu expedientCreat = arxiuClient.expedientCrear(expedient);
		System.out.println("  - Expedient creat (id=" + expedientCreat.getIdentificador() + ")");
		arxiuClient.expedientEsborrar(expedientCreat.getIdentificador());
		System.out.println("  - Expedient esborrat (id=" + expedientCreat.getIdentificador() + ")");
		try {
			arxiuClient.expedientDetalls(
					expedientCreat.getIdentificador(),
					null);
			System.out.println("  - ERROR: L'expedient no s'ha esborrat (id=" + expedientCreat.getIdentificador() + ")");
		} catch (ArxiuNotFoundException ex) {
			System.out.println("  - Expedient esborrat correctament (id=" + expedientCreat.getIdentificador() + ")");
		}
	}

	private static IArxiuPlugin getArxiuPlugin() throws IOException {
		Properties properties = new Properties();
		properties.load(
				ArxiuCaibClient.class.getClassLoader().getResourceAsStream(
						"es/caib/plugins/arxiu/client/client-caib.properties"));
		System.getProperties().putAll(properties);
		return new ArxiuPluginCaib();
	}

}
