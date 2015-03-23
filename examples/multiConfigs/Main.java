package multiConfigs;

import de.ketrwu.compactconfig.MultiConfigHandler;

/**
 * Simple and easy to use configuration system
 * 
 * @author Kenneth Wuﬂmann
 * @version 1.2.0
 */

public class Main {
	
	public static MultiConfigHandler handler = new MultiConfigHandler();
	
	public static void main(String[] args) {
		handler.addConfig("Config 1", "config_1.txt");
		handler.addConfig("Config 2", "config_2.txt");
		
		handler.getConfig("Config 1").set("Insert smart title here", "is this easy", true);
		if(handler.getConfig("Config 1").getBoolean("Insert smart title here", "is this easy")) {
			System.out.println("This is easy!");
		}
		handler.getConfig("Config 2").set("Insert smart title here", "money", 99999999);
		
		//Save all configs
		handler.saveAllConfigs();
		
	}
}
