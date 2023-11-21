package com.ali.mirsalari.wrench.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender emailSender;
    @InjectMocks
    private EmailServiceImpl underTest;

    @Test
    void itShouldSendSimpleMessage() {
        //Arrange
        String to = "alimirsalari@hotmail.com";
        String text = "It's a test";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mirsalariseyyedali@gmail.com");
        message.setTo(to);
        message.setSubject("فعال سازی ایمیل");
        message.setText(text);
        //Act
        underTest.sendSimpleMessage(to, text);
        //Assert
        verify(emailSender, times(1)).send(message);
    }
}