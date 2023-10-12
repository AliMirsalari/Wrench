package com.ali.mirsalari.wrench;

import com.ali.mirsalari.wrench.config.ServiceConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WrenchApplication {

	public static void main(String[] args) {
		SpringApplication.run(WrenchApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner (ServiceConfig serviceConfig){
		return args -> {
			serviceConfig.start();
		};
	}

}
