package br.com.erudio.controllers;

import br.com.erudio.controllers.docs.AuthControllerDocs;
import br.com.erudio.data.dto.security.AccountCredentialsDTO;
import br.com.erudio.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

	@Autowired
	AuthServices authServices;
	
	@SuppressWarnings("rawtypes")
	@Operation(summary = "Authenticates a user and returns a token")
	@PostMapping(value = "/signin")
	@Override
	public ResponseEntity signin(@RequestBody AccountCredentialsDTO credentials) {
		if (checkIfParamsIsNotNull(credentials))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		var token = authServices.signin(credentials);
		if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		return token;
	}

	@SuppressWarnings("rawtypes")
	@Operation(summary = "Refresh token for authenticated user and returns a token")
	@PutMapping(value = "/refresh/{username}")
	@Override
	public ResponseEntity refreshToken(@PathVariable("username") String username,
									   @RequestHeader("Authorization") String refreshToken) {
		if (checkIfParamsIsNotNull(username, refreshToken))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		var token = authServices.refreshToken(username, refreshToken);
		if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		return token;
	}

	@PostMapping(value = "/createUser",
			consumes = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_YAML_VALUE},
			produces = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_YAML_VALUE}
	)
	@Override
	public AccountCredentialsDTO create(@RequestBody AccountCredentialsDTO user) {
		return authServices.create(user);
	}

	private boolean checkIfParamsIsNotNull(String username, String refreshToken) {
		return StringUtils.isBlank(refreshToken) ||
				StringUtils.isBlank(username);
	}

	private boolean checkIfParamsIsNotNull(AccountCredentialsDTO credentials) {
		return credentials == null || StringUtils.isBlank(credentials.getUsername())
			 || StringUtils.isBlank(credentials.getPassword());
	}
}