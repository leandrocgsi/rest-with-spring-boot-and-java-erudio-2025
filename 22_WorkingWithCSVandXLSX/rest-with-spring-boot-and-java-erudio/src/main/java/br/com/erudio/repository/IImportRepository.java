package br.com.erudio.repository;

import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.model.Person;

import java.io.InputStream;
import java.util.List;

public interface IImportRepository {

	List<PersonDTO> importFile(InputStream file, String fileName) throws Exception;
    
}