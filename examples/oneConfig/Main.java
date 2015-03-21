package oneConfig;

import de.ketrwu.compactconfig.Configuration;
import de.ketrwu.compactconfig.exceptions.ConfigurationLoadException;
import de.ketrwu.compactconfig.exceptions.ConfigurationParsingException;

public class Main {
	
	public static Configuration config = new Configuration("settings.txt");
	
	public static void main(String[] args) {
		config.set("Settings", "isThisSimple", true);
		if(config.getBoolean("Settings", "isThisSimple")) {
			System.out.println("See how simple CompactConfig is!");
		}
		
		config.set("Settings", "text", "Foo bar!");
		
		config.set("Integers", "testInt", 9599);
		
		//Important: Save the config before terminating the application!
		config.saveConfig();
		
		
		//You can also reload a configuration, if you thing the user changed some values!
		try {
			config.reloadConfig();
		} catch (ConfigurationLoadException | ConfigurationParsingException e) {
			e.printStackTrace();
		}
		
		//You want to remove an entry?
		config.set("Settings", "text", null);
		config.saveConfig();
	}
}
