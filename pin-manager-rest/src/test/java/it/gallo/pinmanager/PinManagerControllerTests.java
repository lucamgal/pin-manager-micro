package it.gallo.pinmanager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import it.gallo.pinmanager.configuration.PropertyConfiguration;
import it.gallo.pinmanager.model.bo.PinVerificationBO;
import it.gallo.pinmanager.model.reqres.PinCreationRequest;
import it.gallo.pinmanager.model.reqres.PinVerificationRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PinManagerControllerTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int randomServerPort;

	@Autowired
	private PropertyConfiguration propertyConfig;

	@Test
	public void createPinAndVerify() throws Exception {

		String username = "user1";
		String phoneNumber = "3331112223";

		ResponseEntity<PinVerificationBO> response1 = createPin(username, phoneNumber);
		Assertions.assertEquals(response1.getStatusCode(), HttpStatus.CREATED);

		PinVerificationBO pinVerification = response1.getBody();
		Assertions.assertNotNull(pinVerification.getPinCode(), "Pin code not generated!");

		ResponseEntity<Object> response2 = verifyPin(pinVerification.getId(), username, phoneNumber, pinVerification.getPinCode());
		Assertions.assertEquals(response2.getStatusCode(), HttpStatus.OK);

	}


	@Test
	public void tooManyParallelSession() throws Exception {

		String username = "user2";
		String phoneNumber = "3334445556";

		Integer sessionParallel = propertyConfig.getSessionParallel();
		for(int i = 0; i < sessionParallel; i++) {
			ResponseEntity<PinVerificationBO> response1 = createPin(username, phoneNumber);
			Assertions.assertEquals(response1.getStatusCode(), HttpStatus.CREATED);
		}

		ResponseEntity<PinVerificationBO> response1 = createPin(username, phoneNumber);
		Assertions.assertEquals(response1.getStatusCode(), HttpStatus.FORBIDDEN);

	}


	@Test
	public void maxAttempts() throws Exception {

		String username = "user3";
		String phoneNumber = "3336667778";

		ResponseEntity<PinVerificationBO> response1 = createPin(username, phoneNumber);
		Assertions.assertEquals(response1.getStatusCode(), HttpStatus.CREATED);

		PinVerificationBO pinVerification = response1.getBody();
		String id = pinVerification.getId();
		String pinCode = pinVerification.getPinCode();

		Integer sessionAttempts = propertyConfig.getSessionAttempts();
		for(int i = 0; i < sessionAttempts; i++) {
			ResponseEntity<Object> response2 = verifyPin(id, username, phoneNumber, pinCode+"0");
			Assertions.assertEquals(response2.getStatusCode(), HttpStatus.BAD_REQUEST);
		}

		ResponseEntity<Object> response3 = verifyPin(id, username, phoneNumber, pinCode);
		Assertions.assertEquals(response3.getStatusCode(), HttpStatus.FORBIDDEN);
	}


	@Test
	public void timeOut() throws Exception {

		String username = "user4";
		String phoneNumber = "3338889990";

		ResponseEntity<PinVerificationBO> response1 = createPin(username, phoneNumber);
		Assertions.assertEquals(response1.getStatusCode(), HttpStatus.CREATED);

		Long sessionAttempts = propertyConfig.getSessionTimeout();
		Thread.sleep(sessionAttempts*1000);

		PinVerificationBO pinVerification = response1.getBody();
		ResponseEntity<Object> response2 = verifyPin(pinVerification.getId(), username, phoneNumber, pinVerification.getPinCode());
		Assertions.assertEquals(response2.getStatusCode(), HttpStatus.FORBIDDEN);

	}


	private ResponseEntity<PinVerificationBO> createPin(String username, String phoneNumber) {
		PinCreationRequest pinCreationRequest = new PinCreationRequest();
		pinCreationRequest.setUsername(username);
		pinCreationRequest.setPhoneNumber(phoneNumber);

		HttpEntity<PinCreationRequest> request1 = new HttpEntity<>(pinCreationRequest, getHeaders());

		ResponseEntity<PinVerificationBO> response1 = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/pincodes/",
						HttpMethod.POST,
						request1,
						PinVerificationBO.class);
		return response1;
	}

	private ResponseEntity<Object> verifyPin(String id, String username, String phoneNumber, String pinCode) {
		PinVerificationRequest pinVerificationRequest = new PinVerificationRequest();
		pinVerificationRequest.setUsername("user1");
		pinVerificationRequest.setPhoneNumber("3331112223");
		pinVerificationRequest.setPinCode(pinCode);

		HttpEntity<PinVerificationRequest> request2 = new HttpEntity<>(pinVerificationRequest, getHeaders());

		ResponseEntity<Object> response2 = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/pincodes/"+id+"/status",
				HttpMethod.PUT,
				request2,
				Object.class);
		return response2;
	}


	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
