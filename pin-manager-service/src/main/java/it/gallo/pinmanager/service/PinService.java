package it.gallo.pinmanager.service;

import it.gallo.pinmanager.model.bo.PinVerificationBO;
import it.gallo.pinmanager.model.reqres.PinCreationRequest;
import it.gallo.pinmanager.model.reqres.PinVerificationRequest;

public interface PinService {

	PinVerificationBO generatePin(PinCreationRequest request);

	PinVerificationBO verify(String id, PinVerificationRequest request);

}
