package co.edu.iudigital.helpmeiud.services.impl;

import co.edu.iudigital.helpmeiud.services.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements IEmailService {

    @Value("${spring.mail.from}")
    private String from;

    @Autowired
    private JavaMailSender sender;

    @Override
    public boolean sendMail(String mensaje, String email, String asunto) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        try {
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(asunto);
            mimeMessageHelper.setText(mensaje);
            sender.send(message);
            return true;
        } catch (MessagingException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
