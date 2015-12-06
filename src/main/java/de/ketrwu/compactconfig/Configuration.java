package de.ketrwu.compactconfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.ketrwu.compactconfig.exceptions.ConfigurationException;
import de.ketrwu.compactconfig.exceptions.ConfigurationLoadException;
import de.ketrwu.compactconfig.exceptions.ConfigurationParsingException;

/**
 * Simple and easy to use configuration system
 * 
 * @author Kenneth Wußmann
 * @version 1.2.0
 */

public class Configuration {
	
	File file;
	HashMap<String, ConfigurationEntry> entries = new HashMap<String, ConfigurationEntry>();
	HashMap<Integer, String> comments = new HashMap<Integer, String>();
	
	/**
	 * Loads existing Configuration or creates a new one
	 * 
	 * @param file Path to the Configuration-File
	 */
	public Configuration(String file) {
		this.file = new File(file);
		try {
			reloadConfig();
		} catch (ConfigurationLoadException | ConfigurationParsingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reloads all values from File. This method discards
	 * unsaved changes!
	 */
	public void reloadConfig() throws ConfigurationLoadException, ConfigurationParsingException {
		comments.clear();
		entries.clear();
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new ConfigurationLoadException("Could not create config file \""+file.getAbsolutePath()+"\"!", e);
			}
		}	
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String strLine;
			String entryHeader = null;
			ConfigurationEntry currentEntry = new ConfigurationEntry(this, null);
			int line = 1;
			while ((strLine = br.readLine()) != null)   {
				if(strLine.isEmpty()) continue;
				if(strLine.startsWith("#") || strLine.startsWith("  #")) {
					comments.put(line, strLine);
					line++;
					continue;
				}
				if(strLine.startsWith("  ")) strLine = strLine.substring(2, strLine.length());
				if(isConfigEndTag(entryHeader, strLine)) {
					if(entryHeader == null) {
						throw new ConfigurationParsingException("Can't parse config \""+file.getAbsolutePath()+"\" at line " + line + "!");
					} else {
						entries.put(entryHeader, currentEntry);
						currentEntry = new ConfigurationEntry(this, null);
						entryHeader = null;
					}
				}
				if(strLine.startsWith("[") & strLine.endsWith("]") & isConfigEndTag(entryHeader, strLine) == false) {
					if(entryHeader == null) {
						entryHeader = strLine.substring(1, strLine.length()-1);
						currentEntry.setHeader(entryHeader);
					} else {
						throw new ConfigurationParsingException("Can't parse config \""+file.getAbsolutePath()+"\" at line " + line + "!");
					}
				} else {
					if(strLine.contains("=") & isConfigEndTag(entryHeader, strLine) == false) {
						String[] stringEntry = strLine.split("\\=", 2);
						currentEntry.set(stringEntry[0], stringEntry[1]);
					} else {
						if(!isConfigEndTag(entryHeader, strLine)) {
							throw new ConfigurationParsingException("Can't parse config \""+file.getAbsolutePath()+"\" at line " + line + "!");
						}
					}
				}
				
				line++;
			}
			br.close();
		} catch (IOException e) {
			throw new ConfigurationLoadException("Could not create config file \""+file.getAbsolutePath()+"\"!", e);
		}
	}
	
	/**
	 * Checks whether input is a end-tag of headerText
	 * 
	 * @param headerText Name of header
	 * @param input String that needs to be checked
	 */
	private boolean isConfigEndTag(String headerText, String input) {
		return input.equals("[/" + headerText + "]") | input.equalsIgnoreCase("[/END]") | input.equals("[/]");
	}
	
	/**
	 * Saves changes to file. This method overwrites changes made manually in file.
	 */
	public void saveConfig() {
		if(entries.isEmpty()) return;
		int line = 1;
		String fileCache = "";
		for(String header : getHeaders()) {
			if(comments.containsKey(line)) {
				fileCache += comments.get(line) + System.getProperty("line.separator");
				line++;
			}
			ConfigurationEntry ce = entries.get(header);
			if(ce.getObjects().size() <= 0) continue;
			fileCache += "[" + header + "]" + System.getProperty("line.separator");
			line++;
			
			for(String key : ce.getObjects().keySet()) {
				String value = ce.getObjects().get(key);
				if(value != null & value.equalsIgnoreCase("null") == false) fileCache += "  " + key + "=" + value + System.getProperty("line.separator");
				line++;
			}
			fileCache += "[/]" + System.getProperty("line.separator") + System.getProperty("line.separator");
			line++;
		}
		try {
			PrintWriter writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");
			writer.write(fileCache);
			writer.close();
			reloadConfig();
		} catch (ConfigurationLoadException | ConfigurationParsingException | FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Works like the set(); method but only sets the value when the key 
	 * does not exists.
	 * Is helpfully for default values in your configuration because it wont
	 * overwrite the changes made by the user
	 * 
	 * @param header Header of your key
	 * @param key The key where you want to save your value
	 * @param value Value, could be Strings, Integers, etc.
	 */
	public void addDefault(String header, String key, Object value) {
		if(!existEntry(header, key)) {
			set(header, key, value);
		}
	}
	
	/**
	 * Get existing headers
	 * 
	 * @return Returns all existing headers in your config
	 */
	public List<String> getHeaders() {
		List<String> headers = new ArrayList<String>();
		headers.addAll(entries.keySet());
		return headers;
	}
	
	/**
	 * Get existing keys in this header
	 * 
	 * @param header The header
	 * @return Returns all existing key from a header in your config
	 */
	public List<String> getKeys(String header) {
		List<String> keys = new ArrayList<String>();
		keys.addAll(getEntries().get(header).getObjects().keySet());
		return keys;
	}
	
	/**
	 * Checks whether a value exists to this header and key
	 * 
	 * @param header The header
	 * @param key The key
	 * @return Boolean
	 */
	public boolean existEntry(String header, String key) {
		if(existHeader(header)) {
			for(String keys : entries.get(header).objects.keySet()) {
				if(key.equals(keys)) return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether a header exists in your config
	 * 
	 * @param header The header
	 * @return Boolean
	 */
	public boolean existHeader(String header) {
		return getHeaders().contains(header);
	}
	
	/**
	 * Set a value to a header and key.
	 * If header or key you have entered does not exist it'll be
	 * created in your configuration. 
	 * 
	 * @param header The header
	 * @param key The key which redirects to the value
	 * @param value The value, the actual data (Strings, Lists, Integers etc)
	 */
	public void set(String header, String key, Object value) {
		if(key.contains("=")) {
			new ConfigurationParsingException("Illegal character \"=\" in key for header \"" + header + "\"!").printStackTrace(); 
			return;
		}
		if(existHeader(header)) {
			if(value == null) {
				entries.get(header).getObjects().remove(key);
				return;
			}
			entries.get(header).set(key, value);
		} else {
			ConfigurationEntry ce = new ConfigurationEntry(this, header);
			ce.set(key, value);
			entries.put(header, ce);
		}
	}
	
	/**
	 * Gets an undefined value from header and key
	 * 
	 * @param header The header
	 * @param key The key which redirects to the value
	 * @return The value you want to get
	 */
	public Object get(String header, String key) {
		if(key.contains("=")) {
			new ConfigurationParsingException("Illegal character \"=\" in key for header \"" + header + "\"!").printStackTrace(); 
			return null;
		}
		if(existHeader(header)) {
			return entries.get(header).get(key);
		}
		return null;
	}
	
	/**
	 * Gets a value by header and key
	 * 
	 * @param header The header
	 * @param key The key which redirects to the value
	 * @return The value you want to get
	 */
	public List<Object> getList(String header, String key) {
		List<Object> list = new ArrayList<Object>();
		Object obj = get(header, key);
		String listString = (String) obj;
		if(listString.startsWith("{\"") & listString.endsWith("\"}")) {
			listString = listString.substring(1, listString.length()-1);
			for(String value : listString.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")) list.add((Object) value.substring(1, value.length()-1));
		} else {
			new ConfigurationException("String \"" + listString + "\" is malformed!").printStackTrace(); 
			return null;
		}
		
		return list;
	}
	
	/**
	 * Gets a value by header and key
	 * 
	 * @param header The header
	 * @param key The key which redirects to the value
	 * @return The value you want to get
	 */
	public List<String> getStringList(String header, String key) {
		List<String> list = new ArrayList<String>();
		for(Object value : getList(header, key)) list.add((String) value);
		return list;
	}
	
	/**
	 * Gets a value by header and key
	 * 
	 * @param header The header
	 * @param key The key which redirects to the value
	 * @return The value you want to get
	 */
	public List<Integer> getIntegerList(String header, String key) {
		List<Integer> list = new ArrayList<Integer>();
		for(Object value : getList(header, key)) list.add(Integer.parseInt(value.toString()));
		return list;
	}
	
	/**
	 * Gets a value by header and key
	 * 
	 * @param header The header
	 * @param key The key which redirects to the value
	 * @return The value you want to get
	 */
	public boolean getBoolean(String header, String key) {
		return Boolean.parseBoolean(get(header, key).toString()); 
	}
	
	/**
	 * Gets a value by header and key
	 * 
	 * @param header The header
	 * @param key The key which redirects to the value
	 * @return The value you want to get
	 */
	public String getString(String header, String key) {
		return get(header, key).toString(); 
	}
	
	/**
	 * Gets a value by header and key
	 * 
	 * @param header The header
	 * @param key The key which redirects to the value
	 * @return The value you want to get
	 */
	public int getInt(String header, String key) {
		return Integer.parseInt(get(header, key).toString());
	}
	
	/**
	 * Gets a value by header and key
	 * 
	 * @param header The header
	 * @param key The key which redirects to the value
	 * @return The value you want to get
	 */
	public long getLong(String header, String key) {
		return Long.parseLong(get(header, key).toString());
	}
	
	/**
	 * Gets a value by header and key
	 * 
	 * @param header The header
	 * @param key The key which redirects to the value
	 * @return The value you want to get
	 */
	public float getFloat(String header, String key) {
		return Float.parseFloat(get(header, key).toString());
	}

	/**
	 * Gets a value by header and key
	 * 
	 * @param header The header
	 * @param key The key which redirects to the value
	 * @return The value you want to get
	 */
	public double getDouble(String header, String key) {
		return Double.parseDouble(get(header, key).toString());
	}

	/**
	 * Get the configuration file
	 * 
	 * @return The file 
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * Set the configuration file
	 * 
	 * @param file The file 
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	/**
	 * Gets all ConfigurationEntries. Not necessary for you.
	 * 
	 * @return All ConfigurationEntry instances
	 */
	public HashMap<String, ConfigurationEntry> getEntries() {
		return entries;
	}
	
	/**
	 * Sets all ConfigurationEntries. Not necessary for you.
	 * 
	 * @param entries All ConfigurationEntry instances
	 */
	public void setEntries(HashMap<String, ConfigurationEntry> entries) {
		this.entries = entries;
	}
}
