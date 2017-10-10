/**
 * 
 */
package es.caib.plugins.arxiu.api;

/**
 * Valors constants per a les enumeracions.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuConstants {

	public static final String EXPEDIENT_ESTAT_OBERT = "E01";
	public static final String EXPEDIENT_ESTAT_TANCAT = "E02";
	public static final String EXPEDIENT_ESTAT_INDEX = "E03";

	public static final String DOCUMENT_ESTAT_ESBORRANY = "ESBORRANY";
	public static final String DOCUMENT_ESTAT_DEFINITIU = "DEFINITIU";

	public static final String DOCUMENT_ESTAT_ELAB_ORIG = "EE01";
	public static final String DOCUMENT_ESTAT_ELAB_COPIA_CF = "EE02";
	public static final String DOCUMENT_ESTAT_ELAB_COPIA_DP = "EE03";
	public static final String DOCUMENT_ESTAT_ELAB_COPIA_PR = "EE04";

	public static final String DOCUMENT_TIPUS_RESOL = "TD01";
	public static final String DOCUMENT_TIPUS_ACORD = "TD02";
	public static final String DOCUMENT_TIPUS_CONTR = "TD03";
	public static final String DOCUMENT_TIPUS_CONVE = "TD04";
	public static final String DOCUMENT_TIPUS_DECLR = "TD05";
	public static final String DOCUMENT_TIPUS_COMUN = "TD06";
	public static final String DOCUMENT_TIPUS_NOTIF = "TD07";
	public static final String DOCUMENT_TIPUS_PUBLI = "TD08";
	public static final String DOCUMENT_TIPUS_ACREC = "TD09";
	public static final String DOCUMENT_TIPUS_ACTA = "TD10";
	public static final String DOCUMENT_TIPUS_CERTI = "TD11";
	public static final String DOCUMENT_TIPUS_DILIG = "TD12";
	public static final String DOCUMENT_TIPUS_INFOR = "TD13";
	public static final String DOCUMENT_TIPUS_SOLIC = "TD14";
	public static final String DOCUMENT_TIPUS_DENUN = "TD15";
	public static final String DOCUMENT_TIPUS_ALEGA = "TD16";
	public static final String DOCUMENT_TIPUS_RECUR = "TD17";
	public static final String DOCUMENT_TIPUS_COMCI = "TD18";
	public static final String DOCUMENT_TIPUS_FACTU = "TD19";
	public static final String DOCUMENT_TIPUS_OT_IN = "TD20";
	public static final String DOCUMENT_TIPUS_OTROS = "TD99";

	public static final String DOCUMENT_FORMAT_GML = "GML";
	public static final String DOCUMENT_FORMAT_WFS = "WFS";
	public static final String DOCUMENT_FORMAT_WMS = "WMS";
	public static final String DOCUMENT_FORMAT_GZIP = "GZIP";
	public static final String DOCUMENT_FORMAT_ZIP = "ZIP";
	public static final String DOCUMENT_FORMAT_AVI = "AVI";
	public static final String DOCUMENT_FORMAT_MP4A = "MPEG-4 MP4 media";
	public static final String DOCUMENT_FORMAT_CSV = "Comma Separated Values";
	public static final String DOCUMENT_FORMAT_HTML = "HTML";
	public static final String DOCUMENT_FORMAT_CSS = "CSS";
	public static final String DOCUMENT_FORMAT_JPEG = "JPEG";
	public static final String DOCUMENT_FORMAT_MHTML = "MHTML";
	public static final String DOCUMENT_FORMAT_OASIS12 = "ISO/IEC 26300:2006 OASIS 1.2";
	public static final String DOCUMENT_FORMAT_SOXML = "Strict Open XML";
	public static final String DOCUMENT_FORMAT_PDF = "PDF";
	public static final String DOCUMENT_FORMAT_PDFA = "PDF/A";
	public static final String DOCUMENT_FORMAT_PNG = "PNG";
	public static final String DOCUMENT_FORMAT_RTF = "RTF";
	public static final String DOCUMENT_FORMAT_SVG = "SVG";
	public static final String DOCUMENT_FORMAT_TIFF = "TIFF";
	public static final String DOCUMENT_FORMAT_TXT = "TXT";
	public static final String DOCUMENT_FORMAT_XHTML = "XHTML";
	public static final String DOCUMENT_FORMAT_MP3 = "MP3. MPEG-1 Audio Layer 3";
	public static final String DOCUMENT_FORMAT_OGG = "OGG-Vorbis";
	public static final String DOCUMENT_FORMAT_MP4V = "MPEG-4 MP4 vídeo";
	public static final String DOCUMENT_FORMAT_WEBM = "WebM";

	public static final String DOCUMENT_EXTENSIO_GML = ".gml";
	public static final String DOCUMENT_EXTENSIO_GZ = ".gz";
	public static final String DOCUMENT_EXTENSIO_ZIP = ".zip";
	public static final String DOCUMENT_EXTENSIO_AVI = ".avi";
	public static final String DOCUMENT_EXTENSIO_CSV = ".csv";
	public static final String DOCUMENT_EXTENSIO_HTML = ".html";
	public static final String DOCUMENT_EXTENSIO_HTM = ".htm";
	public static final String DOCUMENT_EXTENSIO_CSS = ".css";
	public static final String DOCUMENT_EXTENSIO_JPG = ".jpg";
	public static final String DOCUMENT_EXTENSIO_JPEG = ".jpeg";
	public static final String DOCUMENT_EXTENSIO_MHTML = ".mhtml";
	public static final String DOCUMENT_EXTENSIO_MHT = ".mht";
	public static final String DOCUMENT_EXTENSIO_ODT = ".odt";
	public static final String DOCUMENT_EXTENSIO_ODS = ".ods";
	public static final String DOCUMENT_EXTENSIO_ODP = ".odp";
	public static final String DOCUMENT_EXTENSIO_ODG = ".odg";
	public static final String DOCUMENT_EXTENSIO_DOCX = ".docx";
	public static final String DOCUMENT_EXTENSIO_XLSX = ".xlsx";
	public static final String DOCUMENT_EXTENSIO_PPTX = ".pptx";
	public static final String DOCUMENT_EXTENSIO_PDF = ".pdf";
	public static final String DOCUMENT_EXTENSIO_PNG = ".png";
	public static final String DOCUMENT_EXTENSIO_RTF = ".rtf";
	public static final String DOCUMENT_EXTENSIO_SVG = ".svg";
	public static final String DOCUMENT_EXTENSIO_TIFF = ".tiff";
	public static final String DOCUMENT_EXTENSIO_TXT = ".txt";
	public static final String DOCUMENT_EXTENSIO_MP3 = ".mp3";
	public static final String DOCUMENT_EXTENSIO_OGG = ".ogg";
	public static final String DOCUMENT_EXTENSIO_OGA = ".oga";
	public static final String DOCUMENT_EXTENSIO_MPEG = ".mpeg";
	public static final String DOCUMENT_EXTENSIO_MP4 = ".mp4";
	public static final String DOCUMENT_EXTENSIO_WEBM = ".webm";

	public static final String CONTINGUT_ORIGEN_CIU = "0";
	public static final String CONTINGUT_ORIGEN_ADM = "1";

	public static final String CONTINGUT_TIPUS_EXPEDIENT = "TC01";
	public static final String CONTINGUT_TIPUS_DOCUMENT = "TC02";
	public static final String CONTINGUT_TIPUS_CARPETA = "TC03";

	public static final String FIRMA_TIPUS_CSV = "TF01";
	public static final String FIRMA_TIPUS_XADES_DET = "TF02";
	public static final String FIRMA_TIPUS_XADES_ENV = "TF03";
	public static final String FIRMA_TIPUS_CADES_DET = "TF04";
	public static final String FIRMA_TIPUS_CADES_ATT = "TF05";
	public static final String FIRMA_TIPUS_PADES = "TF06";
	public static final String FIRMA_TIPUS_SMIME = "TF07";
	public static final String FIRMA_TIPUS_ODT = "TF08";
	public static final String FIRMA_TIPUS_OOXML = "TF09";

	public static final String FIRMA_PERFIL_BES = "BES";
	public static final String FIRMA_PERFIL_EPES = "EPES";
	public static final String FIRMA_PERFIL_LTV = "LTV";
	public static final String FIRMA_PERFIL_T = "T";
	public static final String FIRMA_PERFIL_C = "C";
	public static final String FIRMA_PERFIL_X = "X";
	public static final String FIRMA_PERFIL_XL = "XL";
	public static final String FIRMA_PERFIL_A = "A";

}
