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
	private String valorOperacio1;		// Valor a utilitzar en el criteri
	private String valorOperacio2;		// Valor a utilitzar en el criteri
	
	public ConsultaFiltre() {
		super();
	}

	public ConsultaFiltre(
			String metadataClau, 
			Operacio operacio, 
			String valorOperacio1) {
		super();
		this.metadataClau = metadataClau;
		this.operacio = operacio;
		this.valorOperacio1 = valorOperacio1;
	}
	
	public ConsultaFiltre(
			String metadataClau, 
			Operacio operacio, 
			String valorOperacio1,
			String valorOperacio2) {
		super();
		this.metadataClau = metadataClau;
		this.operacio = operacio;
		this.valorOperacio1 = valorOperacio1;
		this.valorOperacio2 = valorOperacio2;
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

	public String getValorOperacio1() {
		return valorOperacio1;
	}

	public void setValorOperacio1(String valorOperacio1) {
		this.valorOperacio1 = valorOperacio1;
	}

	public String getValorOperacio2() {
		return valorOperacio2;
	}

	public void setValorOperacio2(String valorOperacio2) {
		this.valorOperacio2 = valorOperacio2;
	}

	
	
}
