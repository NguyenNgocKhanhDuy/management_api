package com.nnkd.managementbe.service;

import com.nnkd.managementbe.dto.request.ApiResponse;
import com.nnkd.managementbe.dto.request.MailSendRequest;
import com.nnkd.managementbe.dto.request.UserCreationRequest;
import com.nnkd.managementbe.dto.request.VerifyCodeRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    public ApiResponse sendMail(MailSendRequest request) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        ApiResponse apiResponse = new ApiResponse();
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(request.getText(), false);

            mailSender.send(mimeMessage);
            apiResponse.setResult("Mail sent successfully!");
        } catch (MessagingException e) {
            throw new MailSendException(e.getMessage());
        }

        return apiResponse;

    }

    public ApiResponse sendCodeVerify(UserCreationRequest request, String code) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        ApiResponse apiResponse = new ApiResponse();
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(request.getEmail());
            helper.setSubject("Verify Email");
            helper.setText("Your Code is: "+code, false);

            mailSender.send(mimeMessage);
            apiResponse.setResult("Mail sent successfully!");
        } catch (MessagingException e) {
            throw new MailSendException(e.getMessage());
        }

        return apiResponse;
    }



}
