package br.com.erudio.file.exporter.impl;

import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.file.exporter.FileExportResponse;
import br.com.erudio.file.exporter.contract.FileExporter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfExporter implements FileExporter {

    @Override
    public FileExportResponse exportFile(List<PersonDTO> people) throws Exception {
        // Obtenha o arquivo de template Jasper
        InputStream inputStream = getClass().getResourceAsStream("/templates/people.jrxml");
        if (inputStream == null) {
            throw new RuntimeException("Template file not found: /templates/people.jrxml");
        }

        // Compile o template Jasper, se necessário
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        // Preencha os dados no template
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "People Report"); // Adicione parâmetros ao template, se necessário

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Exporte o relatório para um array de bytes
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            Resource resource = new ByteArrayResource(outputStream.toByteArray());
            return new FileExportResponse(resource, "people_exported.pdf");
        }
    }
}