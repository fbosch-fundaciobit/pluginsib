import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class CheckKeyStore {

  public static void main(String[] args) {
    try {

      if (args.length != 4) {
        System.err
            .println("Usar: java -classpath . CheckKeyStore [file.jks] [alias] [pwd_keystore] [pwd_certificate]");
        System.exit(-1);
        return;
      }

      final String keystoreLocation = args[0];
      final String alias = args[1];
      final String pwd_keystore = args[2];
      final String pwd_certificate = args[3];

      File file = new File(keystoreLocation);

      if (!file.exists()) {
        System.err.println("Fitxer " + keystoreLocation + " no existeix.");
        System.exit(-1);
      }

      KeyStore keystore;
      FileInputStream is = null;
      try {
        is = new FileInputStream(file);
        keystore = KeyStore.getInstance(KeyStore.getDefaultType());

        keystore.load(is, pwd_keystore.toCharArray());

        System.out.println("=====  LListat d'alias del Keystore =======");

        Enumeration<?> enumeration = keystore.aliases();
        while (enumeration.hasMoreElements()) {
          String a = (String) enumeration.nextElement();

          Certificate certificate = keystore.getCertificate(a);
          System.out.println("  - Alias ]" + a + "[ => " + certificate.getType() + " "
              + ((X509Certificate) certificate).getSubjectDN());

        }
        System.out.println("--------------- final llistat----------------------");

      } catch (Throwable th) {

        System.err
            .println("El keystore no te el format correcte o la contrasenya del keystore no es la correcta.");
        System.exit(-1);
        return;

      } finally {
        if (is != null) {
          try {
            is.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

      // UPDATE- OBTAIN PRIVATE KEY

      Key key = null;
      
      try {
        key = keystore.getKey(alias, pwd_certificate.toCharArray());
      } catch (Exception e) {
        e.printStackTrace();
      }
      
      // String encodedKey = new Base64Encoder().encode(key.getEncoded());

      if (key == null) {
        System.err.println();
        System.err.println("L'alias ]" + alias
            + "[ no existeix o la contrasenya del certificat no es correcta.");
        System.exit(-1);
      }

      System.out.println(key.toString());
      System.out.println("Contrasenya certificat OK");

    } catch (Exception e) {
      e.printStackTrace();

    }
  }

}