package es.caib.plugins.arxiu.filesystem;

import java.util.List;

import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.InformacioItem;

public class CarpetaDao {
	
	private String identificador;
	private String nom;
	
	private String pare;
	
	private List<InformacioItem> informacioItems;
	
	
	public Carpeta getCarpeta() {
		
		return new Carpeta(
				identificador,
				nom,
				informacioItems);
	}
	
	public CarpetaDao(
			String identificador,
			String nom,
			String pare,
			List<InformacioItem> informacioItems) {
		
		super();
		
		this.identificador = identificador;
		this.nom = nom;
		this.pare = pare;
		this.informacioItems = informacioItems;
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
	
	public String getPare() {
		return pare;
	}
	public void setPare(String pare) {
		this.pare = pare;
	}
	
	public List<InformacioItem> getInformacioItems() {
		return informacioItems;
	}
	public void setInformacioItems(List<InformacioItem> informacioItems) {
		this.informacioItems = informacioItems;
	}
	
	
}
