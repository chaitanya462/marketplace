package com.simplify.marketplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

//@Component
public class EmailNotificationService {

    @Autowired
    MailService mailservice;

    //    private JavaMailSender javaMailSender = new JavaMailSenderImpl();
    //
    //	public void sendEmail() {
    //
    //        SimpleMailMessage msg = new SimpleMailMessage();
    //        msg.setTo("chaitanyaprasad794@gmail.com");
    //        msg.setFrom("simplifymarketplace0@gmail.com");
    //        msg.setSubject("Testing from Spring Boot");
    //        msg.setText("Hello World \n Spring Boot Email");
    //
    //        System.out.println("\n\n\n\n\n"+javaMailSender+"\n\n\n\n\n");
    //
    //        javaMailSender.send(msg);
    //
    //    }

    public void sendMail() {
        mailservice.sendEmail("chaitanyaprasad794@gmail.com", "welcome mail", "click here to sign IN", false, false);
    }
}
