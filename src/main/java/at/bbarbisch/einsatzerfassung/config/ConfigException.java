package at.bbarbisch.einsatzerfassung.config;

public class ConfigException extends Exception {
	private static final long serialVersionUID = 6092738862149165591L;

	public ConfigException(String message) {
		super(message);
	}
	
	public ConfigException(String message, Throwable t) {
		super(message, t);
	}
}
