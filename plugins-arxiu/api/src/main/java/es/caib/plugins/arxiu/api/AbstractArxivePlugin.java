package es.caib.plugins.arxiu.api;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

/**
 * Implementacio genèrica de Arxive.
 *
 * @author Limit
 *
 */
public abstract class AbstractArxivePlugin extends AbstractPluginProperties implements IArxiuPlugin {

	protected final Logger log = Logger.getLogger(getClass());

	public AbstractArxivePlugin() {
		super();
	}
	public AbstractArxivePlugin(String propertyKeyBase, Properties properties) {
		super(propertyKeyBase, properties);
	}
	public AbstractArxivePlugin(String propertyKeyBase) {
		super(propertyKeyBase);
	}


	protected abstract String[] getSupportedSignatureTypes();
	
	protected abstract String getPropertyBase();
}
