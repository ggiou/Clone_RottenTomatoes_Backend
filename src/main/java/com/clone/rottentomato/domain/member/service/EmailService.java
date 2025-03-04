package com.clone.rottentomato.domain.member.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender; // spring.mail.username 값 주입


    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // HTML 설정 및 인코딩 설정

            helper.setFrom(sender); // 발신자 설정 (중요)
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // HTML 내용 설정

            javaMailSender.send(message);

            log.info("이메일 전송 성공 - 발신자: {}, 수신자: {}, 제목: {}", sender, to, subject);

        } catch (MailException | MessagingException ex) {
            log.error("이메일 전송 실패 - 발신자: {}, 수신자: {}에게 {} 제목의 메일 전송 중 오류 발생: {}", sender, to, subject, ex.getMessage(), ex);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
        }
    }


}
