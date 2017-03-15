package org.fundaciobit.plugins.validatesignature.api;

import java.util.Date;
import java.util.Properties;

/**
 * 
 * @author anadal
 *
 */
public class CertificateInfo {

  protected String apellidosResponsable;
  protected String organizacionEmisora;
  protected String segundoApellidoResponsable;
  protected String versionPolitica;
  protected String usoCertificado;
  protected String pais;
  protected String subject;
  protected String numeroSerie;
  protected String primerApellidoResponsable;
  protected String nombreApellidosResponsable;
  protected Date validoHasta;
  protected String idPolitica;
  protected String idEuropeo;
  protected Date validoDesde;
  protected String tipoCertificado;
  protected String email;
  protected String clasificacion;
  protected String idEmisor;
  protected String nifResponsable;
  protected String extensionUsoCertificado;
  protected String nombreResponsable;
  protected String politica;
  protected String nifEntidadSuscriptora;
  protected String entidadSuscriptora;
  protected String unidadOrganizativa;
  protected String numeroIdentificacionPersonal;
  protected String organizacion;
  protected String puesto;
  
  
  protected Properties otherProperties = null;
  
  
  public String getApellidosResponsable() {
    return apellidosResponsable;
  }
  public void setApellidosResponsable(String apellidosResponsable) {
    this.apellidosResponsable = apellidosResponsable;
  }
  public String getOrganizacionEmisora() {
    return organizacionEmisora;
  }
  public void setOrganizacionEmisora(String organizacionEmisora) {
    this.organizacionEmisora = organizacionEmisora;
  }
  public String getSegundoApellidoResponsable() {
    return segundoApellidoResponsable;
  }
  public void setSegundoApellidoResponsable(String segundoApellidoResponsable) {
    this.segundoApellidoResponsable = segundoApellidoResponsable;
  }
  public String getVersionPolitica() {
    return versionPolitica;
  }
  public void setVersionPolitica(String versionPolitica) {
    this.versionPolitica = versionPolitica;
  }
  public String getUsoCertificado() {
    return usoCertificado;
  }
  public void setUsoCertificado(String usoCertificado) {
    this.usoCertificado = usoCertificado;
  }
  public String getPais() {
    return pais;
  }
  public void setPais(String pais) {
    this.pais = pais;
  }
  public String getSubject() {
    return subject;
  }
  public void setSubject(String subject) {
    this.subject = subject;
  }
  public String getNumeroSerie() {
    return numeroSerie;
  }
  public void setNumeroSerie(String numeroSerie) {
    this.numeroSerie = numeroSerie;
  }
  public String getPrimerApellidoResponsable() {
    return primerApellidoResponsable;
  }
  public void setPrimerApellidoResponsable(String primerApellidoResponsable) {
    this.primerApellidoResponsable = primerApellidoResponsable;
  }
  public String getNombreApellidosResponsable() {
    return nombreApellidosResponsable;
  }
  public void setNombreApellidosResponsable(String nombreApellidosResponsable) {
    this.nombreApellidosResponsable = nombreApellidosResponsable;
  }
  public Date getValidoHasta() {
    return validoHasta;
  }
  public void setValidoHasta(Date validoHasta) {
    this.validoHasta = validoHasta;
  }
  public String getIdPolitica() {
    return idPolitica;
  }
  public void setIdPolitica(String idPolitica) {
    this.idPolitica = idPolitica;
  }

  public Date getValidoDesde() {
    return validoDesde;
  }
  public void setValidoDesde(Date validoDesde) {
    this.validoDesde = validoDesde;
  }
  public String getTipoCertificado() {
    return tipoCertificado;
  }
  public void setTipoCertificado(String tipoCertificado) {
    this.tipoCertificado = tipoCertificado;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getClasificacion() {
    return clasificacion;
  }
  public void setClasificacion(String clasificacion) {
    this.clasificacion = clasificacion;
  }
  public String getIdEmisor() {
    return idEmisor;
  }
  public void setIdEmisor(String idEmisor) {
    this.idEmisor = idEmisor;
  }

  public String getExtensionUsoCertificado() {
    return extensionUsoCertificado;
  }
  public void setExtensionUsoCertificado(String extensionUsoCertificado) {
    this.extensionUsoCertificado = extensionUsoCertificado;
  }
  public String getNombreResponsable() {
    return nombreResponsable;
  }
  public void setNombreResponsable(String nombreResponsable) {
    this.nombreResponsable = nombreResponsable;
  }
  public String getPolitica() {
    return politica;
  }
  public void setPolitica(String politica) {
    this.politica = politica;
  }
  public String getIdEuropeo() {
    return idEuropeo;
  }
  public void setIdEuropeo(String idEuropeo) {
    this.idEuropeo = idEuropeo;
  }
  public String getNifResponsable() {
    return nifResponsable;
  }
  public void setNifResponsable(String nifResponsable) {
    this.nifResponsable = nifResponsable;
  }
  public Properties getOtherProperties() {
    return otherProperties;
  }
  public void setOtherProperties(Properties otherProperties) {
    this.otherProperties = otherProperties;
  }
  public String getNifEntidadSuscriptora() {
    return nifEntidadSuscriptora;
  }
  public void setNifEntidadSuscriptora(String nifEntidadSuscriptora) {
    this.nifEntidadSuscriptora = nifEntidadSuscriptora;
  }
  public String getEntidadSuscriptora() {
    return entidadSuscriptora;
  }
  public void setEntidadSuscriptora(String entidadSuscriptora) {
    this.entidadSuscriptora = entidadSuscriptora;
  }
  public String getUnidadOrganizativa() {
    return unidadOrganizativa;
  }
  public void setUnidadOrganizativa(String unidadOrganizativa) {
    this.unidadOrganizativa = unidadOrganizativa;
  }
  public String getNumeroIdentificacionPersonal() {
    return numeroIdentificacionPersonal;
  }
  public void setNumeroIdentificacionPersonal(String numeroIdentificacionPersonal) {
    this.numeroIdentificacionPersonal = numeroIdentificacionPersonal;
  }
  public String getOrganizacion() {
    return organizacion;
  }
  public void setOrganizacion(String organizacion) {
    this.organizacion = organizacion;
  }
  public String getPuesto() {
    return puesto;
  }
  public void setPuesto(String puesto) {
    this.puesto = puesto;
  }

  
}
