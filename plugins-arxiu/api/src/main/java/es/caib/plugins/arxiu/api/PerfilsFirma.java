package es.caib.plugins.arxiu.api;

/**
 * Tipos de perfil empleado en una firma con certificado electrónico.
 * 
 * @author u104848
 *
 */
public enum PerfilsFirma {
	
	EPES ("EPES"), //BES + Politica de firma
	LTV	 ("LTV"), //Long term validation (Segell temps)
	T	 ("T"),
	C	 ("C"),
	X	 ("X"),
	XL	 ("XL"),
	A	 ("A");

    private String value;

    private PerfilsFirma(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
    
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}