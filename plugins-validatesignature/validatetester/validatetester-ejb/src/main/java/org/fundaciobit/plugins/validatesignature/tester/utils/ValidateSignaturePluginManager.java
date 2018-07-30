package org.fundaciobit.plugins.validatesignature.tester.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.utils.PluginsManager;
import org.fundaciobit.plugins.validatesignature.api.IValidateSignaturePlugin;

/**
 *
 * @author anadal
 *
 */
public class ValidateSignaturePluginManager {

  protected static final Logger log = Logger.getLogger(ValidateSignaturePluginManager.class);

  private static final ValidateSignaturePluginManager pluginManager;

  // Properties BASE
  public static final String PROPERTIES_BASE = "org.fundaciobit.signaturevalidate.";

  public static final String PLUGINS_LIST_PROPERTY = "validatesignatureplugins.path";

  // Cache de Plugins
  private List<Plugin> plugins = null;

  private Map<Long, Plugin> pluginsCache = new HashMap<Long, Plugin>();

  private Map<Long, IValidateSignaturePlugin> instancesCache = new HashMap<Long, IValidateSignaturePlugin>();

  private final String pluginPropertiesPath;

  static {

    String pluginPropertiesPath = System.getProperty(PLUGINS_LIST_PROPERTY);

    if (pluginPropertiesPath == null) {
      log.error("La propietat '" + PLUGINS_LIST_PROPERTY + "' " + "no te valor assignat.",
          new Exception());
    }

    pluginManager = new ValidateSignaturePluginManager(pluginPropertiesPath);

  }

  public static ValidateSignaturePluginManager getInstance() {
    return pluginManager;
  };

  /**
   * @param pluginPropertiesPath
   */
  private ValidateSignaturePluginManager(String pluginPropertiesPath) {
    super();
    this.pluginPropertiesPath = pluginPropertiesPath;
  }

  public void deleteCache(Plugin instance) {
    if (instance != null) {
      synchronized (pluginsCache) {
        pluginsCache.remove(instance.getPluginID());
      }
    }
  }

  public void clearCache() {
    synchronized (pluginsCache) {
      pluginsCache.clear();
    }
  }

  public void addPluginToCache(Long pluginID, Plugin pluginInstance) {
    synchronized (pluginsCache) {
      pluginsCache.put(pluginID, pluginInstance);
    }
  }

  public Plugin getPluginFromCache(Long pluginID) {
    synchronized (pluginsCache) {
      return pluginsCache.get(pluginID);
    }
  }

  // Ara és llegeixen els plugins d'un fitxer que la ruta es troba en una
  // entrada de les propietats de sistema
  // TODO Es pot modificar per llegir-ho directament de BBDD o de
  // System-Properties.
  public List<Plugin> getAllPlugins() throws Exception {
    if (plugins == null) {

      if (pluginPropertiesPath == null) {
        String msg = "La propietat de sistema '" + PLUGINS_LIST_PROPERTY + "' "
            + "no te valor assignat.";
        log.error(msg, new Exception());
        throw new Exception(msg);
      }

      File pluginPropertiesFile = new File(pluginPropertiesPath);

      if (!pluginPropertiesFile.exists()) {
        log.error("No existeix el fitxer  " + pluginPropertiesFile.getAbsolutePath(),
            new Exception());
        return null;
      }

      Properties pluginProperties = new Properties();

      try {
        Reader reader = new InputStreamReader(new FileInputStream(pluginPropertiesFile),
            "UTF-8");
        pluginProperties.load(reader);

      } catch (Exception e) {
        log.error(
            "Error desconegut llegint les propietats del fitxer "
                + pluginPropertiesFile.getAbsolutePath(), new Exception());
        return null;
      }

      String idsStr = pluginProperties.getProperty(PROPERTIES_BASE
          + "validatesignatureplugins");

      log.debug("Plugins a Carregar idsStr: " + idsStr);

      // Miram els identificadors de plugins que volem
      String[] ids = idsStr.split(",");

      ArrayList<Plugin> tmpplugins = new ArrayList<Plugin>();
      for (int i = 0; i < ids.length; i++) {
        long pluginID = Long.parseLong(ids[i]);
        String base = PROPERTIES_BASE + pluginID + ".";

        String nom = pluginProperties.getProperty(base + "nom");

        log.debug("Plugin a carregar NOM[" + base + "nom" + "]: " + nom);
        String classe = pluginProperties.getProperty(base + "class");
        String descripcioCurta = pluginProperties.getProperty(base + "desc");

        // Llegir propietats
        Properties properties = new Properties();
        Set<Object> keys = pluginProperties.keySet();

        for (Object object : keys) {
          String key = (String) object;

          if (key.startsWith(base)) {
            String value = pluginProperties.getProperty(key);

            // Posam la mateixa BASE a totes les propietats de totes els plugins

            String nomFinal = key.substring(base.length());

            properties.put(PROPERTIES_BASE + nomFinal, value);
          }

        }

        log.info(" -------------  PLUGIN " + pluginID + "------------------");
        log.info("nom: " + nom);
        log.info("descripcioCurta: " + descripcioCurta);
        log.info("classe: " + classe);
        log.info("properties: " + properties);

        tmpplugins.add(new Plugin(pluginID, nom, descripcioCurta, classe, properties));

      }

      plugins = tmpplugins;

    }
    return plugins;
  }

  public Plugin getPluginById(long pluginID) throws Exception {

    List<Plugin> list = getAllPlugins();

    for (Plugin plugin : list) {
      if (plugin.getPluginID() == pluginID) {
        return plugin;
      }
    }

    return null;
  }

  public IValidateSignaturePlugin getInstanceByPluginID(long pluginID) throws Exception {

    IValidateSignaturePlugin instance = instancesCache.get(pluginID);

    if (instance == null) {

      Plugin plugin = getPluginFromCache(pluginID);

      if (plugin == null) {

        plugin = getPluginById(pluginID);

        if (plugin == null) {
          return null;
        }

        addPluginToCache(pluginID, plugin);
      }

      Properties prop = plugin.getProperties();

      instance = (IValidateSignaturePlugin) PluginsManager.instancePluginByClassName(
          plugin.getClasse(), PROPERTIES_BASE, prop);

      if (instance == null) {
        throw new Exception("plugin.donotinstantiate: " + plugin.getNom() + " ("
            + plugin.getClasse() + ")");
      }

      instancesCache.put(pluginID, instance);

    }

    return instance;

  }

}
