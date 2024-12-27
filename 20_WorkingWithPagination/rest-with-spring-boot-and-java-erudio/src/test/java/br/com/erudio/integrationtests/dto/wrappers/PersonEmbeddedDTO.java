package br.com.erudio.integrationtests.dto.wrappers;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.erudio.integrationtests.dto.PersonDTO;

public class PersonEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("people")
    private List<PersonDTO> persons;

    public PersonEmbeddedDTO() {}

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDTO> persons) {
        this.persons = persons;
    }
}
