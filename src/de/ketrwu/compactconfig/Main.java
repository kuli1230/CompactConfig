package de.ketrwu.compactconfig;


public class Main {
	public static void main(String[] args) {
		Configuration config = new Configuration("testconfig.txt");
		for(Integer value : config.getIntegerList("List-Test", "Integer-Liste")) {
			System.out.println(value);
		}
		config.saveConfig();
	}
}
