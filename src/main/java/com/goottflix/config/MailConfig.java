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
        mailSender.setHost("smtp.mail.nate.com");
        mailSender.setPort(465);
        mailSender.setUsername("yoohwanjoo@nate.com");
        mailSender.setPassword(mailPassword);


        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true"); // SSL 활성화
        props.put("mail.smtp.starttls.enable", "false"); // 465 포트에서는 STARTTLS 사용 X
        props.put("mail.smtp.ssl.trust", "smtp.mail.nate.com"); // SSL 신뢰 설정
        props.put("mail.debug", "true"); // 디버깅 로그 활성화 (필요 시)

        mailSender.setJavaMailProperties(props);
        return mailSender;
    }
}