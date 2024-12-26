package br.com.erudio.integrationtests.controllers.withyaml.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

public class YMLMapper implements ObjectMapper{

    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;
    protected TypeFactory typeFactory;
    
    public YMLMapper() {
        objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new YAMLFactory());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        typeFactory = TypeFactory.defaultInstance();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object deserialize(ObjectMapperDeserializationContext context) {
        try {
            String dataToSerialize = context.getDataToDeserialize().asString();
            
            Class type = (Class) context.getType();
            return objectMapper.readValue(dataToSerialize, typeFactory.constructType(type));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error deserializing YAML content", e);
        }
    }

    @Override
    public Object serialize(ObjectMapperSerializationContext context) {
        try {
            return objectMapper.writeValueAsString(context.getObjectToSerialize());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializing object to YAML", e);
        }
    }
}
