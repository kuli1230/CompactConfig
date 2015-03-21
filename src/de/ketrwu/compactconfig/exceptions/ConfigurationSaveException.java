package de.ketrwu.compactconfig.exceptions;

public class ConfigurationSaveException extends Exception {
	public ConfigurationSaveException() { super(); }
	public ConfigurationSaveException(String message) { super(message); }
	public ConfigurationSaveException(String message, Throwable cause) { super(message, cause); }
	public ConfigurationSaveException(Throwable cause) { super(cause); }
}
