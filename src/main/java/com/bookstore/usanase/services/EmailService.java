package com.bookstore.usanase.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;
@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    public String sendVerificationEmail(String to, String verificationCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Verify Your Email");
        helper.setText("Your verification code is: " + verificationCode, true);
        mailSender.send(message);
        return verificationCode;
    }
}
