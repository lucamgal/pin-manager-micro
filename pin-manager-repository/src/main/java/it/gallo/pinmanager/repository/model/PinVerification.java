package it.gallo.pinmanager.repository.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "pinVerifications")
public class PinVerification {

	@Id
	private String id;

	private String phoneNumber;

	private String username;

	private String pinCode;

	private Instant timeStamp;

	private Integer attemptsNumber;

	private VerificationStatus status;
}
