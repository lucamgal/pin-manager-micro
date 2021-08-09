package it.gallo.pinmanager.model.reqres;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PinCreationRequest extends Request {

	@NotBlank
	private String phoneNumber;
}
