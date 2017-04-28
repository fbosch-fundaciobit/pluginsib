package org.fundaciobit.plugins.validatesignature.api;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

/**
 * 
 * @author anadal
 *
 */
public abstract class AbstractValidateSignaturePlugin extends AbstractPluginProperties
    implements IValidateSignaturePlugin, ValidateSignatureConstants {

  protected Logger log = Logger.getLogger(this.getClass());

  /**
   * 
   */
  public AbstractValidateSignaturePlugin() {
    super();
  }

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public AbstractValidateSignaturePlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);
  }

  /**
   * @param propertyKeyBase
   */
  public AbstractValidateSignaturePlugin(String propertyKeyBase) {
    super(propertyKeyBase);
  }

  @Override
  public boolean filter(ValidateSignatureRequest vsr) {

    SignatureRequestedInformation required = vsr.getSignatureRequestedInformation();
    SignatureRequestedInformation supported = getSupportedSignatureRequestedInformation();

    if (!checkRequiredSupported(required.getReturnSignatureTypeFormatProfile(),
        supported.getReturnSignatureTypeFormatProfile())) {
      return false;
    }
    if (!checkRequiredSupported(required.getValidateCertificateRevocation(),
        supported.getValidateCertificateRevocation())) {
      return false;
    }
    if (!checkRequiredSupported(required.getReturnCertificateInfo(),
        supported.getReturnCertificateInfo())) {
      return false;
    }
    if (!checkRequiredSupported(required.getReturnValidationChecks(),
        supported.getReturnValidationChecks())) {
      return false;
    }
    if (!checkRequiredSupported(required.getReturnCertificates(),
        supported.getReturnCertificates())) {
      return false;
    }
    if (!checkRequiredSupported(required.getReturnTimeStampInfo(),
        supported.getReturnTimeStampInfo())) {
      return false;
    }

    return true;
  }

  protected boolean checkRequiredSupported(Boolean required, Boolean supported) {
    if (required == null || required == false) {
      return true;
    } else {
      // required == true
      if (supported == null) {
        // Suposarem que si que es retornarà
        return true;
      } else {
        return supported.booleanValue();
      }
    }
  }

  /**
   * 
   * @param vs
   */
  public static void printSignatureInfo(ValidateSignatureResponse vs) {
    System.out.println(" **************************************** ");
    System.out.println();
    System.out.println();

    ValidationStatus status = vs.getValidationStatus();
    if (status.getStatus() == ValidationStatus.SIGNATURE_VALID) {
      System.out.println(" status = VALID");
    } else {
      System.out
          .println(" status = "
              + ((status.getStatus() == ValidationStatus.SIGNATURE_INVALID) ? "INVALID"
                  : "ERROR"));
      System.out.println(" statusMsg = " + status.getErrorMsg());
    }

    System.out.println(" vs.getSignType() = " + vs.getSignType());
    System.out.println(" vs.getSignFormat() = " + vs.getSignFormat());
    System.out.println(" vs.getSignProfile() = " + vs.getSignProfile());

    SignatureDetailInfo[] diList = vs.getSignatureDetailInfo();
    if (diList != null) {
      for (int i = 0; i < diList.length; i++) {

        SignatureDetailInfo di = diList[i];

        System.out.println(" ================ SIGN[" + i + "] ===============");

        System.out.println(" - Algortime: " + di.getAlgorithm());
        System.out.println(" - DigestValue: " + di.getDigestValue());
        if (di.getSignDate() != null) {
          System.out
              .println(" - SignDate: " + new SimpleDateFormat().format(di.getSignDate()));
        }

        System.out.print(printChecks(di.getValidChecks(), " +++ VALID CHECKS +++"));
        System.out.print(printChecks(di.getInvalidChecks(), " +++ INVALID CHECKS +++"));
        System.out.print(printChecks(di.getIndeterminateChecks(),
            " +++ INDETERMINATE CHECKS +++"));

        {
          TimeStampInfo tsi = di.getTimeStampInfo();
          if (tsi != null) {

            System.out.println(" - SEGELL DE TEMPS ");
            System.out.println("      + Data: " + tsi.getCreationTime());
            System.out.println("      + Algo: " + tsi.getAlgorithm());
            System.out.println("      + CertificateIssuer: " + tsi.getCertificateIssuer());
            System.out.println("      + CertificateSubject: " + tsi.getCertificateSubject());
            System.out.println("      + Certificate: " + tsi.getCertificate());

          }
        }

        {
          byte[][] certificate = di.getCertificateChain();

          if (certificate != null) {
            System.out.println(" - Certificates[" + certificate.length + "]: ");

            for (int j = 0; j < certificate.length; j++) {
              System.out.println("    + Cert[" + j + "] = "
                  + org.fundaciobit.plugins.utils.Base64.encode(certificate[j]));
            }

          }
        }

        CertificateInfo ci = di.getCertificateInfo();
        if (ci != null) {
          System.out.println(" - Certificate Info:");
          System.out.println("     + CertInfo[ApellidosResponsable]: "
              + ci.getApellidosResponsable());
          System.out.println("     + CertInfo[OrganizacionEmisora]: "
              + ci.getOrganizacionEmisora());
          System.out.println("     + CertInfo[segundoApellidoResponsable]: "
              + ci.getSegundoApellidoResponsable());
          System.out.println("     + CertInfo[versionPolitica]: " + ci.getVersionPolitica());
          System.out.println("     + CertInfo[usoCertificado]: " + ci.getUsoCertificado());
          System.out.println("     + CertInfo[pais]: " + ci.getPais());
          System.out.println("     + CertInfo[subject]: " + ci.getSubject());
          System.out.println("     + CertInfo[numeroSerie]: " + ci.getNumeroSerie());
          System.out.println("     + CertInfo[primerApellidoResponsable]: "
              + ci.getPrimerApellidoResponsable());
          System.out.println("     + CertInfo[NombreApellidosResponsable]: "
              + ci.getNombreApellidosResponsable());
          System.out.println("     + CertInfo[validoHasta]: " + ci.getValidoHasta());
          System.out.println("     + CertInfo[idPolitica]: " + ci.getIdPolitica());
          System.out.println("     + CertInfo[ID_europeo]: " + ci.getIdEuropeo());
          System.out.println("     + CertInfo[validoDesde]: " + ci.getValidoDesde());
          System.out.println("     + CertInfo[tipoCertificado]: " + ci.getTipoCertificado());
          System.out.println("     + CertInfo[email]: " + ci.getEmail());
          System.out.println("     + CertInfo[clasificacion]: " + ci.getClasificacion());
          System.out.println("     + CertInfo[idEmisor]: " + ci.getIdEmisor());
          System.out.println("     + CertInfo[NIFResponsable]: " + ci.getNifResponsable());
          System.out.println("     + CertInfo[extensionUsoCertificado]: "
              + ci.getExtensionUsoCertificado());
          System.out.println("     + CertInfo[nombreResponsable]: "
              + ci.getNombreResponsable());
          System.out.println("     + CertInfo[politica]: " + ci.getPolitica());
          // Empleat Public
          System.out.println("     + CertInfo[NIFEntidadSuscriptora]: "
              + ci.getNifEntidadSuscriptora());
          System.out.println("     + CertInfo[entidadSuscriptora]: "
              + ci.getEntidadSuscriptora());
          System.out.println("     + CertInfo[unidadOrganizativa]: "
              + ci.getUnidadOrganizativa());
          System.out.println("     + CertInfo[numeroIdentificacionPersonal]: "
              + ci.getNumeroIdentificacionPersonal());
          System.out.println("     + CertInfo[organizacion]: " + ci.getOrganizacion());
          System.out.println("     + CertInfo[puesto]: " + ci.getPuesto());

        }
      }
    }
  }

  public static String printChecks(List<SignatureCheck> details, String title) {

    if (details == null || details.size() == 0) {
      return "";
    }

    StringBuffer str = new StringBuffer(title + "\n");
    int d = 0;
    for (SignatureCheck detail : details) {

      str.append(d + ".- Check " + detail.getName() + "");
      if (detail.getType() != null) {
        str.append(" (" + detail.getType() + ")");
      }
      str.append("\n");
      d++;
    }
    str.append("\n");
    return str.toString();
  }

}
