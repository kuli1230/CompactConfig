package de.ketrwu.compactconfig;

import java.util.HashMap;

import de.ketrwu.compactconfig.exceptions.ConfigurationLoadException;
import de.ketrwu.compactconfig.exceptions.ConfigurationParsingException;

public class MultiConfigHandler {
	
	HashMap<String, Configuration> configs = new HashMap<String, Configuration>();
	
	public Configuration addConfig(String keyName, String file) {
		if(configs.containsKey(keyName)) return configs.get(keyName);
		Configuration config = new Configuration(file);
		configs.put(keyName, config);		
		return config;
	}
	
	public void saveAllConfigs() {
		for(Configuration config : configs.values()) config.saveConfig();
	}
	
	public void reloadAllConfigs() {
		try {
			for(Configuration config : configs.values()) config.reloadConfig();
		} catch (ConfigurationLoadException | ConfigurationParsingException e) {
			e.printStackTrace();
		}
		
	}
	
	public Configuration getConfig(String keyName) {
		return configs.get(keyName);
	}
	
	public boolean existConfig(String keyName) {
		return configs.containsKey(keyName);
	}
	
}
