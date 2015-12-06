package de.ketrwu.compactconfig.exceptions;

public class ConfigurationLoadException extends Exception {
	public ConfigurationLoadException() { super(); }
	public ConfigurationLoadException(String message) { super(message); }
	public ConfigurationLoadException(String message, Throwable cause) { super(message, cause); }
	public ConfigurationLoadException(Throwable cause) { super(cause); }
}
