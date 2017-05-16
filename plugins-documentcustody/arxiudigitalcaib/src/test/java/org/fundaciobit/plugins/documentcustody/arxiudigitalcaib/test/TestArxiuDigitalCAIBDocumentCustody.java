package org.fundaciobit.plugins.documentcustody.arxiudigitalcaib.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import junit.framework.Assert;

import org.fundaciobit.plugins.documentcustody.api.AnnexCustody;
import org.fundaciobit.plugins.documentcustody.api.CustodyException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.documentcustody.api.test.TestDocumentCustody;
import org.fundaciobit.plugins.documentcustody.arxiudigitalcaib.ArxiuDigitalCAIBDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.arxiudigitalcaib.ExpedientCarpeta;
import org.fundaciobit.plugins.documentcustody.arxiudigitalcaib.test.beans.Anexo;
import org.fundaciobit.plugins.documentcustody.arxiudigitalcaib.test.beans.Interesado;
import org.fundaciobit.plugins.documentcustody.arxiudigitalcaib.test.beans.Oficina;
import org.fundaciobit.plugins.documentcustody.arxiudigitalcaib.test.beans.Organismo;
import org.fundaciobit.plugins.documentcustody.arxiudigitalcaib.test.beans.RegistroDetalle;
import org.fundaciobit.plugins.documentcustody.arxiudigitalcaib.test.beans.RegistroEntrada;
import org.fundaciobit.plugins.documentcustody.arxiudigitalcaib.test.beans.TipoDocumental;
import org.fundaciobit.plugins.documentcustody.arxiudigitalcaib.test.beans.Usuario;
import org.fundaciobit.plugins.documentcustody.arxiudigitalcaib.test.beans.UsuarioEntidad;
import org.fundaciobit.plugins.utils.FileUtils;
import org.fundaciobit.plugins.utils.Metadata;
import org.fundaciobit.plugins.utils.PluginsManager;

import es.caib.arxiudigital.apirest.ApiArchivoDigital;
import es.caib.arxiudigital.apirest.facade.pojos.Expediente;
import es.caib.arxiudigital.apirest.facade.resultados.Resultado;

/**
 * 
 * @author anadal
 *
 */
public class TestArxiuDigitalCAIBDocumentCustody extends TestDocumentCustody {

  public enum TipusGuardat {
    SAVEALL, DOCUMENT_PRIMER, FIRMA_PRIMER
  };

  public static final String packageBase = "es.caib.exemple.";

  public static final String propertyBase = packageBase
      + ArxiuDigitalCAIBDocumentCustodyPlugin.ARXIUDIGITALCAIB_PROPERTY_BASE;

  SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

  protected void compareDocument(String titol, AnnexCustody docSet, AnnexCustody docGet,
      boolean compareData) {

    if (docSet == null && docGet == null) {
      return; // OK
    }

    if ((docSet == null && docGet != null) || (docSet != null && docGet == null)) {
      Assert.assertNull(titol + ".- Algun dels dos valors val null i l'altre no: [SET = "
          + docSet + "][GET = " + docGet + "]");
    }

    Assert.assertEquals(titol + " Els noms no són iguals", docSet.getName(), docGet.getName());

    Assert.assertEquals(titol + " Les longituds no són iguals", docSet.getLength(),
        docGet.getLength());

    Assert
        .assertEquals(titol + " Els mimes no són iguals", docSet.getMime(), docGet.getMime());

    Assert
        .assertEquals(titol + " Els mimes no són iguals", docSet.getMime(), docGet.getMime());

    if (compareData) {
      if (!Arrays.equals(docSet.getData(), docGet.getData())) {
        Assert.fail(titol + " El contingut no es igual");
      }
    } else {
      // Comprovar que la dades del get valen NULL
      Assert.assertNull(titol
          + ".- Les dades del fitxer haurien de ser NUll i valen alguna cosa",
          docGet.getData());
    }
  }

