package org.etf.unibl.SecureForum.additional.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailSender{

    private final static String DEFAULT_EMAIL_SUBJECT = "Activate Your Account via Link";
    private final static String DEFAULT_EMAIL_FROM = "danilo.vrancic@student.etf.unibl.org";
    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);


    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender)
    {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void send(String to, String email) {
        try{
            String[] emailParts = email.split("\\|\\|\\|"); // Split text and HTML content
            String textContent = emailParts[0];
            String htmlContent = emailParts[1];

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setText(textContent, htmlContent);
            helper.setTo(to);
            helper.setSubject(DEFAULT_EMAIL_SUBJECT);
            helper.setFrom(DEFAULT_EMAIL_FROM);

            mailSender.send(mimeMessage);
        }catch (MessagingException ex)
        {
            LOGGER.error("Failed to send e-mail.", ex);
            throw new IllegalStateException("Failed to send e-mail.", ex);
        }
    }

    public void send(String to, String email, String subject) {
        try{
            String[] emailParts = email.split("\\|\\|\\|"); // Split text and HTML content
            String textContent = emailParts[0];
            String htmlContent = emailParts[1];

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setText(textContent, htmlContent);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(DEFAULT_EMAIL_FROM);

            mailSender.send(mimeMessage);
        }catch (MessagingException ex)
        {
            LOGGER.error("Failed to send e-mail.", ex);
            throw new IllegalStateException("Failed to send e-mail.", ex);
        }
    }

    public void send(String to, String email, String subject, String from) {
        try{
            String[] emailParts = email.split("\\|\\|\\|"); // Split text and HTML content
            String textContent = emailParts[0];
            String htmlContent = emailParts[1];

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setText(textContent, htmlContent);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(from);

            mailSender.send(mimeMessage);
        }catch (MessagingException ex)
        {
            LOGGER.error("Failed to send e-mail.", ex);
            throw new IllegalStateException("Failed to send e-mail.", ex);
        }
    }
}
