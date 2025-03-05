package com.clone.rottentomato.domain.member.service;

import com.clone.rottentomato.common.component.dto.CommonResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service

public class EmailService {
    private final JavaMailSender javaMailSender;
      // Ensure that JavaMailSender is properly configured in the application context by adding a MailConfiguration class.

    //@Value("${spring.mail.username}")
    private String sender; // spring.mail.username 값 주입

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public CommonResponse sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // HTML 설정 및 인코딩 설정

            sender = "REMOVED_SECRET_EMAIL";

            helper.setFrom(sender); // 발신자 설정 (중요)
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // HTML 내용 설정

            javaMailSender.send(message);

            log.info("이메일 전송 성공 - 발신자: {}, 수신자: {}, 제목: {}", sender, to, subject);
            return  CommonResponse.success("이메일 전송 성공 - 발신자: " + sender +" , 수신자: "+ to +", 제목: " + subject );


        } catch (MailException | MessagingException ex) {
            log.error("이메일 전송 실패 - 발신자: {}, 수신자: {}에게 {} 제목의 메일 전송 중 오류 발생: {}", sender, to, subject, ex.getMessage(), ex);
            return  CommonResponse.error("이메일 전송 실패 : "+ ex.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
            return CommonResponse.error("예상치 못한 오류 : "+ e.getMessage(),500);
        }
    }


}
