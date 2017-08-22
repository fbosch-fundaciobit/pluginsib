package es.caib.plugins.arxiu.api;

public enum Estat {

	E01 ("Obert"),
	E02 ("Tancat"),
	E03 ("Index per remissio tancat");

	private final String text;
	private final String id;
	
	Estat(String text) {
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
