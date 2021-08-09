package it.gallo.pinmanager.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.gallo.pinmanager.repository.model.PinVerification;
import it.gallo.pinmanager.repository.model.VerificationStatus;

public interface PinRepository extends MongoRepository<PinVerification, String> {

	public List<PinVerification> findByPhoneNumberAndStatus(String phoneNumber, VerificationStatus status);

}
