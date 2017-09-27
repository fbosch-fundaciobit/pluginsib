package es.caib.plugins.arxiu.api;


public enum Aspectes {
	
	FIRMADOBASE 	 ("eni:firmadoBase"),
	FIRMADO 		 ("eni:firmado"),
	INTEROPERABLE 	 ("eni:interoperable"),
	REGISTABLE 		 ("eni:registable"),
	TRANSFERIBLE 	 ("eni:transferible"),
	BORRADOR 		 ("gdib:borrador"),
	FIRMADOMIGRACION ("gdib:firmadoMigracion"),
	TRANSFORMADO 	 ("gdib:transformado"),
	TRASLADADO 		 ("gdib:trasladado");
	
    private String value;

    private Aspectes(String value) {
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
