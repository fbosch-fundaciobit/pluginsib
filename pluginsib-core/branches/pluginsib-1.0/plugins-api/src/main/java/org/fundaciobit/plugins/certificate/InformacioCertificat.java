/**
 * 
 */
package org.fundaciobit.plugins.certificate;

import java.math.BigInteger;
import java.util.Date;

/**
 * Informació sobre un certificat
 * 
 * @author anadal
 */
public class InformacioCertificat {

  public static final int CLASSIFICACIO_DESCONEGUDA = -1;

  public static final int CLASSIFICACIO_PERSONA_FISICA = 0;
  public static final int CLASSIFICACIO_PERSONA_JURIDICA = 1;
  public static final int CLASSIFICACIO_COMPONENTS = 2;

  private String tipusCertificat;
  private String subject;
  private String nomResponsable;
  private String primerLlinatgeResponsable;
  private String segonLlinatgeResponsable;
  private String nifResponsable;
  private String emissorID;
  private String unitatOrganitzativaNifCif;
  private String email;
  private String dataNaixement;
  private String raoSocial;
  private int classificacio = CLASSIFICACIO_DESCONEGUDA;
  private BigInteger numeroSerie;

  private String usCertificat;
  private String usCertificatExtensio;
  private Date validFins;
  private Date validDesDe;
  private String politica;
  private String politicaVersio;
  private String politicaID;
  private String emissorOrganitzacio;
  private String pais;
  private String unitatOrganitzativa;
  
  private String nomCompletResponsable;

  public String getNomCompletResponsable() {
    return nomCompletResponsable;
  }

  public void setNomCompletResponsable(String nomCompletResponsable) {
    this.nomCompletResponsable = nomCompletResponsable;
  }
  
  public String getTipusCertificat() {
    return tipusCertificat;
  }

