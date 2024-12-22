package br.com.erudio.hateoas;

import br.com.erudio.exception.UnprocessableEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HateoasLinkAdder<T extends RepresentationModel<T>, C> {

    private final Logger logger = LoggerFactory.getLogger(HateoasLinkAdder.class);

    private final Class<C> controllerClass;

    public HateoasLinkAdder(Class<C> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public void addLinks(T model, Long id) {
        try {
            /**
             model.add(linkTo(methodOn(controllerClass).findById(id)).withSelfRel());
             model.add(linkTo(methodOn(controllerClass).create(model)).withRel("create").withType("POST"));
             model.add(linkTo(methodOn(controllerClass).update(model)).withRel("update").withType("PUT"));
             model.add(linkTo(methodOn(controllerClass).delete(id)).withRel("delete").withType("DELETE"));
             */
        } catch (Exception e) {
            logger.error("Failed to add HATEOAS links", e);
            throw new UnprocessableEntityException("Failed to generate HATEOAS links.");
        }
    }
}
