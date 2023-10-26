package com.ali.mirsalari.wrench;

import com.ali.mirsalari.wrench.config.ServiceConfig;
import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.service.BidService;
import com.ali.mirsalari.wrench.service.ExpertService;
import com.ali.mirsalari.wrench.service.OrderService;
import com.ali.mirsalari.wrench.service.ServiceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.Instant;
@EnableWebMvc
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class WrenchApplication {

    public static void main(String[] args) {
        SpringApplication.run(WrenchApplication.class, args);
    }
}
