package br.com.erudio.repository;

import br.com.erudio.model.Person;

import java.io.InputStream;
import java.util.List;

public interface IImportRepository {

	List<Person> importFile(InputStream file, String fileName) throws Exception;
    
}