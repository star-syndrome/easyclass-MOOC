package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.request.EmailRequest;
import org.binaracademy.finalproject.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

@Service
@Slf4j
public class EmailServiceImplements implements EmailService {

    @Value("${email.account}")
    private String emailAccount;

    @Value("${email.password}")
    private String emailPassword;

    @Value("${email.host}")
    private String host;

    @Value("${email.port}")
    private String port;

    @Value("${email.auth}")
    private String isAuth;

    @Value("${email.tls}")
    private String isTLS;

    @Value("${ssl.trust}")
    private String sslTrust;

    @Override
    public void sendEmail(EmailRequest emailRequest) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", isAuth);
        prop.put("mail.smtp.starttls.enable", isTLS);
        prop.put("mail.smtp.ssl.trust", sslTrust);
        Session session = Session.getDefaultInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailAccount, emailPassword);
                    }
                });
        //compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(emailRequest.getRecipient()));
            message.setSubject(emailRequest.getSubject());
            message.setText(emailRequest.getContent());
            //send message
            Transport.send(message);
            log.info("Message sent successfully to {}", emailRequest.getRecipient());
        } catch (MessagingException e) {throw new RuntimeException(e);}
    }
}