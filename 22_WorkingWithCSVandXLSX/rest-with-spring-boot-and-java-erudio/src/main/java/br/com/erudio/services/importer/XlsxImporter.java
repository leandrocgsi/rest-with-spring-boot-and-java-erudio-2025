package br.com.erudio.services.importer;

import br.com.erudio.data.dto.PersonDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class XlsxImporter {

    public List<PersonDTO> readXLSX(InputStream input) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(input)) {
            XSSFSheet sheet = workbook.getSheetAt(0); // Get the first sheet
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) rowIterator.next();

            return parseRowsToPersonDTOList(rowIterator);
        } catch (Exception e) {
            throw new ApplicationContextException("Error processing the XLSX file: " + e.getMessage(), e);
        }
    }

    private List<PersonDTO> parseRowsToPersonDTOList(Iterator<Row> rowIterator) {
        List<PersonDTO> persons = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isRowValid(row)) {
                persons.add(parseRowToPersonDTO(row));
            }
        }
        return persons;
    }

    private boolean isRowValid(Row row) {
        // Check if required fields are not blank
        return isCellNotBlank(row, 0) && isCellNotBlank(row, 1)
                && isCellNotBlank(row, 2) && isCellNotBlank(row, 3);
    }

    private boolean isCellNotBlank(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return cell != null && cell.getCellType() != CellType.BLANK;
    }

    private PersonDTO parseRowToPersonDTO(Row row) {
        PersonDTO person = new PersonDTO();

        person.setFirstName(row.getCell(0) != null ? row.getCell(0).getStringCellValue() : null); // First Name
        person.setLastName(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : null);  // Last Name
        person.setAddress(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : null);   // Address
        person.setGender(row.getCell(3) != null ? row.getCell(3).getStringCellValue() : null);    // Gender
        person.setEnabled(true); // Default to enabled

        return person;
    }

}
