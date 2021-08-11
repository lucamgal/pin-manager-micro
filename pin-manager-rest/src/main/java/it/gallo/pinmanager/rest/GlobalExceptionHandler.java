package it.gallo.pinmanager.rest;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import it.gallo.pinmanager.exception.NotFoundPinVerificationException;
import it.gallo.pinmanager.exception.NotMatchedPinVerificationException;
import it.gallo.pinmanager.exception.NotPermittedPinVerificationException;
import it.gallo.pinmanager.exception.TooManyPinVerificationException;
import it.gallo.pinmanager.rest.exception.ApiError;

@ControllerAdvice
public class GlobalExceptionHandler {


	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NotMatchedPinVerificationException.class)
	@ResponseBody ApiError handleNotMatchedPinVerificationException(HttpServletRequest req, Exception ex) {
		return new ApiError(ex.getLocalizedMessage(), req.getRequestURL().toString(), Instant.now(), HttpStatus.BAD_REQUEST.value());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundPinVerificationException.class)
	@ResponseBody ApiError handleNotFoundPinVerificationException(HttpServletRequest req, Exception ex) {
		return new ApiError(ex.getLocalizedMessage(), req.getRequestURL().toString(), Instant.now(), HttpStatus.NOT_FOUND.value());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(NotPermittedPinVerificationException.class)
	@ResponseBody ApiError handleNotPermittedPinVerificationException(HttpServletRequest req, Exception ex) {
		return new ApiError(ex.getLocalizedMessage(), req.getRequestURL().toString(), Instant.now(), HttpStatus.FORBIDDEN.value());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(TooManyPinVerificationException.class)
	@ResponseBody ApiError handleTooManyPinVerificationException(HttpServletRequest req, Exception ex) {
		return new ApiError(ex.getLocalizedMessage(), req.getRequestURL().toString(), Instant.now(), HttpStatus.FORBIDDEN.value());
	}

}
