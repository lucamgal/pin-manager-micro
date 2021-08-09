package it.gallo.pinmanager.exception;

@SuppressWarnings("serial")
public class NotFoundPinVerificationException extends RuntimeException {

	public NotFoundPinVerificationException(String message) {
		super(message);
	}

}
