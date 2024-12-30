package br.com.erudio.services.importer;

import br.com.erudio.model.Person;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class XLSXImporter {

    public List<Person> readXLSX(InputStream input) {
        try {
            XSSFWorkbook wBook = new XSSFWorkbook(input);
            XSSFSheet sheet = wBook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            
            List<Person> persons = getPersonsList(rowIterator);
//            wBook.close();
            return persons;
        } catch (Exception ioe) {
            ioe.printStackTrace();
            throw new ApplicationContextException("Loadfile Error");
        }
    }

    private List<Person> getPersonsList(Iterator<Row> rowIterator) {
        Row row;
        List<Person> persons = new ArrayList<Person>();
        while (rowIterator.hasNext()) {
            row = rowIterator.next();

            if(row.getCell(0) != null && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK &&
               row.getCell(1) != null && row.getCell(1).getCellType() != Cell.CELL_TYPE_BLANK &&
               row.getCell(2) != null && row.getCell(2).getCellType() != Cell.CELL_TYPE_BLANK &&
               row.getCell(3) != null && row.getCell(3).getCellType() != Cell.CELL_TYPE_BLANK) {

                Person person = getPerson(row);
                persons.add(person);
            }
        }
        return persons;
    }

    private Person getPerson(Row row) {
        Person person = new Person();
        person.setName(row.getCell(0)!=null?row.getCell(0).getStringCellValue():null);
        person.setEmail(row.getCell(1)!=null?row.getCell(1).getStringCellValue():null);
        person.setPhone(row.getCell(2)!=null?row.getCell(2).getStringCellValue():null);
        person.setCpf(row.getCell(3)!=null?row.getCell(3).getStringCellValue():null);
//                    person.setBirthDayDate(row.getCell(4)!=null?row.getCell(4).getStringCellValue():null);
        person.setLogin(row.getCell(5)!=null?row.getCell(5).getStringCellValue():null);
        person.setPassword(row.getCell(6)!=null?row.getCell(6).getStringCellValue():null);
        person.setPermission(row.getCell(7)!=null?row.getCell(7).getStringCellValue():null);
//                    person.setGender(row.getCell(8)!=null?row.getCell(8).getStringCellValue():null);
        return person;
    }
}
