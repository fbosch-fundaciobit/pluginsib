package es.caib.plugins.arxiu.api;

/**
 * 
 * @author Limit
 *
 */
public class ConsultaFiltre {

	private String metadataClau;		// Metadada sobre la que s’aplica el criteri de filtre
	private Operacio operacio;			// Operació a aplicar sobre la metadada:
										//		- IGUAL,
										//		- CONTE
										//		- MENOR
										//		- MAJOR
										//		- ENTRE
	private String valorOperacio;		// Valor a utilitzar en el criteri
	
	public ConsultaFiltre() {
		super();
	}

	public ConsultaFiltre(
			String metadataClau, 
			Operacio operacio, 
			String valorOperacio) {
		super();
		this.metadataClau = metadataClau;
		this.operacio = operacio;
		this.valorOperacio = valorOperacio;
	}

	public String getMetadataClau() {
		return metadataClau;
	}

	public void setMetadataClau(String metadataClau) {
		this.metadataClau = metadataClau;
	}

	public Operacio getOperacio() {
		return operacio;
	}

	public void setOperacio(Operacio operacio) {
		this.operacio = operacio;
	}

	public String getValorOperacio() {
		return valorOperacio;
	}

	public void setValorOperacio(String valorOperacio) {
		this.valorOperacio = valorOperacio;
	}
	
}
