package br.com.erudio.integrationtests.dto.wrappers.xml;

import java.util.List;

import br.com.erudio.integrationtests.dto.PersonDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pagedModelPerson")
public class PagedModelPerson {

	@JsonProperty("content")
	@XmlElement(name = "content")
	private List<PersonDTO> content;

	public PagedModelPerson() {}

	public List<PersonDTO> getContent() {
		return content;
	}

	public void setContent(List<PersonDTO> content) {
		this.content = content;
	}
}
