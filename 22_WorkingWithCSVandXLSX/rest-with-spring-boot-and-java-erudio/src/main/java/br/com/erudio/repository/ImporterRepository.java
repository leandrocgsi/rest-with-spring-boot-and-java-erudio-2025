package br.com.erudio.repository;

import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.services.importer.CsvImporter;
import br.com.erudio.services.importer.XlsxImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ImporterRepository implements IImportRepository, Serializable{

    private static final long serialVersionUID = 1L;

    @Autowired
    private XlsxImporter xlsxImporter;

    @Autowired
    private CsvImporter baseQuestionCSVImporter;

    public List<PersonDTO> importFile(InputStream inputStream, String fileName) throws Exception {
        if(fileName.endsWith(".xlsx")) return xlsxImporter.readXLSX(inputStream);
        else if(fileName.endsWith(".csv")) return baseQuestionCSVImporter.readCSV(inputStream);
        else throw new Exception("Invalid Format");
    }
}
