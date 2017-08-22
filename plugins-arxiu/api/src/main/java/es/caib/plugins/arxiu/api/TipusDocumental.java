package es.caib.plugins.arxiu.api;

public enum TipusDocumental {

	TD01 ("Resolucio"),
	TD02 ("Acord"),
	TD03 ("Contracte"),
	TD04 ("Conveni"),
	TD05 ("Declaracio"),
	TD06 ("comunicacio"),
	TD07 ("Notificacio"),
	TD08 ("Publicacio"),
	TD09 ("Acus de rebut"),
	TD10 ("acta"),
	TD11 ("Certificat"),
	TD12 ("Diligencia"),
	TD13 ("Informe"),
	TD14 ("Solicitud"),
	TD15 ("Denuncia"),
	TD16 ("Alegacio"),
	TD17 ("Recursos"),
	TD18 ("Comunicacio ditada"),
	TD19 ("Factura"),
	TD20 ("Altres incautats"),
	TD99 ("Altres");

	private final String text;
	private final String id;
	
	TipusDocumental(String text) {
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
