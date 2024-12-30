package br.com.erudio.services.importer;

import br.com.erudio.data.dto.PersonDTO;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImporter {

    public List<PersonDTO> readCSV(InputStream input) {
        try {
            // Configura o CSVFormat usando o Builder recomendado
            CSVFormat csvFormat = CSVFormat.Builder.create()
                    .setHeader() // Define que a primeira linha será usada como cabeçalho
                    .setSkipHeaderRecord(true) // Ignora o registro de cabeçalho
                    .setIgnoreEmptyLines(true) // Ignora linhas vazias
                    .setTrim(true) // Remove espaços em branco de entradas
                    .build();

            // Faz o parsing do arquivo de entrada
            Iterable<CSVRecord> records = csvFormat.parse(new InputStreamReader(input));

            return parseRecordsToPersonDTOs(records);
        } catch (IOException e) {
            throw new ApplicationContextException("Error processing the CSV file: " + e.getMessage(), e);
        }
    }

    private List<PersonDTO> parseRecordsToPersonDTOs(Iterable<CSVRecord> records) {
        List<PersonDTO> persons = new ArrayList<>();
        for (CSVRecord record : records) {
            persons.add(parseRecordToPersonDTO(record));
        }
        return persons;
    }

    private PersonDTO parseRecordToPersonDTO(CSVRecord record) {
        try {
            PersonDTO person = new PersonDTO();
            person.setFirstName(record.get("name")); // Replace with the actual column names in your header
            person.setLastName(record.get("email"));
            person.setAddress(record.get("phone"));
            person.setGender(record.get("cpf"));
            person.setEnabled(true);
            return person;
        } catch (IllegalArgumentException e) {
            throw new ApplicationContextException("Error processing the record: " + record.toString(), e);
        }
    }
}
