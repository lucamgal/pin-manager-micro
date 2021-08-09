package it.gallo.pinmanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.gallo.pinmanager.model.bo.PinVerificationBO;
import it.gallo.pinmanager.model.reqres.PinCreationRequest;
import it.gallo.pinmanager.model.reqres.PinVerificationRequest;
import it.gallo.pinmanager.service.PinService;

@RestController
@RequestMapping("/pincodes")
public class PinManagerController {

	@Autowired
	private PinService pinService;

	@PostMapping("/")
	public ResponseEntity<PinVerificationBO> generate(@RequestBody @Validated PinCreationRequest request) {

		return new ResponseEntity<>(pinService.generatePin(request), HttpStatus.CREATED);
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<PinVerificationBO> updateStatus(@PathVariable("id") String id,
			@RequestBody @Validated PinVerificationRequest request) {

		return new ResponseEntity<>(pinService.verify(id, request), HttpStatus.OK);
	}
}
