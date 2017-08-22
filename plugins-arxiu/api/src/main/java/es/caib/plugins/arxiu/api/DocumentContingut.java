package es.caib.plugins.arxiu.api;

/**
 * 
 * @author Limit
 *
 */
public class DocumentContingut {

	private byte[] contingut;				// Contingut del fitxer
	private String tipusMime;				// Tipus de format del contingut
	private String identificadorOrigen;		// Identificador del document copiat
	
	public DocumentContingut() {
		super();
	}

	public DocumentContingut(
			byte[] contingut, 
			String tipusMime) {
		super();
		this.contingut = contingut;
		this.tipusMime = tipusMime;
	}
	
	public DocumentContingut(
			byte[] contingut, 
			String tipusMime, 
			String identificadorOrigen) {
		super();
		this.contingut = contingut;
		this.tipusMime = tipusMime;
		this.identificadorOrigen = identificadorOrigen;
	}
	
	public DocumentContingut(DocumentContingut dc) {
		super();
		this.contingut = dc.contingut;
		this.tipusMime = dc.tipusMime;
		this.identificadorOrigen = dc.identificadorOrigen;
	}

	public byte[] getContingut() {
		return contingut;
	}

	public void setContingut(byte[] contingut) {
		this.contingut = contingut;
	}

	public String getTipusMime() {
		return tipusMime;
	}

	public void setTipusMime(String tipusMime) {
		this.tipusMime = tipusMime;
	}

	public String getIdentificadorOrigen() {
		return identificadorOrigen;
	}

	public void setIdentificadorOrigen(String identificadorOrigen) {
		this.identificadorOrigen = identificadorOrigen;
	}
	
}