  public void testMetadades() throws Exception {

    Properties specificProperties = new Properties();
    IDocumentCustodyPlugin documentCustodyPlugin = instantiateDocumentCustodyPlugin(specificProperties);
    ArxiuDigitalCAIBDocumentCustodyPlugin plugin;
    plugin = (ArxiuDigitalCAIBDocumentCustodyPlugin) documentCustodyPlugin;

    if (!plugin.supportsMetadata()) {
      System.err.print("Aquest sistema de custòdia no suporta metadades");
      return;
    }

    // XYZ ZZZ
    {
      File f = new File("expedient.txt");
      if (f.exists()) {
        String custodyID = new String(FileUtils.readFromFile(f));

        documentCustodyPlugin.deleteCustody(custodyID);

      }
    }

    Map<String, Object> custodyParameters = new HashMap<String, Object>();

    RegistroEntrada registro = getRegistro();
    custodyParameters.put("registro", registro);
    Anexo anexo = getAnexo(registro);
    custodyParameters.put("anexo", anexo);

    String custodyID = documentCustodyPlugin.reserveCustodyID(custodyParameters);

    // XYZ ZZZ Eliminar
    FileOutputStream fos = new FileOutputStream(new File("expedient.txt"));
    fos.write(custodyID.getBytes());
    fos.close();

    DocumentCustody document = null;
    /*
     * document = new DocumentCustody(); document.setName("holacaracola.txt");
     * document.setData("holacaracola".getBytes());
     * document.setMime("text/plain");
     * document.setLength(document.getData().length);
     * 
     * documentCustodyPlugin.saveDocument(custodyID, custodyParameters,
     * document);
     */

    InputStream is2 = FileUtils.readResource(this.getClass(),
        "testarxiudigitalcaib/Firma2.pdf");
    byte[] data2 = FileUtils.toByteArray(is2);

    SignatureCustody signature2 = new SignatureCustody();
    signature2.setName("Firma2.pdf");
    signature2.setAttachedDocument(false);
    signature2.setData(data2);
    signature2.setLength(signature2.getData().length);
    signature2.setMime("application/pdf");
    signature2.setSignatureType(SignatureCustody.PADES_SIGNATURE);

    documentCustodyPlugin.saveSignature(custodyID, custodyParameters, signature2);
    // documentCustodyPlugin.saveAll(custodyID, custodyParameters, document,
    // signature2, null);

    Map<String, List<Metadata>> allmetas = plugin.getAllMetadata(custodyID);

    for (String k : allmetas.keySet()) {
      List<Metadata> list = allmetas.get(k);

      for (Metadata metadata : list) {
        System.out.println(metadata.getKey() + " => " + metadata.getValue());
      }

    }

    //
    final String key = "hola";
    Metadata mSet = new Metadata(key, "caracola");

    plugin.addMetadata(custodyID, mSet, custodyParameters);

    Metadata mGet = plugin.getOnlyOneMetadata(custodyID, key);

    System.out.println(" META = " + mGet.getKey() + ": " + mGet.getValue());

    Assert.assertEquals("M1 Les metadades no són iguals", mSet, mGet);

    plugin.deleteMetadata(custodyID, key);

    mGet = plugin.getOnlyOneMetadata(custodyID, key);

    Assert.assertNull("M2 La metadada amb clau " + key
        + " ha sigut esborrada però encara la puc llegir", mGet);

    // XYZ ZZZ Descomentar documentCustodyPlugin.deleteCustody(custodyID);

  }
  
  
  
  public void testSimpleDoc() throws Exception {

    // = true significa que no es sobre escriu
    // = false es sobreescriu sobre el mateix fitxer
    final boolean reservarCadaVegada =  true;

    final TipusGuardat tipusGuardat =  TipusGuardat.SAVEALL;
    
        System.out.println();
        System.out.println(" =========================================");
        System.out.println("   -------- reservarCadaVegada = " + reservarCadaVegada);
        System.out.println("   -------- tipusGuardat = " + tipusGuardat);
        System.out.println(" =========================================");
        System.out.println();
        testSimpleDocConfigurable(reservarCadaVegada, tipusGuardat, false);
      
    

  }
  
  

