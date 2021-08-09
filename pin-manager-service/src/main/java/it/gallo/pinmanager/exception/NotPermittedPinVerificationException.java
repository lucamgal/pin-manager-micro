package it.gallo.pinmanager.exception;

@SuppressWarnings("serial")
public class NotPermittedPinVerificationException extends RuntimeException {

	public NotPermittedPinVerificationException(String message) {
		super(message);
	}
}
