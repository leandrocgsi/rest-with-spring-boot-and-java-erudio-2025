package br.com.erudio.file.exporter.impl;

import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.file.exporter.contract.FileExporter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfExporter implements FileExporter {

    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/templates/people.jrxml");
        if (inputStream == null) {
            throw new RuntimeException("Template file not found: /templates/people.jrxml");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);
        Map<String, Object> parameters = new HashMap<>();
        // parameters.put("title", "People Report");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }

    @Override
    public Resource exportPerson(PersonDTO person) throws Exception {
        // Carrega o template principal
        InputStream mainTemplateStream = getClass().getResourceAsStream("/templates/person.jrxml");
        if (mainTemplateStream == null) {
            throw new RuntimeException("Template file not found: /templates/person.jrxml");
        }

        // Carrega o template do subrelatório
        InputStream subReportStream = getClass().getResourceAsStream("/templates/books.jrxml");
        if (subReportStream == null) {
            throw new RuntimeException("Subreport file not found: /templates/books.jrxml");
        }

        // Compila o subrelatório
        JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

        // Compila o relatório principal
        JasperReport mainReport = JasperCompileManager.compileReport(mainTemplateStream);

        // Gera o QR Code e passa como parâmetro
        InputStream qrCodeStream = generateQRCode(person.getProfileUrl(), 200, 200);

        // Configura o DataSource do subrelatório
        JRBeanCollectionDataSource subReportDataSource = new JRBeanCollectionDataSource(person.getBooks());

        // Configura os parâmetros para o relatório principal
        Map<String, Object> parameters = new HashMap<>();
        // parameters.put("SUBREPORT_DIR", "/templates/"); // Diretório base do subrelatório
        String path = getClass().getResource("/templates/books.jasper").getPath();
        //parameters.put("SUBREPORT_DIR", path);
        parameters.put("SUBREPORT_DIR", "D:/Code/Java/rest-with-spring-boot-and-java-erudio-2025/23_WorkingWithJasperReports/rest-with-spring-boot-and-java-erudio/target/classes/templates/books.jasper");
        parameters.put("booksSubreport", subReport);
        parameters.put("booksDataSource", subReportDataSource);
        parameters.put("QRCodeImage", qrCodeStream); // Passa o QR Code como parâmetro

        // Configura o DataSource do relatório principal
        JRBeanCollectionDataSource mainReportDataSource = new JRBeanCollectionDataSource(
                Collections.singletonList(person));

        // Preenche o relatório principal
        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, mainReportDataSource);

        // Exporta o relatório como PDF e retorna como recurso
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }

    private InputStream generateQRCode(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}