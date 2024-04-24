package exceptions;

public class ManagerSaveException extends RuntimeException {
	String error;

	public ManagerSaveException(String error) {
		this.error = error;
	}
}