  public void testSimpleDocFull() throws Exception {

    // = true significa que no es sobre escriu
    // = false es sobreescriu sobre el mateix fitxer
    final boolean[] reservarCadaVegada = {  false , true};

    final TipusGuardat[] tipusGuardat = { TipusGuardat.SAVEALL, TipusGuardat.DOCUMENT_PRIMER,
        TipusGuardat.FIRMA_PRIMER };

    for (int r = 0; r < reservarCadaVegada.length; r++) {

      for (int t = 0; t < tipusGuardat.length; t++) {

        System.out.println();
        System.out.println(" =========================================");
        System.out.println("   -------- reservarCadaVegada[r] = " + reservarCadaVegada[r]);
        System.out.println("   -------- tipusGuardat[t] = " + tipusGuardat[t]);
        System.out.println(" =========================================");
        System.out.println();
        testSimpleDocConfigurable(reservarCadaVegada[r], tipusGuardat[t], true);
      }
    }

  }

  public void testSimpleDocConfigurable(boolean reservarCadaVegada, TipusGuardat tipusGuardat,
      boolean deleteOnFinish)
      throws Exception {

    Properties specificProperties = new Properties();
    IDocumentCustodyPlugin documentCustodyPlugin = instantiateDocumentCustodyPlugin(specificProperties);
    ArxiuDigitalCAIBDocumentCustodyPlugin plugin;
    plugin = (ArxiuDigitalCAIBDocumentCustodyPlugin) documentCustodyPlugin;
    ApiArchivoDigital api = plugin.getApiArxiu(null);

    /*
     * { String nom = "ADDITIONALINFO"; byte[] content = "PLATANO".getBytes();
     * String uuidExp = "f2673b34-9369-4bdf-b58f-6768537bcca4";
     * 
     * createSimpleDoc(api, nom, content, uuidExp);
     * 
     * 
     * } if (true) { return; }
     */

    // XYZ ZZZ Eliminar
    {
      File f = new File("expedient.txt");
      if (f.exists()) {
        String custodyID = new String(FileUtils.readFromFile(f));

        documentCustodyPlugin.deleteCustody(custodyID);

      }
    }

    Map<String, Object> custodyParameters = new HashMap<String, Object>();

    // new Date(167526000000L)

    RegistroEntrada registro = getRegistro();

    custodyParameters.put("registro", registro);

    Anexo anexo = getAnexo(registro);
    custodyParameters.put("anexo", anexo);

    String custodyID = null;

    if (!reservarCadaVegada) {
      // Reserve ID
      custodyID = documentCustodyPlugin.reserveCustodyID(custodyParameters);

      System.out.println(" Reservat custodyID = " + custodyID);

      // XYZ ZZZ Eliminar
      FileOutputStream fos = new FileOutputStream(new File("expedient.txt"));
      fos.write(custodyID.getBytes());
      fos.close();

      ExpedientCarpeta ec = plugin.decodeCustodyID(custodyID);

      Resultado<Expediente> expedient = api.obtenerExpediente(ec.expedientID);

      Map<String, Object> metadatas = expedient.getElementoDevuelto().getMetadataCollection();

      for (String key : metadatas.keySet()) {
        Object value = metadatas.get(key);

        System.out.println(" META[" + key + "] = " + value);

      }

    }

    final DocumentCustody[] documents;

    final SignatureCustody[] signatures;

    Metadata[] metas = null;
    {

      DocumentCustody document = new DocumentCustody();
      document.setName("holacaracola.txt");
      document.setData("holacaracola".getBytes());
      document.setMime("text/plain");
      document.setLength(document.getData().length);

      DocumentCustody document2 = new DocumentCustody();
      document2.setName("holacaracola.xml");
      document2.setData("holacaracolaV2222222".getBytes());
      document2.setMime("text/plain");
      document2.setLength(document2.getData().length);

      InputStream is = FileUtils.readResource(this.getClass(),
          "testarxiudigitalcaib/Firma.pdf");
      byte[] data = FileUtils.toByteArray(is);

      SignatureCustody signature = new SignatureCustody();
      signature.setName("Firma.pdf");
      signature.setAttachedDocument(false);
      signature.setData(data);
      signature.setLength(signature.getData().length);
      signature.setMime("application/pdf");
      signature.setSignatureType(SignatureCustody.PADES_SIGNATURE);

      InputStream is2 = FileUtils.readResource(this.getClass(),
          "testarxiudigitalcaib/Firma2.pdf");
      byte[] data2 = FileUtils.toByteArray(is2);

      SignatureCustody signature2 = new SignatureCustody();
      signature2.setName("Firma2.pdf");
      signature2.setAttachedDocument(false);
      signature2.setData(data2);
      signature2.setLength(signature2.getData().length);
      signature2.setMime("application/pdf");
      signature2.setSignatureType(SignatureCustody.PADES_SIGNATURE);

      if (reservarCadaVegada) {

        documents = new DocumentCustody[] { document2, document, null, document2, document2 };

        signatures = new SignatureCustody[] { null, signature, signature, signature,
            signature2 };

      } else {
        // TODO TESTS ERRONIS
        // (1) Pasar de document a null
        // (2) Passar de signature a null.

        // MULTIPLE TESTS
        documents = new DocumentCustody[] { document, document2, document };
        signatures = new SignatureCustody[] { null, signature, signature2 };
      }
    }

    // custodyID = documentCustodyPlugin.reserveCustodyID(custodyParameters);

    for (int i = 0; i < signatures.length; i++) {

      if (reservarCadaVegada) {
        custodyID = documentCustodyPlugin.reserveCustodyID(custodyParameters);
      }

      System.out.println();
      System.out.println(" ---------  Test[" + i + "]  --------");
      System.out.println();
      System.out.flush();
      Thread.sleep(500);

      DocumentCustody doc = documents[i];
      SignatureCustody sign = signatures[i];

      switch (tipusGuardat) {
      case SAVEALL:
        documentCustodyPlugin.saveAll(custodyID, custodyParameters, doc, sign, metas);
        break;
      case DOCUMENT_PRIMER:
        if (doc != null) {
          documentCustodyPlugin.saveDocument(custodyID, custodyParameters, doc);
        }
        if (sign != null) {
          documentCustodyPlugin.saveSignature(custodyID, custodyParameters, sign);
        }
        break;
      case FIRMA_PRIMER:
        if (sign != null) {
          documentCustodyPlugin.saveSignature(custodyID, custodyParameters, sign);
        }
        if (doc != null) {
          documentCustodyPlugin.saveDocument(custodyID, custodyParameters, doc);
        }
        break;
      }

      // ----------- DOCUMENT
      {

        System.out.println("     * Check D1");
        byte[] binData = documentCustodyPlugin.getDocument(custodyID);

        if (doc == null) {
          Assert.assertNull("D1.- S'ha guardat un document NULL però retorna valors", binData);
        } else {
          if (!Arrays.equals(binData, doc.getData())) {
            Assert.fail("D1.- El contingut del document No es igual");
          }
        }

        DocumentCustody onlyinfo = documentCustodyPlugin.getDocumentInfoOnly(custodyID);
        System.out.println("     * Check D2");
        compareDocument("D2", doc, onlyinfo, false);

        DocumentCustody fulldoc = documentCustodyPlugin.getDocumentInfo(custodyID);
        System.out.println("     * Check D3: ");
        compareDocument("D3", doc, fulldoc, true);
      }
      // ----------- SIGNATURE
      {

        System.out.println("     * Check S1");
        byte[] binData = documentCustodyPlugin.getSignature(custodyID);

        if (sign == null) {
          Assert.assertNull("S1.- S'ha guardat una signatura NULL però retorna valors",
              binData);
        } else {
          if (!Arrays.equals(binData, sign.getData())) {
            Assert.fail("S1.- El contingut del document No es igual");
          }
        }

        SignatureCustody onlyinfo = documentCustodyPlugin.getSignatureInfoOnly(custodyID);
        System.out.println("     * Check S2");
        compareDocument("S2", sign, onlyinfo, false);

        SignatureCustody fulldoc = documentCustodyPlugin.getSignatureInfo(custodyID);
        System.out.println("     * Check S3: ");
        compareDocument("S3", sign, fulldoc, true);
      }

      if (reservarCadaVegada) {
        documentCustodyPlugin.deleteCustody(custodyID);
      }

    }

    /*
     * // UPDATE DOCUMENT doc.setData("holacaracola_v2.0".getBytes());
     * documentCustodyPlugin.saveDocument(custodyID, custodyParameters, doc);
     * 
     * DocumentCustody docInfo =
     * documentCustodyPlugin.getDocumentInfo(custodyID);
     * 
     * if (docInfo == null) { Assert.fail("No pot llegir document info"); }
     * 
     * System.out.println("Tamany Info = " + docInfo.getLength());
     * 
     * byte[] data = documentCustodyPlugin.getDocument(custodyID); if (data ==
     * null) { Assert.fail("No pot llegir document byte []"); }
     * 
     * System.out.println("Tamany Info = " + docInfo.getLength());
     * System.out.println("Tamany byte[] = " + data.length);
     */

    if (!deleteOnFinish) {
      documentCustodyPlugin.deleteCustody(custodyID);
    }

  }

