package br.com.erudio.services.importer;

import br.com.erudio.model.Person;

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
public class CSVImporter {

    public List<Person> readCSV(InputStream input) {
        try {
            // Configure the CSV parser with standard delimiter (comma)
            Iterable<CSVRecord> records = CSVFormat.RFC4180
                    .withFirstRecordAsHeader() // Skip the first line as a header
                    .withIgnoreEmptyLines() // Ignore empty lines
                    .withTrim() // Trim whitespace from entries
                    .parse(new InputStreamReader(input));

            return parseRecordsToPersons(records);
        } catch (IOException e) {
            throw new ApplicationContextException("Error processing the CSV file: " + e.getMessage(), e);
        }
    }

    private List<Person> parseRecordsToPersons(Iterable<CSVRecord> records) {
        List<Person> persons = new ArrayList<>();
        for (CSVRecord record : records) {
            persons.add(parseRecordToPerson(record));
        }
        return persons;
    }

    private Person parseRecordToPerson(CSVRecord record) {
        try {
            Person person = new Person();
            person.setFirstName(record.get("name")); // Replace with the actual column names in your header
            person.setLastName(record.get("email"));
            person.setAddress(record.get("phone"));
            person.setGender(record.get("cpf"));
            person.setEnabled(true);
            // Add more fields as necessary
            return person;
        } catch (IllegalArgumentException e) {
            throw new ApplicationContextException("Error processing the record: " + record.toString(), e);
        }
    }
}
