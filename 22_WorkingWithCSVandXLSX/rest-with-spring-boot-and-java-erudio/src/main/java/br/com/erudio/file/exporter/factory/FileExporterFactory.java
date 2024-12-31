package br.com.erudio.file.exporter.factory;

import br.com.erudio.file.MediaTypes;
import br.com.erudio.file.exporter.contract.FileExporter;
import br.com.erudio.file.exporter.impl.CsvExporter;
import br.com.erudio.file.exporter.impl.XlsxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileExporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileExporterFactory.class.getName());

    @Autowired
    private ApplicationContext applicationContext;

    public FileExporter getExporter(String acceptHeader) throws Exception {
        if (MediaTypes.APPLICATION_XLSX_VALUE.equalsIgnoreCase(acceptHeader)) {
            return applicationContext.getBean(XlsxExporter.class);
        } else if (MediaTypes.APPLICATION_CSV_VALUE.equalsIgnoreCase(acceptHeader)) {
            return applicationContext.getBean(CsvExporter.class);
        } else {
            throw new Exception("Invalid Format");
        }
    }

}