  protected Anexo getAnexo(RegistroEntrada registro) {
    TipoDocumental tipoDocumental = new TipoDocumental(77L, "TD02", "FundacioBit");

    Long validezDocumento = 1L; // TIPOVALIDEZDOCUMENTO_COPIA

    Long tipoDocumento = 1L; // TIPO_DOCUMENTO_FORMULARIO = 1L

    Integer origenCiudadanoAdmin = 1;

    Date fechaCaptura = new Date(System.currentTimeMillis());
    int modoFirma = 1; // MODO_FIRMA_ANEXO_ATTACHED = 1;

    Anexo anexo = new Anexo(222L, "Titulo Anexo", tipoDocumental, validezDocumento,
        tipoDocumento, registro.getRegistroDetalle(), "Observacions de l'Anexo",
        origenCiudadanoAdmin, fechaCaptura, modoFirma);
    return anexo;
  }

  protected RegistroEntrada getRegistro() throws Exception {
    Usuario usuario = new Usuario("Antoni", "Nadal", "Bennasar", "12345678Z", "anadal");

    UsuarioEntidad usuarioEntitat = new UsuarioEntidad(usuario, "FundacioBit");

    Oficina oficina = new Oficina(33L, "OFI44556677");

    Organismo destino = new Organismo("ORGA_CAIB", "Comunitat Autonoma Illes Balears");

    // Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse("04/04/2017");

    Date fecha = new Date(System.currentTimeMillis());

    Integer numeroRegistro = Integer.parseInt(SDF.format(fecha));

    String numeroRegistroFormateado = "FORMAT_" + numeroRegistro;

    Long tipoDocumentacionFisica = 1L;
    Long idioma = 1L;

    String codigoAsunto = "COD_ASU_001";

    Oficina oficinaOrigen = new Oficina(66L, "OFI112233");

    List<Interesado> interesados = new ArrayList<Interesado>();
    {
      Interesado interesado = new Interesado("Antoni", "Nadal", "Bennasar", "12345678Z");
      interesados.add(interesado);
    }

    RegistroDetalle registroDetalle = new RegistroDetalle("Prova de Arxiu Digital CAIB ",
        tipoDocumentacionFisica, idioma, codigoAsunto, oficinaOrigen, interesados);

    RegistroEntrada registro = new RegistroEntrada(12345L, usuarioEntitat, oficina, destino,
        destino.getCodigo(), destino.getDenominacion(), fecha, numeroRegistro,
        numeroRegistroFormateado, registroDetalle);
    return registro;
  }

