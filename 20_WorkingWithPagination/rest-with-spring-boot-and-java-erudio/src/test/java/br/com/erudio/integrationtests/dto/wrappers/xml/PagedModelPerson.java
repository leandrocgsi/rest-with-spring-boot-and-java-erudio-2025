package br.com.erudio.integrationtests.dto.wrappers.xml;

import java.util.List;

import br.com.erudio.integrationtests.dto.PersonDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelPerson { 
	
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
