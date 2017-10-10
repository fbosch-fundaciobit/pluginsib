/**
 * 
 */
package es.caib.plugins.arxiu.caib;

import java.util.List;

import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.ConsultaFiltre;

/**
 * Mètodes d'ajuda per a la generació de les consultes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuConsultaHelper {
	
	// Camps comuns
	public static final String TABLE = "table";
	public static final String ID = "id";
	public static final String NOM = "nom";
	// Camps de sequencia
	public static final String SEQ_VALOR = "seq_valor";
	// Camps de versions
	public static final String VER_VERSIO = "ver_versio";
	public static final String VER_IDS = "ver_ids";
	// Camps de Expedient
	public static final String EX_VERSIO = "ex_versio";
	public static final String EX_VMANAGER = "ex_vmanager";
	public static final String EX_OBERT = "ex_obert";
	public static final String EX_METADADESID = "ex_metadadesid";
	public static final String EX_VERSIONTI = "ex_versionti";
	public static final String EX_ORIGEN = "ex_origen";
	public static final String EX_ORGAN = "ex_organ";
	public static final String EX_DATA_OBERTURA = "ex_data_obertura";
	public static final String EX_CLASSIFICACIO = "ex_classificacio";
	public static final String EX_ESTAT = "ex_estat";
	public static final String EX_INTERESSAT = "ex_interessat";
	public static final String EX_SERIE_DOCUMENTAL = "ex_serie_documental";
	public static final String EX_CONTINGUTS = "ex_continguts";
	// Camps de Document
	public static final String DOC_VERSIO = "doc_versio";//String
	public static final String DOC_VMANAGER = "doc_vmanager";//String
	public static final String DOC_DRAFT = "doc_draft";//String
	public static final String DOC_PARE = "doc_pare";//String
	public static final String DOC_METADADESID = "doc_metadadesid";//String
	public static final String DOC_VERSIONTI = "doc_versionti";//String
	public static final String DOC_ORGAN = "doc_organ";//List<String>
	public static final String DOC_DATA = "doc_data";//Date
	public static final String DOC_ORIGEN = "doc_origen";//String
	public static final String DOC_ESTAT_ELABORACIO = "doc_estat_elaboracio";//String
	public static final String DOC_TIPUS_DOCUMENTAL = "doc_tipus_documental";//String
	public static final String DOC_SERIE_DOCUMENTAL = "doc_serie_documental";//String
	public static final String DOC_FIRMES = "doc_firmes";//List<String>
	public static final String DOC_CONTINGUT = "doc_contingut";//byte[]
	public static final String DOC_TIPUS_MIME = "doc_tipus_mime";//String
	public static final String DOC_ID_ORIGEN = "doc_id_origen";//String
	// Camps de carpetes
	public static final String CPT_PARE = "cpt_pare";//String
	public static final String CPT_ITEMS = "cpt_items";//byte[]
	// Camps de firma
	public static final String FIR_DOCUMENT_PARE = "fir_document_pare";//String
	public static final String FIR_CONTINGUT = "fir_contingut";//byte[]
	public static final String FIR_MIME = "fir_mime";//String
	public static final String FIR_TIPUS = "fir_tipus";//String
	public static final String FIR_CSV_REGULACIO = "fir_nom";//String
	// Camps metainformacio
	public static final String MI_EMCOD = "mi_emcod";//String

	private static enum ConsultaCampTipus {String, List, Date};

	public static String generarConsulta(
			String queryType,
			List<ConsultaFiltre> filtres) throws ArxiuException {
		String query = "+TYPE:" + queryType;
		for (int i = 0; i < filtres.size(); i++) {
			ConsultaFiltre filtre = filtres.get(i);
			String metadada = filtre.getMetadada();
			String valor1 = filtre.getValorOperacio1();
			String valor2 = filtre.getValorOperacio2();
			ConsultaCampTipus type = null;
			switch (metadada) {
				case EX_METADADESID:
					type = ConsultaCampTipus.String;
					break;
				case EX_VERSIO:
					type = ConsultaCampTipus.String;
					break;
				case EX_VERSIONTI:
					type = ConsultaCampTipus.String;
					break;
				case EX_ORIGEN:
					type = ConsultaCampTipus.String;
					break;
				case EX_ORGAN:
					type = ConsultaCampTipus.List;
					break;
				case EX_DATA_OBERTURA:
					type = ConsultaCampTipus.Date;
					break;
				case EX_CLASSIFICACIO:
					type = ConsultaCampTipus.String;
					break;
				case EX_ESTAT:
					type = ConsultaCampTipus.String;
					break;
				case EX_INTERESSAT:
					type = ConsultaCampTipus.List;
					break;
				case EX_SERIE_DOCUMENTAL:
					type = ConsultaCampTipus.String;
					break;
				case DOC_METADADESID:
					type = ConsultaCampTipus.String;
					break;
				case DOC_VERSIONTI:
					type = ConsultaCampTipus.String;
					break;
				case DOC_ORGAN:
					type = ConsultaCampTipus.List;
					break;
				case DOC_DATA:
					type = ConsultaCampTipus.Date;
					break;
				case DOC_ORIGEN:
					type = ConsultaCampTipus.String;
					break;
				case DOC_ESTAT_ELABORACIO:
					type = ConsultaCampTipus.String;
					break;
				case DOC_SERIE_DOCUMENTAL:
					type = ConsultaCampTipus.String;
					break;
			}
			if (type != null) {
				query += "+@eni\\:";
				switch(type) {
					case String:
						switch(filtre.getOperacio()) {
							case IGUAL:
								query = query + metadada + ":\"" + valor1 + "\"";
								break;
							case CONTE:
								query = query + metadada + ":*" + valor1 + "*";
								break;
							case MAJOR:
								query = query + metadada + ":[" + valor1 + " TO *]" +
										" -" + metadada + ":\"" + valor1 + "\"";
								break;
							case MENOR:
								query = query + metadada + ":[* TO " + valor1 + "]" +
										" -" + metadada + ":\"" + valor1 + "\"";
								break;
							case ENTRE:
								query = query + metadada + ":[" + valor1 + " TO " + valor2 + "]";
								break;
							default:
								throw new ArxiuException("No s'ha definit un operador per la metadada " + metadada);
						}
						break;
					case List:
						switch(filtre.getOperacio()) {
							case IGUAL:
								throw new ArxiuException("L'operador " + filtre.getOperacio() + " no es aplicable al un tipus de dades " + type);
							case CONTE:
								query = query + metadada + ":\"" + valor1 + "\"";
								break;
							case MAJOR:
								throw new ArxiuException("L'operador " + filtre.getOperacio() + " no es aplicable al un tipus de dades " + type);
							case MENOR:
								throw new ArxiuException("L'operador " + filtre.getOperacio() + " no es aplicable al un tipus de dades " + type);
							case ENTRE:
								throw new ArxiuException("L'operador " + filtre.getOperacio() + " no es aplicable al un tipus de dades " + type);
							default:
								throw new ArxiuException("No s'ha definit un operador per la metadada " + metadada);
						}
						break;
					case Date:
						switch(filtre.getOperacio()) {
							case IGUAL:
								query = query + metadada + ":\"" + valor1 + "\"";
								break;
							case CONTE:
								throw new ArxiuException("L'operador " + filtre.getOperacio() + " no es aplicable al un tipus de dades " + type);
							case MAJOR:
								query = query + metadada + ":[" + valor1 + " TO *]" +
										" -" + metadada + ":\"" + valor1 + "\"";
								break;
							case MENOR:
								query = query + metadada + ":[* TO " + valor1 + "]" +
										" -" + metadada + ":\"" + valor1 + "\"";
								break;
							case ENTRE:
								query = query + metadada + ":[" + valor1 + " TO " + valor2 + "]";
								break;
							default:
								throw new ArxiuException("No s'ha definit un operador per la metadada " + metadada);
						}
						break;
					default:
						throw new ArxiuException("La metadada " + metadada + " no esta definida");
				}
			}
			if (i < filtres.size()-1) {
				query = query + " AND ";
			}
		}
		return query;
	}

}
