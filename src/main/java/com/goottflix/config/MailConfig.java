package com.goottflix.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${MAIL_PASSWORD}")
    private String mailPassword;

    @Bean
    JavaMailSender javaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("yoohwanjoo@gmail.com");
        mailSender.setPassword(mailPassword);


        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // STARTTLS 활성화
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // 신뢰할 호스트 설정
        props.put("mail.debug", "true"); // 필요 시 디버깅 활성화

        mailSender.setJavaMailProperties(props);
        return mailSender;
    }
}