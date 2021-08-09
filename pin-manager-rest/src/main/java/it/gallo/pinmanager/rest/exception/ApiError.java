package it.gallo.pinmanager.rest.exception;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {

	private String exception;
	private String url;
	private Instant timestamp;
	private Integer status;

	public ApiError() {}

	public ApiError(String exception, String url, Instant timestamp, Integer status) {
		super();
		this.exception = exception;
		this.url = url;
		this.timestamp = timestamp;
		this.status = status;
	}

}
