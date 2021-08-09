package it.gallo.pinmanager.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import it.gallo.pinmanager.configuration.PropertyConfiguration;
import it.gallo.pinmanager.exception.NotFoundPinVerificationException;
import it.gallo.pinmanager.exception.NotMatchedPinVerificationException;
import it.gallo.pinmanager.exception.NotPermittedPinVerificationException;
import it.gallo.pinmanager.exception.TooManyPinVerificationException;
import it.gallo.pinmanager.model.bo.PinVerificationBO;
import it.gallo.pinmanager.model.reqres.PinCreationRequest;
import it.gallo.pinmanager.model.reqres.PinVerificationRequest;
import it.gallo.pinmanager.repository.PinRepository;
import it.gallo.pinmanager.repository.model.PinVerification;
import it.gallo.pinmanager.repository.model.VerificationStatus;
import it.gallo.pinmanager.service.PinService;
import it.gallo.pinmanager.util.ModelToModelUtils;
import it.gallo.pinmanager.util.PinUtils;

@Service
public class PinServiceImpl implements PinService {

	@Autowired
	private PinRepository repository;

	@Autowired
	private PropertyConfiguration propertyConfig;

	@Autowired
	private MessageSource messageSource;

	@Override
	public PinVerificationBO generatePin(PinCreationRequest request) {

		String phoneNumber = request.getPhoneNumber();
		String userName    = request.getUsername();

		List<PinVerification> entities = getOpenVerification(phoneNumber);
		if(isMaxParallelReached(entities)) {
			throw new TooManyPinVerificationException(messageSource.getMessage("errors.too-many-session", new Object[] {phoneNumber}, Locale.getDefault()));
		}

		PinVerification entity = new PinVerification();
		entity.setPhoneNumber(phoneNumber);
		entity.setUsername(userName);
		entity.setPinCode(PinUtils.getPinCode(propertyConfig.getPinLength()));
		entity.setStatus(VerificationStatus.OPEN);
		entity.setTimeStamp(Instant.now());
		entity.setAttemptsNumber(0);
		PinVerification savedEntity = repository.save(entity);

		return ModelToModelUtils.fromDocumentToModel(savedEntity);
	}

	@Override
	public PinVerificationBO verify(String id, PinVerificationRequest request) {

		Optional<PinVerification> optional = repository.findById(id);
		if(optional.isEmpty()) {
			throw new NotFoundPinVerificationException(messageSource.getMessage("errors.not-found", new Object[] {id}, Locale.getDefault()));
		}

		PinVerification entity = optional.get();
		if(isNotOpen(entity)) {
			throw new NotPermittedPinVerificationException(messageSource.getMessage("errors.not-permitted.not-open", null, Locale.getDefault()));
		}

		if(isExpired(entity)) {
			expireSession(entity);
			throw new NotPermittedPinVerificationException(messageSource.getMessage("errors.not-permitted.expired", null, Locale.getDefault()));
		}

		if(isMaxAttemptReached(entity)) {
			throw new NotPermittedPinVerificationException(messageSource.getMessage("errors.not-permitted.max-attempts", null, Locale.getDefault()));
		}

		boolean pinMatch = isPinMatch(request, entity);
		if(!pinMatch) {
			incrementAttempt(entity);
			throw new NotMatchedPinVerificationException(messageSource.getMessage("errors.not-matched", null, Locale.getDefault()));
		}

		PinVerification verifiedSession = verifySession(entity);

		return ModelToModelUtils.fromDocumentToModel(verifiedSession);

	}

	private List<PinVerification> getOpenVerification(String phoneNumber){
		List<PinVerification> results = new ArrayList<>();

		List<PinVerification> entities = repository.findByPhoneNumberAndStatus(phoneNumber, VerificationStatus.OPEN);
		if(CollectionUtils.isNotEmpty(entities)) {
			Map<Boolean, List<PinVerification>> splittedEntitiesMap = entities.stream()
			.collect(Collectors.partitioningBy(e -> isExpired(e)));

			splittedEntitiesMap.get(true)
			.stream()
			.forEach(entity -> expireSession(entity));

			results.addAll(splittedEntitiesMap.get(false));
		}

		return results;
	}

	private boolean isPinMatch(PinVerificationRequest request, PinVerification entity) {
		return StringUtils.equals(request.getPinCode() , entity.getPinCode()) &&
				StringUtils.equals(request.getPhoneNumber(), entity.getPhoneNumber());
	}

	private boolean isMaxParallelReached(List<PinVerification> entities) {
		return CollectionUtils.size(entities) >= propertyConfig.getSessionParallel();
	}

	private boolean isMaxAttemptReached(PinVerification entity) {
		return entity.getAttemptsNumber() == propertyConfig.getSessionAttempts();
	}

	private boolean isNotOpen(PinVerification entity) {
		return !VerificationStatus.OPEN.equals(entity.getStatus());
	}

	private boolean isExpired(PinVerification entity) {
		return Instant.now().isAfter( entity.getTimeStamp().plus(propertyConfig.getSessionTimeout(), ChronoUnit.SECONDS) );
	}

	private PinVerification incrementAttempt(PinVerification entity) {
		entity.setAttemptsNumber( entity.getAttemptsNumber() + 1 );
		return repository.save(entity);
	}

	private PinVerification expireSession(PinVerification entity) {
		return updateStatus(entity, VerificationStatus.EXPIRED);
	}

	private PinVerification verifySession(PinVerification entity) {
		return updateStatus(entity, VerificationStatus.VERIFIED);
	}

	private PinVerification updateStatus(PinVerification entity, VerificationStatus newStatus) {
		entity.setStatus(newStatus);
		return repository.save(entity);
	}


}
