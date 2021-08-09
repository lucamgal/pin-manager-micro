package it.gallo.pinmanager.model.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PinVerificationBO {

	private String id;

	private String phoneNumber;

	private String username;

	private String pinCode;

	private String status;

}
