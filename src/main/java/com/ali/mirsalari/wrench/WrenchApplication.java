package com.ali.mirsalari.wrench;

import com.ali.mirsalari.wrench.config.ServiceConfig;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.service.ExpertService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import java.time.Instant;

@SpringBootApplication
public class WrenchApplication {

    public static void main(String[] args) {
        SpringApplication.run(WrenchApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ServiceConfig serviceConfig,
                                        ExpertService expertService
                                        ) {
        return args -> {
//			serviceConfig.start();
//            Expert expert = new Expert(
//                    1L,
//                    "Ali",
//                    "Mirsalari",
//                    "Ali@gmail.com",
//                    "Fr3$#B1gD0g$RanTh3H1ll$F@st!",
//                    Instant.now(),
//                    "src/main/resources/images/Avatar.jpg");
//            expertService.save(expert);

//            expertService.retrieveAndSavePhotoToFile(expertService.findById(1L).get(), "src/main/resources/images/new/Avatar.jpg");
        };
    }

}
