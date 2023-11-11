package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(
            String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mirsalariseyyedali@gmail.com");
        message.setTo(to);
        message.setSubject("فعال سازی ایمیل");
        message.setText(text);
        emailSender.send(message);
    }
}