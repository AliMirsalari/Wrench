package com.ali.mirsalari.wrench.service;

import org.springframework.stereotype.Service;
@Service
public interface EmailService {
    void sendSimpleMessage(
            String to, String text);
}
