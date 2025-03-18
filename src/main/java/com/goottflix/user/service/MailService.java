package com.goottflix.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    public String sendVerificationCode(String email) throws MessagingException {

        // 6자리 인증번호 생성
        String verificationCode = String.format("%06d", new Random().nextInt(1000000));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        System.out.println("여기는 MailService입니다. email = " + email);

        helper.setFrom("yoohwanjoo@gmail.com");
        helper.setTo(email);
        helper.setSubject("구트플릭스 회원가입을 위한 인증번호입니다.");

        String htmlContent = "<div style='text-align: center; font-family: Arial, sans-serif;'>"
                + "<h2>확인 코드</h2>"
                + "<h1 style='font-size: 48px; color: #333;'>" + verificationCode + "</h1>"
                + "<p style='color: #666;'>이 코드는 전송 10분 후에 만료됩니다.</p>"
                + "<hr style='margin: 20px 0;'>"
                + "<p style='font-size: 14px; color: #666;'>gootFlix Services는 절대 사용자 암호, 신용카드 또는 은행 계좌 번호를 요구하지 않습니다...</p>"
                + "</div>";

        helper.setText(htmlContent, true);  // 두 번째 매개변수는 HTML 설정
        mailSender.send(message);

        return verificationCode;
    }

    @Value("${REACTENDPOINT}")
    private String reactEndpointUrl;

    public void sendPasswordResetEmail(String email, String loginId, String token) throws MessagingException {
        String resetUrl = reactEndpointUrl+"/auth/reset?token=" + token;

        // MimeMessage 사용하여 HTML 이메일 전송
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(email);
        helper.setSubject("GoottFlix 회원정보 및 비밀번호 재설정 이메일입니다.");

        // 이메일 본문에 HTML 사용
        String content = "<div style=\"text-align: center; font-family: Arial, sans-serif;\">"
                + "<h2 style=\"color: #333;\">계정 찾기</h2>"
                + "<p style=\"font-size: 16px; color: #555;\">로그인 아이디: <strong>" + loginId + "</strong></p>"
                + "<p style=\"font-size: 16px; color: #555;\">비밀번호 변경을 원하시면 아래 링크를 통해 변경하실 수 있습니다.</p>"
                + "<p style=\"font-size: 16px; color: #555;\">해당 링크를 통한 비밀번호 재설정 가능시간은 5분입니다.</p>"
                + "<a href=\"" + resetUrl + "\" style=\""
                + "display: inline-block; "
                + "padding: 15px 30px; "
                + "background-color: #3498db; "
                + "color: #ffffff; "
                + "text-decoration: none; "
                + "border-radius: 5px; "
                + "font-size: 18px;\">비밀번호 변경하기</a>"
                + "<p style=\"margin-top: 20px; font-size: 12px; color: #888;\">gootFlix Services는 절대 사용자 암호, 신용카드 또는 은행 계좌 번호를 요구하지 않습니다...</p>"
                + "</div>";


        // HTML로 설정
        helper.setText(content, true);

        mailSender.send(message);
    }
}
