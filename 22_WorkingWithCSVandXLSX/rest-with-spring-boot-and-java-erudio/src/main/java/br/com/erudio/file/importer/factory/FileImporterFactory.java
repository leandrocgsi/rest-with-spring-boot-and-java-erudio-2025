package br.com.erudio.file.importer.factory;

import br.com.erudio.file.importer.impl.CsvImporter;
import br.com.erudio.file.importer.contract.FileImporter;
import br.com.erudio.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileImporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileImporterFactory.class.getName());

    @Autowired
    private ApplicationContext applicationContext;

    public FileImporter getImporter(String fileName) throws Exception {
        if (fileName.endsWith(".xlsx")) {
            // return new XlsxImporter();
            logger.info("Using XlsxImporter for file: {}", fileName);

            return applicationContext.getBean(XlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            // return new CsvImporter();
            logger.info("Using CsvImporter for file: {}", fileName);
            return applicationContext.getBean(CsvImporter.class);
        } else {
            throw new Exception("Invalid Format");
        }
    }

}