  @org.junit.Test
  public void testFull() throws Exception {

    Properties specificProperties = new Properties();

    Map<String, Object> custodyParameters = createCustodyParameters();

    internalTestGeneralDocumentCustody(specificProperties, custodyParameters, true);

  }

  /*
   * @org.junit.Test public void testFolderFromCustodyParameters() throws
   * Exception {
   * 
   * File baseDir = new File("./testReposWithFolder"); baseDir.mkdirs();
   * 
   * Properties specificProperties = new Properties();
   * 
   * specificProperties.setProperty(packageBase +
   * ArxiuDigitalCAIBDocumentCustodyPlugin.FILESYSTEM_PROPERTY_BASEDIR,
   * baseDir.getAbsolutePath());
   * 
   * boolean deleteOnFinish = true; // true;
   * 
   * internalTestFolderFromCustodyParameters(specificProperties,
   * deleteOnFinish);
   * 
   * }
   */

  /*
   * @org.junit.Test public void testAutomaticMetadatas() throws Exception {
   * 
   * File baseDir = new File("./testAutomaticMetadatas"); baseDir.mkdirs();
   * 
   * Properties specificProperties = new Properties();
   * 
   * specificProperties.setProperty(packageBase +
   * ArxiuDigitalCAIBDocumentCustodyPlugin.FILESYSTEM_PROPERTY_BASEDIR,
   * baseDir.getAbsolutePath());
   * 
   * final boolean deleteOnFinish = true;
   * 
   * internalTestAutomaticMetadatas(specificProperties, deleteOnFinish);
   * 
   * }
   */

