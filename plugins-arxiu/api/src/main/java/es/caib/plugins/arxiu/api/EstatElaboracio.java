package es.caib.plugins.arxiu.api;

public enum EstatElaboracio {

	EE01 ("Original"),
	EE02 ("Copia electronica autentica amb canvi de format"),
	EE03 ("Copia electronica autentica de document paper"),
	EE04 ("Copia electronica parcial autentica");

	private final String text;
	private final String id;
	
	EstatElaboracio(String text) {
		this.text = text;
		this.id = this.name();
	}
	
	public String getText() {
		return text;
	}
	
	public String getId() {
		return id;
	}
	
}
