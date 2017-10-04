package es.caib.plugins.arxiu.api;

public enum Origen {

	ADMINISTRACIO ("1"),
	CIUTADA ("0");

	private final String text;
	private final String id;
	
	Origen(String text) {
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
