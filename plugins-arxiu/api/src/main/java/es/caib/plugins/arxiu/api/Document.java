package es.caib.plugins.arxiu.api;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.ListUtils;

import es.caib.arxiudigital.apirest.constantes.Aspectos;

/**
 * 
 * @author Limit
 *
 */
public class Document {

	private String identificador;				// Identificador del document
	private String nom;							// Nom del document
	private DocumentMetadades metadades;		// Metadades del document
	private List<Firma> firmes;					// Llista de firmes del document
	private DocumentContingut contingut;		// Contingut del document
	private List<Aspectos> aspectes;			// Aspectes del document
	
	public boolean igual(
			Document d) {
		
		if(d.getFirmes().size() != firmes.size()) return false;
		for(int i = 0; i < firmes.size(); i++) {
			if(!d.getFirmes().get(i).igual(firmes.get(i))) return false;
		}
		
		String origenId = d.getContingut().getIdentificadorOrigen() == null ?
				"" : d.getContingut().getIdentificadorOrigen();
		
		return d.getIdentificador().equals(identificador) &&
				d.getNom().equals(nom) &&
				d.getMetadades().getIdentificador().equals(metadades.getIdentificador()) &&
				d.getMetadades().getVersioNti().equals(metadades.getVersioNti()) &&
				d.getMetadades().getVersioNti().equals(metadades.getVersioNti()) &&
				ListUtils.isEqualList(d.getMetadades().getOrgans(), metadades.getOrgans()) &&
				d.getMetadades().getData().getTime() == metadades.getData().getTime() &&
				d.getMetadades().getOrigen().equals(metadades.getOrigen()) &&
				d.getMetadades().getEstatElaboracio().equals(metadades.getEstatElaboracio()) &&
				d.getMetadades().getTipusDocumental().equals(metadades.getTipusDocumental()) &&
				d.getMetadades().getSerieDocumental().equals(metadades.getSerieDocumental()) &&
				Arrays.equals(d.getContingut().getContingut(), contingut.getContingut()) &&
				d.getContingut().getTipusMime().equals(contingut.getTipusMime()) &&
				origenId.equals(contingut.getIdentificadorOrigen());
				
	}
	
	public Document() {
		super();
	}

	public Document(
			String identificador, 
			String nom, 
			DocumentContingut contingut) {
		super();
		this.identificador = identificador;
		this.nom = nom;
		this.contingut = contingut;
	}
	
	public Document(
			String identificador, 
			String nom,
			DocumentContingut contingut, 
			DocumentMetadades metadades) {
		super();
		this.identificador = identificador;
		this.nom = nom;
		this.contingut = contingut;
		this.metadades = metadades;
	}
	
	public Document(
			String identificador, 
			String nom,
			DocumentContingut contingut, 
			List<Firma> firmes) {
		super();
		this.identificador = identificador;
		this.nom = nom;
		this.contingut = contingut;
		this.firmes = firmes;
	}
	
	public Document(
			String identificador, 
			String nom,
			DocumentContingut contingut, 
			DocumentMetadades metadades, 
			List<Firma> firmes) {
		super();
		this.identificador = identificador;
		this.nom = nom;
		this.contingut = contingut;
		this.metadades = metadades;
		this.firmes = firmes;
	}
	
	public Document(Document d) {
		super();
		this.identificador = d.identificador;
		this.nom = d.nom;
		this.contingut = d.contingut;
		this.metadades = d.metadades;
		this.firmes = d.firmes;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public DocumentMetadades getMetadades() {
		return metadades;
	}

	public void setMetadades(DocumentMetadades metadades) {
		this.metadades = metadades;
	}

	public List<Firma> getFirmes() {
		return firmes;
	}

	public void setFirmes(List<Firma> firmes) {
		this.firmes = firmes;
	}

	public DocumentContingut getContingut() {
		return contingut;
	}

	public void setContingut(DocumentContingut contingut) {
		this.contingut = contingut;
	}

	public List<Aspectos> getAspectes() {
		return aspectes;
	}

	public void setAspectes(List<Aspectos> aspectes) {
		this.aspectes = aspectes;
	}
	
}
