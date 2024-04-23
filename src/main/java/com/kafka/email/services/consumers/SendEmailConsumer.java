package com.kafka.email.services.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.email.dtos.SendEmailMessageDto;
import com.kafka.email.services.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service
public class SendEmailConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(
        id = "EmailServiceConsumerGroup",
        topics = {"sendEmail"}
    )

    public void handleSendMail(String message) throws Exception{
        System.out.println("Received send Email message!");

        SendEmailMessageDto messageDto = objectMapper.readValue(message, SendEmailMessageDto.class);

        System.out.println("We will proceed to send email to: "+ messageDto);

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("raov2412@gmail.com", "tfhyzwujkloogkgu");
            }
        };
        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session,
                messageDto.getTo(),
                messageDto.getSubject(),
                messageDto.getBody());

    }
}