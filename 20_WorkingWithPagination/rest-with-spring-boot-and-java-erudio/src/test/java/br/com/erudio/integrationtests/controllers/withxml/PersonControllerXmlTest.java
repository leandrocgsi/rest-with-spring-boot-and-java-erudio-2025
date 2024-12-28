package br.com.erudio.integrationtests.controllers.withxml;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.controllers.withxml.mapper.LinkDeserializer;
import br.com.erudio.integrationtests.dto.PersonDTO;
import br.com.erudio.integrationtests.dto.wrappers.xml.PagedModelPerson;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;

    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new SimpleModule().addDeserializer(Link.class, new LinkDeserializer()));

        person = new PersonDTO();
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

        var content = given(specification)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
                .body(person)
            .when()
                .post()
            .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
            .extract()
                .body()
                    .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());

    }
    
    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        person.setLastName("Benedict Torvalds");

        var content = given(specification)
            .contentType(MediaType.APPLICATION_XML_VALUE)
            .accept(MediaType.APPLICATION_XML_VALUE)
                .body(person)
            .when()
                .put()
            .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
            .extract()
                .body()
                    .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());

    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                    .pathParam("id", person.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void disableTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                    .pathParam("id", person.getId())
                .when()
                    .patch("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void deleteTest() throws JsonProcessingException {

        given(specification)
                .pathParam("id", person.getId())
            .when()
                .delete("{id}")
            .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .queryParams("page", 6 , "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
        var people = wrapper.getContent();

        PersonDTO personOne = people.get(0);

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Anton", personOne.getFirstName());
        assertEquals("Rollo", personOne.getLastName());
        assertEquals("8 Norway Maple Lane", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        assertFalse(personOne.getEnabled());

        PersonDTO personFour = people.get(4);

        assertNotNull(personFour.getId());
        assertTrue(personFour.getId() > 0);

        assertEquals("Ari", personFour.getFirstName());
        assertEquals("Pask", personFour.getLastName());
        assertEquals("8 Darwin Lane", personFour.getAddress());
        assertEquals("Male", personFour.getGender());
        assertTrue(personFour.getEnabled());
    }

    @Test
    @Order(7)
    void hateoasTest() throws JsonProcessingException {

        Response response = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .queryParams("page", 6 , "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .response();

        // Obtém o corpo da resposta como uma string XML
        String xml = response.getBody().asString();

        // Usa XmlPath para fazer as validações no XML
        // Tenho um erro aqui Cannot resolve constructor 'XmlPath()'
        XmlPath xmlPath = new XmlPath(xml);

        // Valida os links HATEOAS dentro de '_embedded.people'
        List<Map<String, String>> peopleLinks = xmlPath.getList("PagedModel.content.content.links");

        for (Map<String, String> links : peopleLinks) {
            // Verifica se os links HATEOAS esperados estão presentes
            assertThat("HATEOAS link 'self' is missing", links, hasKey("self"));
            assertThat("HATEOAS link 'findAll' is missing", links, hasKey("findAll"));
            assertThat("HATEOAS link 'create' is missing", links, hasKey("create"));
            assertThat("HATEOAS link 'update' is missing", links, hasKey("update"));
            assertThat("HATEOAS link 'disable' is missing", links, hasKey("disable"));
            assertThat("HATEOAS link 'delete' is missing", links, hasKey("delete"));

            // Valida o formato das URLs e se o método HTTP associado está presente
            links.forEach((key, value) -> {
                assertThat("HATEOAS link '" + key + "' has an invalid URL", value, matchesPattern("https?://.+/api/person/v1.*"));
                // Certifica-se de que a URL é válida e não nula
                assertThat("HATEOAS link '" + key + "' has an invalid HTTP method", value, notNullValue());
            });
        }

        // Valida os links de navegação da página
        Map<String, String> pageLinks = xmlPath.getMap("PagedModel.links");
        assertThat("Page link 'first' is missing", pageLinks, hasKey("first")); // Link para a primeira página
        assertThat("Page link 'self' is missing", pageLinks, hasKey("self")); // Link para a página atual
        assertThat("Page link 'next' is missing", pageLinks, hasKey("next")); // Link para a próxima página
        assertThat("Page link 'last' is missing", pageLinks, hasKey("last")); // Link para a última página

        // Valida os atributos relacionados à paginação no XML
        Map<String, String> pageAttributes = xmlPath.getMap("page");
        assertThat(pageAttributes.get("size"), is("10")); // Verifica o tamanho da página (10 itens)
        assertThat(pageAttributes.get("number"), is("6")); // Verifica o número da página atual (6)

        // Verifica se os atributos 'totalElements' e 'totalPages' são maiores que zero
        Assertions.assertTrue(Integer.parseInt(pageAttributes.get("totalElements")) > 0, "totalElements deve ser maior que 0");
        Assertions.assertTrue(Integer.parseInt(pageAttributes.get("totalPages")) > 0, "totalPages deve ser maior que 0");
    }

    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki - Finland");
        person.setGender("Male");
        person.setEnabled(true);
    }
}