/**
 * 
 */
package es.caib.plugins.arxiu.caib.test;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ContingutArxiu;
import es.caib.plugins.arxiu.api.ContingutOrigen;
import es.caib.plugins.arxiu.api.ContingutTipus;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentEstat;
import es.caib.plugins.arxiu.api.DocumentEstatElaboracio;
import es.caib.plugins.arxiu.api.DocumentExtensio;
import es.caib.plugins.arxiu.api.DocumentFormat;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.DocumentTipus;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientEstat;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.Firma;
import es.caib.plugins.arxiu.api.FirmaPerfil;
import es.caib.plugins.arxiu.api.FirmaTipus;
import es.caib.plugins.arxiu.api.IArxiuPlugin;
import es.caib.plugins.arxiu.caib.ArxiuPluginCaib;

/**
 * Test de la implementació de l'API de l'arxiu que utilitza
 * l'API REST de l'arxiu de la CAIB.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuAnotacioTest {

	private static final String SERIE_DOCUMENTAL1 = "S0001";
	private static final String SERIE_DOCUMENTAL2 = "S0002";

	private static List<String> organsTest;
	private static List<String> interessatsTest;

	private static IArxiuPlugin arxiuPlugin;

	@BeforeClass
	public static void setUp() throws IOException {
		Properties properties = new Properties();
		properties.load(
				ArxiuAnotacioTest.class.getClassLoader().getResourceAsStream(
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
	public void crearAnotacioIMoureDocumentAltreExpedient() throws Exception {
		System.out.println("TEST: CREAR ANOTACIO I MOURE DOCUMENT");
		final Expedient expedientOrigenPerCrear = nouExpedientPerTest(SERIE_DOCUMENTAL1);
		testCreantElements(
				new TestAmbElementsCreats() {
					@Override
					public void executar(List<ContingutArxiu> elementsCreats) throws IOException {
						ContingutArxiu expedientOrigenCreat = elementsCreats.get(0);
						String expedientOrigenCreatId = expedientOrigenCreat.getIdentificador();
						elementsCreats.remove(0);
						System.out.println("Ok");
						System.out.println(
								"1.- Creant document definitiu dins anotacio (" +
								"id=" + expedientOrigenCreatId + ")... ");
						final Document documentPerCrear = nouDocument(
								DocumentEstat.DEFINITIU,
								DocumentFormat.PDF,
								DocumentExtensio.PDF,
								novaFirmaPades());
						ContingutArxiu documentCreat = arxiuPlugin.documentCrear(
								documentPerCrear,
								expedientOrigenCreatId);
						assertNotNull(documentCreat);
						String documentCreatId = documentCreat.getIdentificador();
						assertNotNull(documentCreatId);
						arxiuPlugin.documentDetalls(documentCreatId, null, false);
						System.out.println("Ok");
						System.out.println(
								"2.- Creant exp. RIPEA (" +
								"id=" + expedientOrigenCreatId + ")... ");
						Expedient expRipeaPerCrear = nouExpedientPerTest(SERIE_DOCUMENTAL2);
						ContingutArxiu expRipeaCreat = arxiuPlugin.expedientCrear(
								expRipeaPerCrear);
						String expRipeaCreatId = expRipeaCreat.getIdentificador();
						arxiuPlugin.expedientDetalls(expRipeaCreatId, null);
						System.out.println("Ok");
						System.out.println(
								"3.- Movent document a l'exp. RIPEA (" +
								"id=" + documentCreatId + ")... ");
						arxiuPlugin.documentMoure(documentCreatId, expRipeaCreatId);
						System.out.println("Ok");
						System.out.println(
								"4.- Tancant expedient RIPEA (" +
								"id=" + expedientOrigenCreatId + ")... ");
						arxiuPlugin.expedientTancar(expRipeaCreatId);
						System.out.println("Ok");
						System.out.println(
								"5.- Tancant expedient origen (" +
								"id=" + expedientOrigenCreatId + ")... ");
						arxiuPlugin.expedientTancar(expedientOrigenCreatId);
						System.out.println("Ok");
						System.out.println(
								"6.- Consultant exp. origen (" +
								"id=" + expedientOrigenCreatId + ")... ");
						arxiuPlugin.expedientDetalls(expedientOrigenCreatId, null);
						System.out.println("Ok");
						System.out.println(
								"7.- Consultant exp. RIPEA (" +
								"id=" + expRipeaCreatId + ")... ");
						arxiuPlugin.expedientDetalls(expRipeaCreatId, null);
						System.out.println("Ok");
					}
				},
				expedientOrigenPerCrear);
	}



	private void testCreantElements(
			TestAmbElementsCreats test,
			Object... elements) throws Exception {
		List<ContingutArxiu> elementsCreats = new ArrayList<ContingutArxiu>();
		String expedientId = null;
		boolean excepcioLlencada = false;
		boolean totBe = false;
		try {
			for (Object element: elements) {
				if (element instanceof Expedient) {
					System.out.println("I.- Creant expedient... ");
					ContingutArxiu expedientCreat = arxiuPlugin.expedientCrear(
							(Expedient)element);
					assertNotNull(expedientCreat);
					assertNotNull(expedientCreat.getIdentificador());
					expedientId = expedientCreat.getIdentificador();
					elementsCreats.add(expedientCreat);
					System.out.println("Ok");
				} else {
					if (expedientId != null) {
						if (element instanceof Document) {
							System.out.println("I.- Creant document... ");
							Document documentPerCrear = (Document)element;
							ContingutArxiu documentCreat = arxiuPlugin.documentCrear(
									documentPerCrear,
									expedientId);
							assertNotNull(documentCreat);
							assertNotNull(documentCreat.getIdentificador());
							elementsCreats.add(documentCreat);
							System.out.println("Ok");
						} else if (element instanceof Carpeta) {
							System.out.println("I.- Creant carpeta... ");
							ContingutArxiu carpetaCreada = arxiuPlugin.carpetaCrear(
									(Carpeta)element,
									expedientId);
							assertNotNull(carpetaCreada);
							assertNotNull(carpetaCreada.getIdentificador());
							elementsCreats.add(carpetaCreada);
							System.out.println("Ok");
						} else {
							throw new RuntimeException(
									"Tipus d'element desconegut: " + element.getClass().getName());
						}
					} else {
						throw new RuntimeException("No s'ha especificat cap expedient");
					}
				}
			}
			test.executar(elementsCreats);
			totBe = true;
		} catch (Exception ex) {
			excepcioLlencada = true;
			System.out.println("Error: " + ex.getLocalizedMessage());
			throw ex;
		} finally {
			if (!excepcioLlencada && !totBe) {
				System.out.println("Nok");
			}
			Collections.reverse(elementsCreats);
			for (ContingutArxiu element: elementsCreats) {
				if (ContingutTipus.EXPEDIENT.equals(element.getTipus())) {
					String identificador = element.getIdentificador();
					System.out.println("F.- Esborrant expedient creat (id=" + identificador + ")... ");
					try {
						arxiuPlugin.expedientEsborrar(identificador);
						System.out.println("Ok");
					} catch (Exception ex) {
						System.out.println("Error: " + ex.getLocalizedMessage());
						throw ex;
					}
					expedientId = null;
				} else if (ContingutTipus.DOCUMENT.equals(element.getTipus())) {
					String identificador = element.getIdentificador();
					System.out.println("F.- Esborrant document creat (id=" + identificador + ")... ");
					try {
						arxiuPlugin.documentEsborrar(identificador);
						System.out.println("Ok");
					} catch (Exception ex) {
						System.out.println("Error: " + ex.getLocalizedMessage());
						throw ex;
					}
				} else if (ContingutTipus.CARPETA.equals(element.getTipus())) {
					String identificador = element.getIdentificador();
					System.out.println("F.- Esborrant carpeta creada (id=" + identificador + ")... ");
					try {
						arxiuPlugin.carpetaEsborrar(identificador);
						System.out.println("Ok");
					} catch (Exception ex) {
						System.out.println("Error: " + ex.getLocalizedMessage());
						throw ex;
					}
				}
			}
		}
	}

	private Expedient nouExpedientPerTest(
			String serie) {
		String nomExp = "ARXIUAPI_prova_exp_" + System.currentTimeMillis();
		final Expedient expedient = new Expedient();
		expedient.setNom(nomExp);
		final ExpedientMetadades metadades = new ExpedientMetadades();
		metadades.setOrgans(organsTest);
		metadades.setDataObertura(new Date());
		metadades.setClassificacio("organo1_PRO_123456789");
		metadades.setEstat(ExpedientEstat.OBERT);
		metadades.setInteressats(interessatsTest);
		metadades.setSerieDocumental(serie);
		expedient.setMetadades(metadades);
		return expedient;
	}

	private Document nouDocument(
			DocumentEstat estat,
			DocumentFormat format,
			DocumentExtensio extensio,
			Firma firma) {
		return nouDocument(
				null,
				estat,
				format,
				extensio,
				firma);
	}
	private Document nouDocument(
			String nom,
			DocumentEstat estat,
			DocumentFormat format,
			DocumentExtensio extensio,
			Firma firma) {
		Document document = new Document();
		String nomDoc = (nom != null) ? nom : "ARXIUAPI_prova_doc_" + System.currentTimeMillis();
		document.setNom(nomDoc);
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
		if (firma != null) {
			document.setFirmes(
					Arrays.asList(firma));
		}
		return document;
	}

	private Firma novaFirmaPades() throws IOException {
		Firma firma = new Firma();
		firma.setTipus(FirmaTipus.PADES);
		firma.setPerfil(FirmaPerfil.EPES);
		firma.setTipusMime("application/pdf");
		firma.setContingut(
				IOUtils.toByteArray(
						getDocumentFirmaPdf()));
		return firma;
	}

	private InputStream getDocumentFirmaPdf() {
		InputStream is = getClass().getResourceAsStream(
        		"/es/caib/plugins/arxiu/caib/firmapades.pdf");
		return is;
	}

	private abstract class TestAmbElementsCreats {
		public abstract void executar(List<ContingutArxiu> elementsCreats) throws Exception;
	}

}
