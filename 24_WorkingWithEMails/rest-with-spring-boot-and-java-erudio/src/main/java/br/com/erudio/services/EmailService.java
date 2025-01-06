package br.com.erudio.services;

import br.com.erudio.data.dto.request.EmailRequestDTO;
import br.com.erudio.utils.email.EmailSender;
import br.com.erudio.utils.email.EmailConfigs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfigs emailConfigs;

    public void sendSimpleMail(String to, String subject, String body) {
        emailSender
            .to(to)
            .withSubject(subject)
            .withMessage(body)
            .send(emailConfigs);
    }

    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment) {
        // Criar variável para o arquivo temporário
        File tempFile = null;

        try {
            // Deserializar o JSON para o EmailRequestDTO
            EmailRequestDTO emailRequest = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);

            // Converter MultipartFile para um arquivo temporário
            tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
            attachment.transferTo(tempFile); // Transferir o conteúdo do MultipartFile para o arquivo temporário

            // Enviar o e-mail usando o EmailSender com interface fluente
            emailSender
                    .to(emailRequest.getTo())
                    .withSubject(emailRequest.getSubject())
                    .withMessage(emailRequest.getBody())
                    .attach(tempFile.getAbsolutePath()) // Usar o caminho do arquivo temporário
                    .send(emailConfigs);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing email request.", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing the attachment.", e);
        } finally {
            // Deletar o arquivo temporário após o envio do e-mail
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}