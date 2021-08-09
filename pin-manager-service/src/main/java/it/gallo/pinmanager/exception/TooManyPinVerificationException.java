package it.gallo.pinmanager.exception;

@SuppressWarnings("serial")
public class TooManyPinVerificationException extends RuntimeException {

	public TooManyPinVerificationException(String message) {
		super(message);
	}
}
