package com.aivhich.Physics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender getMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("pop3.timeweb.ru");
        mailSender.setPort(587);

        mailSender.setUsername("no-reply@applang.ru");
        mailSender.setPassword("vndq10vz66");

        Properties props = mailSender.getJavaMailProperties();
        props.put("pop3.timeweb.ru", "pop3");
        props.put("mail.pop3.auth", "true");
        props.put("mail.pop3.starttls.enable", "true");
        props.put("mail.debug", "false");

        return mailSender;
    }
}
