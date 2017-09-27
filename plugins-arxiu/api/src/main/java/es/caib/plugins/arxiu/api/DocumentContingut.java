/**
 * 
 */
package es.caib.plugins.arxiu.api;

import java.util.Arrays;

/**
 * Informació sobre un contingut d’un document.
 *
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DocumentContingut {

	private byte[] contingut;
	private String tipusMime;
	private String identificadorOrigen;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(contingut);
		result = prime * result + ((identificadorOrigen == null) ? 0 : identificadorOrigen.hashCode());
		result = prime * result + ((tipusMime == null) ? 0 : tipusMime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentContingut other = (DocumentContingut) obj;
		if (!Arrays.equals(contingut, other.contingut))
			return false;
		if (identificadorOrigen == null) {
			if (other.identificadorOrigen != null)
				return false;
		} else if (!identificadorOrigen.equals(other.identificadorOrigen))
			return false;
		if (tipusMime == null) {
			if (other.tipusMime != null)
				return false;
		} else if (!tipusMime.equals(other.tipusMime))
			return false;
		return true;
	}
	
	
	
}
