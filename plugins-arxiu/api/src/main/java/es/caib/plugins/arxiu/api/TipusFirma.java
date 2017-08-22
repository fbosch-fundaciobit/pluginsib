package es.caib.plugins.arxiu.api;

public enum TipusFirma {

	TF01 ("CSV"),
	TF02 ("XAdES internally detached signature"),
	TF03 ("XAdES enveloped signature"),
	TF04 ("CAdES detached/explicit signature"),
	TF05 ("CAdES attached/implicit signature"),
	TF06 ("PAdES"),
	TF07 ("SMIME"),
	TF08 ("ODT"),
	TF09 ("OOXML");

	private final String text;
	private final String id;
	
	TipusFirma(String text) {
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
