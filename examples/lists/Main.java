package lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.ketrwu.compactconfig.Configuration;

public class Main {
	
	public static Configuration config = new Configuration("settings.txt");
	
	public static void main(String[] args) {
		
		//Since version 1.1.0 you can save and get Lists!
		
		List<String> colors = new ArrayList<String>( Arrays.asList("red", "organge", "blue", "yellow", "white") );
		
		config.set("Settings", "colors", colors);
		
		//Important: Save the config before terminating the application!
		config.saveConfig();
		
		printColors();
		
	}
	
	public static void printColors() {
		for(String color : config.getStringList("Settings", "colors")) 
			System.out.println(color);
		
		/*  Output:
		 *	red
		 *	organge
		 *	blue
		 *	yellow
		 *	white
		 */
	}
}
