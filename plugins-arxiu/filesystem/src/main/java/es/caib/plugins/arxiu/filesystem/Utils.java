package es.caib.plugins.arxiu.filesystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.Operacio;

public class Utils {

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		return out.toByteArray();
	}

	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return is.readObject();
	}

	public static boolean isValid(
			ExpedientDao expedient,
			List<ConsultaFiltre> filtres) throws ArxiuException {
		
		for(ConsultaFiltre filtre : filtres) {
			switch(filtre.getMetadataClau()) {
				case Fields.EX_METADADESID:
					if(!validateString(
							expedient.getIdMetadades(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
				case Fields.EX_VERSIONTI:
					if(!validateString(
							expedient.getVersioNti(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
				case Fields.EX_ORIGEN:
					if(!validateString(
							expedient.getOrigen(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
				case Fields.EX_ORGAN:
					if(!validateListString(
							expedient.getOrgans(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
				case Fields.EX_DATA_OBERTURA:
					if(!validatedata(
							expedient.getDataObertura(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
				case Fields.EX_CLASSIFICACIO:
					if(!validateString(
							expedient.getClassificacio(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
				case Fields.EX_ESTAT:
					if(!validateString(
							expedient.getEstat(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
				case Fields.EX_INTERESSAT:
					if(!validateListString(
							expedient.getInteressats(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
				case Fields.EX_SERIE_DOCUMENTAL:
					if(!validateString(
							expedient.getSerieDocumental(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
			}
		}
		
		return true;
	}
	
	public static boolean isValid(
			DocumentDao document,
			List<ConsultaFiltre> filtres) throws ArxiuException {
		
		for(ConsultaFiltre filtre : filtres) {
			switch(filtre.getMetadataClau()) {
				case Fields.DOC_METADADESID:
					if(!validateString(
							document.getMetadadesid(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
				case Fields.DOC_VERSIONTI:
					if(!validateString(
							document.getVersioNti(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
				case Fields.DOC_ORGAN:
					if(!validateListString(
							document.getOrgans(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
				case Fields.DOC_DATA:
					if(!validatedata(
							document.getData(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
				case Fields.DOC_ORIGEN:
					if(!validateString(
							document.getOrigen(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
				case Fields.DOC_ESTAT_ELABORACIO:
					if(!validateString(
							document.getEstatElaboracio(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
				case Fields.DOC_SERIE_DOCUMENTAL:
					if(!validateString(
							document.getSerieDocumental(),
							filtre.getValorOperacio(),
							filtre.getOperacio())
							) return false;
					break;
			}
		}
		
		return true;
	}
	
	private static boolean validateString(
			String camp,
			String valor,
			Operacio operacio) throws ArxiuException {
		
		switch(operacio) {
			case CONTE:
				return camp.contains(valor);
			case IGUAL:
				return camp.equals(valor);
			default:
				throw new ArxiuException("El filtre pel camp String no conté un operador valid.");
		}
	}
	private static boolean validateListString(
			List<String> camps,
			String valor,
			Operacio operacio) throws ArxiuException {
		
		switch(operacio) {
			case CONTE:
				for(String camp : camps)
					if(camp.equals(valor)) return true;
				
				return false;
			default:
				throw new ArxiuException("El filtre pel camp List<String> no conté un operador valid.");
		}
	}
	private static boolean validatedata(
			Date camp,
			String valor,
			Operacio operacio) throws ArxiuException {
		
		Date data = new Date(Integer.parseInt(valor));
		switch(operacio) {
			case IGUAL:
				return camp.getTime() == data.getTime();
			case MAJOR:
				return camp.getTime() > data.getTime();
			case MENOR:
				return camp.getTime() < data.getTime();
			default:
				throw new ArxiuException("El filtre pel camp Date no conté un operador valid.");
		}
	}
	
	public static String nextVersionValue(
			String version) {
		
		int versionInt = Integer.parseInt(version);
		return String.valueOf(versionInt++);
	}
	

}