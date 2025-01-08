package br.com.erudio.integrationtests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.dto.AccountCredentialsDTO;
import br.com.erudio.integrationtests.dto.TokenDTO;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest {

	private static TokenDTO tokenDTO;
	
	@Test
	@Order(1)
	public void testSignin() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsDTO user =
				new AccountCredentialsDTO("leandro", "admin123");
		
		tokenDTO = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(user)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenDTO.class);
		
		assertNotNull(tokenDTO.getAccessToken());
		assertNotNull(tokenDTO.getRefreshToken());
	}
	
	@Test
	@Order(2)
	public void testRefresh() throws JsonMappingException, JsonProcessingException {
		
		var newTokenDTO = given()
				.basePath("/auth/refresh")
				.port(TestConfigs.SERVER_PORT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
					.pathParam("username", tokenDTO.getUsername())
					.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
				.when()
					.put("{username}")
				.then()
					.statusCode(200)
				.extract()
					.body()
						.as(TokenDTO.class);
		
		assertNotNull(newTokenDTO.getAccessToken());
		assertNotNull(newTokenDTO.getRefreshToken());
	}
}
