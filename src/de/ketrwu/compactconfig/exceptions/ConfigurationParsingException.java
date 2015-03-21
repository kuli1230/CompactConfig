package de.ketrwu.compactconfig.exceptions;

public class ConfigurationParsingException extends Exception {
	public ConfigurationParsingException() { super(); }
	public ConfigurationParsingException(String message) { super(message); }
	public ConfigurationParsingException(String message, Throwable cause) { super(message, cause); }
	public ConfigurationParsingException(Throwable cause) { super(cause); }
}
