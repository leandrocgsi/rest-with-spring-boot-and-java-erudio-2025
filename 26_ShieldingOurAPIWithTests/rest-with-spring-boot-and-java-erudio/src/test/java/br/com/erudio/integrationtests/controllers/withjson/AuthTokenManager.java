package br.com.erudio.integrationtests.controllers.withjson;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.dto.AccountCredentialsDTO;
import br.com.erudio.integrationtests.dto.TokenDTO;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

public class AuthTokenManager {

    private static TokenDTO tokenDTO;

    public static TokenDTO getToken(String contentType) {
        if (tokenDTO == null) {
            AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");
            tokenDTO = given()
                    .basePath("/auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    // .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(contentType)
                    .body(user)
                    .when()
                    .post()
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(TokenDTO.class);
        }
        return tokenDTO;
    }
}
