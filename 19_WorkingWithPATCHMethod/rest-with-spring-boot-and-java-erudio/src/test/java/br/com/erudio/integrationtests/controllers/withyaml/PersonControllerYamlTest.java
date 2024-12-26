package br.com.erudio.integrationtests.controllers.withyaml;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.controllers.withyaml.mapper.YMLMapper;
import br.com.erudio.integrationtests.dto.PersonDTO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YMLMapper objectMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        objectMapper = new YMLMapper();
        person = new PersonDTO();
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        person = given().config(RestAssuredConfig
                        .config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(person, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PersonDTO.class, objectMapper);

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Linus", person.getFirstName());
        assertEquals("Torvalds", person.getLastName());
        assertEquals("Helsinki - Finland", person.getAddress());
        assertEquals("Male", person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(2)
    void testUpdate() throws JsonProcessingException {
        person.setLastName("Benedict Torvalds");

        person = given().config(RestAssuredConfig
                        .config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(person, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PersonDTO.class, objectMapper);

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Linus", person.getFirstName());
        assertEquals("Benedict Torvalds", person.getLastName());
        assertEquals("Helsinki - Finland", person.getAddress());
        assertEquals("Male", person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(3)
    void testDisablePerson() throws JsonProcessingException {
        var patchedPerson = given().config(RestAssuredConfig
                        .config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PersonDTO.class, objectMapper);

        assertNotNull(patchedPerson);
        assertNotNull(patchedPerson.getId());
        assertEquals("Linus", patchedPerson.getFirstName());
        assertEquals("Benedict Torvalds", patchedPerson.getLastName());
        assertEquals("Helsinki - Finland", patchedPerson.getAddress());
        assertEquals("Male", patchedPerson.getGender());
        assertFalse(patchedPerson.getEnabled());
    }

    @Test
    @Order(4)
    void testFindById() throws JsonProcessingException {
        person = given().config(RestAssuredConfig
                        .config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PersonDTO.class, objectMapper);

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Linus", person.getFirstName());
        assertEquals("Benedict Torvalds", person.getLastName());
        assertEquals("Helsinki - Finland", person.getAddress());
        assertEquals("Male", person.getGender());
        assertFalse(person.getEnabled());
    }

    @Test
    @Order(5)
    void testDelete() {

        given().config(RestAssuredConfig
                    .config()
                    .encoderConfig(EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
            .spec(specification)
            .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void testFindAll() throws JsonProcessingException {
        var response = given().config(RestAssuredConfig
                        .config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PersonDTO[].class, objectMapper);


        List<PersonDTO> people = Arrays.asList(response);

        PersonDTO foundPersonOne = people.get(0);
        assertNotNull(foundPersonOne);
        assertNotNull(foundPersonOne.getId());
        assertEquals("Ayrton", foundPersonOne.getFirstName());
        assertEquals("Senna", foundPersonOne.getLastName());
        assertEquals("São Paulo - Brasil", foundPersonOne.getAddress());
        assertEquals("Male", foundPersonOne.getGender());
        assertTrue(foundPersonOne.getEnabled());

        PersonDTO foundPersonFour = people.get(4);
        assertNotNull(foundPersonFour);
        assertNotNull(foundPersonFour.getId());
        assertEquals("Muhamamd", foundPersonFour.getFirstName());
        assertEquals("Ali", foundPersonFour.getLastName());
        assertEquals("Kentucky - US", foundPersonFour.getAddress());
        assertEquals("Male", foundPersonFour.getGender());
        assertTrue(foundPersonFour.getEnabled());
    }

    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki - Finland");
        person.setGender("Male");
        person.setEnabled(true);
    }
}