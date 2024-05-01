package exceptions;

public class ManagerSaveException extends RuntimeException {
	private final String error;

	public ManagerSaveException(String error) {
		this.error = error;
	}
}
