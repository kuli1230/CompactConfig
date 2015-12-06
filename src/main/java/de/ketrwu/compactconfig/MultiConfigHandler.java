package de.ketrwu.compactconfig;

import java.util.HashMap;

import de.ketrwu.compactconfig.exceptions.ConfigurationLoadException;
import de.ketrwu.compactconfig.exceptions.ConfigurationParsingException;

/**
 * Simple and easy to use configuration system
 * 
 * @author Kenneth Wuﬂmann
 * @version 1.2.1
 */

public class MultiConfigHandler {
	
	HashMap<String, Configuration> configs = new HashMap<String, Configuration>();
	
	/**
	 * Create an instance for a MultiConfigHandler.
	 * It can manage multiple configurations for you.
	 */
	public MultiConfigHandler() {  }
	
	/**
	 * Create and add a new Configuration to the MultiConfigHandler
	 * 
	 * @param keyName This is how you identify this config (e.g. Config-1 or settings)
	 * @param file The path to the configuration file
	 * @return The Configuration instance
	 */
	public Configuration addConfig(String keyName, String file) {
		if(configs.containsKey(keyName)) return configs.get(keyName);
		Configuration config = new Configuration(file);
		configs.put(keyName, config);		
		return config;
	}
	
	/**
	 * Save all Configurations managed by MultiConfigHandler
	 */
	public void saveAllConfigs() {
		for(Configuration config : configs.values()) config.saveConfig();
	}
	
	/**
	 * Reload all Configurations managed by MultiConfigHandler
	 */
	public void reloadAllConfigs() {
		try {
			for(Configuration config : configs.values()) config.reloadConfig();
		} catch (ConfigurationLoadException | ConfigurationParsingException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Get a Configuration
	 * 
	 * @param keyName This is your identifier for your config (e.g. Config-1 or settings)
	 * @return The configuration instance
	 */
	public Configuration getConfig(String keyName) {
		return configs.get(keyName);
	}
	
	/**
	 * Check whether a configuration exists in MultiConfigHandler or not
	 * 
	 * @param keyName This is your identifier for your config (e.g. Config-1 or settings)
	 * @return Boolean
	 */
	public boolean existConfig(String keyName) {
		return configs.containsKey(keyName);
	}
	
}
