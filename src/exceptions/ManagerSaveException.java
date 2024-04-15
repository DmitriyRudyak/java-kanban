package exceptions;

import java.io.IOException;

public class ManagerSaveException extends Exception {
	public ManagerSaveException() {
		super();
	}

	public ManagerSaveException(IOException e) {
	}

	@Override
	public String getMessage() {
		return "Error";
	}
}
