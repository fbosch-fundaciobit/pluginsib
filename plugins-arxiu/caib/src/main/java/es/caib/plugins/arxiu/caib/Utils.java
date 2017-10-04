package es.caib.plugins.arxiu.caib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.Fields;
import es.caib.plugins.arxiu.api.Tables;

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
	
	public static String formatDateIso8601(Date date) {
		
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(tz);
		
		return df.format(date);
	}
	
	public static Date parseDateIso8601(String date) throws ArxiuException {
		
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(tz);
		
		try {
			return df.parse(date);
		} catch (ParseException e) {
			throw new ArxiuException(
					"No s'ha pogut parsejar el valor per el camp Data (" +
					"valor=" + date + ")");
		}
	}
	
	private static enum Type {String, List, Date};
	
	public static String getQuery(Tables taula, List<ConsultaFiltre> filtres) throws ArxiuException {
		
		String query = "+TYPE:";
		
		if (taula != null) {
			switch (taula) {
			case TABLE_EXPEDIENT:
				query += "\"eni:expediente\" ";
				break;
			case TABLE_DOCUMENT:
				query += "\"eni:documento\" ";
				break;
			default:
				throw new ArxiuException("Aquesta no és una entitat vàlida a consultar: " + taula.name());	
			}
			
		} else {
			throw new ArxiuException("No s'ha especificat cap taula a la que consultar");
		}
		
		for(int i = 0; i < filtres.size(); i++) {
			ConsultaFiltre filtre = filtres.get(i);
			
			Type type = metadataClass(filtre.getMetadataClau());
			if (type != null) {
				query += "+@eni\\:";
				switch(type) {
					case String:
						switch(filtre.getOperacio()) {
							case IGUAL:
								query = query + filtre.getMetadataClau() + ":\"" + filtre.getValorOperacio1() + "\"";
								break;
							case CONTE:
								query = query + filtre.getMetadataClau() + ":*" + filtre.getValorOperacio1() + "*";
								break;
							case MAJOR:
								query = query + filtre.getMetadataClau() + ":[" + filtre.getValorOperacio1() + " TO *]" +
										" -" + filtre.getMetadataClau() + ":\"" + filtre.getValorOperacio1() + "\"";
								break;
							case MENOR:
								query = query + filtre.getMetadataClau() + ":[* TO " + filtre.getValorOperacio1() + "]" +
										" -" + filtre.getMetadataClau() + ":\"" + filtre.getValorOperacio1() + "\"";
								break;
							case ENTRE:
								query = query + filtre.getMetadataClau() + ":[" + filtre.getValorOperacio1() + " TO " + filtre.getValorOperacio2() + "]";
								break;
							default:
								throw new ArxiuException("No s'ha definit un operador per la metadada " + filtre.getMetadataClau());
						}
						break;
					case List:
						switch(filtre.getOperacio()) {
							case IGUAL:
								throw new ArxiuException("L'operador " + filtre.getOperacio() + " no es aplicable al un tipus de dades " + type);
							case CONTE:
								query = query + filtre.getMetadataClau() + ":\"" + filtre.getValorOperacio1() + "\"";
								break;
							case MAJOR:
								throw new ArxiuException("L'operador " + filtre.getOperacio() + " no es aplicable al un tipus de dades " + type);
							case MENOR:
								throw new ArxiuException("L'operador " + filtre.getOperacio() + " no es aplicable al un tipus de dades " + type);
							case ENTRE:
								throw new ArxiuException("L'operador " + filtre.getOperacio() + " no es aplicable al un tipus de dades " + type);
							default:
								throw new ArxiuException("No s'ha definit un operador per la metadada " + filtre.getMetadataClau());
						}
						break;
					case Date:
						switch(filtre.getOperacio()) {
							case IGUAL:
								query = query + filtre.getMetadataClau() + ":\"" + filtre.getValorOperacio1() + "\"";
								break;
							case CONTE:
								throw new ArxiuException("L'operador " + filtre.getOperacio() + " no es aplicable al un tipus de dades " + type);
							case MAJOR:
								query = query + filtre.getMetadataClau() + ":[" + filtre.getValorOperacio1() + " TO *]" +
										" -" + filtre.getMetadataClau() + ":\"" + filtre.getValorOperacio1() + "\"";
								break;
							case MENOR:
								query = query + filtre.getMetadataClau() + ":[* TO " + filtre.getValorOperacio1() + "]" +
										" -" + filtre.getMetadataClau() + ":\"" + filtre.getValorOperacio1() + "\"";
								break;
							case ENTRE:
								query = query + filtre.getMetadataClau() + ":[" + filtre.getValorOperacio1() + " TO " + filtre.getValorOperacio2() + "]";
								break;
							default:
								throw new ArxiuException("No s'ha definit un operador per la metadada " + filtre.getMetadataClau());
						}
						break;
					default:
						throw new ArxiuException("La metadada " + filtre.getMetadataClau() + " no esta definida");
				}
			}
			if(i < filtres.size()-1) query = query + " AND ";
		}
		
		return query;
	}
	
	private static Type metadataClass(String metadata) throws ArxiuException {
		
		switch(metadata) {
			case Fields.EX_METADADESID: return Type.String;
			case Fields.EX_VERSIO: return Type.String;
			case Fields.EX_VERSIONTI: return Type.String;
			case Fields.EX_ORIGEN: return Type.String;
			case Fields.EX_ORGAN: return Type.List;
			case Fields.EX_DATA_OBERTURA: return Type.Date;
			case Fields.EX_CLASSIFICACIO: return Type.String;
			case Fields.EX_ESTAT: return Type.String;
			case Fields.EX_INTERESSAT: return Type.List;
			case Fields.EX_SERIE_DOCUMENTAL: return Type.String;
			case Fields.DOC_METADADESID: return Type.String;
			case Fields.DOC_VERSIONTI: return Type.String;
			case Fields.DOC_ORGAN: return Type.List;
			case Fields.DOC_DATA: return Type.Date;
			case Fields.DOC_ORIGEN: return Type.String;
			case Fields.DOC_ESTAT_ELABORACIO: return Type.String;
			case Fields.DOC_SERIE_DOCUMENTAL: return Type.String;
			default: return null;
		}
	}
	

}