  public void setTipusCertificat(String tipusCertificat) {
    this.tipusCertificat = tipusCertificat;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getNomResponsable() {
    return nomResponsable;
  }

  public void setNomResponsable(String nomResponsable) {
    this.nomResponsable = nomResponsable;
  }

  public String getPrimerLlinatgeResponsable() {
    return primerLlinatgeResponsable;
  }

  public void setPrimerLlinatgeResponsable(String primerLlinatgeResponsable) {
    this.primerLlinatgeResponsable = primerLlinatgeResponsable;
  }

  public String getSegonLlinatgeResponsable() {
    return segonLlinatgeResponsable;
  }

  public void setSegonLlinatgeResponsable(String segonLlinatgeResponsable) {
    this.segonLlinatgeResponsable = segonLlinatgeResponsable;
  }

  public String getNifResponsable() {
    return nifResponsable;
  }

  public void setNifResponsable(String nifResponsable) {
    this.nifResponsable = nifResponsable;
  }

  public String getEmissorID() {
    return emissorID;
  }

  public void setEmissorID(String emissorID) {
    this.emissorID = emissorID;
  }

  public String getUnitatOrganitzativaNifCif() {
    return unitatOrganitzativaNifCif;
  }

  public void setUnitatOrganitzativaNifCif(String unitatOrganitzativaNifCif) {
    this.unitatOrganitzativaNifCif = unitatOrganitzativaNifCif;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDataNaixement() {
    return dataNaixement;
  }

  public void setDataNaixement(String dataNaixement) {
    this.dataNaixement = dataNaixement;
  }

  public String getRaoSocial() {
    return raoSocial;
  }

  public void setRaoSocial(String raoSocial) {
    this.raoSocial = raoSocial;
  }

  public int getClassificacio() {
    return classificacio;
  }

  public void setClassificacio(int classificacio) {
    this.classificacio = classificacio;
  }

  public BigInteger getNumeroSerie() {
    return numeroSerie;
  }

  public void setNumeroSerie(BigInteger numeroSerie) {
    this.numeroSerie = numeroSerie;
  }

  public String getUsCertificat() {
    return usCertificat;
  }

  public void setUsCertificat(String usCertificat) {
    this.usCertificat = usCertificat;
  }

  public String getUsCertificatExtensio() {
    return usCertificatExtensio;
  }

  public void setUsCertificatExtensio(String usCertificatExtensio) {
    this.usCertificatExtensio = usCertificatExtensio;
  }

  public Date getValidFins() {
    return validFins;
  }

  public void setValidFins(Date validFins) {
    this.validFins = validFins;
  }

  public Date getValidDesDe() {
    return validDesDe;
  }

  public void setValidDesDe(Date validDesDe) {
    this.validDesDe = validDesDe;
  }

  public String getPolitica() {
    return politica;
  }

  public void setPolitica(String politica) {
    this.politica = politica;
  }

  public String getPoliticaVersio() {
    return politicaVersio;
  }

  public void setPoliticaVersio(String politicaVersio) {
    this.politicaVersio = politicaVersio;
  }

  public String getPoliticaID() {
    return politicaID;
  }

  public void setPoliticaID(String politicaID) {
    this.politicaID = politicaID;
  }

  public String getEmissorOrganitzacio() {
    return emissorOrganitzacio;
  }

  public void setEmissorOrganitzacio(String emissorOrganitzacio) {
    this.emissorOrganitzacio = emissorOrganitzacio;
  }

  public String getPais() {
    return pais;
  }

  public void setPais(String pais) {
    this.pais = pais;
  }

  public String getUnitatOrganitzativa() {
    return unitatOrganitzativa;
  }

  public void setUnitatOrganitzativa(String unitatOrganitzativa) {
    this.unitatOrganitzativa = unitatOrganitzativa;
  }

  @Override
  public String toString() {
    StringBuffer st = new StringBuffer();
    st.append("Nom Complet Responsable: ").append(getNomCompletResponsable()).append("\n");
    
    st.append("Nom Responsable: ").append(getNomResponsable()).append("\n");
    st.append("Primer Llinatge Responsable: ").append(getPrimerLlinatgeResponsable())
        .append("\n");
    st.append("Segon Llinatge Responsable: ").append(getSegonLlinatgeResponsable())
        .append("\n");
    st.append("NIF Responsable: ").append(getNifResponsable()).append("\n");
    st.append("Email: ").append(getEmail()).append("\n");
    st.append("Data Naixement: ").append(getDataNaixement()).append("\n");
    st.append("Rao Social: ").append(getRaoSocial()).append("\n");
    st.append("Classificacio: ");
    switch (getClassificacio()) {
    case CLASSIFICACIO_COMPONENTS:
      st.append("COMPONENTS");
      break;
    case CLASSIFICACIO_PERSONA_FISICA:
      st.append("PERSONA FISICA");
      break;
    case CLASSIFICACIO_PERSONA_JURIDICA:
      st.append("PERSONA JURIDICA");
      break;
    default:
      st.append("Desconegut");
    }
    st.append(" (").append(getClassificacio()).append(")").append("\n");

    st.append("Organitzacio: ").append(getUnitatOrganitzativa()).append("\n");
    st.append("Organitzacio nifCif: ").append(getUnitatOrganitzativaNifCif()).append("\n");
    st.append("Pais: ").append(getPais()).append("\n");

    st.append("Tipus Certificat: ").append(getTipusCertificat()).append("\n");
    st.append("Subject: ").append(getSubject()).append("\n");
    st.append("Emissor ID: ").append(getEmissorID()).append("\n");
    st.append("Emissor Organitzacio: ").append(getEmissorOrganitzacio()).append("\n");
    st.append("Numero Serie: ").append(getNumeroSerie()).append("\n");
    st.append("Us Certificat: ").append(getUsCertificat()).append("\n");
    st.append("Us Certificat Extes: ").append(getUsCertificatExtensio()).append("\n");
    st.append("Valid Des De: ").append(getValidDesDe()).append("\n");
    st.append("Valid Fins: ").append(getValidFins()).append("\n");
    st.append("Politica: ").append(getPolitica()).append("\n");
    st.append("Politica Versio: ").append(getPoliticaVersio()).append("\n");
    st.append("Politica ID: ").append(getPoliticaID()).append("\n");

    return st.toString();

  }

}
