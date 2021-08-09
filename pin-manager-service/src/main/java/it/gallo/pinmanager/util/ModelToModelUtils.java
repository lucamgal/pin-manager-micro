package it.gallo.pinmanager.util;

import it.gallo.pinmanager.model.bo.PinVerificationBO;
import it.gallo.pinmanager.repository.model.PinVerification;

public class ModelToModelUtils {

	public static PinVerificationBO fromDocumentToModel(PinVerification document) {
		PinVerificationBO bo = new PinVerificationBO();
		bo.setId(document.getId());
		bo.setPhoneNumber(document.getPhoneNumber());
		bo.setUsername(document.getUsername());
		bo.setPinCode(document.getPinCode());
		bo.setStatus(document.getStatus()!=null ? document.getStatus().name() : null);
		return bo;
	}
}
