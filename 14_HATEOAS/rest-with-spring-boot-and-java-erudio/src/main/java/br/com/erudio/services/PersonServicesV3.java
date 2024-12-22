/**
package br.com.erudio.services;

import br.com.erudio.controllers.PersonController;
import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.exception.ResourceNotFoundException;
import br.com.erudio.hateoas.HateoasLinkAdder;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.erudio.mapper.ObjectMapper.parseListObjects;
import static br.com.erudio.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServicesV3 {

    private Logger logger = LoggerFactory.getLogger(PersonServicesV3.class.getName());

    @Autowired
    PersonRepository repository;

    private final HateoasLinkAdder<PersonDTO> hateoasLinkAdder = new HateoasLinkAdder<>(PersonController.class);

    public List<PersonDTO> findAll() {

        logger.info("Finding all People!");

        var persons = parseListObjects(repository.findAll(), PersonDTO.class);
        persons
                .stream()
                .forEach(person -> {
                    hateoasLinkAdder.addLinks(person, person.getId());
                });
        return persons;
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        var entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = parseObject(entity, PersonDTO.class);
        hateoasLinkAdder.addLinks(dto, id);
        return dto;
    }

    public PersonDTO create(PersonDTO person) {

        logger.info("Creating one Person!");
        var entity = parseObject(person, Person.class);
        var dto =  parseObject(repository.save(entity), PersonDTO.class);
        hateoasLinkAdder.addLinks(dto, dto.getId());
        return dto;
    }

    public PersonDTO update(PersonDTO person) {

        logger.info("Updating one Person!");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var dto =  parseObject(repository.save(entity), PersonDTO.class);
        hateoasLinkAdder.addLinks(dto, dto.getId());
        return dto;
    }

    public void delete(Long id) {

        logger.info("Deleting one Person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }
}
*/