  @Override
  public IDocumentCustodyPlugin instantiateDocumentCustodyPlugin(Properties specificProperties)
      throws CustodyException {

    Properties fsProperties = new Properties();

    try {
      fsProperties.load(new FileInputStream("plugin.properties"));
    } catch (Exception e) {
      throw new CustodyException("Error llegint fitxer plugin.properties: " + e.getMessage(),
          e);
    }

    if (specificProperties != null) {
      fsProperties.putAll(specificProperties);
    }

    IDocumentCustodyPlugin documentCustodyPlugin;
    documentCustodyPlugin = (IDocumentCustodyPlugin) PluginsManager.instancePluginByClass(
        ArxiuDigitalCAIBDocumentCustodyPlugin.class, packageBase, fsProperties);
    return documentCustodyPlugin;
  }

  protected Map<String, Object> createCustodyParameters() throws Exception {
    Map<String, Object> custodyParameters;
    custodyParameters = new HashMap<String, Object>();

    // new Date(167526000000L)

    RegistroEntrada registro = getRegistro();

    custodyParameters.put("registro", registro);

    Anexo anexo = getAnexo(registro);
    custodyParameters.put("anexo", anexo);

    return custodyParameters;
  }

  public static void main(String[] args) {
    try {

      System.out.println(ArxiuDigitalCAIBDocumentCustodyPlugin.class.getCanonicalName());

      TestArxiuDigitalCAIBDocumentCustody tester = new TestArxiuDigitalCAIBDocumentCustody();

      // tester.testAutomaticMetadatas();

      // tester.testFull();

      tester.testSimpleDoc();

      // tester.testMetadades();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public String getPropertyBase() {
    return propertyBase;
  }

}
