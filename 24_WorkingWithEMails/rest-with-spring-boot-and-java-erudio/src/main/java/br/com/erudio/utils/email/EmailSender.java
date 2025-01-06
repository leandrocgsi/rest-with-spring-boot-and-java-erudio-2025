package br.com.erudio.utils.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

@Component
public class EmailSender implements Serializable {

    private final JavaMailSender mailSender;
    private String to;
    private String subject;
    private String body;
    private ArrayList<InternetAddress> recipients = new ArrayList<>();
    private File attachment;

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public EmailSender to(String to) {
        this.to = to;
        this.recipients = getRecipients(to);
        return this;
    }

    public EmailSender withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailSender withMessage(String body) {
        this.body = body;
        return this;
    }

    public EmailSender attach(String fileDir) {
        this.attachment = new File(fileDir);
        return this;
    }

    public void send(EmailConfigs configs) {

        /**
        System.out.println("Host: " + configs.getHost());
        System.out.println("Port: " + configs.getPort());
        System.out.println("Username: " + configs.getUsername());
        System.out.println("Password: " + configs.getPassword());
        */

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(configs.getUsername()); // Usando o 'from' das configurações
            helper.setTo(recipients.toArray(new InternetAddress[0]));
            helper.setSubject(subject);
            helper.setText(body, true);
            if (attachment != null) {
                helper.addAttachment(attachment.getName(), attachment);
            }
            mailSender.send(message);
            System.out.printf("Email sent to %s with the subject '%s'%n", to, subject);

            // Resetar o estado após o envio bem-sucedido
            reset();
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending the email", e);
        }
    }

    private void reset() {
        this.to = null;
        this.subject = null;
        this.body = null;
        this.recipients.clear();
        this.attachment = null;
    }

    private ArrayList<InternetAddress> getRecipients(String to) {
        String tosWithoutSpaces = to.replaceAll("\\s", "");
        StringTokenizer tok = new StringTokenizer(tosWithoutSpaces, ";");
        ArrayList<InternetAddress> recipientList = new ArrayList<>();
        while (tok.hasMoreElements()) {
            try {
                recipientList.add(new InternetAddress(tok.nextElement().toString()));
            } catch (Exception e) {
                // Log ou tratar a exceção para endereços de e-mail inválidos
            }
        }
        return recipientList;
    }
}