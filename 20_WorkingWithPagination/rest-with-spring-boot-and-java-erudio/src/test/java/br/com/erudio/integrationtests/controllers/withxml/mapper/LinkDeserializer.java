package br.com.erudio.integrationtests.controllers.withxml.mapper;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.hateoas.Link;

import java.io.IOException;

public class LinkDeserializer extends StdDeserializer<Link> {

    public LinkDeserializer() {
        this(null);
    }

    public LinkDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Link deserialize(com.fasterxml.jackson.core.JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String href = p.readValueAsTree().get("href").toString();
        return Link.of(href);
    }
}
