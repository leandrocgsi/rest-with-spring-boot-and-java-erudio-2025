package br.com.erudio.importer.factory;

import br.com.erudio.importer.impl.CsvImporter;
import br.com.erudio.importer.contract.FileImporter;
import br.com.erudio.importer.impl.XlsxImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileImporterFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public FileImporter getImporter(String fileName) throws Exception {
        if (fileName.endsWith(".xlsx")) {
            // return new XlsxImporter();
            return applicationContext.getBean(XlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            // return new CsvImporter();
            return applicationContext.getBean(CsvImporter.class);
        } else {
            throw new Exception("Invalid Format");
        }
    }

}
