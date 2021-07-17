package at.bbarbisch.einsatzerfassung.data;

public class DataException extends Exception {
	private static final long serialVersionUID = 8965418103375358898L;

	public DataException(String message) {
		super(message);
	}
	
	public DataException(String message, Throwable t) {
		super(message, t);
	}
}
