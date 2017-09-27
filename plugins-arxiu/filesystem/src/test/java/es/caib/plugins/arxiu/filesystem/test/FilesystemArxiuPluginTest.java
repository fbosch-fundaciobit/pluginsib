
package es.caib.plugins.arxiu.filesystem.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ConsultaResultat;
import es.caib.plugins.arxiu.api.ContingutTipus;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.InformacioItem;
import es.caib.plugins.arxiu.api.ConsultaOperacio;
import es.caib.plugins.arxiu.filesystem.Fields;
import es.caib.plugins.arxiu.filesystem.FilesystemArxiuFilesystem;
import es.caib.plugins.arxiu.filesystem.FilesystemArxiuPlugin;
import es.caib.plugins.arxiu.filesystem.Utils;

/**
 * Tests per al plugin filesystem de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/es/caib/plugins/arxiu/filesystem/test/application-context-test.xml"})
public class FilesystemArxiuPluginTest {
	
	private static final String PATH = "/home/moisesp/Limit/programs/arxiu/test01/";
	private static final String CODIFICAT = "true";
	
	private static final String PATH_DOC_PROVES = "/home/moisesp/Limit/programs/arxiu/prova.pdf";
	private static final String PATH_SIG_PROVES_01 = "/home/moisesp/Limit/programs/arxiu/prova.pdf.1.sig";
	private static final String PATH_SIG_PROVES_02 = "/home/moisesp/Limit/programs/arxiu/prova.pdf.2.sig";
	private static final String PATH_SIG_PROVES_03 = "/home/moisesp/Limit/programs/arxiu/prova.pdf.3.sig";
	
	private static final int NUM_EXP = 15;
	private static final int NUM_CPT = 30;
	private static final int NUM_DOC = 60;
	
	private static final int PROP_EXP_CARP = 70;
	private static final int PROP_DOC_ESBORRANY = 60;
	private static final int PROP_MOD = 3;
	private static final int PROP_ESBORR = 30;
	private static final int PROP_OT = 50;
	private static final int PROP_MOURE_COPIAR = 40;
	
	private FilesystemArxiuPlugin plugin;
	private Random random;
	
	private String[] expedients;
	private String[] carpetes;
	private String[] documents;
	
	private boolean[] doc_esborrany;
	
	private InformacioItem[] expedients_modificat;
	private InformacioItem[] carpetes_modificat;
	private InformacioItem[] documents_modificat;
	
	private int[] exp_check;
	private int[] cpt_check;
	private int[] doc_check;
	
	@Before
	public void setUp() throws ArxiuException, IOException {
				
		FilesystemArxiuFilesystem.deleteFolder(PATH);
				
		System.setProperty("plugins.arxiu.filesystem.base.path", PATH);
		System.setProperty("plugins.arxiu.filesystem.emmagatzemament.codificat", CODIFICAT);
		
		exp_check = new int[NUM_EXP];
		cpt_check = new int[NUM_CPT];
		doc_check = new int[NUM_DOC];
		
		doc_esborrany = new boolean[NUM_DOC];
		
		plugin = new FilesystemArxiuPlugin();
		
		random = new Random(0);
		Date start, end;
		
		start = new Date();
		expedients = new String[NUM_EXP];
		for(int i = 0; i < expedients.length; i++) {
			expedients[i] = plugin.expedientCrear(
					"expedient_" + i,
					GeneradorObjectes.getExpedientMetadades(i, false));
		}
		end = new Date();
		System.out.println(NUM_EXP + " expedients creats en " + (end.getTime() - start.getTime()));
		
		start = new Date();
		carpetes = new String[NUM_CPT];
		for(int i = 0; i < carpetes.length; i++) {
			int prop = random.nextInt(100);
			String pare = null;
			if(i == 0 || prop > PROP_EXP_CARP) {
				pare = expedients[random.nextInt(NUM_EXP)];
			} else {
				pare = carpetes[random.nextInt(i)];
			}
			carpetes[i] = plugin.carpetaCrear("carpeta_" + i, pare);
		}
		end = new Date();
		System.out.println(NUM_CPT + " carpetes creades en " + (end.getTime() - start.getTime()));
		
		start = new Date();
		documents = new String[NUM_DOC];
		for(int i = 0; i < documents.length; i++) {
			int prop = random.nextInt(100);
			String pare = null;
			if(i == 0 || prop > PROP_EXP_CARP) {
				pare = expedients[random.nextInt(NUM_EXP)];
			} else {
				pare = carpetes[random.nextInt(NUM_CPT)];
			}
			Document document = new Document(
					null,
					"document_" + i,
					GeneradorObjectes.getDocumentContingut(PATH_DOC_PROVES),
					GeneradorObjectes.getMetadadesDocument(i, false),
					GeneradorObjectes.getDocumentFirmes(
							i,
							PATH_SIG_PROVES_01,
							PATH_SIG_PROVES_02,
							PATH_SIG_PROVES_03,
							false));
			int esborrany = random.nextInt(100);
			if(doc_esborrany[i] = esborrany < PROP_DOC_ESBORRANY)
				documents[i] = plugin.documentEsborranyCrear(document, pare);
			else
				documents[i] = plugin.documentCrear(document, pare);
		}
		end = new Date();
		System.out.println(NUM_DOC + " documents creats en " + (end.getTime() - start.getTime()));
		
	}
	
	@Test
	public void comprovarDetalls() throws ArxiuException {
		
		Arrays.fill(exp_check, 0);
		Arrays.fill(cpt_check, 0);
		Arrays.fill(doc_check, 0);
		
		for(int i = 0; i < NUM_EXP; i++)
			detallsExpedient(expedients[i]);
				
		for(int i = 0; i < NUM_EXP; i++)
			assertSame("Expedient " + expedients[i] + ", Index " + i + ", no comprovat", exp_check[i], 1);
		for(int i = 0; i < NUM_CPT; i++)
			assertSame("Carpeta " + carpetes[i] + ", Index " + i + ", no comprovada", cpt_check[i], 1);
		for(int i = 0; i < NUM_DOC; i++)
			assertSame("Document " + documents[i] + ", Index " + i + ", no comprovat", doc_check[i], 1);
		
		for(int crp : cpt_check) assertSame(crp, 1);
		for(int doc : doc_check) assertSame(doc, 1);
		
		System.out.println("Test [ComprovarDetalls] finalitzat");
	}
	private void detallsExpedient(
			String expedientId) throws ArxiuException {
		
		int index = 0;
		while(index < NUM_EXP && !expedients[index].equals(expedientId)) index++;
		if(index == NUM_EXP) return;
		
		Expedient expedient = plugin.expedientDetalls(expedientId, null);
		assertTrue("Error al comprovar l'expedient " + expedientId,
				expedient.equals(new Expedient(
						expedientId,
						"expedient_" + index,
						GeneradorObjectes.getExpedientMetadades(index, false),
						expedient.getContinguts())));
		exp_check[index]++;
		
		for(InformacioItem item : expedient.getContinguts()) {
			if(item.getTipus() == ContingutTipus.CARPETA) {
				detallsCarpeta(item.getIdentificador());
			} else if(item.getTipus() == ContingutTipus.DOCUMENT) {
				detallsDocument(item.getIdentificador());
			} else if(item.getTipus() == ContingutTipus.EXPEDIENT) {
				fail("L'expedient " + expedientId + " conté un expedient");
			} else {
				fail("No s'ha reconegut en contingut de l'expedient: " + item.getTipus());
			}
		}
		
	}
	private void detallsCarpeta(
			String carpetaId) throws ArxiuException {
		
		int index = 0;
		while(index < NUM_CPT && !carpetes[index].equals(carpetaId)) index++;
		if(index == NUM_CPT) return;
		
		Carpeta carpeta = plugin.carpetaDetalls(carpetaId);
		assertTrue("Error al comprovar la carpeta " + carpetaId,
				carpeta.equals(new Carpeta(
						carpetaId,
						"carpeta_" + index,
						carpeta.getInformacioItems())));
		cpt_check[index]++;
		
		for(InformacioItem item : carpeta.getInformacioItems()) {
			if(item.getTipus() == ContingutTipus.CARPETA) {
				detallsCarpeta(item.getIdentificador());
			} else if(item.getTipus() == ContingutTipus.DOCUMENT) {
				detallsDocument(item.getIdentificador());
			} else if(item.getTipus() == ContingutTipus.EXPEDIENT) {
				fail("La carpeta " + carpetaId + " conté un expedient");
			} else {
				fail("No s'ha reconegut en contingut de la carpeta: " + item.getTipus());
			}
		}
		
	}
	private void detallsDocument(
			String documentId) throws ArxiuException {
		
		int index = 0;
		while(index < NUM_DOC && !documents[index].equals(documentId)) index++;
		if(index == NUM_DOC) return;
		
		Document document = plugin.documentDetalls(
				documentId, 
				null, 
				true);
		assertTrue("Error al comprovar el document " + documentId,
				document.equals(new Document(
						documentId,
						"document_" + index,
						GeneradorObjectes.getDocumentContingut(PATH_DOC_PROVES),
						GeneradorObjectes.getMetadadesDocument(index, false),
						GeneradorObjectes.getDocumentFirmes(
								index,
								PATH_SIG_PROVES_01,
								PATH_SIG_PROVES_02,
								PATH_SIG_PROVES_03,
								false))));
		doc_check[index]++;
	}
	
	@Test
	public void comprovarModificar() throws ArxiuException {
		
		int num_exp_mod = NUM_EXP / PROP_MOD;
		int num_cpt_mod = NUM_CPT / PROP_MOD;
		int num_doc_mod = NUM_DOC / PROP_MOD;
		
		expedients_modificat = new InformacioItem[num_exp_mod];
		carpetes_modificat = new InformacioItem[num_cpt_mod];
		documents_modificat = new InformacioItem[num_doc_mod];
		
		for(int i = 0; i < num_exp_mod; i++) {
			int index = random.nextInt(NUM_EXP);
			
			expedients_modificat[i] = plugin.expedientModificar(
					expedients[index],
					"expedient" + index + "_MOD",
					GeneradorObjectes.getExpedientMetadades(i, true));
			
			Expedient expedient = plugin.expedientDetalls(
					expedients_modificat[i].getIdentificador(),
					null);
			
			assertTrue("Error al comprovar l'expedient modificar " + expedients_modificat[i].getIdentificador(),
					expedient.equals(new Expedient(
							expedients_modificat[i].getIdentificador(),
							"expedient" + index + "_MOD",
							GeneradorObjectes.getExpedientMetadades(i, true),
							expedient.getContinguts())));
		}
		
		for(int i = 0; i < num_cpt_mod; i++) {
			int index = random.nextInt(NUM_CPT);
			
			carpetes_modificat[i] = plugin.carpetaModificar(
					carpetes[index],
					"carpeta_" + index + "_MOD");
			
			Carpeta carpeta = plugin.carpetaDetalls(carpetes_modificat[i].getIdentificador());
			
			assertTrue("Error al comprovar la carpeta modificada " + carpetes_modificat[i].getIdentificador(),
					carpeta.equals(new Carpeta(
							carpetes_modificat[i].getIdentificador(),
							"carpeta_" + index + "_MOD",
							carpeta.getInformacioItems())));
		}
		
		for(int i = 0; i < num_doc_mod; i++) {
			int index = random.nextInt(NUM_DOC);
			
			documents_modificat[i] = plugin.documentModificar(
					new Document(
							documents[index],
							"document" + index + "_MOD",
							GeneradorObjectes.getDocumentContingut(PATH_DOC_PROVES),
							GeneradorObjectes.getMetadadesDocument(index, true),
							GeneradorObjectes.getDocumentFirmes(
									index,
									PATH_SIG_PROVES_01,
									PATH_SIG_PROVES_02,
									PATH_SIG_PROVES_03,
									true)));
			
			Document document = plugin.documentDetalls(
					documents_modificat[i].getIdentificador(),
					null,
					true);
			
			assertTrue("Error al comprovar el document modificar " + documents_modificat[i].getIdentificador(),
					document.equals(new Document(
							documents_modificat[i].getIdentificador(),
							"document" + index + "_MOD",
							GeneradorObjectes.getDocumentContingut(PATH_DOC_PROVES),
							GeneradorObjectes.getMetadadesDocument(index, true),
							GeneradorObjectes.getDocumentFirmes(
									index,
									PATH_SIG_PROVES_01,
									PATH_SIG_PROVES_02,
									PATH_SIG_PROVES_03,
									true))));
		}
		
		System.out.println("Test [ComprovarModificar] finalitzat");
	}
	
	@Test
	public void comprovarEsborrar() throws ArxiuException {
		
		Arrays.fill(exp_check, 0);
		Arrays.fill(cpt_check, 0);
		Arrays.fill(doc_check, 0);
		
		int count_exp = 0, count_cpt = 0, count_doc = 0;
		
		for(int i = 0; i < NUM_EXP; i++) {
			int prop = random.nextInt(100);
			if(prop < PROP_ESBORR) {
				detallsExpedient(expedients[i]);
				plugin.expedientEsborrar(expedients[i]);
				count_exp++;
			}
		}
		System.out.println("Expedients esborrats " + count_exp);
		
		for(int i = 0; i < NUM_CPT; i++) {
			int prop = random.nextInt(100);
			if(prop < PROP_ESBORR && cpt_check[i] == 0) {
				detallsCarpeta(carpetes[i]);
				plugin.carpetaEsborrar(carpetes[i]);
				count_cpt++;
			}
		}
		System.out.println("Carpetes esborrades " + count_cpt);
		
		for(int i = 0; i < NUM_DOC; i++) {
			int prop = random.nextInt(100);
			if(prop < PROP_ESBORR && doc_check[i] == 0) {
				detallsDocument(documents[i]);
				plugin.documentEsborrar(documents[i]);
				count_doc++;
			}
		}
		System.out.println("Documents esborrats " + count_doc);
		
		for(int i = 0; i < NUM_EXP; i++) {
			if(exp_check[i] == 1) {
				try {
					plugin.expedientDetalls(expedients[i], null);
					fail("L'expedient " + expedients[i] + " no s'ha esborrat correctament");
				} catch(ArxiuException e) {}
			} else if(exp_check[i] == 0) {
				plugin.expedientDetalls(expedients[i], null);
			} else {
				fail("L'expedient s'ha esborrat " + exp_check[i] + " vegades");
			}
		}
		
		for(int i = 0; i < NUM_CPT; i++) {
			if(cpt_check[i] == 1) {
				try {
					plugin.carpetaDetalls(carpetes[i]);
					fail("La carpeta " + carpetes[i] + " no s'ha esborrat correctament");
				} catch(ArxiuException e) {}
			} else if(cpt_check[i] == 0) {
				plugin.carpetaDetalls(carpetes[i]);
			} else {
				fail("La carpeta s'ha esborrat " + cpt_check[i] + " vegades");
			}
		}
		
		for(int i = 0; i < NUM_DOC; i++) {
			if(doc_check[i] == 1) {
				try {
					plugin.documentDetalls(documents[i], null, true);
					fail("El document " + documents[i] + " no s'ha esborrat correctament");
				} catch(ArxiuException e) {}
			} else if(doc_check[i] == 0) {
				plugin.documentDetalls(documents[i], null, true);
			} else {
				fail("El document s'ha esborrat " + doc_check[i] + " vegades");
			}
		}
		
		System.out.println("Test [ComprovarEsborrar] finalitzat");
	}
	
	@Test
	public void comprovarVersions() throws ArxiuException {
		
		comprovarModificar();
		
		for(int i = 0; i < NUM_EXP; i++) {
			List<InformacioItem> items = plugin.expedientVersions(expedients[i]);
			for(int j = 0; j < items.size(); j++) {
				InformacioItem item0 = items.get(j);
				Expedient expedient0 = plugin.expedientDetalls(item0.getIdentificador(), null);
				for(int z = 0; z < items.size(); z++) {
					if(j != z) {
						InformacioItem item1 = items.get(z);
						Expedient expedient1 = plugin.expedientDetalls(
								item1.getIdentificador(),
								item0.getVersio());
						assertTrue(
								"L'expedient " + item0.getIdentificador() +
								" i l'expedient " + item1.getIdentificador() + " no son version del mateix expedient",
								expedient0.equals(expedient1));
					}
				}
			}
		}
		
		for(int i = 0; i < NUM_DOC; i++) {
			List<InformacioItem> items = plugin.documentVersions(documents[i]);
			for(int j = 0; j < items.size(); j++) {
				InformacioItem item0 = items.get(j);
				Document document0 = plugin.documentDetalls(item0.getIdentificador(), null, true);
				for(int z = 0; z < items.size(); z++) {
					if(j != z) {
						InformacioItem item1 = items.get(z);
						Document document1 = plugin.documentDetalls(
								item1.getIdentificador(),
								item0.getVersio(),
								true);
						assertTrue(
								"El document " + item0.getIdentificador() +
								" i el document " + item1.getIdentificador() + " no son version del mateix document",
								document0.equals(document1));
					}
				}
			}
		}
		
		System.out.println("Test [ComprovarVersions] finalitzat");
	}
	
	@Test
	public void comprovarConsultes() throws ArxiuException {
		
		List<ConsultaFiltre> filtres = new ArrayList<ConsultaFiltre>();
		
		filtres.add(new ConsultaFiltre(
				Fields.EX_VERSIONTI,
				ConsultaOperacio.CONTE,
				"1"));
		ConsultaResultat resultat = plugin.expedientConsulta(filtres, 1, 10);
		assertSame(resultat.getNumRetornat().intValue(), 6);
		
		filtres.add(new ConsultaFiltre(
				Fields.EX_DATA_OBERTURA,
				ConsultaOperacio.MAJOR,
				String.valueOf(new Date(11).getTime())));
		resultat = plugin.expedientConsulta(filtres, 1, 10);
		assertSame(resultat.getNumRetornat().intValue(), 3);
		
		filtres.add(new ConsultaFiltre(
				Fields.EX_SERIE_DOCUMENTAL,
				ConsultaOperacio.IGUAL,
				"ex_seriedoc_12"));
		resultat = plugin.expedientConsulta(filtres, 1, 10);
		assertSame(resultat.getNumRetornat().intValue(), 1);
		
		filtres = new ArrayList<ConsultaFiltre>();
		
		filtres.add(new ConsultaFiltre(
				Fields.DOC_VERSIONTI,
				ConsultaOperacio.CONTE,
				"1"));
		resultat = plugin.documentConsulta(filtres, 1, 100);
		assertSame(resultat.getNumRetornat().intValue(), 15);
		
		filtres.add(new ConsultaFiltre(
				Fields.DOC_DATA,
				ConsultaOperacio.MAJOR,
				String.valueOf(new Date(20).getTime())));
		resultat = plugin.documentConsulta(filtres, 1, 100);
		assertSame(resultat.getNumRetornat().intValue(), 4);
		
		filtres.add(new ConsultaFiltre(
				Fields.DOC_SERIE_DOCUMENTAL,
				ConsultaOperacio.IGUAL,
				"doc_serieDocumental_31"));
		resultat = plugin.documentConsulta(filtres, 1, 100);
		assertSame(resultat.getNumRetornat().intValue(), 1);
		
		filtres = new ArrayList<ConsultaFiltre>();
		
		filtres.add(new ConsultaFiltre(
				Fields.DOC_ORGAN,
				ConsultaOperacio.CONTE,
				"doc_organ_a_42"));
		resultat = plugin.documentConsulta(filtres, 1, 100);
		assertSame(resultat.getNumRetornat().intValue(), 1);
		
		System.out.println("Test [ComprovarConsultes] finalitzat");
	}
	
	@Test
	public void comprovarExpedientTancatObert() throws ArxiuException {
		
		for(int i = 0; i < NUM_EXP; i++) {
			int prop = random.nextInt(100);
			if(prop > PROP_OT) {
				try {
					plugin.expedientReobrir(expedients[i]);
					fail("L'expedient id:" + expedients[i] + " s'ha obert quan ja hi estava");
				} catch (ArxiuException e) {}
				plugin.expedientModificar(
						expedients[i],
						"expedient" + i + "_MOD",
						GeneradorObjectes.getExpedientMetadades(i, true));
				plugin.expedientTancar(expedients[i]);
				try {
					plugin.expedientModificar(
							expedients[i],
							"expedient" + i + "_MOD",
							GeneradorObjectes.getExpedientMetadades(i, true));
					fail("L'expedient id:" + expedients[i] + " que esta tancat s'ha pogut modificar");
				} catch (ArxiuException e) {}
				try {
					plugin.expedientTancar(expedients[i]);
					fail("L'expedient id:" + expedients[i] + " s'ha tancat quan ja hi estava");
				} catch (ArxiuException e) {}
			}
		}
		
		System.out.println("Test [ComprovarExpedientTancatObert] finalitzat");
	}
	
	@Test
	public void comprovarDocumentEsborrany() throws ArxiuException {
		
		for(int i = 0; i < NUM_DOC; i++) {
			
			if(doc_esborrany[i]) {
				plugin.documentEstablirDefinitiu(documents[i]);
				try {
					plugin.documentEstablirDefinitiu(documents[i]);
					fail("El document id:" + documents[i] + " s'ha establert com a definitiu dues vegades");
				} catch (ArxiuException e) {}
			} else {
				try {
					plugin.documentEstablirDefinitiu(documents[i]);
					fail("El document id:" + documents[i] + " s'ha establert com a definitiu dues vegades");
				} catch (ArxiuException e) {}
			}
		}
		
		System.out.println("Test [ComprovarDocumentEsborrany] finalitzat");
	}
	
	@Test
	public void comprovarDocumentMoure() throws ArxiuException {
		
		for(int i = 0; i < NUM_DOC; i++) {
			int prop = random.nextInt(100);
			if(prop < PROP_MOURE_COPIAR) {
				prop = random.nextInt(100);
				List<InformacioItem> items = null;
				if(prop > PROP_EXP_CARP) {
					int index = random.nextInt(NUM_EXP);
					plugin.documentMoure(documents[i], expedients[index]);
					Expedient expedient = plugin.expedientDetalls(expedients[index], null);
					items = expedient.getContinguts();
				} else {
					int index = random.nextInt(NUM_CPT);
					plugin.documentMoure(documents[i], carpetes[index]);
					Carpeta carpeta = plugin.carpetaDetalls(carpetes[index]);
					items = carpeta.getInformacioItems();
				}
				
				int fills = 0;
				for(InformacioItem item : items) {
					if(item.getIdentificador().equals(documents[i])) fills++;
				}
				
				assertSame(fills, 1);
			}
		}
		
		System.out.println("Test [ComprovarDocumentMoure] finalitzat");
	}
	
	@Test
	public void comprovarDocumentCopiar() throws ArxiuException {
		
		for(int i = 0; i < NUM_DOC; i++) {
			int prop = random.nextInt(100);
			if(prop < PROP_MOURE_COPIAR) {
				prop = random.nextInt(100);
				String documentId = null;
				List<InformacioItem> items = null;
				if(prop > PROP_EXP_CARP) {
					int index = random.nextInt(NUM_EXP);
					Expedient expedient = plugin.expedientDetalls(expedients[index], null);
					int count = expedient.getContinguts().size();
					documentId = plugin.documentCopiar(documents[i], expedients[index]);
					expedient = plugin.expedientDetalls(expedients[index], null);
					assertSame(count+1, expedient.getContinguts().size());
					items = expedient.getContinguts();
				} else {
					int index = random.nextInt(NUM_CPT);
					Carpeta carpeta = plugin.carpetaDetalls(carpetes[index]);
					int count = carpeta.getInformacioItems().size();
					documentId = plugin.documentCopiar(documents[i], carpetes[index]);
					carpeta = plugin.carpetaDetalls(carpetes[index]);
					assertSame(count+1, carpeta.getInformacioItems().size());
					items = carpeta.getInformacioItems();
				}
				
				int fills = 0;
				for(InformacioItem item : items) {
					if(item.getIdentificador().equals(documentId)) fills++;;
				}
				assertSame(fills, 1);
				
				Document document0 = plugin.documentDetalls(documents[i], null, true);
				Document document1 = plugin.documentDetalls(documentId, null, true);
				assertEquals(documents[i], document1.getContingut().getIdentificadorOrigen());
				document0.setIdentificador(documentId);
				document0.getContingut().setIdentificadorOrigen(documents[i]);
				assertTrue(document0.equals(document1));
			}
		}
		
		System.out.println("Test [ComprovarDocumentCopiar] finalitzat");
	}
	
	@Test
	public void comprovarCarpetaMoure() throws ArxiuException {
		
		for(int i = 0; i < NUM_DOC; i++) {
			int prop = random.nextInt(100);
			if(prop < PROP_MOURE_COPIAR) {
				prop = random.nextInt(100);
				String documentId = null;
				List<InformacioItem> items = null;
				if(prop > PROP_EXP_CARP) {
					int index = random.nextInt(NUM_EXP);
					Expedient expedient = plugin.expedientDetalls(expedients[index], null);
					int count = expedient.getContinguts().size();
					documentId = plugin.documentCopiar(documents[i], expedients[index]);
					expedient = plugin.expedientDetalls(expedients[index], null);
					assertSame(count+1, expedient.getContinguts().size());
					items = expedient.getContinguts();
				} else {
					int index = random.nextInt(NUM_CPT);
					Carpeta carpeta = plugin.carpetaDetalls(carpetes[index]);
					int count = carpeta.getInformacioItems().size();
					documentId = plugin.documentCopiar(documents[i], carpetes[index]);
					carpeta = plugin.carpetaDetalls(carpetes[index]);
					assertSame(count+1, carpeta.getInformacioItems().size());
					items = carpeta.getInformacioItems();
				}
				
				int fills = 0;
				for(InformacioItem item : items) {
					if(item.getIdentificador().equals(documentId)) fills++;;
				}
				assertSame(fills, 1);
				
				compararDocuments(documents[i], documentId);
			}
		}
		
		System.out.println("Test [ComprovarCarpetaMoure] finalitzat");
	}
	
	@Test
	public void comprovarCarpetaCopiar() throws ArxiuException {
		
		for(int i = 0; i < NUM_CPT; i++) {
			int prop = random.nextInt(100);
			if(prop < PROP_MOURE_COPIAR) {
				prop = random.nextInt(100);
				String carpetaId = null;
				List<InformacioItem> items = null;
				if(prop > PROP_EXP_CARP) {
					int index = random.nextInt(NUM_EXP);
					Expedient expedient = plugin.expedientDetalls(expedients[index], null);
					int count = expedient.getContinguts().size();
					carpetaId = plugin.carpetaCopiar(carpetes[i], expedients[index]);
					expedient = plugin.expedientDetalls(expedients[index], null);
					assertSame(count+1, expedient.getContinguts().size());
					items = expedient.getContinguts();
				} else {
					int index = random.nextInt(NUM_CPT);
					Carpeta carpeta = plugin.carpetaDetalls(carpetes[index]);
					int count = carpeta.getInformacioItems().size();
					carpetaId = plugin.carpetaCopiar(carpetes[i], carpetes[index]);
					carpeta = plugin.carpetaDetalls(carpetes[index]);
					assertSame(count+1, carpeta.getInformacioItems().size());
					items = carpeta.getInformacioItems();
				}
				
				int fills = 0;
				for(InformacioItem item : items) {
					if(item.getIdentificador().equals(carpetaId)) fills++;
				}
				assertSame(fills, 1);
				
				compararCarpetes(carpetes[i], carpetaId);
			}
		}
		
		System.out.println("Test [ComprovarCarpetaCopiar] finalitzat");
	}
	private void compararCarpetes(
			String carpetaId0,
			String carpetaId1) throws ArxiuException {
		
		Carpeta carpeta0 = plugin.carpetaDetalls(carpetaId0);
		Carpeta carpeta1 = plugin.carpetaDetalls(carpetaId1);
		carpeta1.setIdentificador(carpetaId0);
		assertTrue(carpeta0.getNom().equals(carpeta1.getNom()));
		assertSame(carpeta0.getInformacioItems().size(), carpeta1.getInformacioItems().size());
		for(int i = 0; i < carpeta0.getInformacioItems().size(); i++) {
			InformacioItem item0 = carpeta0.getInformacioItems().get(i);
			InformacioItem item1 = carpeta1.getInformacioItems().get(i);
			if(item0.getTipus() == ContingutTipus.CARPETA) {
				compararCarpetes(item0.getIdentificador(), item1.getIdentificador());
			} else if(item0.getTipus() == ContingutTipus.DOCUMENT) {
				compararDocuments(item0.getIdentificador(), item1.getIdentificador());
			} else {
				fail("El item id:" + item0.getIdentificador() + " no correspon a una carpeta o document");
			}
		}
	}
	private void compararDocuments(
			String documentId0,
			String documentId1) throws ArxiuException {
		
		Document document0 = plugin.documentDetalls(documentId0, null, true);
		Document document1 = plugin.documentDetalls(documentId1, null, true);
		assertEquals(documentId0, document1.getContingut().getIdentificadorOrigen());
		document0.setIdentificador(documentId1);
		document0.getContingut().setIdentificadorOrigen(documentId0);
		assertTrue(document0.equals(document1));
	}
	

}
