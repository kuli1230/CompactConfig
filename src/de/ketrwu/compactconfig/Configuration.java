package de.ketrwu.compactconfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import de.ketrwu.compactconfig.exceptions.ConfigurationLoadException;
import de.ketrwu.compactconfig.exceptions.ConfigurationParsingException;

public class Configuration {
	
	File file;
	HashMap<String, ConfigurationEntry> entries = new HashMap<String, ConfigurationEntry>();
	HashMap<Integer, String> comments = new HashMap<Integer, String>();
	
	public Configuration(String file) {
		this.file = new File(file);
		try {
			reloadConfig();
		} catch (ConfigurationLoadException | ConfigurationParsingException e) {
			e.printStackTrace();
		}
	}
	
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
				if(strLine.startsWith("#")) {
					comments.put(line, strLine);
					continue;
				}
				if(strLine.equals("[/" + entryHeader + "]")) {
					if(entryHeader == null) {
						throw new ConfigurationParsingException("Can't parse config \""+file.getAbsolutePath()+"\" at line " + line + "!");
					} else {
						entries.put(entryHeader, currentEntry);
						currentEntry = new ConfigurationEntry(this, null);
					}
				}
				if(strLine.startsWith("[") & strLine.endsWith("]") & strLine.startsWith("[/") == false) {
					entryHeader = strLine.substring(1, strLine.length()-1);
					currentEntry.setHeader(entryHeader);
				} else {
					if(strLine.contains("=") & strLine.equals("[/" + entryHeader + "]") == false) {
						String[] stringEntry = strLine.split("\\=", 2);
						currentEntry.set(stringEntry[0], stringEntry[1]);
					} else {
						if(!strLine.equals("[/" + entryHeader + "]")) {
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
				if(value != null & value.equals("") == false & value.equalsIgnoreCase("null") == false) fileCache += key + "=" + value + System.getProperty("line.separator");
				line++;
			}
			fileCache += "[/" + header + "]" + System.getProperty("line.separator") + System.getProperty("line.separator");
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
	
	public List<String> getHeaders() {
		List<String> headers = new ArrayList<String>();
		headers.addAll(entries.keySet());
		return headers;
	}
	
	public List<String> getKeys(String header) {
		List<String> keys = new ArrayList<String>();
		keys.addAll(getEntries().get(header).getObjects().keySet());
		return keys;
	}
	
	public boolean existEntry(String header, String key) {
		if(existHeader(header)) {
			for(String keys : entries.get(header).objects.keySet()) {
				if(key.equals(keys)) return true;
			}
		}
		return false;
	}
	
	public boolean existHeader(String header) {
		return getHeaders().contains(header);
	}
	
	public void set(String header, String key, Object value) {
		if(getHeaders().contains(header)) {
			entries.get(header).set(key, value);
		} else {
			ConfigurationEntry ce = new ConfigurationEntry(this, header);
			ce.set(key, value);
			entries.put(header, ce);
		}
	}
	
	public Object get(String header, String key) {
		if(entries.containsKey(header)) {
			return entries.get(header).get(key);
		}
		return null;
	}
	
	public boolean getBoolean(String header, String key) {
		return Boolean.parseBoolean(get(header, key).toString()); 
	}
	
	public String getString(String header, String key) {
		return get(header, key).toString(); 
	}
	
	public int getInt(String header, String key) {
		return Integer.parseInt(get(header, key).toString());
	}
	
	public long getLong(String header, String key) {
		return Long.parseLong(get(header, key).toString());
	}
	
	public float getFloat(String header, String key) {
		return Float.parseFloat(get(header, key).toString());
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public HashMap<String, ConfigurationEntry> getEntries() {
		return entries;
	}

	public void setEntries(HashMap<String, ConfigurationEntry> entries) {
		this.entries = entries;
	}
}
