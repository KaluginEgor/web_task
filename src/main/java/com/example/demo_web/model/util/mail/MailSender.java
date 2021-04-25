package com.example.demo_web.model.util.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
    private MimeMessage message;
    private String sendToEmail;
    private String mailSubject;
    private String mailText;
    private Properties properties;
    private static final String MAIL_PROPERTIES = "/property/mail.properties";
    private static final Logger logger = LogManager.getLogger(MailSender.class);

    public MailSender() {
        properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(MAIL_PROPERTIES));
        } catch (IOException e) {
            logger.error("Loading properties for mail sending error",e);
        }
    }

    public MailSender(String mailSubject, String mailText, String sendToEmail) {
        this.sendToEmail = sendToEmail;
        this.mailSubject = mailSubject;
        this.mailText = mailText;
        properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(MAIL_PROPERTIES));
        } catch (IOException e) {
            logger.error("Loading properties for mail sending error",e);
        }
    }

    public void send() {
        try {
            initMessage();
            Transport.send(message); // sending mail
        } catch (AddressException e) {
            logger.error("Invalid address: " + sendToEmail + " " + e); // in log
        } catch (MessagingException e) {
            logger.error("Error generating or sending message: " + e); // in log
        }
    }

    private void initMessage() throws MessagingException {
        // mail session object
        Session mailSession = SessionFactory.createSession(properties);
        mailSession.setDebug(true);
        message = new MimeMessage(mailSession); // create a mailing object
        // loading parameters into the mail message object
        message.setSubject(mailSubject);
        message.setContent(mailText, "text/html");
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendToEmail));
    }
}
