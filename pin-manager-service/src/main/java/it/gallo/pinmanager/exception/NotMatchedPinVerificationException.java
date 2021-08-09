package it.gallo.pinmanager.exception;

@SuppressWarnings("serial")
public class NotMatchedPinVerificationException extends RuntimeException {

	public NotMatchedPinVerificationException(String message) {
		super(message);
	}
}
