/**
 * 
 */
package es.caib.plugins.arxiu.caib;

import java.util.List;

import es.caib.plugins.arxiu.api.ArxiuException;
import es.caib.plugins.arxiu.api.Capsalera;
import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.InformacioItem;

/**
 * Interfície del client per a accedir a la funcionalitat de
 * l'arxiu digital.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface CaibArxiuClient {
	
	
	public final short NUMERO_PAGINES_RESULTAT_CERCA = 50;
	
	
	/**
	 * ================= M E T O D E S   E X P E D I E N T S =================
	 */
	
	public Expedient fileCreate(
			String nom,
			ExpedientMetadades metadades,
			String aplicacioCodi,
			Capsalera capsalera) throws ArxiuException;
	
	public void fileUpdate(
			String identificador, 
			String nom,
			ExpedientMetadades metadades,
			String aplicacioCodi,
			Capsalera capsalera) throws ArxiuException;
	
	public void fileDelete(
			String identificador,
			Capsalera capsalera) throws ArxiuException;
	
	public Expedient fileGet(
			String identificador,
			Capsalera capsalera) throws ArxiuException;
	
	public List<InformacioItem> fileSearch(
			List<ConsultaFiltre> filtres,
			Integer pagina,
			Capsalera capsalera) throws ArxiuException;
	
	public List<InformacioItem> fileVersionList(
			String identificador,
			Capsalera capsalera) throws ArxiuException;
	
	public void fileClose(
			String identificador,
			Capsalera capsalera) throws ArxiuException;
	
	public void fileReopen(
			String identificador,
			Capsalera capsalera) throws ArxiuException;
	
	public String fileExport(
			String identificador,
			Capsalera capsalera) throws ArxiuException;
	
	
	/**
	 * ================= M E T O D E S   D O C U M E N T S =================
	 */
	
	public Document documentFinalCreate(
			String pareIdentificador,
			Document document,
			String aplicacioCodi,
			Capsalera capsalera) throws ArxiuException;
	
	public Document documentDraftCreate(
			String pareIdentificador,
			Document document,
			String aplicacioCodi,
			Capsalera capsalera) throws ArxiuException;
	
	public void documentFinalSet(
			Document document,
			String aplicacioCodi,
			Capsalera capsalera) throws ArxiuException;
	
	public void documentUpdate(
			Document document,
			String aplicacioCodi,
			Capsalera capsalera) throws ArxiuException;
	
	public void documentDelete(
			String identificador,
			Capsalera capsalera) throws ArxiuException;
	
	public Document documentGet(
			String identificador,
			String csv,
			boolean ambContingut,
			Capsalera capsalera) throws ArxiuException;
	
	public List<InformacioItem> documentSearch(
			List<ConsultaFiltre> filtres,
			Integer pagina,
			Capsalera capsalera) throws ArxiuException;
	
	public List<InformacioItem> documentVersionList(
			String identificador,
			Capsalera capsalera) throws ArxiuException;
	
	public String documentCsvGenerate(
			Capsalera capsalera) throws ArxiuException ;
	
	public String documentCopy(
			String nodeId,
			String targetNodeId,
			Capsalera capsalera) throws ArxiuException;
	
	public void documentMove(
			String nodeId,
			String targetNodeId,
			Capsalera capsalera) throws ArxiuException;
	
	public String documentEniGet(
			String identificador,
			Capsalera capsalera) throws ArxiuException;
	
	
	/**
	 * ================= M E T O D E S   C A R P E T E S =================
	 */

	public String folderCreate(
			String pareIdentificador,
			String nom,
			Capsalera capsalera) throws ArxiuException;
	
	public void folderUpdate(
			String identificador,
			String nom,
			Capsalera capsalera) throws ArxiuException;
	
	public void folderDelete(
			String identificador,
			Capsalera capsalera) throws ArxiuException;
	
	public Carpeta folderGet(
			String identificador,
			Capsalera capsalera) throws ArxiuException;
	
	public String folderCopy(
			String nodeId,
			String targetNodeId,
			Capsalera capsalera) throws ArxiuException;
	
	public void folderMove(
			String nodeId,
			String targetNodeId,
			Capsalera capsalera) throws ArxiuException ;

}
