package br.com.erudio.controllers;

import br.com.erudio.controllers.docs.EmailControllerDocs;
import br.com.erudio.data.dto.request.EmailRequestDTO;
import br.com.erudio.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email/v1")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService emailService;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequest) {
        emailService.sendSimpleMail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
        return new ResponseEntity<>("E-mail enviado com sucesso!", HttpStatus.OK);
    }

    @PostMapping(value = "/with-attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(
            @RequestParam("emailRequest") String emailRequestJson,
            @RequestParam("attachment") MultipartFile attachment) {

        // Enviar o e-mail com anexo, delegando a lógica ao serviço
        emailService.sendEmailWithAttachment(emailRequestJson, attachment);

        return new ResponseEntity<>("Email with attachment sent successfully!", HttpStatus.OK);
    }
}
