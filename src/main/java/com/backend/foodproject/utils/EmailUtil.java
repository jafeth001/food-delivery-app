package com.backend.foodproject.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender javaMailSender;

    public void sendUserTokenEmail(String email, String token) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify Token");
        mimeMessageHelper.setText("""
                <div>
                  <a href="https://localhost:8080/auth/user/verify-account?email=%s&token=%s" target="_blank">click link to verify and activate account</a>
                </div>
                """.formatted(email, token), true);

        javaMailSender.send(mimeMessage);
    }

    public void sendSellerTokenEmail(String email, String token) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify Token");
        mimeMessageHelper.setText("""
                <div>
                  <a href="https://localhost:8080/auth/seller/verify-account?email=%s&token=%s" target="_blank">click link to verify and activate account</a>
                </div>
                """.formatted(email, token), true);

        javaMailSender.send(mimeMessage);
    }

    public void sendSellerForgetPassword(String email, String token) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Reset Password");
        mimeMessageHelper.setText("""
                <div>
                  <a href="https://localhost:8080/seller/reset-password?email=%s&token=%s" target="_blank">click link to reset password</a>
                </div>
                """.formatted(email, token), true);

        javaMailSender.send(mimeMessage);
    }

    public void sendUserForgetPassword(String email, String token) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Reset Password");
        mimeMessageHelper.setText("""
                <div>
                  <a href="https://localhost:8080/user/reset-password?email=%s&token=%s" target="_blank">click link to reset password</a>
                </div>
                """.formatted(email, token), true);

        javaMailSender.send(mimeMessage);
    }

}
