package es.caib.plugins.arxiu.caib.test;

import java.util.Arrays;
import java.util.Date;

import es.caib.arxiudigital.apirest.constantes.EstadosExpediente;
import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.caib.CaibArxiuPlugin;

public class TestArxiuWS01 {
	
	private static final String SERIE_DOCUMENTAL = "S0001";
	
	private static String expedientName(int i) {
		return "Expedient_" + i;
	}
	
	private static String folderName(int i) {
		return "Carpeta_" + i;
	}
	
	
	private static ExpedientMetadades getExpedientMetadades(int i) {
		
		return new ExpedientMetadades();
	}
	private static String getEstadosExpediente(int i) {
		
		EstadosExpediente[] estadosExpediente = EstadosExpediente.values();
		return estadosExpediente[i % 3].name();
	}
	
	private static void setProperties() {
		System.setProperty("plugins.arxive.arxiucaib.base.url", "https://afirmades.caib.es:4430/esb");
		System.setProperty("plugins.arxive.arxiucaib.aplicacio.codi", "ARXIUTEST");
		System.setProperty("plugins.arxive.arxiucaib.usuari", "app1");
		System.setProperty("plugins.arxive.arxiucaib.contrasenya", "app1");
	}

	public static void main(String[] args) throws ArxiuException {
		CaibArxiuPlugin caibArxiuPlugin = new CaibArxiuPlugin();
		
		setProperties();
		
		int n = 0;
		String expedientId = caibArxiuPlugin.expedientCrear(
				expedientName(n),
				getExpedientMetadades(n));
		System.out.println(expedientId);
	}

}
