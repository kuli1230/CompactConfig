package de.ketrwu.compactconfig;

import java.util.HashMap;
import java.util.List;

/**
 * Simple and easy to use configuration system
 * 
 * @author Kenneth Wuﬂmann
 * @version 1.2.0
 */

public class ConfigurationEntry {
	
	Configuration config;
	String header = null;
	HashMap<String, String> objects = new HashMap<String, String>();
	public ConfigurationEntry(Configuration config, String header) {
		this.config = config;
		this.header = header;
	}
	
	public void set(String key, Object value) {
		if(value instanceof List<?>) {
			String list = "{";
			for(Object obj : ((List<?>) value)) list += "\"" + obj.toString() + "\",";
			list = list.substring(0, list.length()-1);
			list += "}";
			value = list;
		}
		objects.put(key, value.toString());
	}
	
	public Object get(String key) {
		if(objects.containsKey(key)) {
			return objects.get(key);
		}
		return null;
	}

	public Configuration getConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}

	public HashMap<String, String> getObjects() {
		return objects;
	}

	public void setObjects(HashMap<String, String> objects) {
		this.objects = objects;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	
	
	
}
