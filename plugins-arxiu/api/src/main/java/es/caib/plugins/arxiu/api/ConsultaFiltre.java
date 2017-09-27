/**
 * 
 */
package es.caib.plugins.arxiu.api;

/**
 * Informació sobre el filtre per a realitzar consultes.
 *
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ConsultaFiltre {

	private String metadataClau;
	private ConsultaOperacio operacio;
	private String valorOperacio1;
	private String valorOperacio2;
	
	public ConsultaFiltre() {
		super();
	}

	public ConsultaFiltre(
			String metadataClau, 
			ConsultaOperacio operacio, 
			String valorOperacio1) {
		super();
		this.metadataClau = metadataClau;
		this.operacio = operacio;
		this.valorOperacio1 = valorOperacio1;
	}
	
	public ConsultaFiltre(
			String metadataClau, 
			ConsultaOperacio operacio, 
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

	public ConsultaOperacio getOperacio() {
		return operacio;
	}

	public void setOperacio(ConsultaOperacio operacio) {
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
