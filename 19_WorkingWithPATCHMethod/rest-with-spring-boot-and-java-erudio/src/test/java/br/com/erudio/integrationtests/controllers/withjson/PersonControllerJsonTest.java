package br.com.erudio.integrationtests.controllers.withjson;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.dto.PersonDTO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
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

        String content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        person = objectMapper.readValue(content, PersonDTO.class);

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

        String content = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        person = objectMapper.readValue(content, PersonDTO.class);

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
        String content = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO patchedPerson = objectMapper.readValue(content, PersonDTO.class);

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
        String content = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        person = objectMapper.readValue(content, PersonDTO.class);

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
        given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void testFindAll() throws JsonProcessingException {
        String content = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        List<PersonDTO> people = objectMapper.readValue(content, new TypeReference<List<PersonDTO>>() {});

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